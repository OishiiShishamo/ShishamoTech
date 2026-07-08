package shishamo_tech.common.data;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import shishamo_tech.data.recipe.AssemblerRecipeLoader;
import shishamo_tech.data.recipe.AssemblyLineLoader;
import shishamo_tech.data.recipe.CraftingRecipeLoader;

import java.util.Set;
import java.util.function.Consumer;

public class STRecipes {
    public static void recipeAddition(Consumer<FinishedRecipe> consumer) {
        AssemblerRecipeLoader.init(consumer);
        AssemblyLineLoader.init(consumer);
        CraftingRecipeLoader.init(consumer);
    }
}
