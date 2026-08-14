package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.AssemblyLineMachine;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import shishamo_tech.common.recipe.STOverclockingLogic;
import shishamo_tech.common.recipe.STRecipeModifierUtil;
import shishamo_tech.config.STConfig;

import java.util.List;

public class STAssemblyLineMachine extends AssemblyLineMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STAssemblyLineMachine.class, AssemblyLineMachine.MANAGED_FIELD_HOLDER);

    public STAssemblyLineMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public int getParallelCount() {
        return STOverclockingLogic.computeParallelCount(getTier(), getDefinition().getTier());
    }

    public static int getDisplayParallelCount(int tier) {
        return STOverclockingLogic.computeDisplayParallel(tier);
    }

    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof STAssemblyLineMachine m)) {
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
        }
    }
}
