package shishamo_tech.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import shishamo_tech.ShishamoTech;
import shishamo_tech.common.machine.ae2.STAE2Machines;
import shishamo_tech.common.machine.botany.STBotanyMachines;
import shishamo_tech.common.recipe.STRecipeTypes;
import shishamo_tech.integration.botany.BotanyPotsRecipeHelper;
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

        if (allRecipes.length > 0) {
            var gtRecipes = STRecipeTypes.buildInscriberRecipes(allRecipes);
            if (!gtRecipes.isEmpty()) {
                for (var recipe : gtRecipes) {
                    STRecipeTypes.INSCRIBER_RECIPES.addToMainCategory(recipe);
                }

                var inscriberType = new RecipeType<>(STRecipeTypes.INSCRIBER_RECIPES.registryName,
                        GTRecipe.class);
                registration.addRecipes(inscriberType, gtRecipes);
            }
        }

        if (ShishamoTech.isModLoaded("botanypots")) {
            var level = Minecraft.getInstance().level;
            if (level != null) {
                var crops = BotanyPotsRecipeHelper.readAllCropRecipes(level);
                var gtRecipes = BotanyPotsRecipeHelper.convertToGTRecipes(crops, STRecipeTypes.GREEN_HOUSE_RECIPES);
                if (!gtRecipes.isEmpty()) {
                    for (var recipe : gtRecipes) {
                        STRecipeTypes.GREEN_HOUSE_RECIPES.addToMainCategory(recipe);
                    }
                    var greenHouseType = new RecipeType<>(STRecipeTypes.GREEN_HOUSE_RECIPES.registryName,
                            GTRecipe.class);
                    registration.addRecipes(greenHouseType, gtRecipes);
                }
            }
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        var inscriberType = new RecipeType<>(STRecipeTypes.INSCRIBER_RECIPES.registryName,
                GTRecipe.class);
        for (var machine : STAE2Machines.AEMachineGetAll()) {
            if (machine != null) {
                registration.addRecipeCatalyst(machine.asStack(), inscriberType);
            }
        }

        var greenHouseType = new RecipeType<>(STRecipeTypes.GREEN_HOUSE_RECIPES.registryName,
                GTRecipe.class);
        for (var machine : STBotanyMachines.botanyMachineGetAll()) {
            if (machine != null) {
                registration.addRecipeCatalyst(machine.asStack(), greenHouseType);
            }
        }
    }
}