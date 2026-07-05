package shishamo_tech.common.machine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import shishamo_tech.config.STConfig;

import org.jetbrains.annotations.Nullable;

/**
 * MachineTrait for configurable parallel processing.
 *
 * <p>Applies {@code ModifierFunction.builder().parallels(count)} where
 * {@code count = baseParallels x STConfig.parallelMultiplier}.
 *
 * <p>This trait can be attached to any machine via
 * {@code machine.attachTrait(new STParallelTrait(machine, baseParallels))}
 * or used as a standalone recipe modifier reference.
 *
 * <h3>Note</h3>
 * Currently, steam and electric multiblocks use their own static
 * {@code recipeModifier} methods for simplicity. This trait exists
 * as an alternative attachment mechanism for machines that need
 * dynamically adjustable parallel counts (e.g. circuit-driven mode switching).
 */
public class STParallelTrait extends MachineTrait {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STParallelTrait.class);

    private final int baseParallels;

    public STParallelTrait(MetaMachine machine, int baseParallels) {
        super(machine);
        this.baseParallels = baseParallels;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    /**
     * @return total parallel count = base x config multiplier
     */
    public int getParallelCount() {
        return baseParallels * STConfig.parallelMultiplier;
    }

    /**
     * Can be used as a RecipeModifier reference.
     */
    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (machine instanceof STParallelTraitHolder holder) {
            STParallelTrait trait = holder.getParallelTrait();
            if (trait != null) {
                int targetParallel = trait.getParallelCount();
                int actualParallel = ParallelLogic.getParallelAmountWithoutEU(machine, recipe, targetParallel);
                if (actualParallel <= 0) return ModifierFunction.NULL;
                return ModifierFunction.builder()
                        .parallels(actualParallel)
                        .build();
            }
        }
        return ModifierFunction.IDENTITY;
    }

    /**
     * Interface for machines that expose their STParallelTrait.
     */
    public interface STParallelTraitHolder {
        @Nullable STParallelTrait getParallelTrait();
    }
}
