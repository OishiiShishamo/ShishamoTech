package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import shishamo_tech.ShishamoTech;
import shishamo_tech.common.recipe.STOverclockingLogic;
import shishamo_tech.common.recipe.STRecipeTypes;
import shishamo_tech.config.STConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Integrated Ore Processing Multiblock (LV, coil-tier parallel bonus).
 *
 * <p><b>Cross-parallel</b>: mixed raw ores share the parallel budget round-robin
 * (max-min fair) in a single operation. {@link OreProcessorRecipeLogic} merges all
 * matched raw ores (each following the programmed-circuit route) into one synthetic
 * recipe whose inputs/outputs aggregate every ore, instead of running the same
 * recipe N times in parallel.
 *
 * <p>The route is selected by a programmed circuit inserted into the input bus:
 *   1 = Macerate->Ore Washer->Thermal Centrifuge->Macerate
 *   2 = Macerate->Ore Washer->Macerate->Centrifuge
 *   3 = Macerate->Macerate->Centrifuge
 *   4 = Macerate->Ore Washer->Sifter
 *   5 = Macerate->Chemical Bathing->Macerate->Centrifuge
 *   6 = Macerate->Chemical Bathing->Thermal Centrifuge->Macerate
 *   7 = Forge Hammer->Forge Hammer->Simple Washer
 */
public class STIntegratedOreProcessorMachine extends CoilWorkableElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STIntegratedOreProcessorMachine.class, CoilWorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    public STIntegratedOreProcessorMachine(IMachineBlockEntity holder, Object... args) {
        super(holder);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new OreProcessorRecipeLogic(this);
    }

    public int getCoilParallelBonus() {
        return STOverclockingLogic.getCoilBonus(getCoilTier());
    }

    public int getParallelCount() {
        return STOverclockingLogic.computeParallelCount(getTier(), getDefinition().getTier(),
                getCoilParallelBonus());
    }

    public static int getDisplayParallelCount(int machineTier, int coilTier) {
        return STOverclockingLogic.computeDisplayParallel(machineTier, coilTier);
    }

    /**
     * Parallelism is handled entirely by {@link OreProcessorRecipeLogic#searchRecipe()}
     * (cross-parallel merging), so this modifier only enforces the machine-disabled gate.
     */
    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!STConfig.isElectricEnabled() || !STConfig.isMachineEnabled(machine)) {
            return ModifierFunction.NULL;
        }
        return ModifierFunction.IDENTITY;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable("shishamo_tech.machine.parallel_count", getParallelCount()));
            textList.add(Component.translatable("shishamo_tech.machine.coil_tier",
                    Component.translatable("block.gtceu." + getCoilType().getName() + "_coil_block")));
            int circuit = getConfiguredCircuit(this);
            if (circuit <= 0) {
                textList.add(Component.translatable("shishamo_tech.machine.ore_processor.route.none"));
            } else {
                textList.add(Component.translatable("shishamo_tech.machine.ore_processor.route", circuit));
            }
        }
    }

    protected static int getConfiguredCircuit(STIntegratedOreProcessorMachine machine) {
        var flat = machine.capabilitiesFlat;
        if (flat == null) return 0;
        var handlers = flat.getOrDefault(IO.IN, Collections.emptyMap())
                .getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList());
        for (var handler : handlers) {
            Object contents = handler.getContents();
            if (contents instanceof List<?> list) {
                for (Object obj : list) {
                    if (obj instanceof ItemStack stack && stack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                        var tag = stack.getTag();
                        if (tag != null) {
                            return tag.getInt("Configuration");
                        }
                    }
                }
            }
        }
        return 0;
    }

    protected static class OreProcessorRecipeLogic extends RecipeLogic {

        private record ItemReq(Ingredient ingredient, int amount) {}

        private record Request(List<ItemReq> items, List<FluidStack> fluids, int duration) {}

        private record ChancedKey(Item item, int chance, int boost) {}

        public OreProcessorRecipeLogic(STIntegratedOreProcessorMachine machine) {
            super(machine);
        }

        @Override
        public Iterator<GTRecipe> searchRecipe() {
            STIntegratedOreProcessorMachine m = (STIntegratedOreProcessorMachine) getMachine();
            int circuit = m.getRecipeType() == null ? 0 : getConfiguredCircuit(m);
            if (circuit <= 0) return Collections.emptyIterator();

            List<GTRecipe> candidates = new ArrayList<>();
            Iterator<GTRecipe> dbResult = m.getRecipeType().searchRecipe(m, r -> true);
            while (dbResult.hasNext()) {
                GTRecipe recipe = dbResult.next();
                if (getRecipeCircuit(recipe) == circuit) {
                    candidates.add(recipe);
                }
            }
            if (candidates.isEmpty()) return Collections.emptyIterator();

            Map<GTRecipe, Request> requests = new LinkedHashMap<>();
            for (GTRecipe c : candidates) {
                requests.put(c, buildRequest(c));
            }

            List<ItemStack> remainingItems = collectItems(m);
            List<FluidStack> remainingFluids = collectFluids(m);

            Map<Item, Integer> inputCounts = new LinkedHashMap<>();
            Map<net.minecraft.world.level.material.Fluid, Integer> fluidTotal = new LinkedHashMap<>();
            Map<Item, Integer> regularOut = new LinkedHashMap<>();
            Map<ChancedKey, Integer> chancedOut = new LinkedHashMap<>();
            int total = 0;
            int baseDuration = 0;
            int maxParallel = Math.max(1, m.getParallelCount());

            // Round-robin (max-min fair): split the parallel budget evenly across every
            // candidate that still fits, refilling the shares each round. Bulk-apply keeps
            // this O(rounds x candidates x contents) instead of O(parallels x ...).
            // Single-ore behavior is unchanged (the lone candidate takes everything).
            List<Map.Entry<GTRecipe, Request>> entries = new ArrayList<>(requests.entrySet());
            int remainingParallel = maxParallel;
            boolean progress = true;
            while (remainingParallel > 0 && progress) {
                progress = false;
                int active = 0;
                for (var entry : entries) {
                    if (maxApplications(entry.getValue(), remainingItems, remainingFluids) > 0) active++;
                }
                if (active == 0) break;
                int share = Math.max(1, remainingParallel / active);
                for (var entry : entries) {
                    if (remainingParallel <= 0) break;
                    Request req = entry.getValue();
                    int fits = maxApplications(req, remainingItems, remainingFluids);
                    if (fits <= 0) continue;
                    int take = Math.min(fits, Math.min(share, remainingParallel));
                    consumeBulk(req, take, remainingItems, remainingFluids);
                    merge(entry.getKey(), req, take, inputCounts, fluidTotal, regularOut, chancedOut);
                    if (baseDuration == 0) baseDuration = req.duration();
                    total += take;
                    remainingParallel -= take;
                    progress = true;
                }
            }

            if (total == 0) return Collections.emptyIterator();

            GTRecipe merged = buildMerged(circuit, baseDuration, inputCounts, fluidTotal, regularOut, chancedOut);
            return (merged == null) ? Collections.emptyIterator() : Collections.singletonList(merged).iterator();
        }

        private static Request buildRequest(GTRecipe recipe) {
            List<ItemReq> items = new ArrayList<>();
            List<FluidStack> fluids = new ArrayList<>();
            var itemInputs = recipe.inputs.get(ItemRecipeCapability.CAP);
            if (itemInputs != null) {
                for (Content c : itemInputs) {
                    Ingredient ing = (Ingredient) c.getContent();
                    if (isCircuit(ing)) continue;
                    int amount = ing instanceof SizedIngredient si ? si.getAmount() : 1;
                    items.add(new ItemReq(ing, amount));
                }
            }
            var fluidInputs = recipe.inputs.get(FluidRecipeCapability.CAP);
            if (fluidInputs != null) {
                for (Content c : fluidInputs) {
                    var fi = FluidRecipeCapability.CAP.of(c.getContent());
                    FluidStack fs = fi.getStacks()[0].copy();
                    fs.setAmount(fi.getAmount());
                    fluids.add(fs);
                }
            }
            return new Request(items, fluids, recipe.duration);
        }

        private static int maxApplications(Request req, List<ItemStack> items, List<FluidStack> fluids) {
            int fits = Integer.MAX_VALUE;
            for (ItemReq r : req.items()) {
                int avail = 0;
                for (ItemStack s : items) {
                    if (!s.isEmpty() && r.ingredient().test(s)) avail += s.getCount();
                }
                fits = Math.min(fits, avail / r.amount());
                if (fits <= 0) return 0;
            }
            for (FluidStack f : req.fluids()) {
                int avail = 0;
                for (FluidStack tank : fluids) {
                    if (tank.isFluidEqual(f)) avail += tank.getAmount();
                }
                fits = Math.min(fits, avail / f.getAmount());
                if (fits <= 0) return 0;
            }
            return fits == Integer.MAX_VALUE ? 0 : fits;
        }

        private static void consumeBulk(Request req, int take, List<ItemStack> items, List<FluidStack> fluids) {
            for (ItemReq r : req.items()) {
                int need = r.amount() * take;
                for (ItemStack s : items) {
                    if (need <= 0) break;
                    if (!s.isEmpty() && r.ingredient().test(s)) {
                        int drain = Math.min(need, s.getCount());
                        s.shrink(drain);
                        need -= drain;
                    }
                }
            }
            for (FluidStack f : req.fluids()) {
                int need = f.getAmount() * take;
                for (FluidStack tank : fluids) {
                    if (need <= 0) break;
                    if (tank.getAmount() > 0 && tank.isFluidEqual(f)) {
                        int drain = Math.min(need, tank.getAmount());
                        tank.shrink(drain);
                        need -= drain;
                    }
                }
            }
        }

        private static void merge(GTRecipe recipe, Request req, int take, Map<Item, Integer> inputCounts,
                                  Map<net.minecraft.world.level.material.Fluid, Integer> fluidTotal,
                                  Map<Item, Integer> regularOut,
                                  Map<ChancedKey, Integer> chancedOut) {
            for (ItemReq r : req.items()) {
                ItemStack sample = r.ingredient().getItems()[0];
                int amount = r.ingredient() instanceof SizedIngredient si ? si.getAmount() : 1;
                inputCounts.merge(sample.getItem(), amount * take, Integer::sum);
            }
            for (FluidStack f : req.fluids()) {
                fluidTotal.merge(f.getFluid(), f.getAmount() * take, Integer::sum);
            }
            var outs = recipe.outputs.get(ItemRecipeCapability.CAP);
            if (outs != null) {
                for (Content c : outs) {
                    Ingredient ing = (Ingredient) c.getContent();
                    ItemStack sample = ing.getItems()[0].copy();
                    int amount = ing instanceof SizedIngredient si ? si.getAmount() : sample.getCount();
                    if (c.chance < c.maxChance) {
                        chancedOut.merge(new ChancedKey(sample.getItem(), c.chance, c.tierChanceBoost), amount * take, Integer::sum);
                    } else {
                        regularOut.merge(sample.getItem(), amount * take, Integer::sum);
                    }
                }
            }
        }

        @Nullable
        private static GTRecipe buildMerged(int circuit, int duration, Map<Item, Integer> inputCounts,
                                            Map<net.minecraft.world.level.material.Fluid, Integer> fluidTotal,
                                            Map<Item, Integer> regularOut,
                                            Map<ChancedKey, Integer> chancedOut) {
            GTRecipeBuilder b = STRecipeTypes.INTEGRATED_ORE_PROCESSING
                    .recipeBuilder(ShishamoTech.id("cross_" + circuit));
            for (Map.Entry<Item, Integer> e : inputCounts.entrySet()) {
                b.inputItems(new ItemStack(e.getKey(), e.getValue()));
            }
            for (Map.Entry<net.minecraft.world.level.material.Fluid, Integer> e : fluidTotal.entrySet()) {
                b.inputFluids(new FluidStack(e.getKey(), e.getValue()));
            }
            for (Map.Entry<Item, Integer> e : regularOut.entrySet()) {
                b.outputItems(new ItemStack(e.getKey(), e.getValue()));
            }
            for (Map.Entry<ChancedKey, Integer> e : chancedOut.entrySet()) {
                b.chancedOutput(new ItemStack(e.getKey().item(), e.getValue()), e.getKey().chance(), e.getKey().boost());
            }
            b.circuitMeta(circuit);
            b.EUt(GTValues.VA[GTValues.LV]);
            b.duration(duration);
            return b.buildRawRecipe();
        }

        private static List<ItemStack> collectItems(STIntegratedOreProcessorMachine machine) {
            List<ItemStack> result = new ArrayList<>();
            var flat = machine.capabilitiesFlat;
            if (flat == null) return result;
            var handlers = flat.getOrDefault(IO.IN, Collections.emptyMap())
                    .getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList());
            for (var handler : handlers) {
                Object contents = handler.getContents();
                if (contents instanceof List<?> list) {
                    for (Object obj : list) {
                        if (obj instanceof ItemStack stack && !stack.isEmpty()) {
                            result.add(stack.copy());
                        }
                    }
                }
            }
            return result;
        }

        private static List<FluidStack> collectFluids(STIntegratedOreProcessorMachine machine) {
            List<FluidStack> result = new ArrayList<>();
            var flat = machine.capabilitiesFlat;
            if (flat == null) return result;
            var handlers = flat.getOrDefault(IO.IN, Collections.emptyMap())
                    .getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList());
            for (var handler : handlers) {
                Object contents = handler.getContents();
                if (contents instanceof List<?> list) {
                    for (Object obj : list) {
                        if (obj instanceof FluidStack stack && !stack.isEmpty()) {
                            result.add(stack.copy());
                        }
                    }
                }
            }
            return result;
        }

        private static boolean isCircuit(Ingredient ing) {
            for (ItemStack s : ing.getItems()) {
                if (s.is(GTItems.PROGRAMMED_CIRCUIT.get())) return true;
            }
            return false;
        }

        private static int getRecipeCircuit(GTRecipe recipe) {
            var itemInputs = recipe.inputs.get(ItemRecipeCapability.CAP);
            if (itemInputs == null) return 0;
            for (var content : itemInputs) {
                if (content.getContent() instanceof Ingredient ing) {
                    for (var stack : ing.getItems()) {
                        if (stack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                            return IntCircuitBehaviour.getCircuitConfiguration(stack);
                        }
                    }
                }
            }
            return 0;
        }
    }
}