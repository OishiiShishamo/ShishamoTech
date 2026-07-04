package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import shishamo_tech.common.recipe.STOverclockingLogic;
import shishamo_tech.config.STConfig;

import java.util.List;

public class STCoilParallelMultiblockMachine extends CoilWorkableElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STCoilParallelMultiblockMachine.class, CoilWorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    public STCoilParallelMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public static int getBaseParallelForTier(int tier) {
        return 4 * (tier + 1);
    }

    public int getBaseParallelForTier() {
        return getBaseParallelForTier(getTier());
    }

    public int getCoilParallelBonus() {
        int coilTier = getCoilTier();
        return switch (coilTier) {
            case 0 -> 0;
            case 1, 2 -> 2;
            case 3, 4 -> 4;
            case 5, 6 -> 8;
            default -> 16;
        };
    }

    public int getParallelCount() {
        int base = getBaseParallelForTier();
        int bonus = getCoilParallelBonus();
        return (base + bonus) * STConfig.PARALLEL_MULTIPLIER.get();
    }

    public static int getDisplayParallelCount(int machineTier, int coilTier) {
        int base = getBaseParallelForTier(machineTier);
        return base + switch (coilTier) {
            case 0 -> 0;
            case 1, 2 -> 2;
            case 3, 4 -> 4;
            case 5, 6 -> 8;
            default -> 16;
        };
    }

    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof STCoilParallelMultiblockMachine multiblock)) {
            return ModifierFunction.IDENTITY;
        }
        long voltage = 0;
        if (machine instanceof IOverclockMachine ocMachine) {
            voltage = ocMachine.getOverclockVoltage();
        }
        int targetParallel = multiblock.getParallelCount();
        int actualParallel = ParallelLogic.getParallelAmountWithoutEU(machine, recipe, targetParallel);
        if (actualParallel <= 0) return ModifierFunction.NULL;
        ContentModifier modifier = ContentModifier.multiplier(actualParallel);
        ModifierFunction parallelMod = ModifierFunction.builder()
                .parallels(actualParallel)
                .inputModifier(modifier)
                .outputModifier(modifier)
                .build();
        long targetVoltage = voltage;
        ModifierFunction composed;
        if (targetVoltage > 0 && RecipeHelper.getRealEUt(recipe).getTotalEU() > 0) {
            ModifierFunction ocMod = STOverclockingLogic.TRIPLE_OVERCLOCK.getModifier(
                    machine, recipe, targetVoltage);
            composed = ocMod.andThen(parallelMod);
            ModifierFunction powerMod = r -> {
                if (r == null) return null;
                var withIO = RecipeHelper.getRealEUtWithIO(r);
                long currentEUt = withIO.stack().getTotalEU();
                if (currentEUt <= 0 || currentEUt == targetVoltage) return r;
                double mult = (double) targetVoltage / currentEUt;
                return ModifierFunction.builder()
                        .eutMultiplier(mult)
                        .build()
                        .apply(r);
            };
            composed = composed.andThen(powerMod);
        } else {
            composed = parallelMod;
        }
        return composed;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable("shishamo_tech.machine.parallel_count",
                    getParallelCount()));
            textList.add(Component.translatable("shishamo_tech.machine.coil_tier",
                    Component.translatable("block.gtceu." + getCoilType().getName() + "_coil_block")));
        }
    }
}
