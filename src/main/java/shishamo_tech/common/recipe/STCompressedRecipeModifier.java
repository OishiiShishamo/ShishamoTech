package shishamo_tech.common.recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import org.jetbrains.annotations.NotNull;

public final class STCompressedRecipeModifier {
    private STCompressedRecipeModifier() {}

    public static final RecipeModifier COMPRESSED = STCompressedRecipeModifier::compressedModifier;

    private static @NotNull ModifierFunction compressedModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        return r -> {
            var copied = r.copy();
            copied.duration = Math.max(1, r.duration / 8);
            return copied;
        };
    }
}
