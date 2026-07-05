package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import shishamo_tech.STRegistration;

import java.util.function.Function;

import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.BATCH_MODE;

public final class STElectricMachines {
    public static MultiblockMachineDefinition registerElectricMachine(
            String name, String langValue, GTRecipeType recipeType, int tier,
            BlockEntry<? extends Block> appearanceBlock,
            ResourceLocation casingTexture, ResourceLocation overlayModel,
            Function<MultiblockMachineDefinition, BlockPattern> patternProvider) {
        int parallel = STParallelMultiblockMachine.getDisplayParallelCount(tier);
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
                        tooltips.add(Component.translatable(
                                "shishamo_tech.machine.parallel_count", parallel)))
                .register();
    }

    public static MultiblockMachineDefinition registerCoilMachine(
            String name, String langValue, GTRecipeType recipeType, int tier,
            BlockEntry<? extends Block> appearanceBlock,
            ResourceLocation casingTexture, ResourceLocation overlayModel,
            Function<MultiblockMachineDefinition, BlockPattern> patternProvider) {
        int parallel = STCoilParallelMultiblockMachine.getDisplayParallelCount(tier, 0);
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
                .tooltipBuilder((stack, tooltips) ->
                        tooltips.add(Component.translatable(
                                "shishamo_tech.machine.parallel_count", parallel)))
                .register();
    }
}
