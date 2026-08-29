package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;

import shishamo_tech.common.recipe.STOverclockingLogic;
import shishamo_tech.common.recipe.STRecipeModifierUtil;
import shishamo_tech.config.STConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class STCoilParallelMultiblockMachine extends CoilWorkableElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STCoilParallelMultiblockMachine.class, CoilWorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    public STCoilParallelMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public int getCoilParallelBonus() {
        return STOverclockingLogic.getCoilBonus(getCoilTier());
    }

    public int getParallelCount() {
        return STOverclockingLogic.computeParallelCount(getTier(), getDefinition().getTier(), getCoilParallelBonus());
    }

    public static int getDisplayParallelCount(int machineTier, int coilTier) {
        return STOverclockingLogic.computeDisplayParallel(machineTier, coilTier);
    }

    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof STCoilParallelMultiblockMachine m)) {
            return ModifierFunction.IDENTITY;
        }
        if (!STConfig.isElectricEnabled() || !STConfig.isMachineEnabled(machine)) {
            return ModifierFunction.NULL;
        }
        long voltage = STRecipeModifierUtil.getOverclockVoltage(machine);
        return STRecipeModifierUtil.createParallelModifier(machine, recipe, m.getParallelCount(), voltage);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable("shishamo_tech.machine.parallel_count",
                    getParallelCount()));
            textList.add(Component.translatable("shishamo_tech.machine.coil_tier",
                    Component.translatable("block.gtceu." + getCoilType().getName() + "_coil_block")));
        }
    }

    //////////////////////////////////////
    // ** Coil-Tier Preview Utility ** //
    //////////////////////////////////////

    /**
     * Generates one {@link MultiblockShapeInfo} per registered heating coil tier
     * (sorted low to high) from raw aisle strings, mirroring how the Electric
     * Blast Furnace declares its previews. Use this as the {@code shapeInfos}
     * argument when registering a coil multiblock - no per-machine preview code
     * is needed.
     * <p>
     * Example:
     *
     * <pre>{@code
     * .shapeInfos(STCoilParallelMultiblockMachine.coilTierShapeInfos(
     *          new String[][] { { "XXX", "CCC", "XSX" }, ... },
     *          Map.ofEntries(
     *              Map.entry('S', definition -> BlockInfo.fromBlockState(
     *                  definition.getBlock().defaultBlockState()
     *                          .setValue(RotationState.ALL.property, Direction.NORTH))),
     *              Map.entry('X', GTBlocks.CASING_INVAR_HEATPROOF),
     *              Map.entry('#', Blocks.AIR))));
     * }</pre>
     *
     * @param aisles   the aisle layers, same format as {@code FactoryBlockPattern.aisle(...)}
     *                 but with a single character per symbol (no multi-char symbols)
     * @param parts    map of character to block info supplier for every non-coil
     *                 symbol; the controller must be supplied here too
     * @param coilChar the character reserved for heating coils in {@code aisles}
     */
    public static Function<MultiblockMachineDefinition, List<MultiblockShapeInfo>> coilTierShapeInfos(
                                                                                                    String[] aisles,
                                                                                                    Map<Character, Function<MultiblockMachineDefinition, BlockInfo>> parts,
                                                                                                    char coilChar) {
        return definition -> {
            List<MultiblockShapeInfo> result = new ArrayList<>();
            var builder = new STAccessibleShapeInfoBuilder();
            for (String aisle : aisles) {
                builder.aisle(aisle);
            }
            for (var entry : parts.entrySet()) {
                builder.where(entry.getKey(), entry.getValue().apply(definition));
            }
            GTCEuAPI.HEATING_COILS.entrySet().stream()
                    .sorted(Comparator.comparingInt(entry -> entry.getKey().getTier()))
                    .forEach(coil -> result.add(builder.shallowCopy()
                            .where(coilChar, coil.getValue().get()).build()));
            return result;
        };
    }
}
