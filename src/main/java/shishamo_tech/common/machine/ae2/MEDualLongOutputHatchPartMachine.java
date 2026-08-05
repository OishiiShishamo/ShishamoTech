package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.list.AEListGridWidget;
import com.gregtechceu.gtceu.integration.ae2.machine.MEHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import shishamo_tech.config.STConfig;

/**
 * An ME hatch that can export both items and fluids to the ME network from a
 * single block. Both internal buffers are {@link KeyStorage}s (long-based),
 * allowing accumulation beyond {@link Integer#MAX_VALUE}.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MEDualLongOutputHatchPartMachine extends MEHatchPartMachine implements IMachineLife {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MEDualLongOutputHatchPartMachine.class, MEHatchPartMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private KeyStorage fluidBuffer;
    @Persisted
    private KeyStorage itemBuffer;

    public MEDualLongOutputHatchPartMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, IO.OUT, args);
        this.itemBuffer = new KeyStorage();
        new InaccessibleInfiniteHandler(this);
    }

    /////////////////////////////////
    // ***** Machine LifeCycle ****//
    /////////////////////////////////

    @Override
    protected NotifiableFluidTank createTank(int initialCapacity, int slots, Object... args) {
        this.fluidBuffer = new KeyStorage();
        return new InaccessibleInfiniteTank(this);
    }

    @Override
    public void onMachineRemoved() {
        var grid = getMainNode().getGrid();
        if (grid != null) {
            if (!fluidBuffer.isEmpty()) {
                for (var entry : fluidBuffer) {
                    grid.getStorageService().getInventory().insert(entry.getKey(), entry.getLongValue(),
                            Actionable.MODULATE, actionSource);
                }
            }
            if (!itemBuffer.isEmpty()) {
                for (var entry : itemBuffer) {
                    grid.getStorageService().getInventory().insert(entry.getKey(), entry.getLongValue(),
                            Actionable.MODULATE, actionSource);
                }
            }
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    /////////////////////////////////
    // ********** Sync ME *********//
    /////////////////////////////////

    @Override
    protected boolean shouldSubscribe() {
        return super.shouldSubscribe() && (!fluidBuffer.storage.isEmpty() || !itemBuffer.storage.isEmpty());
    }

    @Override
    protected void autoIO() {
        if (!STConfig.isMachineFullyEnabled(this)) return;
        if (!this.shouldSyncME()) return;
        if (this.updateMEStatus()) {
            var grid = getMainNode().getGrid();
            if (grid != null) {
                if (!fluidBuffer.isEmpty()) {
                    fluidBuffer.insertInventory(grid.getStorageService().getInventory(), actionSource);
                }
                if (!itemBuffer.isEmpty()) {
                    itemBuffer.insertInventory(grid.getStorageService().getInventory(), actionSource);
                }
            }
            this.updateTankSubscription();
        }
    }

    ///////////////////////////////
    // ********** GUI ***********//
    ///////////////////////////////

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(0, 0, 170, 100);
        group.addWidget(new LabelWidget(5, 0, () -> this.isOnline ?
                "gtceu.gui.me_network.online" :
                "gtceu.gui.me_network.offline"));
        group.addWidget(new LabelWidget(5, 10, "gtceu.gui.waiting_list"));
        group.addWidget(new AEListGridWidget.Fluid(5, 20, 2, this.fluidBuffer));
        group.addWidget(new AEListGridWidget.Item(5, 56, 2, this.itemBuffer));
        return group;
    }

    private class InaccessibleInfiniteTank extends NotifiableFluidTank {

        FluidStorageDelegate storage;

        public InaccessibleInfiniteTank(MetaMachine holder) {
            super(holder, List.of(new FluidStorageDelegate()), IO.OUT, IO.NONE);
            fluidBuffer.setOnContentsChanged(this::onContentsChanged);
            storage = (FluidStorageDelegate) getStorages()[0];
            allowSameFluids = true;
        }

        @Override
        public int getTanks() {
            return 128;
        }

        @Override
        public @NotNull List<Object> getContents() {
            return Collections.emptyList();
        }

        @Override
        public double getTotalContentAmount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }

        @Override
        public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {}

        @Override
        public int getTankCapacity(int tank) {
            return storage.getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return true;
        }

        @Override
        @Nullable
        public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left,
                                                       boolean simulate) {
            if (io != IO.OUT) return left;
            FluidAction action = simulate ? FluidAction.SIMULATE : FluidAction.EXECUTE;
            for (var it = left.iterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }

                var fluids = ingredient.getStacks();
                if (fluids.length == 0 || fluids[0].isEmpty()) {
                    it.remove();
                    continue;
                }

                FluidStack output = fluids[0];
                ingredient.shrink(storage.fill(output, action));
                if (ingredient.getAmount() <= 0) it.remove();
            }
            return left.isEmpty() ? null : left;
        }
    }

    private class FluidStorageDelegate extends CustomFluidTank {

        public FluidStorageDelegate() {
            super(0);
        }

        @Override
        public int getCapacity() {
            return Integer.MAX_VALUE;
        }

        @Override
        public void setFluid(FluidStack fluid) {
            // NO-OP
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            var key = AEFluidKey.of(resource.getFluid(), resource.getTag());
            int amount = resource.getAmount();
            long oldValue = fluidBuffer.storage.getOrDefault(key, 0L);
            long changeValue = Math.min(Long.MAX_VALUE - oldValue, amount);
            if (changeValue > 0 && action.execute()) {
                fluidBuffer.storage.put(key, oldValue + changeValue);
                fluidBuffer.onChanged();
            }
            return (int) changeValue;
        }

        @Override
        public boolean supportsFill(int tank) {
            return false;
        }

        @Override
        public boolean supportsDrain(int tank) {
            return false;
        }
    }

    private class InaccessibleInfiniteHandler extends NotifiableItemStackHandler {

        public InaccessibleInfiniteHandler(MetaMachine holder) {
            super(holder, 1, IO.OUT, IO.NONE, ItemStackHandlerDelegate::new);
            itemBuffer.setOnContentsChanged(this::onContentsChanged);
        }

        @Override
        public @NotNull List<Object> getContents() {
            return Collections.emptyList();
        }

        @Override
        public double getTotalContentAmount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }

    private class ItemStackHandlerDelegate extends CustomItemStackHandler {

        public ItemStackHandlerDelegate(Integer integer) {
            super();
        }

        @Override
        public int getSlots() {
            return Short.MAX_VALUE;
        }

        @Override
        public int getSlotLimit(int slot) {
            return Integer.MAX_VALUE;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            // NO-OP
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            var key = AEItemKey.of(stack);
            int count = stack.getCount();
            long oldValue = itemBuffer.storage.getOrDefault(key, 0L);
            long changeValue = Math.min(Long.MAX_VALUE - oldValue, count);
            if (changeValue > 0) {
                if (!simulate) {
                    itemBuffer.storage.put(key, oldValue + changeValue);
                    itemBuffer.onChanged();
                }
                return stack.copyWithCount((int) (count - changeValue));
            } else {
                return ItemStack.EMPTY;
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    }
}
