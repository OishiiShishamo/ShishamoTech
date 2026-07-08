package shishamo_tech.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES;
import static com.gregtechceu.gtceu.common.data.machines.GTMultiMachines.*;
import static com.gregtechceu.gtceu.data.recipe.CustomTags.*;
import static shishamo_tech.common.data.STMultiMachines.*;

public class AssemblyLineLoader {
    public static void init(Consumer<FinishedRecipe> provider) {
        ASSEMBLY_LINE_RECIPES.recipeBuilder("non_omnipotent_universe_forge")
                .inputItems(ASSEMBLY_LINE.asStack(), 64)
                .inputItems(LuV_CIRCUITS, 64)
                .outputItems(NON_OMNIPOTENT_UNIVERSE_FORGE.asStack())
                .duration(65536).EUt(VA[LuV]).save(provider);
    }
}
