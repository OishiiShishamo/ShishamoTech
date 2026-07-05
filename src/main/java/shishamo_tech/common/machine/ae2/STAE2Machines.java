package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import shishamo_tech.STRegistration;
import shishamo_tech.common.recipe.STRecipeTypes;
import shishamo_tech.config.STConfig;

import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.BATCH_MODE;

import java.util.List;

public final class STAE2Machines {
    private STAE2Machines() {}

    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_MV;
    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_HV;
    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_EV;
    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_IV;

    public static List<MachineDefinition> getAll() {
        return List.of(PRESS_FREE_INSCRIBER_MV, PRESS_FREE_INSCRIBER_HV, PRESS_FREE_INSCRIBER_EV, PRESS_FREE_INSCRIBER_IV);
    }

    public static void init() {
        PRESS_FREE_INSCRIBER_MV = registerInscriber(
                "press_free_inscriber_mv",
                "Press-Free Inscriber MK-I",
                2,
                GTBlocks.CASING_STEEL_SOLID,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/gcym/large_material_press"));

        PRESS_FREE_INSCRIBER_HV = registerInscriber(
                "press_free_inscriber_hv",
                "Press-Free Inscriber MK-II",
                4,
                GTBlocks.CASING_STAINLESS_CLEAN,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/gcym/large_material_press"));

        PRESS_FREE_INSCRIBER_EV = registerInscriber(
                "press_free_inscriber_ev",
                "Press-Free Inscriber MK-III",
                6,
                GTBlocks.CASING_TITANIUM_STABLE,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_stable_titanium"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/gcym/large_material_press"));

        PRESS_FREE_INSCRIBER_IV = registerInscriber(
                "press_free_inscriber_iv",
                "Press-Free Inscriber MK-IV",
                8,
                GTBlocks.CASING_TUNGSTENSTEEL_ROBUST,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/gcym/large_material_press"));
    }

    public static Material getFrameMaterial(int tier) {
        return switch (tier) {
            case 2 -> GTMaterials.Steel;
            case 4 -> GTMaterials.StainlessSteel;
            case 6 -> GTMaterials.Titanium;
            case 8 -> GTMaterials.TungstenSteel;
            default -> GTMaterials.Steel;
        };
    }

    private static MultiblockMachineDefinition registerInscriber(
            String name,
            String langValue,
            int tier,
            com.tterrag.registrate.util.entry.BlockEntry<? extends net.minecraft.world.level.block.Block> appearanceBlock,
            net.minecraft.resources.ResourceLocation casingTexture,
            net.minecraft.resources.ResourceLocation overlayModel) {
        int parallel = STInscriberMultiblockMachine.getDisplayParallelCount(tier);
        return STRegistration.REGISTRATE
                .multiblock(name, STInscriberMultiblockMachine::new)
                .rotationState(RotationState.ALL)
                .langValue(langValue)
                .tier(tier)
                .recipeType(STRecipeTypes.INSCRIBER_RECIPES)
                .recipeModifiers(STInscriberMultiblockMachine::recipeModifier, BATCH_MODE)
                .appearanceBlock(appearanceBlock)
                .pattern(pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("###AAA###", "#########", "#########", "#########", "#########", "#########", "#########", "#########", "#########", "#########", "#########", "###AAA###")
                        .aisle("#AAAAAAA#", "#B#AAA#B#", "#B#####B#", "#B#####B#", "#B#####B#", "#B#CCC#B#", "#B#####B#", "#B#####B#", "#B#####B#", "#B#####B#", "#B#####B#", "#AAAAAAA#")
                        .aisle("#AAAAAAA#", "##AAAAA##", "#########", "#########", "#########", "##CCCCC##", "#########", "#########", "#########", "#########", "#########", "#AAAAAAA#")
                        .aisle("AAAAAAAAA", "#AAAAAAA#", "#########", "#########", "#########", "#CCCCCCC#", "###BAB###", "###BAB###", "###BAB###", "###BAB###", "###BAB###", "AAAAAAAAA")
                        .aisle("AAAAAAAAD", "#AAAAAAA#", "#########", "#########", "#########", "#CCCCCCC#", "###AAA###", "###AAA###", "###AAA###", "###AAA###", "###AAA###", "AAAAAAAAA")
                        .aisle("AAAAAAAAA", "#AAAAAAA#", "#########", "#########", "#########", "#CCCCCCC#", "###BAB###", "###BAB###", "###BAB###", "###BAB###", "###BAB###", "AAAAAAAAA")
                        .aisle("#AAAAAAA#", "##AAAAA##", "#########", "#########", "#########", "##CCCCC##", "#########", "#########", "#########", "#########", "#########", "#AAAAAAA#")
                        .aisle("#AAAAAAA#", "#B#AAA#B#", "#B#####B#", "#B#####B#", "#B#####B#", "#B#CCC#B#", "#B#####B#", "#B#####B#", "#B#####B#", "#B#####B#", "#B#####B#", "#AAAAAAA#")
                        .aisle("###AAA###", "#########", "#########", "#########", "#########", "#########", "#########", "#########", "#########", "#########", "#########", "###AAA###")
                        .where("A", blocks(appearanceBlock.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(2))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .where("C", blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where("B", frames(getFrameMaterial(tier)))
                        .where("D", controller(blocks(pattern.getBlock())))
                        .where("#", air())
                        .build())
                .workableCasingModel(casingTexture, overlayModel)
                .tooltipBuilder((stack, tooltips) ->
                        tooltips.add(net.minecraft.network.chat.Component.translatable(
                                "shishamo_tech.machine.parallel_count",
                                parallel)))
                .register();
    }
}
