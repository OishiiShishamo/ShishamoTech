package shishamo_tech.data.recipe;

import appeng.block.misc.InscriberBlock;
import appeng.core.definitions.AEBlocks;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.gregtechceu.gtceu.common.data.machines.GCYMMachines.*;
import static com.gregtechceu.gtceu.common.data.machines.GTMultiMachines.*;
import static com.gregtechceu.gtceu.data.recipe.CustomTags.*;
import static shishamo_tech.common.data.STMultiMachines.*;

public class AssemblerRecipeLoader {
    public static void init(Consumer<FinishedRecipe> consumer) {
        ASSEMBLER_RECIPES.recipeBuilder("hyper_tower_centrifuge")
                .inputItems(LARGE_CENTRIFUGE.asStack(), 64)
                .inputItems(IV_CIRCUITS, 64)
                .outputItems(HYPER_TOWER_CENTRIFUGE, 1)
                .duration(65536).EUt(V[IV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("lcr_cluster")
                .inputItems(LARGE_CHEMICAL_REACTOR.asStack(), 64)
                .inputItems(HV_CIRCUITS, 64)
                .outputItems(LCR_CLUSTER, 1)
                .duration(65536).EUt(V[HV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("eternal_force_freezer")
                .inputItems(VACUUM_FREEZER.asStack(), 64)
                .inputItems(EV_CIRCUITS, 64)
                .outputItems(ETERNAL_FORCE_FREEZER, 1)
                .duration(65536).EUt(V[EV], 1).save(consumer);

        ASSEMBLER_RECIPES.recipeBuilder("press_free_inscriber_mv")
                .inputItems(AEBlocks.INSCRIBER.stack(), 64)
                .inputItems(MV_CIRCUITS, 64)
                .outputItems(PRESS_FREE_INSCRIBER_MV, 1)
                .duration(65536).EUt(V[MV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("press_free_inscriber_hv")
                .inputItems(PRESS_FREE_INSCRIBER_MV, 64)
                .inputItems(HV_CIRCUITS, 64)
                .outputItems(PRESS_FREE_INSCRIBER_HV, 1)
                .duration(65536).EUt(V[HV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("press_free_inscriber_ev")
                .inputItems(PRESS_FREE_INSCRIBER_HV, 64)
                .inputItems(EV_CIRCUITS, 64)
                .outputItems(PRESS_FREE_INSCRIBER_EV, 1)
                .duration(65536).EUt(V[EV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("press_free_inscriber_iv")
                .inputItems(PRESS_FREE_INSCRIBER_EV, 64)
                .inputItems(EV_CIRCUITS, 64)
                .outputItems(PRESS_FREE_INSCRIBER_IV, 1)
                .duration(65536).EUt(V[IV], 1).save(consumer);
    }
}
