package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import shishamo_tech.common.recipe.STOverclockingLogic;
import shishamo_tech.common.recipe.STRecipeModifierUtil;
import shishamo_tech.config.STConfig;

import java.util.List;

public class STParallelMultiblockMachine extends WorkableElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STParallelMultiblockMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    public STParallelMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
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

    public int getParallelCount() {
        return STOverclockingLogic.getParallelBonus(getTier()) * STOverclockingLogic.getParallelBonus(getDefinition().getTier()) * STConfig.parallelMultiplier;
    }

    public static int getDisplayParallelCount(int tier) {
        return getBaseParallelForTier(tier) * STConfig.parallelMultiplier;
    }

    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof STParallelMultiblockMachine m)) {
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
        }
    }
}
