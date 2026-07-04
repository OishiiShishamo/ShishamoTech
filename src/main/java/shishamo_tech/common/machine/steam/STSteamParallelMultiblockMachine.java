package shishamo_tech.common.machine.steam;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import shishamo_tech.config.STConfig;

public class STSteamParallelMultiblockMachine extends SteamParallelMultiblockMachine {
    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STSteamParallelMultiblockMachine.class, SteamParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    public STSteamParallelMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        setMaxParallels(super.getMaxParallels() * STConfig.PARALLEL_MULTIPLIER.get());
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public static int getDisplayParallelCount(int base) {
        return base;
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof STSteamParallelMultiblockMachine steamMachine)) {
            return ModifierFunction.IDENTITY;
        }
        int targetParallels = steamMachine.getMaxParallels();
        int actualParallels = ParallelLogic.getParallelAmount(machine, recipe, targetParallels);
        if (actualParallels <= 0) return ModifierFunction.NULL;
        ContentModifier modifier = ContentModifier.multiplier(actualParallels);
        return ModifierFunction.builder()
                .parallels(actualParallels)
                .inputModifier(modifier)
                .outputModifier(modifier)
                .build();
    }

}
