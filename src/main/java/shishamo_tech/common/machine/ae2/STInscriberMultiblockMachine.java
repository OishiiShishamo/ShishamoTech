package shishamo_tech.common.machine.ae2;

import appeng.recipes.handlers.InscriberRecipe;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import shishamo_tech.common.recipe.STOverclockingLogic;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import shishamo_tech.common.recipe.STRecipeTypes;
import shishamo_tech.config.STConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class STInscriberMultiblockMachine extends WorkableElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STInscriberMultiblockMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    public STInscriberMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new InscriberRecipeLogic(this);
    }

    public int getParallelCount() {
        return 4 * (getTier() + 1) * STConfig.PARALLEL_MULTIPLIER.get();
    }

    public static int getDisplayParallelCount(int tier) {
        return 4 * (tier + 1);
    }

    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof STInscriberMultiblockMachine inscriber)) {
            return ModifierFunction.IDENTITY;
        }
        long voltage = 0;
        if (machine instanceof IOverclockMachine ocMachine) {
            voltage = ocMachine.getOverclockVoltage();
        }
        int targetParallel = inscriber.getParallelCount();
        int actualParallel = ParallelLogic.getParallelAmountWithoutEU(machine, recipe, targetParallel);
        if (actualParallel <= 0) return ModifierFunction.NULL;
        ContentModifier modifier = ContentModifier.multiplier(actualParallel);
        ModifierFunction parallelMod = ModifierFunction.builder()
                .parallels(actualParallel)
                .inputModifier(modifier)
                .outputModifier(modifier)
                .build();
        long targetVoltage = voltage;
        ModifierFunction composed;
        if (targetVoltage > 0 && RecipeHelper.getRealEUt(recipe).getTotalEU() > 0) {
            ModifierFunction ocMod = STOverclockingLogic.TRIPLE_OVERCLOCK.getModifier(
                    machine, recipe, targetVoltage);
            composed = ocMod.andThen(parallelMod);
            ModifierFunction powerMod = r -> {
                if (r == null) return null;
                var withIO = RecipeHelper.getRealEUtWithIO(r);
                long currentEUt = withIO.stack().getTotalEU();
                if (currentEUt <= 0 || currentEUt == targetVoltage) return r;
                double mult = (double) targetVoltage / currentEUt;
                return ModifierFunction.builder()
                        .eutMultiplier(mult)
                        .build()
                        .apply(r);
            };
            composed = composed.andThen(powerMod);
        } else {
            composed = parallelMod;
        }
        return composed;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable("shishamo_tech.machine.parallel_count", getParallelCount()));
        }
    }

protected static class InscriberRecipeLogic extends RecipeLogic {

        public InscriberRecipeLogic(STInscriberMultiblockMachine machine) {
            super(machine);
        }

        @Override
        public void onMachineLoad() {
            super.onMachineLoad();
            var level = getMachine().getLevel();
            if (level != null && !level.isClientSide) {
                var recipeManager = level.getRecipeManager();
                var ae2Recipes = recipeManager.getAllRecipesFor(InscriberRecipe.TYPE);
                if (ae2Recipes != null) {
                    STRecipeTypes.populateRecipeDB(ae2Recipes.toArray(new InscriberRecipe[0]));
                }
            }
        }

        @Override
        public Iterator<GTRecipe> searchRecipe() {
            STInscriberMultiblockMachine m = (STInscriberMultiblockMachine) getMachine();
            Iterator<GTRecipe> dbResult = m.getRecipeType().searchRecipe(m, r -> true);

            int configuredCircuit = getConfiguredCircuit(m);
            GTCEu.LOGGER.debug("STInscriber searchRecipe: configuredCircuit={}", configuredCircuit);

            List<GTRecipe> filtered = new ArrayList<>();
            while (dbResult.hasNext()) {
                GTRecipe recipe = dbResult.next();
                int recipeCircuit = getRecipeCircuit(recipe);
                GTCEu.LOGGER.debug("STInscriber searchRecipe: recipe={} recipeCircuit={} configuredCircuit={}",
                        recipe.getId(), recipeCircuit, configuredCircuit);
                if (configuredCircuit == 0) {
                    if (recipeCircuit == 0) {
                        filtered.add(recipe);
                    }
                } else if (recipeCircuit == configuredCircuit) {
                    filtered.add(recipe);
                }
            }
            GTCEu.LOGGER.debug("STInscriber searchRecipe: {} recipes after circuit filtering (from {} total)",
                    filtered.size(), filtered.size() + (dbResult instanceof ArrayList<?> a ? 0 : 0));
            return filtered.iterator();
        }

        private static int getRecipeCircuit(GTRecipe recipe) {
            var itemInputs = recipe.inputs.get(ItemRecipeCapability.CAP);
            if (itemInputs == null) return 0;
            for (var content : itemInputs) {
                if (content.getContent() instanceof net.minecraft.world.item.crafting.Ingredient ing) {
                    for (var stack : ing.getItems()) {
                        if (stack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                            return IntCircuitBehaviour.getCircuitConfiguration(stack);
                        }
                    }
                }
            }
            return 0;
        }

        private int getConfiguredCircuit(STInscriberMultiblockMachine machine) {
            var flat = machine.capabilitiesFlat;
            if (flat == null) return 0;
            var handlers = flat
                    .getOrDefault(IO.IN, java.util.Collections.emptyMap())
                    .getOrDefault(ItemRecipeCapability.CAP, java.util.Collections.emptyList());
            GTCEu.LOGGER.debug("STInscriber getConfiguredCircuit: scanning {} handlers", handlers.size());
            for (var handler : handlers) {
                Object contents = handler.getContents();
                GTCEu.LOGGER.debug("STInscriber getConfiguredCircuit: handler={} contents={}",
                        handler.getClass().getSimpleName(),
                        contents != null ? contents.getClass().getSimpleName() : "null");
                if (contents instanceof List<?> list) {
                    for (Object obj : list) {
                        GTCEu.LOGGER.debug("STInscriber getConfiguredCircuit:   obj={}", obj);
                        if (obj instanceof ItemStack stack) {
                            GTCEu.LOGGER.debug("STInscriber getConfiguredCircuit:     stack={} tag={}",
                                    stack.getItem(), stack.getTag());
                            if (stack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                                var tag = stack.getTag();
                                if (tag != null) {
                                    int circuit = tag.getInt("Configuration");
                                    GTCEu.LOGGER.debug("STInscriber getConfiguredCircuit: FOUND circuit={}", circuit);
                                    return circuit;
                                }
                            }
                        }
                    }
                }
            }
            return 0;
        }
    }
}
