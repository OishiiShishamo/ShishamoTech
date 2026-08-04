package shishamo_tech.common.machine.steam;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shishamo_tech.ShishamoTech;
import shishamo_tech.common.recipe.STRecipeTypes;
import shishamo_tech.config.STConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Steam-powered multiblock that generates random resources out of the void.
 * <p>
 * Each finished recipe rolls one resource from a weighted table: hardcoded primary
 * fluids (oil &amp; air family), every item in the {@code forge:raw_materials} tag,
 * plus any config-defined extras. The rolled resource is then output multiplied by
 * the machine's parallel count.
 */
public class STVoidResourceMinerMachine extends STSteamParallelMultiblockMachine {

    public static final int BASE_EUT = 32; // LV tier, compatible with steam machines
    public static final int BASE_DURATION = 200; // 10s per run

    private static final int FLUID_AMOUNT = 100; // mB rolled per recipe
    private static final int FLUID_WEIGHT = 8;
    private static final int RAW_MATERIAL_WEIGHT = 4;
    private static final TagKey<Item> RAW_MATERIALS_TAG =
            TagKey.create(Registries.ITEM, new ResourceLocation("forge", "raw_materials"));

    private static final Random RANDOM = new Random();
    private static final AtomicReference<List<ResourceEntry>> RESOURCE_TABLE = new AtomicReference<>();
    private static final AtomicReference<GTRecipe> BASE_RECIPE = new AtomicReference<>();

    public STVoidResourceMinerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new VoidResourceMinerLogic(this);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable("shishamo_tech.machine.void_miner.resources",
                    getResourceTable().size()));
        }
    }

    public record ResourceEntry(ItemStack item, FluidStack fluid, int weight) {

        public boolean isFluid() {
            return fluid != null && !fluid.isEmpty();
        }

        public boolean isItem() {
            return item != null && !item.isEmpty();
        }
    }

    /**
     * The synthetic recipe searched by {@link VoidResourceMinerLogic}. It has no real
     * inputs besides LV-tier EU (drained as steam by the steam machine) and no outputs;
     * the actual output is rolled when the recipe finishes.
     */
    public static GTRecipe getBaseRecipe() {
        GTRecipe recipe = BASE_RECIPE.get();
        if (recipe == null) {
            recipe = STRecipeTypes.VOID_RESOURCE_MINING_RECIPES.recipeBuilder(
                            ShishamoTech.id("mega_steam_void_resource_miner"))
                    .EUt(BASE_EUT)
                    .duration(BASE_DURATION)
                    .buildRawRecipe();
            BASE_RECIPE.set(recipe);
        }
        return recipe;
    }

    /**
     * Builds a display recipe for JEI showing a single rolled resource (count 1).
     */
    public static GTRecipe buildJeiRecipe(int index, ResourceEntry entry) {
        var builder = STRecipeTypes.VOID_RESOURCE_MINING_RECIPES.recipeBuilder(
                        ShishamoTech.id("void_resource_miner_jei_" + index))
                .EUt(BASE_EUT)
                .duration(BASE_DURATION);
        if (entry.isFluid()) {
            builder.outputFluids(entry.fluid().copy());
        } else if (entry.isItem()) {
            builder.outputItems(entry.item().copy());
        }
        return builder.buildRawRecipe();
    }

    public static void invalidateResourceTable() {
        RESOURCE_TABLE.set(null);
    }

    public static List<ResourceEntry> getResourceTable() {
        List<ResourceEntry> table = RESOURCE_TABLE.get();
        if (table == null) {
            table = List.copyOf(buildResourceTable());
            RESOURCE_TABLE.set(table);
        }
        return table;
    }

    @Nullable
    public static ResourceEntry rollResource() {
        List<ResourceEntry> table = getResourceTable();
        if (table.isEmpty()) return null;
        long total = 0;
        for (ResourceEntry entry : table) total += entry.weight();
        if (total <= 0) return null;
        long r = (long) (RANDOM.nextDouble() * total);
        long acc = 0;
        for (ResourceEntry entry : table) {
            acc += entry.weight();
            if (r < acc) return entry;
        }
        return table.get(table.size() - 1);
    }

    private static List<ResourceEntry> buildResourceTable() {
        List<ResourceEntry> entries = new ArrayList<>();

        addFluid(entries, GTMaterials.Oil.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.OilHeavy.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.OilLight.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.NaturalGas.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.SulfuricHeavyFuel.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.SulfuricLightFuel.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.RawOil.getFluid(FLUID_AMOUNT));

        addFluid(entries, GTMaterials.Nitrogen.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.Oxygen.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.Hydrogen.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.Helium.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.Argon.getFluid(FLUID_AMOUNT));
        addFluid(entries, GTMaterials.CarbonDioxide.getFluid(FLUID_AMOUNT));

        var rawMaterials = ForgeRegistries.ITEMS.tags().getTag(RAW_MATERIALS_TAG);
        if (rawMaterials != null && !rawMaterials.isEmpty()) {
            for (Item item : rawMaterials) {
                if (item != null && item != Items.AIR) {
                    entries.add(new ResourceEntry(new ItemStack(item), FluidStack.EMPTY, RAW_MATERIAL_WEIGHT));
                }
            }
        }

        // Ancient Debris is not part of the forge:raw_materials tag, so it is added explicitly.
        entries.add(new ResourceEntry(new ItemStack(Items.ANCIENT_DEBRIS, 1), FluidStack.EMPTY, RAW_MATERIAL_WEIGHT));

        for (String spec : STConfig.voidMinerExtraItems) {
            ResourceEntry entry = parseItemEntry(spec);
            if (entry != null) entries.add(entry);
        }
        for (String spec : STConfig.voidMinerExtraFluids) {
            ResourceEntry entry = parseFluidEntry(spec);
            if (entry != null) entries.add(entry);
        }
        return entries;
    }

    private static void addFluid(List<ResourceEntry> entries, FluidStack fluid) {
        if (fluid != null && !fluid.isEmpty()) {
            entries.add(new ResourceEntry(ItemStack.EMPTY, fluid, FLUID_WEIGHT));
        }
    }

    @Nullable
    private static ResourceEntry parseItemEntry(String spec) {
        String[] parts = spec.split("\\|");
        if (parts.length == 0) return null;
        ResourceLocation id = ResourceLocation.tryParse(parts[0].trim());
        if (id == null) return null;
        Item item = BuiltInRegistries.ITEM.get(id);
        if (item == null || item == Items.AIR) {
            ShishamoTech.LOGGER.warn("STVoidResourceMinerMachine: unknown item '{}' in voidMinerExtraItems", id);
            return null;
        }
        int count = parseInt(parts, 1, 1);
        int weight = parseInt(parts, 2, 1);
        if (count <= 0 || weight <= 0) return null;
        return new ResourceEntry(new ItemStack(item, count), FluidStack.EMPTY, weight);
    }

    @Nullable
    private static ResourceEntry parseFluidEntry(String spec) {
        String[] parts = spec.split("\\|");
        if (parts.length == 0) return null;
        ResourceLocation id = ResourceLocation.tryParse(parts[0].trim());
        if (id == null) return null;
        Fluid fluid = BuiltInRegistries.FLUID.get(id);
        if (fluid == null || fluid.isSame(Fluids.EMPTY)) {
            ShishamoTech.LOGGER.warn("STVoidResourceMinerMachine: unknown fluid '{}' in voidMinerExtraFluids", id);
            return null;
        }
        int amount = parseInt(parts, 1, FLUID_AMOUNT);
        int weight = parseInt(parts, 2, 1);
        if (amount <= 0 || weight <= 0) return null;
        return new ResourceEntry(ItemStack.EMPTY, new FluidStack(fluid, amount), weight);
    }

    private static int parseInt(String[] parts, int index, int defaultValue) {
        if (parts.length <= index) return defaultValue;
        try {
            return Integer.parseInt(parts[index].trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Recipe logic that always runs the synthetic base recipe and, on finish, rolls a
     * random resource which is output multiplied by the machine's parallel count.
     */
    public static class VoidResourceMinerLogic extends RecipeLogic {

        public VoidResourceMinerLogic(STVoidResourceMinerMachine machine) {
            super(machine);
        }

        @Override
        public @NotNull Iterator<GTRecipe> searchRecipe() {
            return List.<GTRecipe>of(STVoidResourceMinerMachine.getBaseRecipe()).iterator();
        }

        @Override
        protected ActionResult handleRecipeIO(GTRecipe recipe, IO io) {
            if (io == IO.OUT) {
                return handleVoidOutputs(recipe);
            }
            return super.handleRecipeIO(recipe, io);
        }

        private ActionResult handleVoidOutputs(GTRecipe recipe) {
            ResourceEntry entry = STVoidResourceMinerMachine.rollResource();
            if (entry == null) return ActionResult.SUCCESS;

            int parallels = Math.max(1, recipe.parallels);
            var builder = STRecipeTypes.VOID_RESOURCE_MINING_RECIPES.recipeBuilder(
                            ShishamoTech.id("void_resource_miner_output"))
                    .duration(1);
            if (entry.isFluid()) {
                FluidStack fluid = entry.fluid().copy();
                fluid.setAmount((int) Math.min(Integer.MAX_VALUE, (long) fluid.getAmount() * parallels));
                builder.outputFluids(fluid);
            } else if (entry.isItem()) {
                ItemStack item = entry.item().copy();
                item.setCount((int) Math.min(Integer.MAX_VALUE, (long) item.getCount() * parallels));
                builder.outputItems(item);
            } else {
                return ActionResult.SUCCESS;
            }
            return RecipeHelper.handleRecipeIO(machine, builder.buildRawRecipe(), IO.OUT, chanceCaches);
        }
    }
}
