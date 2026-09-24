package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.api.capability.recipe.IFilteredHandler;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerGroupDistinctness;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import shishamo_tech.common.machine.ae2.MEOversizePatternBufferPartMachine.InternalSlot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Aggregated slot recipe handlers for {@link MEOversizePatternBufferPartMachine}.
 *
 * <p>Naively exposing one {@link RecipeHandlerList} per pattern slot (1152 for the
 * oversize buffer) makes every GT recipe search simulate that many lists, each cloning
 * content lists. Instead a single {@link STBufferRecipeHandlerList} is exposed: it tries
 * each occupied slot individually (preserving per-slot distinct semantics and slot-index
 * match order) with a cheap item/fluid type prefilter, falling back to shared-only
 * inventory when no slot matches.
 */
public final class STInternalSlotRecipeHandler {

    private final STBufferRecipeHandlerList bufferHandler;
    private final AggregateItemHandler aggregateItemHandler;
    private final AggregateFluidHandler aggregateFluidHandler;
    private final List<RecipeHandlerList> slotHandlers;

    public STInternalSlotRecipeHandler(MEOversizePatternBufferPartMachine buffer, InternalSlot[] slots) {
        this.aggregateItemHandler = new AggregateItemHandler(buffer, slots);
        this.aggregateFluidHandler = new AggregateFluidHandler(buffer, slots);
        this.bufferHandler = new STBufferRecipeHandlerList(buffer, slots,
                aggregateItemHandler, aggregateFluidHandler);
        this.slotHandlers = List.of(bufferHandler);
        for (var slot : slots) {
            slot.setOnContentsChanged(() -> {
                aggregateItemHandler.notifyListeners();
                aggregateFluidHandler.notifyListeners();
            });
        }
    }

    public List<RecipeHandlerList> getSlotHandlers() {
        return slotHandlers;
    }

    public STBufferRecipeHandlerList getBufferHandler() {
        return bufferHandler;
    }

    public AggregateItemHandler getAggregateItemHandler() {
        return aggregateItemHandler;
    }

    public AggregateFluidHandler getAggregateFluidHandler() {
        return aggregateFluidHandler;
    }

    /** Single distinct handler list replacing one-list-per-slot. */
    public static class STBufferRecipeHandlerList extends RecipeHandlerList {

        private final InternalSlot[] slots;
        private final SlotItemHandler[] itemHandlers;
        private final SlotFluidHandler[] fluidHandlers;
        private final IRecipeHandler<?> circuitInventory;
        private final IRecipeHandler<?> shareInventory;
        private final IRecipeHandler<?> shareTank;
        private final List<IRecipeHandler<?>> sharedItemHandlers;
        private final List<IRecipeHandler<?>> sharedFluidHandlers;

        public STBufferRecipeHandlerList(MEOversizePatternBufferPartMachine buffer, InternalSlot[] slots,
                                         AggregateItemHandler aggregateItems,
                                         AggregateFluidHandler aggregateFluids) {
            super(IO.IN);
            this.slots = slots;
            this.itemHandlers = new SlotItemHandler[slots.length];
            this.fluidHandlers = new SlotFluidHandler[slots.length];
            for (int i = 0; i < slots.length; i++) {
                itemHandlers[i] = new SlotItemHandler(slots[i]);
                fluidHandlers[i] = new SlotFluidHandler(slots[i]);
            }
            this.circuitInventory = buffer.getCircuitInventory();
            this.shareInventory = buffer.getShareInventory();
            this.shareTank = buffer.getShareTank();
            this.sharedItemHandlers = List.of(circuitInventory, shareInventory);
            this.sharedFluidHandlers = List.of(shareTank);
            addHandlers(circuitInventory, shareInventory, shareTank, aggregateItems, aggregateFluids);
            this.setGroup(RecipeHandlerGroupDistinctness.BUS_DISTINCT);
        }

        @Override
        public Map<RecipeCapability<?>, List<Object>> handleRecipe(IO io, GTRecipe recipe,
                                                                   Map<RecipeCapability<?>, List<Object>> contents,
                                                                   boolean simulate) {
            if (io != IO.IN || contents.isEmpty()) {
                return contents;
            }
            for (int i = 0; i < slots.length; i++) {
                var slot = slots[i];
                if (slot.isItemEmpty() && slot.isFluidEmpty()) continue;
                if (!couldSlotMatchContents(slot, contents)) continue;
                var left = consume(i, io, recipe, contents, true);
                if (!left.isEmpty()) continue;
                return simulate ? left : consume(i, io, recipe, contents, false);
            }
            var shared = consume(-1, io, recipe, contents, true);
            if (!shared.isEmpty()) {
                return contents;
            }
            return simulate ? shared : consume(-1, io, recipe, contents, false);
        }

        private Map<RecipeCapability<?>, List<Object>> consume(int slotIndex, IO io, GTRecipe recipe,
                                                               Map<RecipeCapability<?>, List<Object>> contents,
                                                               boolean simulate) {
            var copy = new Reference2ObjectOpenHashMap<>(contents);
            for (var it = copy.reference2ObjectEntrySet().fastIterator(); it.hasNext();) {
                var entry = it.next();
                for (var handler : handlersFor(entry.getKey(), slotIndex)) {
                    if (handler.getTotalContentAmount() == 0) continue;
                    var left = handler.handleRecipe(io, recipe, entry.getValue(), simulate);
                    if (left == null) {
                        it.remove();
                        break;
                    }
                    entry.setValue(new ArrayList<>(left));
                }
            }
            return copy;
        }

        private List<IRecipeHandler<?>> handlersFor(RecipeCapability<?> cap, int slotIndex) {
            if (cap == ItemRecipeCapability.CAP) {
                return slotIndex < 0 ? sharedItemHandlers :
                        List.of(circuitInventory, shareInventory, itemHandlers[slotIndex]);
            }
            if (cap == FluidRecipeCapability.CAP) {
                return slotIndex < 0 ? sharedFluidHandlers :
                        List.of(shareTank, fluidHandlers[slotIndex]);
            }
            return List.of();
        }

        private static boolean couldSlotMatchContents(InternalSlot slot,
                                                      Map<RecipeCapability<?>, List<Object>> contents) {
            List<Object> itemContents = contents.get(ItemRecipeCapability.CAP);
            if (itemContents != null) {
                Set<Item> itemTypes = slot.getItemTypes();
                if (!itemTypes.isEmpty()) {
                    for (Object obj : itemContents) {
                        if (!(obj instanceof Ingredient ingredient) || ingredient.isEmpty()) continue;
                        for (ItemStack stack : ingredient.getItems()) {
                            if (itemTypes.contains(stack.getItem())) return true;
                        }
                    }
                }
            }
            List<Object> fluidContents = contents.get(FluidRecipeCapability.CAP);
            if (fluidContents != null) {
                Set<Fluid> fluidTypes = slot.getFluidTypes();
                if (!fluidTypes.isEmpty()) {
                    for (Object obj : fluidContents) {
                        if (!(obj instanceof FluidIngredient ingredient) || ingredient.isEmpty()) continue;
                        for (FluidStack stack : ingredient.getStacks()) {
                            if (fluidTypes.contains(stack.getFluid())) return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public void setDistinct(boolean ignored, boolean notify) {}
    }

    /** Lightweight per-slot item handler; notification is aggregated, not per slot. */
    private static class SlotItemHandler implements IRecipeHandler<Ingredient> {

        private final InternalSlot slot;

        private SlotItemHandler(InternalSlot slot) {
            this.slot = slot;
        }

        @Override
        public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
            if (io != IO.IN || slot.isItemEmpty()) return left;
            return slot.handleItemInternal(left, simulate);
        }

        @Override
        public @NotNull List<Object> getContents() {
            return new ArrayList<>(slot.getItems());
        }

        @Override
        public double getTotalContentAmount() {
            long sum = 0;
            for (ItemStack stack : slot.getItems()) sum += stack.getCount();
            return sum;
        }

        @Override
        public RecipeCapability<Ingredient> getCapability() {
            return ItemRecipeCapability.CAP;
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public int getPriority() {
            return IFilteredHandler.HIGH;
        }
    }

    /** Lightweight per-slot fluid handler; notification is aggregated, not per slot. */
    private static class SlotFluidHandler implements IRecipeHandler<FluidIngredient> {

        private final InternalSlot slot;

        private SlotFluidHandler(InternalSlot slot) {
            this.slot = slot;
        }

        @Override
        public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left,
                                                       boolean simulate) {
            if (io != IO.IN || slot.isFluidEmpty()) return left;
            return slot.handleFluidInternal(left, simulate);
        }

        @Override
        public @NotNull List<Object> getContents() {
            return new ArrayList<>(slot.getFluids());
        }

        @Override
        public double getTotalContentAmount() {
            long sum = 0;
            for (FluidStack stack : slot.getFluids()) sum += stack.getAmount();
            return sum;
        }

        @Override
        public RecipeCapability<FluidIngredient> getCapability() {
            return FluidRecipeCapability.CAP;
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public int getPriority() {
            return IFilteredHandler.HIGH;
        }
    }

    /** Content view over all occupied slots for search/sort queries; never consumes. */
    public static class AggregateItemHandler extends NotifiableRecipeHandlerTrait<Ingredient> {

        private final InternalSlot[] slots;

        public AggregateItemHandler(MEOversizePatternBufferPartMachine buffer, InternalSlot[] slots) {
            super(buffer);
            this.slots = slots;
        }

        @Override
        public IO getHandlerIO() {
            return IO.IN;
        }

        @Override
        public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
            return left;
        }

        @Override
        public @NotNull List<Object> getContents() {
            List<Object> contents = new ArrayList<>();
            for (var slot : slots) {
                if (!slot.isItemEmpty()) contents.addAll(slot.getItems());
            }
            return contents;
        }

        @Override
        public double getTotalContentAmount() {
            long sum = 0;
            for (var slot : slots) {
                if (slot.isItemEmpty()) continue;
                for (ItemStack stack : slot.getItems()) sum += stack.getCount();
            }
            return sum;
        }

        @Override
        public RecipeCapability<Ingredient> getCapability() {
            return ItemRecipeCapability.CAP;
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public void setDistinct(boolean ignored) {}

        @Override
        public int getPriority() {
            return IFilteredHandler.HIGH;
        }
    }

    /** Content view over all occupied slots for search/sort queries; never consumes. */
    public static class AggregateFluidHandler extends NotifiableRecipeHandlerTrait<FluidIngredient> {

        private final InternalSlot[] slots;

        public AggregateFluidHandler(MEOversizePatternBufferPartMachine buffer, InternalSlot[] slots) {
            super(buffer);
            this.slots = slots;
        }

        @Override
        public IO getHandlerIO() {
            return IO.IN;
        }

        @Override
        public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left,
                                                       boolean simulate) {
            return left;
        }

        @Override
        public @NotNull List<Object> getContents() {
            List<Object> contents = new ArrayList<>();
            for (var slot : slots) {
                if (!slot.isFluidEmpty()) contents.addAll(slot.getFluids());
            }
            return contents;
        }

        @Override
        public double getTotalContentAmount() {
            long sum = 0;
            for (var slot : slots) {
                if (slot.isFluidEmpty()) continue;
                for (FluidStack stack : slot.getFluids()) sum += stack.getAmount();
            }
            return sum;
        }

        @Override
        public RecipeCapability<FluidIngredient> getCapability() {
            return FluidRecipeCapability.CAP;
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public void setDistinct(boolean ignored) {}

        @Override
        public int getPriority() {
            return IFilteredHandler.HIGH;
        }
    }
}
