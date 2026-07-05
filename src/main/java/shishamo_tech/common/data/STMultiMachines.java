package shishamo_tech.common.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import net.minecraft.world.level.block.Blocks;
import shishamo_tech.ShishamoTech;

import java.util.function.Function;

import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.PARALLEL_HATCH;
import static shishamo_tech.common.machine.ae2.STAE2Machines.registerInscriber;
import static shishamo_tech.common.machine.electric.STElectricMachines.registerCoilMachine;
import static shishamo_tech.common.machine.electric.STElectricMachines.registerElectricMachine;
import static shishamo_tech.common.machine.steam.STSteamMachines.registerSteamMachine;

public class STMultiMachines {
    public static MultiblockMachineDefinition MEGA_STEAM_GRINDER;
    public static MultiblockMachineDefinition MEGA_STEAM_FURNACE;
    public static MultiblockMachineDefinition MEGA_STEAM_COMPRESSOR;
    public static MultiblockMachineDefinition MEGA_STEAM_EXTRACTOR;
    public static MultiblockMachineDefinition MEGA_STEAM_HAMMER;
    public static MultiblockMachineDefinition MEGA_STEAM_ALLOY_SMELTER;
    public static MultiblockMachineDefinition MEGA_STEAM_ROCK_CRUSHER;

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
    public static MultiblockMachineDefinition NON_OMNIPOTENT_UNIVERSE_FORGE;

    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_MV;
    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_HV;
    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_EV;
    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_IV;

    private static final String ALL_X = "XXXXXXXXXXX";
    private static final String PIPE_ROW = "XGGGGGGGGGX";
    private static final String INNER_ROW = "XG#######GX";
    private static final String CTRL_ROW = "XG#######GS";
    private static final String FIREBOX_ROW = "FFFFFFFFFFF";

    private static String[] steamBuildRows(int depth, int ctrlDepth, boolean hasFirebox) {
        String[] rows = new String[11];
        boolean isWall = depth == 0 || depth == 10;
        boolean isPipe = depth == 1 || depth == 9;
        for (int r = 0; r < 11; r++) {
            boolean isBottom = r == 0;
            boolean isTop = r == 10;
            boolean isPipeRow = r == 1 || r == 9;
            boolean isAir = r >= 2 && r <= 8;
            if (hasFirebox && isBottom) {
                rows[r] = FIREBOX_ROW;
            } else if (isWall) {
                rows[r] = ALL_X;
            } else if (isPipe) {
                rows[r] = (isTop || isBottom) ? ALL_X : PIPE_ROW;
            } else if (isPipeRow) {
                rows[r] = PIPE_ROW;
            } else if (isAir) {
                rows[r] = (depth == ctrlDepth && r == 5) ? CTRL_ROW : INNER_ROW;
            } else {
                rows[r] = ALL_X;
            }
        }
        return rows;
    }

    private static Function<MultiblockMachineDefinition, BlockPattern> steamSharedPattern(boolean hasFirebox) {
        int ctrlDepth = 5;
        return pattern -> {
            var builder = FactoryBlockPattern.start(FRONT, UP, RIGHT);
            for (int d = 0; d < 11; d++) {
                builder.aisle(steamBuildRows(d, ctrlDepth, hasFirebox));
            }
            builder.where("S", controller(blocks(pattern.getBlock())))
                    .where("G", blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                    .where("#", air());
            if (hasFirebox) {
                builder.where("F", blocks(GTBlocks.FIREBOX_BRONZE.get())
                        .or(abilities(PartAbility.STEAM).setExactLimit(1)));
                builder.where("X", blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                        .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                        .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1)));
            } else {
                builder.where("X", blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                        .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                        .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1))
                        .or(abilities(PartAbility.STEAM).setExactLimit(1)));
            }
            return builder.build();
        };
    }

    public static void steamInit() {
        MEGA_STEAM_GRINDER = registerSteamMachine(
                "mega_steam_grinder", "Mega Steam Grinder",
                GTRecipeTypes.MACERATOR_RECIPES,
                GTCEu.id("block/multiblock/steam_grinder"),
                steamSharedPattern(false));

        MEGA_STEAM_FURNACE = registerSteamMachine(
                "mega_steam_furnace", "Mega Steam Furnace",
                GTRecipeTypes.FURNACE_RECIPES,
                GTCEu.id("block/multiblock/steam_oven"),
                steamSharedPattern(true));

        MEGA_STEAM_COMPRESSOR = registerSteamMachine(
                "mega_steam_compressor", "Mega Steam Compressor",
                GTRecipeTypes.COMPRESSOR_RECIPES,
                GTCEu.id("block/multiblock/implosion_compressor"),
                steamSharedPattern(false));

        MEGA_STEAM_EXTRACTOR = registerSteamMachine(
                "mega_steam_extractor", "Mega Steam Extractor",
                GTRecipeTypes.EXTRACTOR_RECIPES,
                GTCEu.id("block/multiblock/multiblock_workable"),
                steamSharedPattern(false));

        MEGA_STEAM_HAMMER = registerSteamMachine(
                "mega_steam_hammer", "Mega Steam Hammer",
                GTRecipeTypes.FORGE_HAMMER_RECIPES,
                GTCEu.id("block/multiblock/multiblock_workable"),
                steamSharedPattern(false));

        MEGA_STEAM_ALLOY_SMELTER = registerSteamMachine(
                "mega_steam_alloy_smelter", "Mega Steam Alloy Smelter",
                GTRecipeTypes.ALLOY_SMELTER_RECIPES,
                GTCEu.id("block/multiblock/steam_oven"),
                steamSharedPattern(true));

        MEGA_STEAM_ROCK_CRUSHER = registerSteamMachine(
                "mega_steam_rock_crusher", "Mega Steam Rock Crusher",
                GTRecipeTypes.ROCK_BREAKER_RECIPES,
                GTCEu.id("block/multiblock/multiblock_workable"),
                steamSharedPattern(false));
    }

    public static void electricInit() {
        LARGE_GRINDING_PLANT = registerElectricMachine(
                "large_grinding_plant",
                "Large Grinding Plant",
                GTRecipeTypes.MACERATOR_RECIPES,
                1,
                GTBlocks.CASING_STEEL_SOLID,
                GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                GTCEu.id("block/multiblock/gcym/large_maceration_tower"),
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
                2,
                GTBlocks.CASING_INVAR_HEATPROOF,
                GTCEu.id("block/casings/solid/machine_casing_heatproof"),
                GTCEu.id("block/multiblock/multi_furnace"),
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
                GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
                GTCEu.id("block/multiblock/multiblock_workable"),
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
                GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                GTCEu.id("block/multiblock/gcym/large_centrifuge"),
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
                GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
                GTCEu.id("block/multiblock/gcym/large_electrolyzer"),
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
                GTCEu.id("block/casings/solid/machine_casing_inert_ptfe"),
                GTCEu.id("block/multiblock/large_chemical_reactor"),
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
                GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                GTCEu.id("block/multiblock/gcym/large_assembler"),
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
                GTCEu.id("block/casings/solid/machine_casing_heatproof"),
                GTCEu.id("block/multiblock/gcym/large_arc_smelter"),
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
                GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                GTCEu.id("block/multiblock/distillation_tower"),
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
                GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
                GTCEu.id("block/multiblock/vacuum_freezer"),
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

        NON_OMNIPOTENT_UNIVERSE_FORGE = registerElectricMachine(
                        "non_omnipotent_universe_forge",
                        "(non) Omnipotent Universe Forge",
                        GTRecipeTypes.ASSEMBLY_LINE_RECIPES,
                        8,
                        STBlocks.CASING_NOUF_GENERAL,
                        ShishamoTech.id("block/casings/nouf/general"),
                        GTCEu.id("block/multiblock/distillation_tower"),
                        pattern -> FactoryBlockPattern.start(RIGHT, UP, FRONT)
                        .aisle("###AAAAAAAAAAAAAAAAA###", "##ACCCCCAAAAAAACCCCCA##", "#ACCCCDDEEEEEEEDDCCCCA#", "ACCCCDEEDDDDDDDEEDCCCCA", "ACCCDEDDDDDDDDDDDEDCCCA", "ACCDEDDDDDBBBDDDDDEDCCA", "ACDEDDDDDBDDDBDDDDDEDCA", "ACDEDDDDBDDDDDDDDDDEDCA", "ADEDDDDDBDDDDBBBDDDDEDA", "ADEDDDBDBDDDBDDDBDDDEDA", "ADEDDBDDDBDBDDDDDBDDEDA", "ADEDDBDDDDBBBDDDDBDDEDA", "ADEDDBDDDDDBDBDDDBDDEDA", "ADEDDDBDDDBDDDBDBDDDEDA", "ADEDDDDBBBDDDDBDDDDDEDA", "ACDEDDDDDDDDDDBDDDDEDCA", "ACDEDDDDDBDDDBDDDDDEDCA", "ACCDEDDDDDBBBDDDDDEDCCA", "ACCCDEDDDDDDDDDDDEDCCCA", "ACCCCDEEDDDDDDDEEDCCCCA", "#ACCCCDDEEEEEEEDDCCCCA#", "##ACCCCCDDDDDDDCCCCCA##", "###AAAAAAAAAAAAAAAAA###")
                        .aisle("###AAAAAAAAAAAAAAAAA###", "##A#################A##", "#A###################A#", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "#A###################A#", "##A#################A##", "###AAAAAAAAAAAAAAAAA###")
                        .aisle("###AAAA#########AAAA###", "##A#################A##", "#A###################A#", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "#A###################A#", "##A#################A##", "###AAAA#########AAAA###")
                        .aisle("###A###############A###", "##A#################A##", "#A###################A#", "A#####################A", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "A#####################A", "#A###################A#", "##A#################A##", "###A###############A###")
                        .aisle("#######################", "##A#################A##", "#A###################A#", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#A###################A#", "##A#################A##", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("###########B###########", "###########B###########", "########DDDDDDD########", "######DDDFFFFFDDD######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "###DDFD#########DFDD###", "###DFD###########DFD###", "##DDFD###########DFDD##", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "##DDFD###########DFDD##", "###DFD###########DFD###", "###DDFD#########DFDD###", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "######DDDFFFFFDDD######", "########DDDDDDD########", "###########B###########", "###########B###########")
                        .aisle("##########BBB##########", "########AAAAAAA########", "######AADDDDDDDAA######", "#####ADDD#####DDDA#####", "####ADD##DDDDD##DDA####", "###AD##DD#BBB#DD##DA###", "##ADD#D####B####D#DDA##", "##AD#D#####B#####D#DA##", "#ADD#D###########D#DDA#", "#AD#D#############D#DA#", "BAD#DB###########BD#DAB", "BAD#DBBB#######BBBD#DAB", "BAD#DB###########BD#DAB", "#AD#D#############D#DA#", "#ADD#D###########D#DDA#", "##AD#D#####B#####D#DA##", "##ADD#D####B####D#DDA##", "###AD##DD#BBB#DD##DA###", "####ADD##DDDDD##DDA####", "#####ADDD#####DDDA#####", "######AADDDDDDDAA######", "########AAAAAAA########", "##########BBB##########")
                        .aisle("###########B###########", "###########B###########", "#########DDDDD#########", "#######DDFFFFFDD#######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "####DFD#########DFD####", "###DFD###########DFD###", "###DFD###########DFD###", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "###DFD###########DFD###", "###DFD###########DFD###", "####DFD#########DFD####", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "#######DDFFFFFDD#######", "#########DDDDD#########", "###########B###########", "###########B###########")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("###########B###########", "###########B###########", "########DDDDDDD########", "######DDDFFFFFDDD######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "###DDFD#########DFDD###", "###DFD###########DFD###", "##DDFD###########DFDD##", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "##DDFD###########DFDD##", "###DFD###########DFD###", "###DDFD#########DFDD###", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "######DDDFFFFFDDD######", "########DDDDDDD########", "###########B###########", "###########B###########")
                        .aisle("##########BBB##########", "########AAAAAAA########", "######AADDDDDDDAA######", "#####ADDD#####DDDA#####", "####ADD##DDDDD##DDA####", "###AD##DD#BBB#DD##DA###", "##ADD#D####B####D#DDA##", "##AD#D#####B#####D#DA##", "#ADD#D###########D#DDA#", "#AD#D#############D#DA#", "BAD#DB###########BD#DAB", "BAD#DBBB#######BBBD#DAB", "BAD#DB###########BD#DAB", "#AD#D#############D#DA#", "#ADD#D###########D#DDA#", "##AD#D#####B#####D#DA##", "##ADD#D####B####D#DDA##", "###AD##DD#BBB#DD##DA###", "####ADD##DDDDD##DDA####", "#####ADDD#####DDDA#####", "######AADDDDDDDAA######", "########AAAAAAA########", "##########BBB##########")
                        .aisle("###########B###########", "###########B###########", "#########DDDDD#########", "#######DDFFFFFDD#######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "####DFD#########DFD####", "###DFD###########DFD###", "###DFD###########DFD###", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "###DFD###########DFD###", "###DFD###########DFD###", "####DFD#########DFD####", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "#######DDFFFFFDD#######", "#########DDDDD#########", "###########B###########", "###########B###########")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("###########B###########", "###########B###########", "########DDDDDDD########", "######DDDFFFFFDDD######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "###DDFD#########DFDD###", "###DFD###########DFD###", "##DDFD###########DFDD##", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "##DDFD###########DFDD##", "###DFD###########DFD###", "###DDFD#########DFDD###", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "######DDDFFFFFDDD######", "########DDDDDDD########", "###########B###########", "###########B###########")
                        .aisle("##########BBB##########", "########AAAAAAA########", "######AADDDDDDDAA######", "#####ADDD#####DDDA#####", "####ADD##DDDDD##DDA####", "###AD##DD#BBB#DD##DA###", "##ADD#D####B####D#DDA##", "##AD#D#####B#####D#DA##", "#ADD#D###########D#DDA#", "#AD#D#############D#DA#", "BAD#DB###########BD#DAB", "BAD#DBBB#######BBBD#DAB", "BAD#DB###########BD#DAB", "#AD#D#############D#DA#", "#ADD#D###########D#DDA#", "##AD#D#####B#####D#DA##", "##ADD#D####B####D#DDA##", "###AD##DD#BBB#DD##DA###", "####ADD##DDDDD##DDA####", "#####ADDD#####DDDA#####", "######AADDDDDDDAA######", "########AAAAAAA########", "##########BBB##########")
                        .aisle("###########B###########", "###########B###########", "#########DDDDD#########", "#######DDFFFFFDD#######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "####DFD#########DFD####", "###DFD###########DFD###", "###DFD###########DFD###", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "###DFD###########DFD###", "###DFD###########DFD###", "####DFD#########DFD####", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "#######DDFFFFFDD#######", "#########DDDDD#########", "###########B###########", "###########B###########")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("###########B###########", "###########B###########", "########DDDDDDD########", "######DDDFFFFFDDD######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "###DDFD#########DFDD###", "###DFD###########DFD###", "##DDFD###########DFDD##", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "##DDFD###########DFDD##", "###DFD###########DFD###", "###DDFD#########DFDD###", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "######DDDFFFFFDDD######", "########DDDDDDD########", "###########B###########", "###########B###########")
                        .aisle("##########BBB##########", "########AAAAAAA########", "######AADDDDDDDAA######", "#####ADDD#####DDDA#####", "####ADD##DDDDD##DDA####", "###AD##DD#BBB#DD##DA###", "##ADD#D####B####D#DDA##", "##AD#D#####B#####D#DA##", "#ADD#D###########D#DDA#", "#AD#D#############D#DA#", "BAD#DB###########BD#DAB", "BAD#DBBB#######BBBD#DAB", "BAD#DB###########BD#DAB", "#AD#D#############D#DA#", "#ADD#D###########D#DDA#", "##AD#D#####B#####D#DA##", "##ADD#D####B####D#DDA##", "###AD##DD#BBB#DD##DA###", "####ADD##DDDDD##DDA####", "#####ADDD#####DDDA#####", "######AADDDDDDDAA######", "########AAAAAAA########", "##########BBB##########")
                        .aisle("###########B###########", "###########B###########", "#########DDDDD#########", "#######DDFFFFFDD#######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "####DFD#########DFD####", "###DFD###########DFD###", "###DFD###########DFD###", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "###DFD###########DFD###", "###DFD###########DFD###", "####DFD#########DFD####", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "#######DDFFFFFDD#######", "#########DDDDD#########", "###########B###########", "###########B###########")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("###########B###########", "###########B###########", "########DDDDDDD########", "######DDDFFFFFDDD######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "###DDFD#########DFDD###", "###DFD###########DFD###", "##DDFD###########DFDD##", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "##DDFD###########DFDD##", "###DFD###########DFD###", "###DDFD#########DFDD###", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "######DDDFFFFFDDD######", "########DDDDDDD########", "###########B###########", "###########B###########")
                        .aisle("##########BBB##########", "########AAAAAAA########", "######AADDDDDDDAA######", "#####ADDD#####DDDA#####", "####ADD##DDDDD##DDA####", "###AD##DD#BBB#DD##DA###", "##ADD#D####B####D#DDA##", "##AD#D#####B#####D#DA##", "#ADD#D###########D#DDA#", "#AD#D#############D#DA#", "BAD#DB###########BD#DAB", "BAD#DBBB#######BBBD#DAB", "BAD#DB###########BD#DAB", "#AD#D#############D#DA#", "#ADD#D###########D#DDA#", "##AD#D#####B#####D#DA##", "##ADD#D####B####D#DDA##", "###AD##DD#BBB#DD##DA###", "####ADD##DDDDD##DDA####", "#####ADDD#####DDDA#####", "######AADDDDDDDAA######", "########AAAAAAA########", "##########BBB##########")
                        .aisle("###########B###########", "###########B###########", "#########DDDDD#########", "#######DDFFFFFDD#######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "####DFD#########DFD####", "###DFD###########DFD###", "###DFD###########DFD###", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "###DFD###########DFD###", "###DFD###########DFD###", "####DFD#########DFD####", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "#######DDFFFFFDD#######", "#########DDDDD#########", "###########B###########", "###########B###########")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("###########B###########", "###########B###########", "########DDDDDDD########", "######DDDFFFFFDDD######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "###DDFD#########DFDD###", "###DFD###########DFD###", "##DDFD###########DFDD##", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "##DDFD###########DFDD##", "###DFD###########DFD###", "###DDFD#########DFDD###", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "######DDDFFFFFDDD######", "########DDDDDDD########", "###########B###########", "###########B###########")
                        .aisle("##########BBB##########", "########AAAAAAA########", "######AADDDDDDDAA######", "#####ADDD#####DDDA#####", "####ADD##DDDDD##DDA####", "###AD##DD#BBB#DD##DA###", "##ADD#D####B####D#DDA##", "##AD#D#####B#####D#DA##", "#ADD#D###########D#DDA#", "#AD#D#############D#DA#", "BAD#DB###########BD#DAB", "BAD#DBBB#######BBBD#DAB", "BAD#DB###########BD#DAB", "#AD#D#############D#DA#", "#ADD#D###########D#DDA#", "##AD#D#####B#####D#DA##", "##ADD#D####B####D#DDA##", "###AD##DD#BBB#DD##DA###", "####ADD##DDDDD##DDA####", "#####ADDD#####DDDA#####", "######AADDDDDDDAA######", "########AAAAAAA########", "##########BBB##########")
                        .aisle("###########B###########", "###########B###########", "#########DDDDD#########", "#######DDFFFFFDD#######", "#####DDFFDDDDDFFDD#####", "####DFFDD##B##DDFFD####", "####DFD#########DFD####", "###DFD###########DFD###", "###DFD###########DFD###", "##DFD#############DFD##", "##DFD#############DFD##", "BBDFDB###########BDFDBB", "##DFD#############DFD##", "##DFD#############DFD##", "###DFD###########DFD###", "###DFD###########DFD###", "####DFD#########DFD####", "####DFFDD##B##DDFFD####", "#####DDFFDDDDDFFDD#####", "#######DDFFFFFDD#######", "#########DDDDD#########", "###########B###########", "###########B###########")
                        .aisle("#######################", "#######################", "#######################", "#########DDDDD#########", "#######DD#####DD#######", "#####DD#########DD#####", "#####D###########D#####", "####D#############D####", "####D#############D####", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "###D###############D###", "####D#############D####", "####D#############D####", "#####D###########D#####", "#####DD#########DD#####", "#######DD#####DD#######", "#########DDDDD#########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###B###############B###", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "###########B###########", "#######################", "#######################", "#######################")
                        .aisle("#######################", "##A#################A##", "#A###################A#", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#A###################A#", "##A#################A##", "#######################")
                        .aisle("###A###############A###", "##A#################A##", "#A###################A#", "A#####################A", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "A#####################A", "#A###################A#", "##A#################A##", "###A###############A###")
                        .aisle("###AAAA#########AAAA###", "##A#################A##", "#A###################A#", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "#######################", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "#A###################A#", "##A#################A##", "###AAAA#########AAAA###")
                        .aisle("###AAAAAAAAAAAAAAAAA###", "##A#################A##", "#A###################A#", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "A#####################A", "#A###################A#", "##A#################A##", "###AAAAAAAAAAAAAAAAA###")
                        .aisle("###AAAAAAAAAAAAAAAAA###", "##ACCCCCAAAAAAACCCCCA##", "#ACCCCDDEEEEEEEDDCCCCA#", "ACCCCDEEDDDDDDDEEDCCCCA", "ACCCDEDDDDDDDDDDDEDCCCA", "ACCDEDDDDDBBBDDDDDEDCCA", "ACDEDDDDDBDDDBDDDDDEDCA", "ACDEDDDDBDDDDDDDDDDEDCA", "ADEDDDDDBDDDDBBBDDDDEDA", "ADEDDDBDBDDDBDDDBDDDEDA", "ADEDDBDDDBDBDDDDDBDDEDA", "ADEDDBDDDDBLBDDDDBDDEDA", "ADEDDBDDDDDBDBDDDBDDEDA", "ADEDDDBDDDBDDDBDBDDDEDA", "ADEDDDDBBBDDDDBDDDDDEDA", "ACDEDDDDDDDDDDBDDDDEDCA", "ACDEDDDDDBDDDBDDDDDEDCA", "ACCDEDDDDDBBBDDDDDEDCCA", "ACCCDEDDDDDDDDDDDEDCCCA", "ACCCCDEEDDDDDDDEEDCCCCA", "#ACCCCDDEEEEEEEDDCCCCA#", "##ACCCCCDDDDDDDCCCCCA##", "###AAAAAAAAAAAAAAAAA###")
                        .where("L", controller(blocks(pattern.getBlock())))
                        .where("D", blocks(STBlocks.CASING_NOUF_GENERAL.get())
                                        .or(abilities(PartAbility.IMPORT_ITEMS).setMaxGlobalLimited(2))
                                        .or(abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(2))
                                        .or(abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(5))
                                        .or(abilities(PartAbility.EXPORT_ITEMS).setExactLimit(1))
                                        .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                                        .or(abilities(PartAbility.PARALLEL_HATCH).setMaxGlobalLimited(1))
                                        .or(abilities(PartAbility.DATA_ACCESS).setMaxGlobalLimited(1))
                                        .or(abilities(PartAbility.INPUT_LASER).setMaxGlobalLimited(10))
                        )
                        .where("A", blocks(STBlocks.CASING_SOLID_MIRACLE_FUMETSU.get()))
                        .where("C", blocks(STBlocks.CASING_SOLID_MIRACLE_METEOR.get()))
                        .where("E", blocks(STBlocks.CASING_SOLID_SPACETIME.get()))
                        .where("B", blocks(STBlocks.CASING_NOUF_MAGICAL.get()))
                        .where("F", blocks(STBlocks.CASING_NOUF_WAVE.get()))
                        .where("#", air())
                        .build()
                );
    }

    public static void AEInit() {
        PRESS_FREE_INSCRIBER_MV = registerInscriber(
                "press_free_inscriber_mv",
                "Press-Free Inscriber MK-I",
                2,
                GTBlocks.CASING_STEEL_SOLID,
                GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                GTCEu.id("block/multiblock/gcym/large_material_press"));

        PRESS_FREE_INSCRIBER_HV = registerInscriber(
                "press_free_inscriber_hv",
                "Press-Free Inscriber MK-II",
                4,
                GTBlocks.CASING_STAINLESS_CLEAN,
                GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                GTCEu.id("block/multiblock/gcym/large_material_press"));

        PRESS_FREE_INSCRIBER_EV = registerInscriber(
                "press_free_inscriber_ev",
                "Press-Free Inscriber MK-III",
                6,
                GTBlocks.CASING_TITANIUM_STABLE,
                GTCEu.id("block/casings/solid/machine_casing_stable_titanium"),
                GTCEu.id("block/multiblock/gcym/large_material_press"));

        PRESS_FREE_INSCRIBER_IV = registerInscriber(
                "press_free_inscriber_iv",
                "Press-Free Inscriber MK-IV",
                8,
                GTBlocks.CASING_TUNGSTENSTEEL_ROBUST,
                GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                GTCEu.id("block/multiblock/gcym/large_material_press"));
    }
}
