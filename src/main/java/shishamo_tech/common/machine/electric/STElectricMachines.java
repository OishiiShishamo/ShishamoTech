package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import net.minecraft.resources.ResourceLocation;
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
    public static MultiblockMachineDefinition LARGE_CENTRIFUGE;
    public static MultiblockMachineDefinition LARGE_ELECTROLYZER;
    public static MultiblockMachineDefinition LARGE_CHEMICAL_PLANT;
    public static MultiblockMachineDefinition LARGE_ASSEMBLY_PLANT;
    public static MultiblockMachineDefinition LARGE_ARC_FURNACE;
    public static MultiblockMachineDefinition LARGE_DISTILLATION_TOWER;
    public static MultiblockMachineDefinition LARGE_VACUUM_FREEZER;

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
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_STEEL_SOLID.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
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
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where('C', heatingCoils())
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_INVAR_HEATPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
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
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_CENTRIFUGE = registerElectricMachine(
                "large_centrifuge",
                "Large Centrifuge",
                GTRecipeTypes.CENTRIFUGE_RECIPES,
                2,
                GTBlocks.CASING_STAINLESS_CLEAN,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/gcym/large_centrifuge"),
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
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
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
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_CHEMICAL_PLANT = registerCoilMachine(
            "large_chemical_plant",
            "Large Chemical Plant",
            GTRecipeTypes.CHEMICAL_RECIPES,
            3,
            GTBlocks.CASING_PTFE_INERT,
            com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_inert_ptfe"),
            com.gregtechceu.gtceu.GTCEu.id("block/multiblock/large_chemical_reactor"),
            pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                .aisle("XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGS", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XGGGGGGGGGGGGGX", "XGCCCCCCCCCCCGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGC#########CGX", "XGCCCCCCCCCCCGX", "XGGGGGGGGGGGGGX", "XXXXXXXXXXXXXXX")
                .aisle("XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX")
                .where('S', controller(blocks(pattern.getBlock())))
                .where('G', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                .where('C', heatingCoils())
                .where('#', air())
                .where('X', blocks(GTBlocks.CASING_PTFE_INERT.get())
                    .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                    .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                    .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                    .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                    .or(abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
                    .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
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
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_STEEL_SOLID.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(2))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
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
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where('C', heatingCoils())
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_INVAR_HEATPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
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
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('P', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_VACUUM_FREEZER = registerElectricMachine(
                "large_vacuum_freezer",
                "Large Vacuum Freezer",
                GTRecipeTypes.VACUUM_RECIPES,
                3,
                GTBlocks.CASING_ALUMINIUM_FROSTPROOF,
                com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/vacuum_freezer"),
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
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
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
