package shishamo_tech.common.recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class STRecipeModifierUtil {
    private STRecipeModifierUtil() {}

    /**
     * Voltage-normalizing suffix modifiers keyed by target voltage. The lambda
     * captures only {@code voltage}, so one instance per distinct voltage avoids
     * allocating a capturing lambda on every recipe search.
     */
    private static final Map<Long, ModifierFunction> VOLTAGE_NORMALIZERS = new ConcurrentHashMap<>();

    @Nullable
    public static ModifierFunction createParallelModifier(
            MetaMachine machine, GTRecipe recipe, int targetParallel, long voltage) {
        int actualParallel = ParallelLogic.getParallelAmountWithoutEU(machine, recipe, targetParallel);
        if (actualParallel <= 0) return ModifierFunction.NULL;

        ContentModifier modifier = ContentModifier.multiplier(actualParallel);
        ModifierFunction parallelMod = ModifierFunction.builder()
                .parallels(actualParallel)
                .inputModifier(modifier)
                .outputModifier(modifier)
                .build();

        ModifierFunction composed;
        if (voltage > 0 && RecipeHelper.getRealEUt(recipe).getTotalEU() > 0) {
            ModifierFunction ocMod = STOverclockingLogic.TRIPLE_OVERCLOCK.getModifier(
                    machine, recipe, voltage);
            composed = ocMod.andThen(parallelMod);
            composed = composed.andThen(voltageNormalizer(voltage));
        } else {
            composed = parallelMod;
        }
        return composed;
    }

    private static ModifierFunction voltageNormalizer(long voltage) {
        return VOLTAGE_NORMALIZERS.computeIfAbsent(voltage, v -> r -> {
            if (r == null) return null;
            var withIO = RecipeHelper.getRealEUtWithIO(r);
            long currentEUt = withIO.stack().getTotalEU();
            if (currentEUt <= 0 || currentEUt == v) return r;
            double mult = (double) v / currentEUt;
            return ModifierFunction.builder()
                    .eutMultiplier(mult)
                    .build()
                    .apply(r);
        });
    }

    public static long getOverclockVoltage(MetaMachine machine) {
        if (machine instanceof IOverclockMachine ocMachine) {
            return ocMachine.getOverclockVoltage();
        }
        return 0;
    }
}
