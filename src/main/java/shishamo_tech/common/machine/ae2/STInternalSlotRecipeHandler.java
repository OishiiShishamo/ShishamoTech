package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.api.capability.recipe.IFilteredHandler;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
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
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Slot-per-recipe-handler list for {@link MEOversizePatternBufferPartMachine}.
 * Mirrors GTCEu's InternalSlotRecipeHandler but bound to this mod's buffer type.
 */
public final class STInternalSlotRecipeHandler {

    private final List<RecipeHandlerList> slotHandlers;

    public STInternalSlotRecipeHandler(MEOversizePatternBufferPartMachine buffer, InternalSlot[] slots) {
        this.slotHandlers = new ArrayList<>(slots.length);
        for (int i = 0; i < slots.length; i++) {
            var rhl = new SlotRHL(buffer, slots[i], i);
            slotHandlers.add(rhl);
        }
    }

    public List<RecipeHandlerList> getSlotHandlers() {
        return slotHandlers;
    }

    public static class SlotRHL extends RecipeHandlerList {

        private final SlotItemRecipeHandler itemRecipeHandler;
        private final SlotFluidRecipeHandler fluidRecipeHandler;

        public SlotRHL(MEOversizePatternBufferPartMachine buffer, InternalSlot slot, int idx) {
            super(IO.IN);
            itemRecipeHandler = new SlotItemRecipeHandler(buffer, slot, idx);
            fluidRecipeHandler = new SlotFluidRecipeHandler(buffer, slot, idx);
            addHandlers(buffer.getCircuitInventory(), buffer.getShareInventory(), buffer.getShareTank(),
                    itemRecipeHandler, fluidRecipeHandler);
            this.setGroup(RecipeHandlerGroupDistinctness.BUS_DISTINCT);
        }

        public SlotItemRecipeHandler getItemRecipeHandler() {
            return itemRecipeHandler;
        }

        public SlotFluidRecipeHandler getFluidRecipeHandler() {
            return fluidRecipeHandler;
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public void setDistinct(boolean ignored, boolean notify) {}
    }

    private static class SlotItemRecipeHandler extends NotifiableRecipeHandlerTrait<Ingredient> {

        private final InternalSlot slot;
        private final int priority;

        private final int size = 81;
        private final RecipeCapability<Ingredient> capability = ItemRecipeCapability.CAP;
        private final IO handlerIO = IO.IN;

        @Override
        public int getSize() {
            return size;
        }

        @Override
        public RecipeCapability<Ingredient> getCapability() {
            return capability;
        }

        @Override
        public IO getHandlerIO() {
            return handlerIO;
        }

        private SlotItemRecipeHandler(MEOversizePatternBufferPartMachine buffer, InternalSlot slot, int index) {
            super(buffer);
            this.slot = slot;
            this.priority = IFilteredHandler.HIGH + index + 1;
            slot.setOnContentsChanged(this::notifyListeners);
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
            return slot.getItems().stream().mapToLong(ItemStack::getCount).sum();
        }
    }

    private static class SlotFluidRecipeHandler extends NotifiableRecipeHandlerTrait<FluidIngredient> {

        private final InternalSlot slot;
        private final int priority;

        private final int size = 81;
        private final RecipeCapability<FluidIngredient> capability = FluidRecipeCapability.CAP;
        private final IO handlerIO = IO.IN;

        @Override
        public int getSize() {
            return size;
        }

        @Override
        public RecipeCapability<FluidIngredient> getCapability() {
            return capability;
        }

        @Override
        public IO getHandlerIO() {
            return handlerIO;
        }

        private SlotFluidRecipeHandler(MEOversizePatternBufferPartMachine buffer, InternalSlot slot, int index) {
            super(buffer);
            this.slot = slot;
            this.priority = IFilteredHandler.HIGH + index + 1;
            slot.setOnContentsChanged(this::notifyListeners);
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
            return slot.getFluids().stream().mapToLong(FluidStack::getAmount).sum();
        }
    }
}
