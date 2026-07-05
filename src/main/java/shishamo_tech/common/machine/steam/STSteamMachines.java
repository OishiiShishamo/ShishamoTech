package shishamo_tech.common.machine.steam;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import shishamo_tech.STRegistration;
import shishamo_tech.config.STConfig;

import java.util.function.Function;

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

    private static final ResourceLocation CASING_TEXTURE = GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks");

    private static final String ALL_X = "XXXXXXXXXXX";
    private static final String PIPE_ROW = "XGGGGGGGGGX";
    private static final String INNER_ROW = "XG#######GX";
    private static final String CTRL_ROW = "XG#######GS";
    private static final String FIREBOX_ROW = "FFFFFFFFFFF";

    private static String[] buildRows(int depth, int ctrlDepth, boolean hasFirebox) {
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

    private static Function<MultiblockMachineDefinition, BlockPattern> sharedPattern(boolean hasFirebox) {
        int ctrlDepth = 5;
        return pattern -> {
            var builder = FactoryBlockPattern.start(FRONT, UP, RIGHT);
            for (int d = 0; d < 11; d++) {
                builder.aisle(buildRows(d, ctrlDepth, hasFirebox));
            }
            builder.where('S', controller(blocks(pattern.getBlock())))
                    .where('G', blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                    .where('#', air());
            if (hasFirebox) {
                builder.where('F', blocks(GTBlocks.FIREBOX_BRONZE.get())
                        .or(abilities(PartAbility.STEAM).setExactLimit(1)));
                builder.where('X', blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                        .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                        .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1)));
            } else {
                builder.where('X', blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                        .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                        .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1))
                        .or(abilities(PartAbility.STEAM).setExactLimit(1)));
            }
            return builder.build();
        };
    }

    public static void init() {
        MEGA_STEAM_GRINDER = registerSteamMachine(
                "mega_steam_grinder", "Mega Steam Grinder",
                GTRecipeTypes.MACERATOR_RECIPES,
                GTCEu.id("block/multiblock/steam_grinder"),
                sharedPattern(false));

        MEGA_STEAM_FURNACE = registerSteamMachine(
                "mega_steam_furnace", "Mega Steam Furnace",
                GTRecipeTypes.FURNACE_RECIPES,
                GTCEu.id("block/multiblock/steam_oven"),
                sharedPattern(true));

        MEGA_STEAM_COMPRESSOR = registerSteamMachine(
                "mega_steam_compressor", "Mega Steam Compressor",
                GTRecipeTypes.COMPRESSOR_RECIPES,
                GTCEu.id("block/multiblock/implosion_compressor"),
                sharedPattern(false));

        MEGA_STEAM_EXTRACTOR = registerSteamMachine(
                "mega_steam_extractor", "Mega Steam Extractor",
                GTRecipeTypes.EXTRACTOR_RECIPES,
                GTCEu.id("block/multiblock/multiblock_workable"),
                sharedPattern(false));

        MEGA_STEAM_HAMMER = registerSteamMachine(
                "mega_steam_hammer", "Mega Steam Hammer",
                GTRecipeTypes.FORGE_HAMMER_RECIPES,
                GTCEu.id("block/multiblock/multiblock_workable"),
                sharedPattern(false));

        MEGA_STEAM_ALLOY_SMELTER = registerSteamMachine(
                "mega_steam_alloy_smelter", "Mega Steam Alloy Smelter",
                GTRecipeTypes.ALLOY_SMELTER_RECIPES,
                GTCEu.id("block/multiblock/steam_oven"),
                sharedPattern(true));

        MEGA_STEAM_ROCK_CRUSHER = registerSteamMachine(
                "mega_steam_rock_crusher", "Mega Steam Rock Crusher",
                GTRecipeTypes.ROCK_BREAKER_RECIPES,
                GTCEu.id("block/multiblock/multiblock_workable"),
                sharedPattern(false));
    }

    private static MultiblockMachineDefinition registerSteamMachine(
            String name, String langValue,
            GTRecipeType recipeType, ResourceLocation overlayModel,
            Function<MultiblockMachineDefinition, BlockPattern> patternProvider) {
        return STRegistration.REGISTRATE
                .multiblock(name, STSteamParallelMultiblockMachine::new)
                .rotationState(RotationState.ALL)
                .langValue(langValue)
                .recipeType(recipeType)
                .recipeModifiers(STSteamParallelMultiblockMachine::recipeModifier, BATCH_MODE)
                .appearanceBlock(GTBlocks.CASING_BRONZE_BRICKS)
                .pattern(patternProvider)
                .workableCasingModel(CASING_TEXTURE, overlayModel)
                .tooltipBuilder((stack, tooltips) ->
                    tooltips.add(Component.translatable(
                            "shishamo_tech.machine.parallel", 8 * STConfig.PARALLEL_MULTIPLIER.get())))
                .register();
    }
}
