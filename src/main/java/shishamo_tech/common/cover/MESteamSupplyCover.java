package shishamo_tech.common.cover;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEFluidKey;
import appeng.api.storage.MEStorage;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import shishamo_tech.config.STConfig;

/**
 * Supplies Steam from the ME network to the covered machine.
 * <p>
 * Steam is pulled from ME storage into a small on-cover buffer, then pushed into
 * the machine's fluid input every tick. The buffer allows polling ME storage at a
 * reduced rate instead of every tick.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MESteamSupplyCover extends MEBaseCover {

    private static final int BUFFER_SIZE = 32_000;
    private static final int POLL_INTERVAL = 20;

    private final FluidStack buffer = new FluidStack(GTMaterials.Steam.getFluid(), 0);

    public MESteamSupplyCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && getMachine() != null && getMachine().getFluidHandlerCap(attachedSide, false) != null;
    }

    @Override
    protected boolean isEnabled() {
        return STConfig.isMESteamSupplyCoverEnabled();
    }

    @Override
    protected void update() {
        if (!STConfig.isMESteamSupplyCoverEnabled()) return;
        MetaMachine machine = getMachine();
        if (machine == null) return;

        // 1. Push as much as the machine accepts from the buffer, every tick.
        IFluidHandlerModifiable machineFluid = machine.getFluidHandlerCap(attachedSide, false);
        if (machineFluid != null && buffer.getAmount() > 0) {
            int accepted = machineFluid.fill(buffer, FluidAction.EXECUTE);
            if (accepted > 0) {
                buffer.setAmount(buffer.getAmount() - accepted);
            }
        }

        // 2. Refill the buffer from ME storage at a reduced rate.
        if (buffer.getAmount() < BUFFER_SIZE && coverHolder.getOffsetTimer() % POLL_INTERVAL == 0) {
            pullFromME(BUFFER_SIZE - buffer.getAmount());
        }
    }

    private void pullFromME(int amount) {
        var grid = mainNode.getGrid();
        if (grid == null) return;
        MEStorage storage = grid.getStorageService().getInventory();
        var steamKey = AEFluidKey.of(GTMaterials.Steam.getFluid());
        long extracted = storage.extract(steamKey, amount, Actionable.MODULATE, actionSource);
        if (extracted > 0) {
            buffer.setAmount(buffer.getAmount() + (int) extracted);
        }
    }

    @Nullable
    @Override
    public IFluidHandlerModifiable getFluidHandlerCap(IFluidHandlerModifiable defaultValue) {
        // Pipes should still be able to reach the machine's fluid handler through this cover.
        return defaultValue;
    }
}
