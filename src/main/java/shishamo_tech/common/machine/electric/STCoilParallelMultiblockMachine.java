package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import shishamo_tech.common.recipe.STOverclockingLogic;
import shishamo_tech.common.recipe.STRecipeModifierUtil;
import shishamo_tech.config.STConfig;

import java.util.List;

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

    public static int getBaseParallelForTier(int tier) {
        return STOverclockingLogic.getParallelBonus(tier);
    }

    public int getBaseParallelForTier() {
        return getBaseParallelForTier(getDefinition().getTier());
    }

    public int getCoilParallelBonus() {
        int coilTier = getCoilTier();
        return switch (coilTier) {
            case 0 -> 0;
            case 1, 2 -> 2;
            case 3, 4 -> 4;
            case 5, 6 -> 8;
            default -> 16;
        };
    }

    public int getParallelCount() {
        return STOverclockingLogic.getParallelBonus(getTier()) * STOverclockingLogic.getParallelBonus(getDefinition().getTier()) * STConfig.parallelMultiplier * switch (getCoilTier()) {
            case 0 -> 1;
            case 1, 2 -> 2;
            case 3, 4 -> 4;
            case 5, 6 -> 8;
            default -> 16;
        };
    }

    public static int getDisplayParallelCount(int machineTier, int coilTier) {
        int base = getBaseParallelForTier(machineTier);
        return base * STConfig.parallelMultiplier * switch (coilTier) {
            case 0 -> 1;
            case 1, 2 -> 2;
            case 3, 4 -> 4;
            case 5, 6 -> 8;
            default -> 16;
        };
    }

    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof STCoilParallelMultiblockMachine m)) {
            return ModifierFunction.IDENTITY;
        }
        if (!STConfig.isElectricEnabled()) {
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
}
