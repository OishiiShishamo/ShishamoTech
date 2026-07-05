package shishamo_tech.common.machine;

import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.handlers.InscriberProcessType;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import shishamo_tech.ShishamoTech;

import java.util.HashSet;
import java.util.Set;

public final class STInscriberFilterUtil {
    private STInscriberFilterUtil() {}

    public static boolean isPressItem(ItemStack stack) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return id != null && id.getPath().endsWith("_press");
    }

    public static boolean isPressIngredient(Ingredient ingredient) {
        for (ItemStack stack : ingredient.getItems()) {
            if (isPressItem(stack)) return true;
        }
        return false;
    }

    public static Set<ResourceLocation> collectInputItemIds(InscriberRecipe recipe) {
        var ids = new HashSet<ResourceLocation>();
        for (var ingredient : new Ingredient[]{
                recipe.getMiddleInput(),
                recipe.getTopOptional(),
                recipe.getBottomOptional()
        }) {
            if (ingredient.isEmpty() || isPressIngredient(ingredient)) continue;
            for (var stack : ingredient.getItems()) {
                var id = ForgeRegistries.ITEMS.getKey(stack.getItem());
                if (id != null) ids.add(id);
            }
        }
        return ids;
    }

    public static boolean inputsConflict(Set<ResourceLocation> a, Set<ResourceLocation> b) {
        return a.containsAll(b) || b.containsAll(a);
    }

    public static GTRecipe convertToGTRecipe(InscriberRecipe ae2Recipe, int circuitIndex, GTRecipeType recipeType) {
        return convertToGTRecipe(ae2Recipe, circuitIndex, recipeType, true);
    }

    public static GTRecipe convertToGTRecipe(InscriberRecipe ae2Recipe, int circuitIndex, GTRecipeType recipeType, boolean addCircuitToRecipe) {
        Ingredient middle = ae2Recipe.getMiddleInput();
        Ingredient top = ae2Recipe.getTopOptional();
        Ingredient bottom = ae2Recipe.getBottomOptional();
        ItemStack output = ae2Recipe.getResultItem();
        boolean isPress = ae2Recipe.getProcessType() == InscriberProcessType.PRESS;

        ShishamoTech.LOGGER.debug("Converting AE2 recipe: {} (mode={}, middle={}, top={}, bottom={}, output={})",
                ae2Recipe.getId(),
                ae2Recipe.getProcessType(),
                middle.isEmpty() ? "empty" : ForgeRegistries.ITEMS.getKey(middle.getItems()[0].getItem()),
                top.isEmpty() ? "empty" : ForgeRegistries.ITEMS.getKey(top.getItems()[0].getItem()),
                bottom.isEmpty() ? "empty" : ForgeRegistries.ITEMS.getKey(bottom.getItems()[0].getItem()),
                ForgeRegistries.ITEMS.getKey(output.getItem()));

        com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder builder =
                com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder.of(recipeType.registryName, recipeType)
                        .duration(200)
                        .EUt(30);

        if (!middle.isEmpty() && !isPressIngredient(middle)) {
            builder.inputItems(middle);
        }
        if (!top.isEmpty() && !isPressIngredient(top)) {
            if (isPress) {
                builder.inputItems(top);
            } else {
                builder.notConsumable(top);
            }
        }
        if (!bottom.isEmpty() && !isPressIngredient(bottom)) {
            if (isPress) {
                builder.inputItems(bottom);
            } else {
                builder.notConsumable(bottom);
            }
        }

        if (!output.isEmpty() && !isPressItem(output)) {
            builder.outputItems(output.copy());
        }
        if (addCircuitToRecipe && circuitIndex > 0) {
            builder.circuitMeta(circuitIndex);
        }

        GTRecipe recipe = builder.buildRawRecipe();
        recipe.setId(ae2Recipe.getId());
        if (recipe.data == null) {
            recipe.data = new CompoundTag();
        }
        recipe.data.putInt("circuit", circuitIndex);

        ShishamoTech.LOGGER.debug("Converted GTRecipe: inputs={}, outputs={}",
                recipe.inputs, recipe.outputs);

        return recipe;
    }
}
