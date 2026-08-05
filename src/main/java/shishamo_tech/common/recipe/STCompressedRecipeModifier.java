package shishamo_tech.common.recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import org.jetbrains.annotations.NotNull;
import shishamo_tech.config.STConfig;

public final class STCompressedRecipeModifier {
    private STCompressedRecipeModifier() {}

    /**
     * Builds the 8x-speed modifier for a compressed single block machine family.
     * The given {@code machinePath} (e.g. "compressed_macerator") is used to look up
     * the per-machine config toggle shared by all tiers of the family.
     */
    public static RecipeModifier compressed(String machinePath) {
        return (machine, recipe) -> compressedModifier(machine, recipe, machinePath);
    }

    private static @NotNull ModifierFunction compressedModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe,
                                                                @NotNull String machinePath) {
        if (!STConfig.isCompressedSingleblockRecipesEnabled() || !STConfig.isMachineEnabled(machinePath)) {
            return ModifierFunction.IDENTITY;
        }
        return r -> {
            var copied = r.copy();
            copied.duration = Math.max(1, r.duration / 8);
            return copied;
        };
    }
}
