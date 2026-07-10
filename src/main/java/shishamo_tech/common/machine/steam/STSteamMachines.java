package shishamo_tech.common.machine.steam;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import shishamo_tech.STRegistration;
import shishamo_tech.config.STConfig;

import java.util.function.Function;

import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.BATCH_MODE;
import static shishamo_tech.common.machine.electric.STElectricMachines.recipeTypeTooltip;

public final class STSteamMachines {
    private static final ResourceLocation CASING_TEXTURE = GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks");

    public static MultiblockMachineDefinition registerSteamMachine(
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
                .tooltipBuilder((stack, tooltips) -> {
                    tooltips.add(Component.translatable(
                            "shishamo_tech.machine.parallel", 8 * STConfig.parallelMultiplier));
                    tooltips.add(recipeTypeTooltip(recipeType));
                })
                .register();
    }

    public static MultiblockMachineDefinition registerLargeSteamBoiler(
            String name, String langValue,
            GTRecipeType recipeType, ResourceLocation overlayModel,
            Function<MultiblockMachineDefinition, BlockPattern> patternProvider) {
        return STRegistration.REGISTRATE
                .multiblock(name, LargeSteamBoilerMachine::new)
                .rotationState(RotationState.ALL)
                .langValue(langValue)
                .recipeType(recipeType)
                .recipeModifiers(LargeSteamBoilerMachine::recipeModifier, BATCH_MODE)
                .appearanceBlock(GTBlocks.CASING_STEEL_SOLID)
                .pattern(patternProvider)
                .workableCasingModel(
                        GTCEu.id("block/casings/solid/machine_casing_solid_steel"), overlayModel)
                .tooltipBuilder((stack, tooltips) -> {
                    tooltips.add(Component.translatable(
                            "shishamo_tech.machine.steam_output",
                            LargeSteamBoilerMachine.STEAM_OUTPUT_PER_TICK));
                    tooltips.add(recipeTypeTooltip(recipeType));
                })
                .register();
    }
}
