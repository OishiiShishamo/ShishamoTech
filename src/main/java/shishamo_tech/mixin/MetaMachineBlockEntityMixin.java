package shishamo_tech.mixin;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import appeng.api.networking.IInWorldGridNodeHost;
import appeng.capabilities.Capabilities;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Exposes the ME grid node of a cover to AE2 cables.
 * <p>
 * GTCEu only looks at the machine itself and its traits when a cable queries
 * {@link Capabilities#IN_WORLD_GRID_NODE_HOST} on a {@link MetaMachineBlockEntity}.
 * A cover is neither, so without this hook an ME-connected cover could never be
 * discovered by the network. This Tail injection adds one extra lookup: if a cover
 * on the queried side implements {@link IInWorldGridNodeHost}, its node is exposed.
 */
@Mixin(MetaMachineBlockEntity.AE2CallWrapper.class)
public class MetaMachineBlockEntityMixin {

    @Inject(method = "getGridNodeHostCapability",
            at = @At("TAIL"),
            cancellable = true,
            remap = false)
    private static void shishamoTech$exposeCoverGridNodeHost(Capability<?> cap, MetaMachine machine, Direction side,
                                                             CallbackInfoReturnable<LazyOptional<?>> cir) {
        if (cir.getReturnValue().isPresent()) return;
        if (cap != Capabilities.IN_WORLD_GRID_NODE_HOST) return;
        var coverContainer = machine.getCoverContainer();
        if (coverContainer == null) return;

        IInWorldGridNodeHost nodeHost = findNodeHost(coverContainer, side);
        if (nodeHost != null) {
            cir.setReturnValue(Capabilities.IN_WORLD_GRID_NODE_HOST.orEmpty(cap,
                    LazyOptional.of(() -> nodeHost)));
        }
    }

    /**
     * Picks the cover to expose as an {@link IInWorldGridNodeHost}.
     * <p>
     * AE2 discovers a host with a side-less capability probe
     * ({@code GridHelper.getNodeHost}), then gates the actual face connection via the
     * node's exposed sides. So for a {@code null} side we return the first cover host to
     * make the machine discoverable, while per-face connections stay limited to the
     * attached face because each cover node is {@code setExposedOnSides(attachedSide)}.
     * For a concrete side we only return a cover that is actually on that face.
     */
    @Nullable
    private static IInWorldGridNodeHost findNodeHost(ICoverable coverContainer, @Nullable Direction side) {
        if (side == null) {
            for (var dir : Direction.values()) {
                if (coverContainer.getCoverAtSide(dir) instanceof IInWorldGridNodeHost host) {
                    return host;
                }
            }
            return null;
        }
        var cover = coverContainer.getCoverAtSide(side);
        return cover instanceof IInWorldGridNodeHost host ? host : null;
    }
}
