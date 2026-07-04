package shishamo_tech.common.machine;

import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.handlers.InscriberProcessType;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

public class STInscriberFilterTrait extends MachineTrait {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STInscriberFilterTrait.class);

    public STInscriberFilterTrait(MetaMachine machine) {
        super(machine);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

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

    public static java.util.Set<ResourceLocation> collectInputItemIds(InscriberRecipe recipe) {
        var ids = new java.util.HashSet<ResourceLocation>();
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

    public static boolean inputsConflict(java.util.Set<ResourceLocation> a, java.util.Set<ResourceLocation> b) {
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

        GTCEu.LOGGER.info("Converting AE2 recipe: {} (mode={}, middle={}, top={}, bottom={}, output={})",
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
            recipe.data = new net.minecraft.nbt.CompoundTag();
        }
        recipe.data.putInt("circuit", circuitIndex);
        
        GTCEu.LOGGER.info("Converted GTRecipe: inputs={}, outputs={}",
                recipe.inputs, recipe.outputs);
        
        return recipe;
    }

    }