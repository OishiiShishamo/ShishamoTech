package shishamo_tech.common.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import shishamo_tech.STRegistration;
import shishamo_tech.common.machine.primitive.STPrimitiveBlastFurnaceMachine;
import shishamo_tech.common.machine.storage.STUltimateUniversalStorageMachine;
import shishamo_tech.common.machine.ae2.STAE2PartMachines;
import shishamo_tech.ShishamoTech;
import shishamo_tech.common.recipe.STRecipeTypes;
import shishamo_tech.config.STConfig;

import java.util.function.Function;

import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.BATCH_MODE;
import static shishamo_tech.common.machine.ae2.STAE2Machines.registerInscriber;
import static shishamo_tech.common.machine.botany.STBotanyMachines.registerGreenHouse;
import static shishamo_tech.common.machine.electric.STElectricMachines.recipeTypeTooltip;
import static shishamo_tech.common.machine.electric.STElectricMachines.registerCoilMachine;
import static shishamo_tech.common.machine.electric.STElectricMachines.registerElectricMachine;
import static shishamo_tech.common.machine.steam.STSteamMachines.registerSteamMachine;
import static shishamo_tech.common.machine.steam.STSteamMachines.registerSteamVoidResourceMiner;
import static shishamo_tech.common.machine.steam.STSteamMachines.registerLargeSteamBoiler;

public class STMultiMachines {
    public static MultiblockMachineDefinition MEGA_STEAM_GRINDER;
    public static MultiblockMachineDefinition MEGA_STEAM_FURNACE;
    public static MultiblockMachineDefinition MEGA_STEAM_COMPRESSOR;
    public static MultiblockMachineDefinition MEGA_STEAM_EXTRACTOR;
    public static MultiblockMachineDefinition MEGA_STEAM_HAMMER;
    public static MultiblockMachineDefinition MEGA_STEAM_ALLOY_SMELTER;
    public static MultiblockMachineDefinition MEGA_STEAM_ROCK_CRUSHER;
    public static MultiblockMachineDefinition MEGA_STEAM_VOID_RESOURCE_MINER;

    public static MultiblockMachineDefinition SUPERIOR_MACERATION_PLANT;
    public static MultiblockMachineDefinition LARGE_SMELTING_PLANT;
    public static MultiblockMachineDefinition LARGE_WASHING_PLANT;
    public static MultiblockMachineDefinition HYPER_TOWER_CENTRIFUGE;
    public static MultiblockMachineDefinition LARGE_ELECTROLYZER;
    public static MultiblockMachineDefinition LCR_CLUSTER;
    public static MultiblockMachineDefinition LARGE_ASSEMBLY_PLANT;
    public static MultiblockMachineDefinition LARGE_ARC_FURNACE;
    public static MultiblockMachineDefinition LARGE_DISTILLATION_TOWER;
    public static MultiblockMachineDefinition ETERNAL_FORCE_FREEZER;
    public static MultiblockMachineDefinition NON_OMNIPOTENT_UNIVERSE_FORGE;

    public static MultiblockMachineDefinition GOD_STEAM_BOILER;

    public static MultiblockMachineDefinition HYPER_PRIMITIVE_BLAST_FURNACE;

    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_MV;
    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_HV;
    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_EV;
    public static MultiblockMachineDefinition PRESS_FREE_INSCRIBER_IV;

    public static MultiblockMachineDefinition GREEN_HOUSE;

    public static MultiblockMachineDefinition ULTIMATE_UNIVERSAL_STORAGE;

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
                    .where("#", any());
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

    private static Function<MultiblockMachineDefinition, BlockPattern> steamVoidMinerSharedPattern() {
        int ctrlDepth = 5;
        return pattern -> {
            var builder = FactoryBlockPattern.start(FRONT, UP, RIGHT);
            for (int d = 0; d < 11; d++) {
                builder.aisle(steamBuildRows(d, ctrlDepth, false));
            }
            builder.where("S", controller(blocks(pattern.getBlock())))
                    .where("G", blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                    .where("#", any())
                    .where("X", blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                            .or(abilities(PartAbility.STEAM).setExactLimit(1))
                            .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1)));
            return builder.build();
        };
    }

    private static Function<MultiblockMachineDefinition, BlockPattern> godSteamBoilerPattern() {
        return pattern -> FactoryBlockPattern.start(RIGHT, UP, FRONT)
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddbbbbbbbbbbbbbbbbbbbbbbbbb", "ddbbbbbbbbbbbbbbbbbbbbbbbdd", "dddbbbbbbbbbbbbbbbbbbbbbbdd", "dddbbbbbbbbbbbbbbbbbbbbbddd", "ddddbbbbbbbbbbbbbbbbbbbbddd", "ddddbbbbbbbbbbbbbbbbbbbdddd", "ddddbbbbbbbbbbbbbbbbbbbdddd", "ddddbbbbbbbbbbbbbbbbbbbdddd", "ddddbbbbbbbbbbbbbbbbbbbdddd", "ddddbbbbbbbbbbbbbbbbbbbdddd", "ddddbbbbbbbbbbbbbbbbbbbdddd", "ddbbbbbbbbbbbbbbbbbbbbbbbdd", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbdddddddddddddddddddbbbb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bbbbdddddddddddddddddddbbbb", "deeedddddddddddddddddddeeeb", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "bfffdddddddddddddddddddfffb", "bfffdddddddddddddddddddfffb", "bfffbbbbbbbbbbbbbbbbbbbfffb", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbgbdddddddddddddddddddbgbb", "bdgdddddddddddddddddddddgdb", "bdgdddddddddddddddddddddgdb", "bdgdddddddddddddddddddddgdb", "bbgbdddddddddddddddddddbgbb", "bededddddddddddddddddddedeb", "bededddddddddddddddddddedeb", "dededddddddddddddddddddedeb", "dededddddddddddddddddddeded", "dededddddddddddddddddddeded", "dededddddddddddddddddddeded", "dededddddddddddddddddddeded", "dededddddddddddddddddddeded", "dededddddddddddddddddddeded", "dededddddddddddddddddddeded", "dededddddddddddddddddddeded", "bededddddddddddddddddddedeb", "bfdfdddddddddddddddddddfdfb", "bfdfdddddddddddddddddddfdfb", "bfdfbbbbbbbbbbbbbbbbbbbfdfb", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbdddddddddddddddddddbbbb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bbbbdddddddddddddddddddbbbb", "beeedddddddddddddddddddeeeb", "beeedddddddddddddddddddeeeb", "beeedddddddddddddddddddeeeb", "beeedddddddddddddddddddeeeb", "deeedddddddddddddddddddeeeb", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "beeedddddddddddddddddddeeeb", "bfffdddddddddddddddddddfffb", "bfffdddddddddddddddddddfffb", "bfffbbbbbbbbbbbbbbbbbbbfffb", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddeeeeeeeeeeeeeeeeeddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bddddeeeeeeeeeeeeeeeeeddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bddddeeeeeeeeeeeeeeeeeddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bddddeeeeeeeeeeeeeeeeeddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfffffffffffffffdddddb", "bdddddfffffffffffffffdddddb", "bdddddfffffffffffffffdddddb", "bddddefffffffffffffffeddddb", "bdddddfffffffffffffffdddddb", "bdddddfffffffffffffffdddddb", "bdddddfffffffffffffffdddddb", "bdddddfffffffffffffffdddddb", "bddddefffffffffffffffeddddb", "bdddddfffffffffffffffdddddb", "bdddddfffffffffffffffdddddb", "bdddddfffffffffffffffdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bdddddbgggggggggggggbdddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbbbbbhhihhbbbbbdddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bbbbbbbdddddddddddddbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bbbbbbbdddddddddddddbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bbbbbbbdddddddddddddbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bbbbbbbdddddddddddddbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bddddefffffffffffffffeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefdddddddddddddfeddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bdddddfdddddddddddddfdddddb", "bddddefffffffffffffffeddddb", "bdddddbbbbbbbbbbbbbbbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bdddddbdddddddddddddbdddddb", "bbbbbbbdddddddddddddbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbdefffffffffffffffedbbbb", "bdddddfffffffffffffffdddddb", "bdddddfffffffffffffffdddddb", "bdddddfffffffffffffffdddddb", "bbbbdefffffffffffffffedbbbb", "beeeddfffffffffffffffddeeeb", "beeeddfffffffffffffffddeeeb", "beeeddfffffffffffffffddeeeb", "beeeddfffffffffffffffddeeeb", "deeedefffffffffffffffedeeed", "deeeddfffffffffffffffddeeed", "deeeddfffffffffffffffddeeed", "deeeddfffffffffffffffddeeed", "deeedefffffffffffffffedeeed", "deeeddbbbbbbbbbbbbbbbddeeed", "deeeddbdddddddddddddbddeeed", "beeeddbdddddddddddddbddeeeb", "bfffddbdddddddddddddbddfffb", "bfffddbdddddddddddddbddfffb", "bfffbbbdddddddddddddbbbfffb", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbgbdeeeeeeeeeeeeeeeeedbgbb", "bdgdddddddddddddddddddddgdb", "bdgdddddddddddddddddddddgdb", "bdgdddddddddddddddddddddgdb", "bbgbdeeeeeeeeeeeeeeeeedbgbb", "bededddddddddddddddddddedeb", "bededddddddddddddddddddedeb", "dededddddddddddddddddddeded", "dededddddddddddddddddddeded", "dededeeeeeeeeeeeeeeeeededed", "dededddddddddddddddddddeded", "dededddddddddddddddddddeded", "dededddddddddddddddddddeded", "dededeeeeeeeeeeeeeeeeededed", "dededdbbbbbbbbbbbbbbbddeded", "dededdbdddddddddddddbddeded", "bededdbdddddddddddddbddedeb", "bfdfddbdddddddddddddbddfdfb", "bfdfddbdddddddddddddbddfdfb", "bfdfbbbdddddddddddddbbbfdfb", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd", "dfdfdddddddddddddddddddfdfd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbdddddddddddddddddddbbbb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bdddddddddddddddddddddddddb", "bbbbdddddddddddddddddddbbbb", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeedddddddddddddddddddeeed", "deeeddbbbbbbbbbbbbbbbddeeed", "deeeddbdddddddddddddbddeeed", "deeeddbdddddddddddddbddeeed", "bfffddbdddddddddddddbddfffb", "bfffddbdddddddddddddbddfffb", "bfffbbbdddddddddddddbbbfffb", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd", "dfffdddddddddddddddddddfffd")
                .aisle("aaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb", "bbbbbbbcccccccccccccbbbbbbb", "bbbbbbbcccccccccccccbbbbbbb", "ddbbbbbcccccccccccccbbbbbdd", "ddbbbbbcccccccccccccbbbbbdd", "dddbbbbcccccccccccccbbbbddd", "dddbbbbcccccccccccccbbbbddd", "ddddbbbcccccccccccccbbbdddd", "ddddbbbcccccccccccccbbbdddd", "ddddbbbcccccccccccccbbbdddd", "ddddbbbbbbbbbbbbbbbbbbbdddd", "ddddbbbbbbbbbbbbbbbbbbbdddd", "ddddbbbbbbbbbbbbbbbbbbbdddd", "ddddbbbdddddddddddddbbbdddd", "ddbbbbbdddddddddddddbbbbbdd", "bbbbbbbdddddddddddddbbbbbbb", "bbbbbbbdddddddddddddbbbbbbb", "bbbbbbbdddddddddddddbbbbbbb", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd", "ddddddddddddddddddddddddddd")
                .where("a", blocks(GTBlocks.FIREBOX_STEEL.get())
                        .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1)))
                .where("b", blocks(GTBlocks.CASING_STEEL_SOLID.get()))
                .where("c", blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                .where("d", any())
                .where("e", heatingCoils())
                .where("f", blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                .where("g", blocks(GTBlocks.CASING_STEEL_GEARBOX.get()))
                .where("h", blocks(GTBlocks.CASING_STEEL_SOLID.get())
                        .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                        .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                        .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                        .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                .where("i", controller(blocks(pattern.getBlock())))
                .build();
    }

    private static Function<MultiblockMachineDefinition, BlockPattern> primitiveBlastFurnacePattern() {
        return pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                .aisle("##AAA##", "#######", "#######", "#######", "##AAA##", "#######", "#######", "#######", "##AAA##")
                .aisle("#AAAAA#", "##CCC##", "##CCC##", "##CCC##", "#AAAAA#", "##CCC##", "##CCC##", "##CCC##", "#AAAAA#")
                .aisle("AAAAAAA", "#CDDDC#", "#CDDDC#", "#CDDDC#", "AAAAAAA", "#CDDDC#", "#CDDDC#", "#CDDDC#", "AAAAAAA")
                .aisle("AAAAAAB", "#CD#DC#", "#CD#DC#", "#CD#DC#", "AAA#AAA", "#CD#DC#", "#CD#DC#", "#CD#DC#", "AAA#AAA")
                .aisle("AAAAAAA", "#CDDDC#", "#CDDDC#", "#CDDDC#", "AAAAAAA", "#CDDDC#", "#CDDDC#", "#CDDDC#", "AAAAAAA")
                .aisle("#AAAAA#", "##CCC##", "##CCC##", "##CCC##", "#AAAAA#", "##CCC##", "##CCC##", "##CCC##", "#AAAAA#")
                .aisle("##AAA##", "#######", "#######", "#######", "##AAA##", "#######", "#######", "#######", "##AAA##")
                .where("B", controller(blocks(pattern.getBlock())))
                .where("D", blocks(Blocks.DIRT))
                .where("C", blocks(Blocks.BRICKS))
                .where("#", any())
                .where("A", blocks(GTBlocks.CASING_PRIMITIVE_BRICKS.get())
                        .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                        .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1)))
                .build();
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

        MEGA_STEAM_VOID_RESOURCE_MINER = registerSteamVoidResourceMiner(
                "mega_steam_void_resource_miner", "Mega Steam Void Resource Miner",
                STRecipeTypes.VOID_RESOURCE_MINING_RECIPES,
                GTCEu.id("block/multiblock/multiblock_workable"),
                steamVoidMinerSharedPattern());

        GOD_STEAM_BOILER = registerLargeSteamBoiler(
                "god_steam_boiler", "God Steam Boiler",
                GTRecipeTypes.LARGE_BOILER_RECIPES,
                GTCEu.id("block/multiblock/generator/large_steel_boiler"),
                godSteamBoilerPattern());
    }

    public static void primitiveInit() {
        HYPER_PRIMITIVE_BLAST_FURNACE = STRegistration.REGISTRATE
                .multiblock("hyper_primitive_blast_furnace", STPrimitiveBlastFurnaceMachine::new)
                .rotationState(RotationState.ALL)
                .langValue("Hyper Primitive Blast Furnace")
                .recipeType(GTRecipeTypes.PRIMITIVE_BLAST_FURNACE_RECIPES)
                .recipeModifiers(STPrimitiveBlastFurnaceMachine::recipeModifier, BATCH_MODE)
                .appearanceBlock(GTBlocks.CASING_PRIMITIVE_BRICKS)
                .pattern(primitiveBlastFurnacePattern())
                .workableCasingModel(
                        GTCEu.id("block/casings/solid/machine_primitive_bricks"),
                        GTCEu.id("block/multiblock/primitive_blast_furnace"))
                .tooltipBuilder((stack, tooltips) -> {
                    STConfig.checkMachineDisabledTooltip("hyper_primitive_blast_furnace", tooltips);
                    tooltips.add(Component.translatable(
                            "shishamo_tech.machine.parallel_count",
                            STPrimitiveBlastFurnaceMachine.getDisplayParallelCount()));
                    tooltips.add(recipeTypeTooltip(GTRecipeTypes.PRIMITIVE_BLAST_FURNACE_RECIPES));
                })
                .register();
    }

    public static void electricInit() {
        SUPERIOR_MACERATION_PLANT = registerElectricMachine(
                "superior_maceration_plant",
                "Superior Maceration Plant",
                GTRecipeTypes.MACERATOR_RECIPES,
                5,
                GCYMBlocks.CASING_SECURE_MACERATION,
                GTCEu.id("block/casings/gcym/secure_maceration_casing"),
                GTCEu.id("block/multiblock/gcym/large_maceration_tower"),
                pattern -> FactoryBlockPattern.start(FRONT, UP, RIGHT)
                        .aisle("AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA#BBB######AAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AA##BBB#######AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA######BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AA#######BBB##AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA#BBB######AAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AA##BBB#######AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA######BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AA#######BBB##AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA#BBB######AAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AA##BBB#######AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA######BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AA#######BBB##AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA#BBB######AAA", "AAA#BBB#BBBBBAAC", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AA##BBB#######AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA######BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AA#######BBB##AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA#BBB######AAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AA##BBB#######AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA######BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AA#######BBB##AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA#BBB######AAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AA##BBB#######AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA######BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AAABBBBB#BBB#AAA", "AA#######BBB##AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAA##########AAA", "AAA#BBB######AAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AAA#BBB#BBBBBAAA", "AA##BBB#######AA", "A##############A")
                        .aisle("AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAA")
                        .where("C", controller(blocks(pattern.getBlock())))
                        .where("B", blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
                        .where("#", any())
                        .where("A", blocks(GCYMBlocks.CASING_SECURE_MACERATION.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_SMELTING_PLANT = registerCoilMachine(
                "large_smelting_plant",
                "Large Smelting Plant",
                GTRecipeTypes.FURNACE_RECIPES,
                3,
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
                        .where("#", any())
                        .where("X", blocks(GTBlocks.CASING_INVAR_HEATPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_WASHING_PLANT = registerElectricMachine(
                "large_washing_plant",
                "Large Washing Plant",
                GTRecipeTypes.ORE_WASHER_RECIPES,
                5,
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
                        .where("#", any())
                        .where("X", blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        HYPER_TOWER_CENTRIFUGE = registerElectricMachine(
                "hyper_tower_centrifuge",
                "Hyper Tower Centrifuge",
                new GTRecipeType[]{GTRecipeTypes.CENTRIFUGE_RECIPES, GTRecipeTypes.THERMAL_CENTRIFUGE_RECIPES},
                5,
                GTBlocks.CASING_STEEL_SOLID,
                GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
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
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .where("B", controller(blocks(pattern.getBlock())))
                        .where("E", blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                        .where("C", blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                        .where("D", blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                        .where("#", any())
                        .build());

        LARGE_ELECTROLYZER = registerElectricMachine(
                "large_electrolyzer",
                "Large Electrolyzer",
                GTRecipeTypes.ELECTROLYZER_RECIPES,
                5,
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
                        .where("#", any())
                        .where("X", blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
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
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .where("D", controller(blocks(pattern.getBlock())))
                        .where("#", any())
                        .build());

        LARGE_ASSEMBLY_PLANT = registerElectricMachine(
                "large_assembly_plant",
                "Large Assembly Plant",
                GTRecipeTypes.ASSEMBLER_RECIPES,
                5,
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
                        .where("#", any())
                        .where("X", blocks(GTBlocks.CASING_STEEL_SOLID.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(2))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_ARC_FURNACE = registerCoilMachine(
                "large_arc_furnace",
                "Large Arc Furnace",
                GTRecipeTypes.ARC_FURNACE_RECIPES,
                5,
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
                        .where("#", any())
                        .where("X", blocks(GTBlocks.CASING_INVAR_HEATPROOF.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        LARGE_DISTILLATION_TOWER = registerElectricMachine(
                "large_distillation_tower",
                "Large Distillation Tower",
                GTRecipeTypes.DISTILLATION_RECIPES,
                4,
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
                        .where("#", any())
                        .where("X", blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                                .or(abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build());

        ETERNAL_FORCE_FREEZER = registerElectricMachine(
                "eternal_force_freezer",
                "Eternal Force Freezer",
                GTRecipeTypes.VACUUM_RECIPES,
                4,
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
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .where("D", controller(blocks(pattern.getBlock())))
                        .where("C", blocks(Blocks.BLUE_ICE))
                        .where("B", blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                        .where("#", any())
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
                                        .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setMinGlobalLimited(1).setMaxGlobalLimited(5))
                                        .or(abilities(PartAbility.EXPORT_ITEMS).setExactLimit(1))
                                        .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                                        .or(abilities(PartAbility.PARALLEL_HATCH).setMaxGlobalLimited(1))
                                        .or(abilities(PartAbility.DATA_ACCESS).setMaxGlobalLimited(1))
                        )
                        .where("A", blocks(STBlocks.CASING_SOLID_MIRACLE_FUMETSU.get()))
                        .where("C", blocks(STBlocks.CASING_SOLID_MIRACLE_METEOR.get()))
                        .where("E", blocks(STBlocks.CASING_SOLID_SPACETIME.get()))
                        .where("B", blocks(STBlocks.CASING_NOUF_MAGICAL.get()))
                        .where("F", blocks(STBlocks.CASING_NOUF_WAVE.get()))
                        .where("#", any())
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

        STAE2PartMachines.init();
    }

    public static void botanyInit() {
        GREEN_HOUSE = registerGreenHouse(
                "green_house",
                "Green House",
                3,
                GTBlocks.CASING_STAINLESS_CLEAN,
                GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                GTCEu.id("block/multiblock/multiblock_workable"));
    }

    public static void storageInit() {
        ULTIMATE_UNIVERSAL_STORAGE = STRegistration.REGISTRATE
                .multiblock("ultimate_universal_storage", STUltimateUniversalStorageMachine::new)
                .langValue("Ultimate Universal Storage")
                .recipeType(GTRecipeTypes.DUMMY_RECIPES)
                .rotationState(RotationState.ALL)
                .appearanceBlock(GTBlocks.CASING_STEEL_SOLID)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("CCC", "CCC", "CCC")
                        .aisle("CCC", "C#C", "CCC")
                        .aisle("CCC", "CSC", "CCC")
                        .where('S', controller(blocks(definition.get())))
                        .where('C', blocks(GTBlocks.CASING_STEEL_SOLID.get()))
                        .where('#', any())
                        .build())
                .shapeInfo(definition -> MultiblockShapeInfo.builder()
                        .aisle("CCC", "CSC", "CCC")
                        .aisle("CCC", "C#C", "CCC")
                        .aisle("CCC", "CCC", "CCC")
                        .where('S', definition.get(), Direction.NORTH)
                        .where('C', GTBlocks.CASING_STEEL_SOLID.get().defaultBlockState())
                        .where('#', Blocks.AIR.defaultBlockState())
                        .build())
                .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                        GTCEu.id("block/multiblock/multiblock_tank"))
                .tooltipBuilder((stack, tooltips) -> {
                    STConfig.checkMachineDisabledTooltip("ultimate_universal_storage", tooltips);
                    tooltips.add(Component.translatable("shishamo_tech.machine.ultimate_universal_storage.pipe_tip"));
                    tooltips.add(Component.translatable("shishamo_tech.machine.configurable.tooltip"));
                })
                .register();
    }
}
