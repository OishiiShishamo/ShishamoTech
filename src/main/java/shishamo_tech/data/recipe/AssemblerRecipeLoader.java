package shishamo_tech.data.recipe;

import appeng.block.misc.InscriberBlock;
import appeng.core.definitions.AEBlocks;
import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import net.darkhax.botanypots.block.BlockBotanyPot;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

import static appeng.core.definitions.AEParts.*;
import static com.glodblock.github.appflux.common.AFItemAndBlock.*;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.gregtechceu.gtceu.common.data.machines.GCYMMachines.*;
import static com.gregtechceu.gtceu.common.data.machines.GTAEMachines.*;
import static com.gregtechceu.gtceu.common.data.machines.GTMultiMachines.*;
import static com.gregtechceu.gtceu.data.recipe.CustomTags.*;
import static shishamo_tech.ShishamoTech.isModLoaded;
import static shishamo_tech.common.cover.STCovers.*;
import static shishamo_tech.common.data.STMultiMachines.*;
import static shishamo_tech.common.machine.ae2.STAE2PartMachines.*;

public class AssemblerRecipeLoader {
    public static void init(Consumer<FinishedRecipe> consumer) {
        ASSEMBLER_RECIPES.recipeBuilder("god_steam_boiler")
                .inputItems(LARGE_BOILER_STEEL.asStack(), 64)
                .inputItems(HV_CIRCUITS, 64)
                .outputItems(GOD_STEAM_BOILER, 1)
                .duration(65536).EUt(V[HV], 1).save(consumer);

        ASSEMBLER_RECIPES.recipeBuilder("giga_ebf")
                .inputItems(ELECTRIC_BLAST_FURNACE.asStack(), 64)
                .inputItems(MV_CIRCUITS, 64)
                .outputItems(GIGA_EBF)
                .duration(65536).EUt(V[MV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("hyper_tower_centrifuge")
                .inputItems(LARGE_CENTRIFUGE.asStack(), 64)
                .inputItems(IV_CIRCUITS, 64)
                .outputItems(HYPER_TOWER_CENTRIFUGE, 1)
                .duration(65536).EUt(V[IV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("superior_maceration_plant")
                .inputItems(LARGE_MACERATION_TOWER.asStack(), 64)
                .inputItems(IV_CIRCUITS, 64)
                .outputItems(SUPERIOR_MACERATION_PLANT, 1)
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
        ASSEMBLER_RECIPES.recipeBuilder("ultimate_universal_storage")
                .inputItems(SUPER_CHEST[LV], 64)
                .inputItems(SUPER_TANK[LV], 64)
                .outputItems(ULTIMATE_UNIVERSAL_STORAGE, 1)
                .duration(65536).EUt(V[LV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("me_dual_stocking_input_hatch")
                .inputItems(STOCKING_IMPORT_BUS_ME)
                .inputItems(STOCKING_IMPORT_HATCH_ME)
                .outputItems(ME_DUAL_STOCKING_INPUT_HATCH)
                .duration(1024).EUt(V[EV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("me_long_output_bus")
                .inputItems(ITEM_EXPORT_BUS_ME, 8)
                .outputItems(ME_LONG_OUTPUT_BUS)
                .duration(1024).EUt(V[EV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("me_long_output_hatch")
                .inputItems(FLUID_EXPORT_HATCH_ME, 8)
                .outputItems(ME_LONG_OUTPUT_HATCH)
                .duration(1024).EUt(V[EV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("me_dual_long_output_hatch")
                .inputItems(ME_LONG_OUTPUT_BUS)
                .inputItems(ME_LONG_OUTPUT_HATCH)
                .outputItems(ME_DUAL_LONG_OUTPUT_HATCH)
                .duration(1024).EUt(V[EV], 1).save(consumer);
        ASSEMBLER_RECIPES.recipeBuilder("me_steam_supply_cover")
                .inputItems(EXPORT_BUS.asItem(), 8)
                .circuitMeta(1)
                .outputItems(ME_STEAM_SUPPLY_ITEM.get())
                .duration(512).EUt(V[LV], 1).save(consumer);
        if (isModLoaded("appflux")) {
            ASSEMBLER_RECIPES.recipeBuilder("me_induction_cover")
                    .inputItems(FLUX_ACCESSOR.asItem())
                    .inputItems(EXPORT_BUS.asItem(), 8)
                    .circuitMeta(2)
                    .outputItems(ME_INDUCTION_ITEM.get())
                    .duration(512).EUt(V[MV], 1).save(consumer);
        }

        if (isModLoaded("botanypots")) {
            ASSEMBLER_RECIPES.recipeBuilder("green_house")
                    .inputItems(TagUtil.createModItemTag("botanypots:hopper_botany_pots"), 64)
                    .inputItems(HV_CIRCUITS, 64)
                    .outputItems(GREEN_HOUSE, 1)
                    .duration(65536).EUt(V[HV], 1).save(consumer);
        }
    }
}
