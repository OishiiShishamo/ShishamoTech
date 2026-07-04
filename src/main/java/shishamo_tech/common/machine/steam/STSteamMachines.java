package shishamo_tech.common.machine.steam;

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
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.BATCH_MODE;

public final class STSteamMachines {
    private STSteamMachines() {}

    public static MultiblockMachineDefinition MEGA_STEAM_GRINDER;
    public static MultiblockMachineDefinition MEGA_STEAM_FURNACE;
    public static MultiblockMachineDefinition MEGA_STEAM_COMPRESSOR;
    public static MultiblockMachineDefinition MEGA_STEAM_EXTRACTOR;
    public static MultiblockMachineDefinition MEGA_STEAM_HAMMER;
    public static MultiblockMachineDefinition MEGA_STEAM_ALLOY_SMELTER;
    public static MultiblockMachineDefinition MEGA_STEAM_ROCK_CRUSHER;

    private static final ResourceLocation CASING_TEXTURE = com.gregtechceu.gtceu.GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks");

    public static void init() {
        MEGA_STEAM_GRINDER = registerSteamMachine(
                "mega_steam_grinder",
                "Mega Steam Grinder",
                GTRecipeTypes.MACERATOR_RECIPES,
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/steam_grinder"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GS", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                                .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM).setExactLimit(1)))
                        .build());

        MEGA_STEAM_FURNACE = registerSteamMachine(
                "mega_steam_furnace",
                "Mega Steam Furnace",
                GTRecipeTypes.FURNACE_RECIPES,
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/steam_oven"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("FFFFFFFFFFF", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GS", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                        .where('F', blocks(GTBlocks.FIREBOX_BRONZE.get())
                                .or(abilities(PartAbility.STEAM).setExactLimit(1)))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                                .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1)))
                        .build());

        MEGA_STEAM_COMPRESSOR = registerSteamMachine(
                "mega_steam_compressor",
                "Mega Steam Compressor",
                GTRecipeTypes.COMPRESSOR_RECIPES,
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/implosion_compressor"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PS", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('P', blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                                .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM).setExactLimit(1)))
                        .build());

        MEGA_STEAM_EXTRACTOR = registerSteamMachine(
                "mega_steam_extractor",
                "Mega Steam Extractor",
                GTRecipeTypes.EXTRACTOR_RECIPES,
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/multiblock_workable"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GS", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                                .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM).setExactLimit(1)))
                        .build());

        MEGA_STEAM_HAMMER = registerSteamMachine(
                "mega_steam_hammer",
                "Mega Steam Hammer",
                GTRecipeTypes.FORGE_HAMMER_RECIPES,
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/multiblock_workable"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PS", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('P', blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                                .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM).setExactLimit(1)))
                        .build());

        MEGA_STEAM_ALLOY_SMELTER = registerSteamMachine(
                "mega_steam_alloy_smelter",
                "Mega Steam Alloy Smelter",
                GTRecipeTypes.ALLOY_SMELTER_RECIPES,
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/steam_oven"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("FFFFFFFFFFF", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GS", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XG#######GX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XGGGGGGGGGX", "XXXXXXXXXXX")
                        .aisle("FFFFFFFFFFF", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('G', blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                        .where('F', blocks(GTBlocks.FIREBOX_BRONZE.get())
                                .or(abilities(PartAbility.STEAM).setExactLimit(1)))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                                .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1)))
                        .build());

        MEGA_STEAM_ROCK_CRUSHER = registerSteamMachine(
                "mega_steam_rock_crusher",
                "Mega Steam Rock Crusher",
                GTRecipeTypes.ROCK_BREAKER_RECIPES,
                com.gregtechceu.gtceu.GTCEu.id("block/multiblock/multiblock_workable"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PS", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XP#######PX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XPPPPPPPPPX", "XXXXXXXXXXX")
                        .aisle("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX")
                        .where('S', controller(blocks(pattern.getBlock())))
                        .where('P', blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                        .where('#', air())
                        .where('X', blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                                .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.STEAM).setExactLimit(1)))
                        .build());
    }

    private static MultiblockMachineDefinition registerSteamMachine(
            String name,
            String langValue,
            com.gregtechceu.gtceu.api.recipe.GTRecipeType recipeType,
            ResourceLocation overlayModel,
            java.util.function.Function<com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition,
                    com.gregtechceu.gtceu.api.pattern.BlockPattern> patternProvider) {
        return STRegistration.REGISTRATE
                .multiblock(name, STSteamParallelMultiblockMachine::new)
                .rotationState(RotationState.ALL)
                .langValue(langValue)
                .recipeType(recipeType)
                .recipeModifiers(STSteamParallelMultiblockMachine::recipeModifier, BATCH_MODE)
                .appearanceBlock(GTBlocks.CASING_BRONZE_BRICKS)
                .pattern(patternProvider)
                .workableCasingModel(CASING_TEXTURE, overlayModel)
                .tooltipBuilder((stack, tooltips) -> {
                    tooltips.add(net.minecraft.network.chat.Component.translatable(
                            "shishamo_tech.machine.parallel", 8 * STConfig.PARALLEL_MULTIPLIER.get()));
                })
                .register();
    }
}
