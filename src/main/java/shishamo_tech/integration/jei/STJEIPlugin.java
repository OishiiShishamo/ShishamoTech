package shishamo_tech.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import shishamo_tech.ShishamoTech;
import shishamo_tech.common.machine.ae2.STAE2Machines;
import shishamo_tech.common.recipe.STRecipeTypes;
import appeng.recipes.handlers.InscriberRecipe;

@JeiPlugin
public class STJEIPlugin implements IModPlugin {

    private static final ResourceLocation UID = ShishamoTech.id("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var connection = Minecraft.getInstance().getConnection();
        if (connection == null) return;

        var recipeManager = connection.getRecipeManager();
        var allRecipes = recipeManager.getAllRecipesFor(InscriberRecipe.TYPE)
                .toArray(new InscriberRecipe[0]);

        if (allRecipes.length == 0) return;

        var gtRecipes = STRecipeTypes.buildInscriberRecipes(allRecipes);
        if (gtRecipes.isEmpty()) return;

        for (var recipe : gtRecipes) {
            STRecipeTypes.INSCRIBER_RECIPES.addToMainCategory(recipe);
        }

        var recipeType = new RecipeType<>(STRecipeTypes.INSCRIBER_RECIPES.registryName,
                com.gregtechceu.gtceu.api.recipe.GTRecipe.class);
        registration.addRecipes(recipeType, gtRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        var recipeType = new RecipeType<>(STRecipeTypes.INSCRIBER_RECIPES.registryName,
                com.gregtechceu.gtceu.api.recipe.GTRecipe.class);
        for (var machine : STAE2Machines.AEMachineGetAll()) {
            registration.addRecipeCatalyst(machine.asStack(), recipeType);
        }
    }
}