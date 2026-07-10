package shishamo_tech.integration.botany;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import net.darkhax.botanypots.BotanyPotHelper;
import net.darkhax.botanypots.data.recipes.crop.BasicCrop;
import net.darkhax.botanypots.data.recipes.crop.HarvestEntry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BotanyPotsRecipeHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger("BotanyPotsCompat");

    private BotanyPotsRecipeHelper() {}

    public record CropDrop(ItemStack output, float chance) {}

    public record CropData(Ingredient seed, int growthTicks, List<CropDrop> drops) {}

    public static List<CropData> readAllCropRecipes(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            LOGGER.debug("Level is not a ServerLevel, cannot read recipes");
            return new ArrayList<>();
        }

        RecipeManager recipeManager = serverLevel.getRecipeManager();
        var cropRecipes = recipeManager.getAllRecipesFor(BotanyPotHelper.CROP_TYPE.get());

        List<BasicCrop> crops = new ArrayList<>();
        for (var recipe : cropRecipes) {
            if (recipe instanceof BasicCrop basicCrop) {
                crops.add(basicCrop);
            }
        }

        LOGGER.debug("Found {} Botany Pots crop recipes", crops.size());

        List<CropData> result = new ArrayList<>();
        for (var crop : crops) {
            Ingredient seed = crop.getSeed();
            int growthTicks = crop.getGrowthTicks();
            List<CropDrop> drops = new ArrayList<>();

            for (HarvestEntry entry : crop.getResults()) {
                ItemStack stack = entry.getItem();
                LOGGER.debug("Crop {} drop: {} (chance={})", crop.getId(), stack, entry.getChance());
                if (!stack.isEmpty()) {
                    drops.add(new CropDrop(stack.copy(), entry.getChance()));
                }
            }

            LOGGER.debug("Crop {} has {} drops, seed={}, ticks={}", crop.getId(), drops.size(), seed.getItems()[0], growthTicks);

            if (!seed.getItems()[0].isEmpty() && growthTicks > 0) {
                result.add(new CropData(seed, growthTicks, drops));
            }
        }

        LOGGER.info("Read {} Botany Pots crop recipes", result.size());
        return result;
    }

    public static List<GTRecipe> convertToGTRecipes(List<CropData> crops, GTRecipeType recipeType) {
        if (crops.isEmpty()) return List.of();

        List<GTRecipe> result = new ArrayList<>();
        for (var cropData : crops) {
            var builder = GTRecipeBuilder.of(recipeType.registryName, recipeType)
                    .inputItems(cropData.seed())
                    .EUt(30);

            for (var drop : cropData.drops()) {
                var stack = drop.output().copy();
                builder.outputItems(stack);
            }

            int duration = Math.max(20, cropData.growthTicks() / 6);
            builder.duration(duration);

            GTRecipe recipe = builder.buildRawRecipe();
            if (recipe != null) {
                LOGGER.debug("GTRecipe {} has {} outputs, {} inputs",
                    recipe.getId(),
                    recipe.outputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList()).size(),
                    recipe.inputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList()).size());
                result.add(recipe);
            }
        }
        return result;
    }

    public static void populateRecipeDB(List<CropData> crops, GTRecipeType recipeType) {
        var recipes = convertToGTRecipes(crops, recipeType);
        if (recipes.isEmpty()) return;

        var handler = recipeType.getAdditionHandler();
        handler.beginStaging();
        for (var recipe : recipes) {
            handler.addStaging(recipe);
        }
        handler.completeStaging();
    }
}