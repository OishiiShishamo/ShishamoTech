package shishamo_tech.data.recipe;

import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import shishamo_tech.common.data.STMultiMachines;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTItems.WOODEN_FORM_EMPTY;

public class CraftingRecipeLoader {
    public static void init(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider, "mega_steam_grinder", STMultiMachines.MEGA_STEAM_GRINDER.asStack(),
                "XXX",
                "XXX",
                "XXX",
                'X', GTMachines.STEAM_MACERATOR.second().asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, "mega_steam_furnace", STMultiMachines.MEGA_STEAM_FURNACE.asStack(),
                "XXX",
                "XXX",
                "XXX",
                'X', GTMachines.STEAM_FURNACE.second().asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, "mega_steam_compressor", STMultiMachines.MEGA_STEAM_COMPRESSOR.asStack(),
                "XXX",
                "XXX",
                "XXX",
                'X', GTMachines.STEAM_COMPRESSOR.second().asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, "mega_steam_extractor", STMultiMachines.MEGA_STEAM_EXTRACTOR.asStack(),
                "XXX",
                "XXX",
                "XXX",
                'X', GTMachines.STEAM_EXTRACTOR.second().asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, "mega_steam_hammer", STMultiMachines.MEGA_STEAM_HAMMER.asStack(),
                "XXX",
                "XXX",
                "XXX",
                'X', GTMachines.STEAM_HAMMER.second().asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, "mega_steam_alloy_smelter", STMultiMachines.MEGA_STEAM_ALLOY_SMELTER.asStack(),
                "XXX",
                "XXX",
                "XXX",
                'X', GTMachines.STEAM_ALLOY_SMELTER.second().asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, "mega_steam_rock_crusher", STMultiMachines.MEGA_STEAM_ROCK_CRUSHER.asStack(),
                "XXX",
                "XXX",
                "XXX",
                'X', GTMachines.STEAM_ROCK_CRUSHER.second().asStack());
    }
}
