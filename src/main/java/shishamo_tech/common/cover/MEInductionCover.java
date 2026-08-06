package shishamo_tech.common.cover;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;

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
 * Supplies GTEU to the covered machine from the ME network's energy storage
 * (Applied Flux). Energy is drawn as FE flux from ME storage and converted to
 * EU before being fed into the machine's energy container.
 * <p>
 * Only registered and usable while Applied Flux is installed.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEInductionCover extends MEBaseCover {

    public MEInductionCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && getEnergyContainer() != null;
    }

    @Override
    protected boolean isEnabled() {
        return STConfig.isMEInductionCoverEnabled();
    }

    @Override
    protected void update() {
        if (!STConfig.isMEInductionCoverEnabled()) return;
        IEnergyContainer machineEnergy = getEnergyContainer();
        if (machineEnergy == null || !machineEnergy.inputsEnergy(attachedSide)) return;

        long space = machineEnergy.getEnergyCanBeInserted();
        if (space <= 0) return;

        var grid = mainNode.getGrid();
        if (grid == null) return;
        MEStorage storage = grid.getStorageService().getInventory();

        int ratio = FeCompat.ratio(false);
        long feToExtract = FeCompat.toFeLong(space, ratio);
        if (feToExtract <= 0) return;

        long feExtracted = storage.extract(FluxKey.of(EnergyType.FE), feToExtract, Actionable.MODULATE, actionSource);
        if (feExtracted > 0) {
            machineEnergy.addEnergy(FeCompat.toEu(feExtracted, ratio));
        }
    }

    @Nullable
    private IEnergyContainer getEnergyContainer() {
        return GTCapabilityHelper.getEnergyContainer(coverHolder.getLevel(), coverHolder.getPos(), attachedSide);
    }
}
