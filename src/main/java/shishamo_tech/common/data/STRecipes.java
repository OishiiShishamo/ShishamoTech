package shishamo_tech.common.data;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import shishamo_tech.data.recipe.AssemblerRecipeLoader;
import shishamo_tech.data.recipe.AssemblyLineLoader;
import shishamo_tech.data.recipe.CompressedRecipeLoader;
import shishamo_tech.data.recipe.CraftingRecipeLoader;

import java.util.Set;
import java.util.function.Consumer;

public class STRecipes {
    public static void recipeAddition(Consumer<FinishedRecipe> consumer) {
        AssemblerRecipeLoader.init(consumer);
        AssemblyLineLoader.init(consumer);
        CompressedRecipeLoader.init(consumer);
        CraftingRecipeLoader.init(consumer);
        registerLargeSteamBoilerFuelRecipes(consumer);
    }

    private static void registerLargeSteamBoilerFuelRecipes(Consumer<FinishedRecipe> consumer) {
        GTRecipeTypes.LARGE_BOILER_RECIPES.recipeBuilder("large_steam_boiler_lava")
                .inputFluids(GTMaterials.Lava.getFluid(100))
                .duration(300)
                .save(consumer);
    }
}
