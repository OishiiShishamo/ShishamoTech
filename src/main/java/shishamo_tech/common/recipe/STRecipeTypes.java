package shishamo_tech.common.recipe;

import appeng.recipes.handlers.InscriberRecipe;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeSerializer;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.ui.GTRecipeTypeUI;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import shishamo_tech.ShishamoTech;
import shishamo_tech.common.machine.STInscriberFilterUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public final class STRecipeTypes {
    private STRecipeTypes() {}

    public static final InscriberRecipeType INSCRIBER_RECIPES;

    static {
        INSCRIBER_RECIPES = new InscriberRecipeType(
                ShishamoTech.id("st_inscriber"),
                com.gregtechceu.gtceu.common.data.GTRecipeTypes.MULTIBLOCK);

        INSCRIBER_RECIPES
                .setMaxIOSize(4, 1, 0, 0)
                .setEUIO(IO.IN)
                .setSlotOverlay(false, false, GuiTextures.PRESS_OVERLAY_1)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
                .setSound(com.gregtechceu.gtceu.common.data.GTSoundEntries.MOTOR)
                .setRecipeUI(new GTRecipeTypeUI(INSCRIBER_RECIPES));

        GTRegistries.register(BuiltInRegistries.RECIPE_TYPE,
                INSCRIBER_RECIPES.registryName, INSCRIBER_RECIPES);
        GTRegistries.register(BuiltInRegistries.RECIPE_SERIALIZER,
                INSCRIBER_RECIPES.registryName, new GTRecipeSerializer());
        GTRegistries.RECIPE_TYPES.register(
                INSCRIBER_RECIPES.registryName, INSCRIBER_RECIPES);
    }

    public static void init() {
    }

    private static final AtomicBoolean RECIPE_DB_POPULATED = new AtomicBoolean(false);
    private static final AtomicBoolean RECIPES_ASSIGNED = new AtomicBoolean(false);
    private static List<RecipeAssignment> cachedAssignments;

    public record RecipeAssignment(InscriberRecipe recipe, int circuit) {}

    private static synchronized List<RecipeAssignment> assignCircuits(InscriberRecipe[] allRecipes) {
        if (RECIPES_ASSIGNED.get()) return cachedAssignments;

        var validRecipes = new ArrayList<InscriberRecipe>();

        for (var aeRecipe : allRecipes) {
            if (aeRecipe == null) continue;
            if (STInscriberFilterUtil.isPressItem(aeRecipe.getResultItem())) continue;
            boolean middleUsable = !aeRecipe.getMiddleInput().isEmpty()
                    && !STInscriberFilterUtil.isPressIngredient(aeRecipe.getMiddleInput());
            boolean topUsable = !aeRecipe.getTopOptional().isEmpty()
                    && !STInscriberFilterUtil.isPressIngredient(aeRecipe.getTopOptional());
            boolean bottomUsable = !aeRecipe.getBottomOptional().isEmpty()
                    && !STInscriberFilterUtil.isPressIngredient(aeRecipe.getBottomOptional());
            if (!middleUsable && !topUsable && !bottomUsable) continue;

            validRecipes.add(aeRecipe);
        }

        int n = validRecipes.size();
        if (n == 0) return List.of();

        validRecipes.sort(Comparator.comparing(r -> r.getId().toString()));

        var inputSets = new ArrayList<Set<ResourceLocation>>();
        for (var recipe : validRecipes) {
            inputSets.add(STInscriberFilterUtil.collectInputItemIds(recipe));
        }

        var result = new ArrayList<RecipeAssignment>();
        int[] circuits = new int[n];

        for (int i = 0; i < n; i++) {
            int circuit = 1;
            while (circuit <= 32) {
                boolean conflict = false;
                for (int j = 0; j < i; j++) {
                    if (circuits[j] == circuit
                            && STInscriberFilterUtil.inputsConflict(inputSets.get(i), inputSets.get(j))) {
                        conflict = true;
                        break;
                    }
                }
                if (!conflict) break;
                circuit++;
            }
            if (circuit > 32) {
                ShishamoTech.LOGGER.warn("STRecipeTypes: no free circuit for recipe {}, sharing circuit 1",
                        validRecipes.get(i).getId());
                circuit = 1;
            }
            circuits[i] = circuit;
            result.add(new RecipeAssignment(validRecipes.get(i), circuit));
        }

        RECIPES_ASSIGNED.set(true);
        cachedAssignments = result;
        return result;
    }

    public static List<GTRecipe> buildInscriberRecipes(InscriberRecipe[] allRecipes) {
        if (allRecipes == null || allRecipes.length == 0) return List.of();
        var assignments = assignCircuits(allRecipes);
        var result = new ArrayList<GTRecipe>();
        for (var a : assignments) {
            var converted = STInscriberFilterUtil.convertToGTRecipe(a.recipe(), a.circuit(), INSCRIBER_RECIPES);
            if (converted != null) result.add(converted);
        }
        return result;
    }

    public static void populateRecipeDB(InscriberRecipe[] allRecipes) {
        if (!RECIPE_DB_POPULATED.compareAndSet(false, true)) return;
        if (allRecipes == null || allRecipes.length == 0) return;

        var handler = INSCRIBER_RECIPES.getAdditionHandler();
        handler.beginStaging();

        var assignments = assignCircuits(allRecipes);
        for (var a : assignments) {
            var converted = STInscriberFilterUtil.convertToGTRecipe(a.recipe(), a.circuit(), INSCRIBER_RECIPES, true);
            if (converted != null) {
                handler.addStaging(converted);
            }
        }

        handler.completeStaging();
        ShishamoTech.LOGGER.info("STRecipeTypes: populated {} recipes into InscriberRecipeType RecipeDB", assignments.size());
    }

    public static class InscriberRecipeType extends GTRecipeType {

        public InscriberRecipeType(ResourceLocation id, String group, RecipeType<?>... proxyTypes) {
            super(id, group, proxyTypes);
        }
    }
}
