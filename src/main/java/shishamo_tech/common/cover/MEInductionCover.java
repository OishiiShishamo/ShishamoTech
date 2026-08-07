package shishamo_tech.common.cover;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;

import appeng.api.config.Actionable;
import appeng.api.storage.MEStorage;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import shishamo_tech.config.STConfig;

/**
 * Bridges GTEU between the covered machine and the ME network's energy storage
 * (Applied Flux), converting between EU and FE flux. Energy flows the direction
 * the machine uses it:
 * <ul>
 *   <li>Power consumers (regular machines, energy input hatches, electric
 *       multiblocks) are supplied from ME storage. Supply is buffered internally
 *       and ME storage is only polled once per {@code meInductionCoverPollInterval}
 *       (default 20 ticks) to reduce network load. The buffer is sized to
 *       {@code meInductionCoverBufferTicks} (default 200, 10 seconds) of the
 *       machine's max input.</li>
 *   <li>Power producers (dynamo hatches, turbine controllers) are drained into
 *       ME storage.</li>
 * </ul>
 * <p>
 * Only registered and usable while Applied Flux is installed.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEInductionCover extends MEBaseCover {

    /** Buffered EU to keep the machine's container topped up, sized to {@code meInductionCoverBufferTicks} of max input. */
    private long buffer = 0;

    public MEInductionCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        IEnergyContainer container = findEnergyContainer(getMachine());
        if (container == null) return false;
        if (super.canAttach()) return true;
        // The base class forbids covers on the front face of a machine that has a
        // front facing. Allow it only when the machine's energy I/O is confined to
        // that face (energy/dynamo hatches, single-block generators like the Steam
        // Turbine), so the cover can bridge them from the ME network.
        return attachedSide == coverHolder.getFrontFacing() && hasEnergyIOOnlyOnFront(container);
    }

    @Override
    protected boolean isEnabled() {
        return STConfig.isMEInductionCoverEnabled();
    }

    @Override
    protected void update() {
        if (!STConfig.isMEInductionCoverEnabled()) return;
        MetaMachine machine = getMachine();
        IEnergyContainer machineEnergy = findEnergyContainer(machine);
        if (machineEnergy == null) return;

        if (isDraining(machine, machineEnergy)) {
            drainToME(machineEnergy);
        } else {
            supplyFromME(machineEnergy);
        }
    }

    /**
     * Whether the cover should pull energy from the machine instead of supplying
     * it. Power generators must be drained. Electric multiblock generators are
     * identified by their definition, because their aggregated container reports
     * both input and output as available.
     */
    private static boolean isDraining(@Nullable MetaMachine machine, IEnergyContainer machineEnergy) {
        if (machine instanceof WorkableElectricMultiblockMachine multiblock) {
            return multiblock.isGenerator();
        }
        return !acceptsEnergy(machineEnergy) && canOutputEnergy(machineEnergy);
    }

    private void supplyFromME(IEnergyContainer machineEnergy) {
        if (!acceptsEnergy(machineEnergy)) return;

        // 1. Push everything the buffer holds into the machine's container every
        //    tick, trying to keep it topped up to full. There is no per-tick cap:
        //    the container accepts only as much as it can hold at once.
        if (buffer > 0) {
            long accepted = machineEnergy.addEnergy(Math.min(buffer, machineEnergy.getEnergyCanBeInserted()));
            if (accepted > 0) {
                buffer -= accepted;
            }
        }

        // 2. Top the buffer back up from ME storage, at most once per poll interval.
        //    The buffer is sized to meInductionCoverBufferTicks of max input, so it
        //    keeps the machine's container topped up for that long between ME polls.
        int pollInterval = STConfig.getMEInductionCoverPollInterval();
        long perTickInput = machineEnergy.getInputVoltage() * machineEnergy.getInputAmperage();
        long bufferCapacity = perTickInput * STConfig.getMEInductionCoverBufferTicks();
        if (bufferCapacity > 0 && buffer < bufferCapacity && coverHolder.getOffsetTimer() % pollInterval == 0) {
            pullFromME(bufferCapacity - buffer);
        }
    }

    private void pullFromME(long amount) {
        var grid = mainNode.getGrid();
        if (grid == null) return;
        MEStorage storage = grid.getStorageService().getInventory();

        int ratio = FeCompat.ratio(false);
        long feToExtract = FeCompat.toFeLong(amount, ratio);
        if (feToExtract <= 0) return;

        long feExtracted = storage.extract(FluxKey.of(EnergyType.FE), feToExtract, Actionable.MODULATE, actionSource);
        if (feExtracted > 0) {
            buffer += FeCompat.toEu(feExtracted, ratio);
        }
    }

    private void drainToME(IEnergyContainer machineEnergy) {
        long stored = machineEnergy.getEnergyStored();
        if (stored <= 0) return;

        var grid = mainNode.getGrid();
        if (grid == null) return;
        MEStorage storage = grid.getStorageService().getInventory();

        int ratio = FeCompat.ratio(false);
        long feToInsert = FeCompat.toFeLong(stored, ratio);
        if (feToInsert <= 0) return;

        long feInserted = storage.insert(FluxKey.of(EnergyType.FE), feToInsert, Actionable.MODULATE, actionSource);
        if (feInserted > 0) {
            machineEnergy.removeEnergy(FeCompat.toEu(feInserted, ratio));
        }
    }

    /**
     * Whether the container accepts energy from at least one face. Energy hatches
     * only allow input on their front face, so an attached-side-only check would
     * block the cover when it is mounted elsewhere on the hatch.
     */
    private static boolean acceptsEnergy(IEnergyContainer container) {
        for (Direction side : Direction.values()) {
            if (container.inputsEnergy(side)) return true;
        }
        return false;
    }

    /**
     * Whether the container can output energy from at least one face.
     */
    private static boolean canOutputEnergy(IEnergyContainer container) {
        for (Direction side : Direction.values()) {
            if (container.outputsEnergy(side)) return true;
        }
        return false;
    }

    /**
     * @return true if the covered machine exchanges energy only on its front face.
     *         Hatches gate their exposed capability to the front, while generators
     *         like the Steam Turbine gate their energy flow instead, so the flow
     *         conditions are what matter here.
     */
    private boolean hasEnergyIOOnlyOnFront(IEnergyContainer container) {
        Direction front = coverHolder.getFrontFacing();
        for (Direction side : Direction.values()) {
            if (side == front) continue;
            if (container.inputsEnergy(side) || container.outputsEnergy(side)) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private IEnergyContainer findEnergyContainer(@Nullable MetaMachine machine) {
        var level = coverHolder.getLevel();
        var pos = coverHolder.getPos();
        IEnergyContainer container = GTCapabilityHelper.getEnergyContainer(level, pos, attachedSide);
        if (container != null) return container;
        for (Direction side : Direction.values()) {
            if (side == attachedSide) continue;
            container = GTCapabilityHelper.getEnergyContainer(level, pos, side);
            if (container != null) return container;
        }
        container = GTCapabilityHelper.getEnergyContainer(level, pos, null);
        if (container != null) return container;
        // Electric multiblocks (turbine controllers, ...) hold their aggregated
        // energy in the machine itself rather than exposing a block capability.
        if (machine instanceof WorkableElectricMultiblockMachine multiblock) {
            return multiblock.getEnergyContainer();
        }
        return null;
    }
}
