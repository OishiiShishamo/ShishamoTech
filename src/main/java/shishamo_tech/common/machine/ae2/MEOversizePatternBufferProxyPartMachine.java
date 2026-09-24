package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Proxy part for the {@link MEOversizePatternBufferPartMachine}. Lets a
 * multiblock access the buffer's pattern slots without the buffer itself
 * being attached to that multiblock. Bound via Data Stick (shift-use on the
 * buffer, then use on the proxy).
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MEOversizePatternBufferProxyPartMachine extends TieredIOPartMachine
                                                     implements IMachineLife, IDataStickInteractable {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MEOversizePatternBufferProxyPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);

    private final STProxySlotRecipeHandler proxySlotRecipeHandler;

    @Persisted
    @DescSynced
    private @Nullable BlockPos bufferPos;

    private @Nullable MEOversizePatternBufferPartMachine buffer = null;
    private boolean bufferResolved = false;

    public MEOversizePatternBufferProxyPartMachine(IMachineBlockEntity holder) {
        super(holder, GTValues.LuV, IO.IN);
        proxySlotRecipeHandler = new STProxySlotRecipeHandler(this);
    }

    public STProxySlotRecipeHandler getProxySlotRecipeHandler() {
        return proxySlotRecipeHandler;
    }

    @Nullable
    public BlockPos getBufferPos() {
        return bufferPos;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel level) {
            level.getServer().tell(new TickTask(0, () -> this.setBuffer(bufferPos)));
        }
    }

    @Override
    public List<RecipeHandlerList> getRecipeHandlers() {
        return proxySlotRecipeHandler.getProxySlotHandlers();
    }

    public void setBuffer(@Nullable BlockPos pos) {
        bufferResolved = true;
        var level = getLevel();
        if (level == null || pos == null) {
            buffer = null;
        } else if (MetaMachine.getMachine(level, pos) instanceof MEOversizePatternBufferPartMachine machine) {
            bufferPos = pos;
            buffer = machine;
            machine.addProxy(this);
            if (!isRemote()) proxySlotRecipeHandler.updateProxy(machine);
        } else {
            buffer = null;
        }
    }

    @Nullable
    public MEOversizePatternBufferPartMachine getBuffer() {
        if (!bufferResolved) setBuffer(bufferPos);
        return buffer;
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return getBuffer() != null;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        assert getBuffer() != null; // UI should never be able to be opened when buffer is null
        return getBuffer().createUI(entityPlayer);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onMachineRemoved() {
        var buf = getBuffer();
        if (buf != null) {
            buf.removeProxy(this);
            proxySlotRecipeHandler.clearProxy();
        }
    }

    @Override
    public InteractionResult onDataStickUse(Player player, ItemStack dataStick) {
        if (dataStick.hasTag()) {
            assert dataStick.getTag() != null;
            if (dataStick.getTag().contains("pos", Tag.TAG_INT_ARRAY)) {
                var posArray = dataStick.getOrCreateTag().getIntArray("pos");
                var newBufferPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
                setBuffer(newBufferPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
