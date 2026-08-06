package shishamo_tech.common.cover;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.integration.ae2.utils.SerializableManagedGridNode;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.ReadOnlyManaged;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;

import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.util.AECableType;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Base class for covers that join the ME network with their own in-world grid node.
 * <p>
 * The node is exposed to cables through {@link IInWorldGridNodeHost}; the
 * {@code MetaMachineBlockEntityMixin} makes GTCEu hand this node host out when an
 * ME cable is placed against the covered side.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class MEBaseCover extends CoverBehavior implements IInWorldGridNodeHost {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEBaseCover.class,
            CoverBehavior.MANAGED_FIELD_HOLDER);

    @Persisted
    @ReadOnlyManaged(onDirtyMethod = "onGridNodeDirty",
                     serializeMethod = "serializeGridNode",
                     deserializeMethod = "deserializeGridNode")
    protected final SerializableManagedGridNode mainNode;

    protected final IActionSource actionSource;

    protected boolean online;
    @Nullable
    protected TickableSubscription subscription;

    public MEBaseCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
        this.mainNode = createManagedNode();
        this.actionSource = IActionSource.ofMachine(mainNode::getNode);
    }

    protected SerializableManagedGridNode createManagedNode() {
        var node = new SerializableManagedGridNode(this, new IGridNodeListener<>() {

            @Override
            public void onStateChanged(MEBaseCover owner, IGridNode node, State state) {
                owner.onMainNodeStateChanged(state);
            }

            @Override
            public void onSaveChanges(MEBaseCover owner, IGridNode node) {
                owner.coverHolder.markDirty();
            }
        });
        node.setFlags(GridFlags.REQUIRE_CHANNEL);
        node.setIdlePowerUsage(ConfigHolder.INSTANCE.compat.ae2.meHatchEnergyUsage);
        node.setInWorldNode(true);
        node.setExposedOnSides(EnumSet.of(attachedSide));
        node.setTagName("cover");
        return node;
    }

    //////////////////////////////////////
    // ***** Grid node lifecycle ******//
    //////////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        if (!coverHolder.isRemote() && coverHolder.getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::createMainNode));
        }
    }

    @Override
    public void onUnload() {
        mainNode.destroy();
        super.onUnload();
    }

    @Override
    public void onRemoved() {
        mainNode.destroy();
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
        super.onRemoved();
    }

    protected void createMainNode() {
        mainNode.create(coverHolder.getLevel(), coverHolder.getPos());
    }

    protected void onMainNodeStateChanged(IGridNodeListener.State reason) {
        boolean online = mainNode.isOnline() && mainNode.isPowered();
        if (this.online != online) {
            this.online = online;
            updateSubscription();
        }
    }

    //////////////////////////////////////
    // ******* IInWorldGridNodeHost *****//
    //////////////////////////////////////

    @Override
    public IGridNode getGridNode(Direction direction) {
        return mainNode.getNode();
    }

    @Override
    public AECableType getCableConnectionType(Direction direction) {
        return AECableType.SMART;
    }

    //////////////////////////////////////
    // ******* Ticking **********//
    //////////////////////////////////////

    protected boolean isOnline() {
        return online;
    }

    protected void updateSubscription() {
        boolean shouldRun = isOnline() && isEnabled();
        if (shouldRun) {
            subscription = coverHolder.subscribeServerTick(subscription, this::update);
        } else if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    /** Whether this cover is enabled by config. */
    protected abstract boolean isEnabled();

    /** Server-tick behaviour; only called while online and enabled. */
    protected abstract void update();

    @Nullable
    protected MetaMachine getMachine() {
        return MetaMachine.getMachine(coverHolder.getLevel(), coverHolder.getPos());
    }

    //////////////////////////////////////
    // ******* Sync/Persistence *****//
    //////////////////////////////////////

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @SuppressWarnings("unused")
    protected boolean onGridNodeDirty(SerializableManagedGridNode node) {
        return node != null && node.isActive() && node.isOnline();
    }

    @SuppressWarnings("unused")
    protected CompoundTag serializeGridNode(SerializableManagedGridNode node) {
        return node.serializeNBT();
    }

    @SuppressWarnings("unused")
    protected SerializableManagedGridNode deserializeGridNode(CompoundTag tag) {
        this.mainNode.deserializeNBT(tag);
        return this.mainNode;
    }
}
