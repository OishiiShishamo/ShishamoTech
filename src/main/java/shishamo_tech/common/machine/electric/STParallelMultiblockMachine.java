package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import shishamo_tech.common.recipe.STOverclockingLogic;
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
        return switch (tier) {
            case 0, 1 -> 4;
            case 2, 3 -> 8;
            case 4, 5 -> 16;
            case 6, 7 -> 32;
            default -> 64;
        };
    }

    public int getBaseParallelForTier() {
        return getBaseParallelForTier(getTier());
    }

    public int getParallelCount() {
        return getBaseParallelForTier() * STConfig.PARALLEL_MULTIPLIER.get();
    }

    public static int getDisplayParallelCount(int tier) {
        return getBaseParallelForTier(tier);
    }

    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof STParallelMultiblockMachine multiblock)) {
            return ModifierFunction.IDENTITY;
        }
        ModifierFunction ocMod = ModifierFunction.IDENTITY;
        if (machine instanceof IOverclockMachine ocMachine) {
            long voltage = ocMachine.getOverclockVoltage();
            ocMod = STOverclockingLogic.TRIPLE_OVERCLOCK.getModifier(
                    machine, recipe, voltage);
        }
        int targetParallel = multiblock.getParallelCount();
        int actualParallel = ParallelLogic.getParallelAmountWithoutEU(machine, recipe, targetParallel);
        if (actualParallel <= 0) return ModifierFunction.NULL;
        ContentModifier modifier = ContentModifier.multiplier(actualParallel);
        ModifierFunction parallelMod = ModifierFunction.builder()
                .parallels(actualParallel)
                .inputModifier(modifier)
                .outputModifier(modifier)
                .build();
        return ocMod.andThen(parallelMod);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable("shishamo_tech.machine.parallel_count",
                    getBaseParallelForTier() * STConfig.PARALLEL_MULTIPLIER.get()));
        }
    }
}
