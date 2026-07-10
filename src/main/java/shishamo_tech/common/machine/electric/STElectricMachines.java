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
        return registerElectricMachine(name, langValue, new GTRecipeType[]{recipeType}, tier,
                appearanceBlock, casingTexture, overlayModel, patternProvider);
    }

    public static MultiblockMachineDefinition registerElectricMachine(
            String name, String langValue, GTRecipeType[] recipeTypes, int tier,
            BlockEntry<? extends Block> appearanceBlock,
            ResourceLocation casingTexture, ResourceLocation overlayModel,
            Function<MultiblockMachineDefinition, BlockPattern> patternProvider) {
        int parallel = STParallelMultiblockMachine.getDisplayParallelCount(tier);
        return STRegistration.REGISTRATE
                .multiblock(name, STParallelMultiblockMachine::new)
                .rotationState(RotationState.ALL)
                .langValue(langValue)
                .tier(tier)
                .recipeTypes(recipeTypes)
                .recipeModifiers(STParallelMultiblockMachine::recipeModifier, BATCH_MODE)
                .appearanceBlock(appearanceBlock)
                .pattern(patternProvider)
                .workableCasingModel(casingTexture, overlayModel)
                .tooltipBuilder((stack, tooltips) -> {
                    tooltips.add(Component.translatable(
                            "shishamo_tech.machine.parallel_count", parallel));
                    var recipeNames = new Component[recipeTypes.length];
                    for (int i = 0; i < recipeTypes.length; i++) {
                        recipeNames[i] = Component.translatable(
                                recipeTypes[i].registryName.getNamespace() + "." + recipeTypes[i].registryName.getPath());
                    }
                    tooltips.add(Component.translatable(
                            "gtceu.machine.available_recipe_map_" + recipeTypes.length + ".tooltip",
                            (Object[]) recipeNames));
                })
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
                .tooltipBuilder((stack, tooltips) -> {
                    tooltips.add(Component.translatable(
                            "shishamo_tech.machine.parallel_count", parallel));
                    tooltips.add(recipeTypeTooltip(recipeType));
                })
                .register();
    }

    public static Component recipeTypeTooltip(GTRecipeType recipeType) {
        return Component.translatable(
                "gtceu.machine.available_recipe_map_1.tooltip",
                Component.translatable(recipeType.registryName.getNamespace() + "." + recipeType.registryName.getPath()));
    }
}
