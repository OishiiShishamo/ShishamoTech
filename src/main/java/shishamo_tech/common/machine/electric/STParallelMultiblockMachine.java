package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
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

public class STParallelMultiblockMachine extends WorkableElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STParallelMultiblockMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    public STParallelMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public static int getBaseParallelForTier(int tier) {
        return STOverclockingLogic.getParallelBonus(tier);
    }

    public int getBaseParallelForTier() {
        return getBaseParallelForTier(getDefinition().getTier());
    }

    public int getParallelCount() {
        return STOverclockingLogic.getParallelBonus(getTier()) * STOverclockingLogic.getParallelBonus(getDefinition().getTier()) * STConfig.PARALLEL_MULTIPLIER.get();
    }

    public static int getDisplayParallelCount(int tier) {
        return getBaseParallelForTier(tier) * STConfig.PARALLEL_MULTIPLIER.get();
    }

    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof STParallelMultiblockMachine multiblock)) {
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
                long currentTotal = withIO.stack().getTotalEU();
                if (currentTotal <= 0 || currentTotal == targetVoltage) return r;
                double mult = (double) targetVoltage / currentTotal;
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
        }
    }
}
