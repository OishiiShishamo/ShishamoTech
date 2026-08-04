package shishamo_tech.common.machine.botany;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import shishamo_tech.STRegistration;
import shishamo_tech.common.recipe.STRecipeTypes;

import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.BATCH_MODE;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.OC_PERFECT_SUBTICK;
import static shishamo_tech.common.data.STMultiMachines.GREEN_HOUSE;
import static shishamo_tech.common.machine.electric.STElectricMachines.recipeTypeTooltip;

import java.util.List;

public final class STBotanyMachines {
    public static List<MachineDefinition> botanyMachineGetAll() {
        return List.of(GREEN_HOUSE);
    }

    public static MultiblockMachineDefinition registerGreenHouse(
            String name, String langValue, int tier,
            BlockEntry<? extends Block> appearanceBlock,
            ResourceLocation casingTexture, ResourceLocation overlayModel) {
        return STRegistration.REGISTRATE
                .multiblock(name, GreenHouseMachine::new)
                .rotationState(RotationState.ALL)
                .langValue(langValue)
                .tier(tier)
                .recipeType(STRecipeTypes.GREEN_HOUSE_RECIPES)
                .recipeModifiers(GreenHouseMachine::recipeModifier, OC_PERFECT_SUBTICK, BATCH_MODE)
                .appearanceBlock(appearanceBlock)
                .pattern(pattern -> FactoryBlockPattern.start(RIGHT, UP, FRONT)
                        .aisle("AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "BBCCCCCCCCCCCBB", "BBDDDDDDDDDDDBB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "BBCCCCCCCCCCCBB", "BBDDDDDDDDDDDBB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "BBCCCCCCCCCCCBB", "BBDDDDDDDDDDDBB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "BBCCCCCCCCCCCBB", "BBDDDDDDDDDDDBB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "BBCCCCCCCCCCCBB", "BBDDDDDDDDDDDBB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "BBCCCCCCCCCCCBB", "BBDDDDDDDDDDDBB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "BBCCCCCCCCCCCBB", "BBDDDDDDDDDDDBB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "BB###########BB", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA")
                        .aisle("AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAEAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA")
                        .where("E", controller(blocks(pattern.getBlock())))
                        .where("B", blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                        .where("D", blocks(Blocks.COMPOSTER))
                        .where("C", blocks(Blocks.DIRT))
                        .where("#", any())
                        .where("A", blocks(appearanceBlock.get())
                                .or(abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                                .or(abilities(PartAbility.INPUT_ENERGY, PartAbility.INPUT_LASER).setPreviewCount(2))
                                .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .build())
                .workableCasingModel(casingTexture, overlayModel)
                .tooltipBuilder((stack, tooltips) -> {
                    tooltips.add(recipeTypeTooltip(STRecipeTypes.GREEN_HOUSE_RECIPES));
                })
                .register();
    }
}
