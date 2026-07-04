package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import shishamo_tech.STRegistration;
import shishamo_tech.config.STConfig;

import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.heatingCoils;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.BATCH_MODE;

public final class STElectricMachines {
    private STElectricMachines() {}

    public static MultiblockMachineDefinition LARGE_GRINDING_PLANT;
    public static MultiblockMachineDefinition LARGE_SMELTING_PLANT;
    public static MultiblockMachineDefinition LARGE_WASHING_PLANT;
    public static MultiblockMachineDefinition BATTLE_TOWER_CENTRIFUGE;
    public static MultiblockMachineDefinition LARGE_ELECTROLYZER;
    public static MultiblockMachineDefinition LCR_CLUSTER;
    public static MultiblockMachineDefinition LARGE_ASSEMBLY_PLANT;
    public static MultiblockMachineDefinition LARGE_ARC_FURNACE;
    public static MultiblockMachineDefinition LARGE_DISTILLATION_TOWER;
    public static MultiblockMachineDefinition ETERNAL_FORCE_FREEZER;

    public static void init() {
        LARGE_GRINDING_PLANT = registerElectricMachine(
                "large_grinding_plant",
                "Large Grinding Plant",
                GTRecipeTypes.MACERATOR_RECIPES,
                1,
                GTBlocks.CASING_STEEL_SOLID,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/gcym/large_maceration_tower"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GS", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX")
                        .where("S", controller(blocks(pattern.getBlock())))
                        .where("G", blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where("#", air())
                        .where("X", blocks(GTBlocks.CASING_STEEL_SOLID.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_SMELTING_PLANT = registerCoilMachine(
                "large_smelting_plant",
                "Large Smelting Plant",
                GTRecipeTypes.FURNACE_RECIPES,
                1,
                GTBlocks.CASING_INVAR_HEATPROOF,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_heatproof"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/multi_furnace"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGC#CCCCC#CGX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XGC#CCCCC#CGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGC#CCCCC#CGX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XGC#CCCCC#CGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGC#CCCCC#CGX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XGC#CCCCC#CGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGC#CCCCC#CGX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XGC#CCCCC#CGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGC#CCCCC#CGX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CS", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XGC#CCCCC#CGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGC#CCCCC#CGX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XGC#CCCCC#CGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGC#CCCCC#CGX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XGC#CCCCC#CGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGC#CCCCC#CGX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XGC#CCCCC#CGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGC#CCCCC#CGX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XC#########CX", "XGC#CCCCC#CGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX")
                        .where("S", controller(blocks(pattern.getBlock())))
                        .where("G", blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where("C", heatingCoils())
                        .where("#", air())
                        .where("X", blocks(GTBlocks.CASING_INVAR_HEATPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_WASHING_PLANT = registerElectricMachine(
                "large_washing_plant",
                "Large Washing Plant",
                GTRecipeTypes.ORE_WASHER_RECIPES,
                1,
                GTBlocks.CASING_ALUMINIUM_FROSTPROOF,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/multiblock_workable"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GS", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX")
                        .where("S", controller(blocks(pattern.getBlock())))
                        .where("G", blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where("#", air())
                        .where("X", blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        BATTLE_TOWER_CENTRIFUGE = registerElectricMachine(
                "battle_tower_centrifuge",
                "Battle Tower Centrifuge",
                GTRecipeTypes.CENTRIFUGE_RECIPES,
                2,
                GTBlocks.CASING_STAINLESS_CLEAN,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/gcym/large_centrifuge"),
                pattern -> FactoryBlockPattern.start(RIGHT, UP, FRONT)
                        .aisle("######AAAAA######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######AAAAA######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######AAAAA######")
                        .aisle("####AAAAAAAAA####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####AAAAAAAAA####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####AAAAAAAAA####")
                        .aisle("###AAAAAAAAAAA###", "###C#DD###DD#C###", "###C#EE###EE#C###", "###C#EE###EE#C###", "###C#AA###AA#C###", "###C#EE###EE#C###", "###C#EE###EE#C###", "###CADDA#ADDAC###", "###AAAAAAAAAAA###", "###CADDA#ADDAC###", "###C#EE###EE#C###", "###C#EE###EE#C###", "###C#AA###AA#C###", "###C#EE###EE#C###", "###C#EE###EE#C###", "###C#DD###DD#C###", "###AAAAAAAAAAA###")
                        .aisle("##AAAAAAAAAAAAA##", "##C#D##D#D##D#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#A##A#A##A#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#D##D#D##D#C##", "##AAA##AAA##AAA##", "##C#D##D#D##D#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#A##A#A##A#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#D##D#D##D#C##", "##AAAAAAAAAAAAA##")
                        .aisle("##AAAAAAAAAAAAA##", "##C#D##D#D##D#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#A##A#A##A#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#D##D#D##D#C##", "##AAA##AAA##AAA##", "##C#D##D#D##D#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#A##A#A##A#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#D##D#D##D#C##", "##AAAAAAAAAAAAA##")
                        .aisle("#AAAAAAAAAAAAAAA#", "#C###DD###DD###C#", "#C###EE###EE###C#", "#C###EE###EE###C#", "#C###AA###AA###C#", "#C###EE###EE###C#", "#C###EE###EE###C#", "#C##ADDA#ADDA##C#", "#AAAAAAAAAAAAAAA#", "#C##ADDA#ADDA##C#", "#C###EE###EE###C#", "#C###EE###EE###C#", "#C###AA###AA###C#", "#C###EE###EE###C#", "#C###EE###EE###C#", "#C###DD###DD###C#", "#AAAAAAAAAAAAAAA#")
                        .aisle("#AAAAAAAAAAAAAAA#", "#C#############C#", "#C#############C#", "#C#############C#", "#C#####A#A#####C#", "#C#############C#", "#C#############C#", "#C#############C#", "#AAAAAAAAAAAAAAA#", "#C#############C#", "#C#############C#", "#C#############C#", "#C#####A#A#####C#", "#C#############C#", "#C#############C#", "#C#############C#", "#AAAAAAAAAAAAAAA#")
                        .aisle("AAAAAAAAAAAAAAAAA", "C#DD####A####DD#C", "C#EE####A####EE#C", "C#EE####A####EE#C", "C#AA###AAA###AA#C", "C#EE####A####EE#C", "C#EE####A####EE#C", "CADDA###A###ADDAC", "AAAAAAAAAAAAAAAAA", "CADDA###A###ADDAC", "C#EE####A####EE#C", "C#EE####A####EE#C", "C#AA###AAA###AA#C", "C#EE####A####EE#C", "C#EE####A####EE#C", "C#DD####A####DD#C", "AAAAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAAAA", "CD##D##AAA##D##DC", "CE##E##AAA##E##EC", "CE##E##AAA##E##EC", "CA##AAAAAAAAA##AC", "CE##E##AAA##E##EC", "CE##E##AAA##E##EC", "CD##D##AAA##D##DC", "AA##AAAAAAAAA##AA", "CD##D##AAA##D##DC", "CE##E##AAA##E##EC", "CE##E##AAA##E##EC", "CA##AAAAAAAAA##AC", "CE##E##AAA##E##EC", "CE##E##AAA##E##EC", "CD##D##AAA##D##DC", "AAAAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAAAA", "CD##D##AAA##D##DC", "CE##E##AAA##E##EC", "CE##E##AAA##E##EC", "CA##AAAAAAAAA##AC", "CE##E##AAA##E##EC", "CE##E##AAA##E##EC", "CD##D##AAA##D##DC", "AA##AAAAAAAAA##AA", "CD##D##AAA##D##DC", "CE##E##AAA##E##EC", "CE##E##AAA##E##EC", "CA##AAAAAAAAA##AC", "CE##E##AAA##E##EC", "CE##E##AAA##E##EC", "CD##D##AAA##D##DC", "AAAAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAAAA", "C#DD####A####DD#C", "C#EE####A####EE#C", "C#EE####A####EE#C", "C#AA###AAA###AA#C", "C#EE####A####EE#C", "C#EE####A####EE#C", "CADDA###A###ADDAC", "AAAAAAAAAAAAAAAAA", "CADDA###A###ADDAC", "C#EE####A####EE#C", "C#EE####A####EE#C", "C#AA###AAA###AA#C", "C#EE####A####EE#C", "C#EE####A####EE#C", "C#DD####A####DD#C", "AAAAAAAAAAAAAAAAA")
                        .aisle("#AAAAAAAAAAAAAAA#", "#C#############C#", "#C#############C#", "#C#############C#", "#C#####A#A#####C#", "#C#############C#", "#C#############C#", "#C#############C#", "#AAAAAAAAAAAAAAA#", "#C#############C#", "#C#############C#", "#C#############C#", "#C#####A#A#####C#", "#C#############C#", "#C#############C#", "#C#############C#", "#AAAAAAAAAAAAAAA#")
                        .aisle("#AAAAAAAAAAAAAAA#", "#C###DD###DD###C#", "#C###EE###EE###C#", "#C###EE###EE###C#", "#C###AA###AA###C#", "#C###EE###EE###C#", "#C###EE###EE###C#", "#C##ADDA#ADDA##C#", "#AAAAAAAAAAAAAAA#", "#C##ADDA#ADDA##C#", "#C###EE###EE###C#", "#C###EE###EE###C#", "#C###AA###AA###C#", "#C###EE###EE###C#", "#C###EE###EE###C#", "#C###DD###DD###C#", "#AAAAAAAAAAAAAAA#")
                        .aisle("##AAAAAAAAAAAAA##", "##C#D##D#D##D#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#A##A#A##A#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#D##D#D##D#C##", "##AAA##AAA##AAA##", "##C#D##D#D##D#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#A##A#A##A#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#D##D#D##D#C##", "##AAAAAAAAAAAAA##")
                        .aisle("##AAAAAAAAAAAAA##", "##C#D##D#D##D#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#A##A#A##A#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#D##D#D##D#C##", "##AAA##AAA##AAA##", "##C#D##D#D##D#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#A##A#A##A#C##", "##C#E##E#E##E#C##", "##C#E##E#E##E#C##", "##C#D##D#D##D#C##", "##AAAAAAAAAAAAA##")
                        .aisle("###AAAAAAAAAAA###", "###C#DD###DD#C###", "###C#EE###EE#C###", "###C#EE###EE#C###", "###C#AA###AA#C###", "###C#EE###EE#C###", "###C#EE###EE#C###", "###CADDA#ADDAC###", "###AAAAAAAAAAA###", "###CADDA#ADDAC###", "###C#EE###EE#C###", "###C#EE###EE#C###", "###C#AA###AA#C###", "###C#EE###EE#C###", "###C#EE###EE#C###", "###C#DD###DD#C###", "###AAAAAAAAAAA###")
                        .aisle("####AAAAAAAAA####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####AAAAAAAAA####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####CC#####CC####", "####AAAAAAAAA####")
                        .aisle("######AABAA######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######AAAAA######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######CCCCC######", "######AAAAA######")
                        .where("A", blocks(GTBlocks.CASING_STEEL_SOLID.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .where("B", controller(blocks(pattern.getBlock())))
                        .where("E", blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                        .where("C", blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                        .where("D", blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                        .where("#", air())
                        .build());

        LARGE_ELECTROLYZER = registerElectricMachine(
                "large_electrolyzer",
                "Large Electrolyzer",
                GTRecipeTypes.ELECTROLYZER_RECIPES,
                2,
                GTBlocks.CASING_ALUMINIUM_FROSTPROOF,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/gcym/large_electrolyzer"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GS", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XG#########GX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XGGGGGGGGGGGX", "XXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX")
                        .where("S", controller(blocks(pattern.getBlock())))
                        .where("G", blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where("#", air())
                        .where("X", blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LCR_CLUSTER = registerCoilMachine(
            "lcr_cluster",
            "LCR Cluster",
            GTRecipeTypes.LARGE_CHEMICAL_RECIPES,
            3,
            GTBlocks.CASING_PTFE_INERT,
            com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_inert_ptfe"),
            com.gregtechceu.gtceu.GTCEu.id("block/multiblock/large_chemical_reactor"),
            pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                    .aisle("AAA#AAA#AAA", "AAA#AAA#AAA", "AAA#AAA#AAA", "###########", "AAA#AAA#AAA", "AAA#AAA#AAA", "AAA#AAA#AAA", "###########", "AAA#AAA#AAA", "AAA#AAA#AAA", "AAA#AAA#AAA")
                    .aisle("AAA#AAA#AAA", "ABCCCBCCCBA", "ACA#ACA#ACA", "#C###C###C#", "ACA#ACA#ACA", "ABCCCBCCCBA", "ACA#ACA#ACA", "#C###C###C#", "ACA#ACA#ACA", "ABCCCBCCCBA", "AAA#AAA#AAA")
                    .aisle("AAA#AAA#AAA", "ACA#ACA#ACA", "AAAAAAAAAAA", "##AAA#AAA##", "AAAAAAAAAAA", "ACA#ACA#ACA", "AAAAAAAAAAA", "##AAA#AAA##", "AAAAAAAAAAA", "ACA#ACA#ACA", "AAA#AAA#AAA")
                    .aisle("###########", "#C###C###C#", "##AAA#AAA##", "##ABCCCBA##", "##ACA#ACA##", "#C#C#C#C#C#", "##ACA#ACA##", "##ABCCCBA##", "##AAA#AAA##", "#C###C###C#", "###########")
                    .aisle("AAA#AAA#AAA", "ACA#ACA#ACA", "AAAAAAAAAAA", "##ACA#ACA##", "AAAAAAAAAAA", "ACA#ACA#ACA", "AAAAAAAAAAA", "##ACA#ACA##", "AAAAAAAAAAA", "ACA#ACA#ACA", "AAA#AAA#AAA")
                    .aisle("AAA#AAA#AAA", "ABCCCBCCCBA", "ACA#ACA#ACA", "#C#C#C#C#C#", "ACA#ACA#ACA", "ABCCCBCCCBD", "ACA#ACA#ACA", "#C#C#C#C#C#", "ACA#ACA#ACA", "ABCCCBCCCBA", "AAA#AAA#AAA")
                    .aisle("AAA#AAA#AAA", "ACA#ACA#ACA", "AAAAAAAAAAA", "##ACA#ACA##", "AAAAAAAAAAA", "ACA#ACA#ACA", "AAAAAAAAAAA", "##ACA#ACA##", "AAAAAAAAAAA", "ACA#ACA#ACA", "AAA#AAA#AAA")
                    .aisle("###########", "#C###C###C#", "##AAA#AAA##", "##ABCCCBA##", "##ACA#ACA##", "#C#C#C#C#C#", "##ACA#ACA##", "##ABCCCBA##", "##AAA#AAA##", "#C###C###C#", "###########")
                    .aisle("AAA#AAA#AAA", "ACA#ACA#ACA", "AAAAAAAAAAA", "##AAA#AAA##", "AAAAAAAAAAA", "ACA#ACA#ACA", "AAAAAAAAAAA", "##AAA#AAA##", "AAAAAAAAAAA", "ACA#ACA#ACA", "AAA#AAA#AAA")
                    .aisle("AAA#AAA#AAA", "ABCCCBCCCBA", "ACA#ACA#ACA", "#C###C###C#", "ACA#ACA#ACA", "ABCCCBCCCBA", "ACA#ACA#ACA", "#C###C###C#", "ACA#ACA#ACA", "ABCCCBCCCBA", "AAA#AAA#AAA")
                    .aisle("AAA#AAA#AAA", "AAA#AAA#AAA", "AAA#AAA#AAA", "###########", "AAA#AAA#AAA", "AAA#AAA#AAA", "AAA#AAA#AAA", "###########", "AAA#AAA#AAA", "AAA#AAA#AAA", "AAA#AAA#AAA")
                    .where("B", blocks(GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE.get()))
                    .where("C", heatingCoils())
                    .where("A", blocks(GTBlocks.CASING_PTFE_INERT.get())
                            .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                            .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                            .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                            .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                    .where("D", controller(blocks(pattern.getBlock())))
                    .where("#", air())
                    .build());

        LARGE_ASSEMBLY_PLANT = registerElectricMachine(
                "large_assembly_plant",
                "Large Assembly Plant",
                GTRecipeTypes.ASSEMBLER_RECIPES,
                3,
                GTBlocks.CASING_STEEL_SOLID,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/gcym/large_assembler"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GS", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XG###########GX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX")
                        .where("S", controller(blocks(pattern.getBlock())))
                        .where("G", blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where("#", air())
                        .where("X", blocks(GTBlocks.CASING_STEEL_SOLID.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(2))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_ARC_FURNACE = registerCoilMachine(
                "large_arc_furnace",
                "Large Arc Furnace",
                GTRecipeTypes.ARC_FURNACE_RECIPES,
                3,
                GTBlocks.CASING_INVAR_HEATPROOF,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_heatproof"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/gcym/large_arc_smelter"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CS", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGCCCCCCCCCCCGX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XC###########CX", "XGCCCCCCCCCCCGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX")
                        .where("S", controller(blocks(pattern.getBlock())))
                        .where("G", blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where("C", heatingCoils())
                        .where("#", air())
                        .where("X", blocks(GTBlocks.CASING_INVAR_HEATPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_DISTILLATION_TOWER = registerElectricMachine(
                "large_distillation_tower",
                "Large Distillation Tower",
                GTRecipeTypes.DISTILLATION_RECIPES,
                3,
                GTBlocks.CASING_STAINLESS_CLEAN,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/distillation_tower"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PS", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XP###########PX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XPPPPPPPPPPPPPX", "XXXXXXXXXXXXXXX")
                        .aisle("XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX")
                        .where("S", controller(blocks(pattern.getBlock())))
                        .where("P", blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where("#", air())
                        .where("X", blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        ETERNAL_FORCE_FREEZER = registerElectricMachine(
                "eternal_force_freezer",
                "Eternal Force Freezer",
                GTRecipeTypes.VACUUM_RECIPES,
                3,
                GTBlocks.CASING_ALUMINIUM_FROSTPROOF,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/vacuum_freezer"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("####AAAAA####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####AAAAA####")
                        .aisle("###AAAAAAA###", "###B#####B###", "###B##C##B###", "###B#####B###", "###B#####B###", "###B#####B###", "###B#####B###", "###B#####B###", "###B##C##B###", "###B#####B###", "###AAAAAAA###")
                        .aisle("##AAAAAAAAA##", "##B#######B##", "##B#######B##", "##B###C###B##", "##B#######B##", "##B#######B##", "##B#######B##", "##B###C###B##", "##B#######B##", "##B#######B##", "##AAAAAAAAA##")
                        .aisle("#AAAAAAAAAAA#", "#B#########B#", "#B#########B#", "#B#########B#", "#B####C####B#", "#B#########B#", "#B####C####B#", "#B#########B#", "#B#########B#", "#B#########B#", "#AAAAAAAAAAA#")
                        .aisle("AAAAAAAAAAAAD", "B#####C#####B", "B#####C#####B", "B#####C#####B", "B#####C#####B", "B#####C#####B", "B#####C#####B", "B#####C#####B", "B#####C#####B", "B#####C#####B", "AAAAAAAAAAAAA")
                        .aisle("#AAAAAAAAAAA#", "#B#########B#", "#B#########B#", "#B#########B#", "#B####C####B#", "#B#########B#", "#B####C####B#", "#B#########B#", "#B#########B#", "#B#########B#", "#AAAAAAAAAAA#")
                        .aisle("##AAAAAAAAA##", "##B#######B##", "##B#######B##", "##B###C###B##", "##B#######B##", "##B#######B##", "##B#######B##", "##B###C###B##", "##B#######B##", "##B#######B##", "##AAAAAAAAA##")
                        .aisle("###AAAAAAA###", "###B#####B###", "###B##C##B###", "###B#####B###", "###B#####B###", "###B#####B###", "###B#####B###", "###B#####B###", "###B##C##B###", "###B#####B###", "###AAAAAAA###")
                        .aisle("####AAAAA####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####BBBBB####", "####AAAAA####")
                        .where("A", blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .where("D", controller(blocks(pattern.getBlock())))
                        .where("C", blocks(Blocks.BLUE_ICE))
                        .where("B", blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                        .where("#", air())
                        .build());
    }

    private static MultiblockMachineDefinition registerElectricMachine(
            String name,
            String langValue,
            com.gregtechceu.gtceu.api.recipe.GTRecipeType recipeType,
            int tier,
            com.tterrag.registrate.util.entry.BlockEntry<? extends net.minecraft.world.level.block.Block> appearanceBlock,
            ResourceLocation casingTexture,
            ResourceLocation overlayModel,
            java.util.function.Function<com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition,
                    com.gregtechceu.gtceu.api.pattern.BlockPattern> patternProvider) {
        int defaultParallel = STParallelMultiblockMachine.getDisplayParallelCount(tier);
        return STRegistration.REGISTRATE
                .multiblock(name, STParallelMultiblockMachine::new)
                .rotationState(RotationState.ALL)
                .langValue(langValue)
                .tier(tier)
                .recipeType(recipeType)
                .recipeModifiers(STParallelMultiblockMachine::recipeModifier, BATCH_MODE)
                .appearanceBlock(appearanceBlock)
                .pattern(patternProvider)
                .workableCasingModel(casingTexture, overlayModel)
                .tooltipBuilder((stack, tooltips) ->
                        tooltips.add(net.minecraft.network.chat.Component.translatable(
                                "shishamo_tech.machine.parallel_count", defaultParallel * STConfig.PARALLEL_MULTIPLIER.get())))
                .register();
    }

    private static MultiblockMachineDefinition registerCoilMachine(
            String name,
            String langValue,
            com.gregtechceu.gtceu.api.recipe.GTRecipeType recipeType,
            int tier,
            com.tterrag.registrate.util.entry.BlockEntry<? extends net.minecraft.world.level.block.Block> appearanceBlock,
            ResourceLocation casingTexture,
            ResourceLocation overlayModel,
            java.util.function.Function<com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition,
                    com.gregtechceu.gtceu.api.pattern.BlockPattern> patternProvider) {
        int defaultParallel = STCoilParallelMultiblockMachine.getDisplayParallelCount(tier, 0);
        return STRegistration.REGISTRATE
                .multiblock(name, STCoilParallelMultiblockMachine::new)
                .rotationState(RotationState.ALL)
                .langValue(langValue)
                .tier(tier)
                .recipeType(recipeType)
                .recipeModifiers(STCoilParallelMultiblockMachine::recipeModifier, BATCH_MODE)
                .appearanceBlock(appearanceBlock)
                .pattern(patternProvider)
                .workableCasingModel(casingTexture, overlayModel)
                .tooltipBuilder((stack, tooltips) -> {
                    tooltips.add(net.minecraft.network.chat.Component.translatable(
                            "shishamo_tech.machine.parallel_count", defaultParallel * STConfig.PARALLEL_MULTIPLIER.get()));
                })
                .register();
    }
}
