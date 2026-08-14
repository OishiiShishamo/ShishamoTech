package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAESlot;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.utils.Position;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import org.jetbrains.annotations.Nullable;

import shishamo_tech.integration.ae2.gui.STLongAEFluidConfigWidget;
import shishamo_tech.integration.ae2.gui.STLongAEItemConfigWidget;

import shishamo_tech.config.STConfig;

import java.util.Comparator;
import java.util.PriorityQueue;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An ME hatch that auto-pulls both items and fluids from the ME network in a
 * single block, without needing any configuration.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEDualStockingInputHatchPartMachine extends MEStockingHatchPartMachine {

    private static final int CONFIG_SIZE = 16;

    protected final ExportOnlyAEStockingItemList aeItemHandler;
    @Nullable
    protected ISubscription itemSubs;

    public MEDualStockingInputHatchPartMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.aeItemHandler = new ExportOnlyAEStockingItemList(this, CONFIG_SIZE);
    }

    /////////////////////////////////
    // ***** Machine LifeCycle ****//
    /////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        if (isRemote()) return;
        this.itemSubs = this.aeItemHandler.addChangedListener(this::updateTankSubscription);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (this.itemSubs != null) {
            this.itemSubs.unsubscribe();
            this.itemSubs = null;
        }
    }

    /////////////////////////////////
    // ********** Sync ME *********//
    /////////////////////////////////

    @Override
    public void autoIO() {
        if (!STConfig.isMachineFullyEnabled(this)) return;
        super.autoIO();
        if (getTicksPerCycle() == 0) {
            setTicksPerCycle(ConfigHolder.INSTANCE.compat.ae2.updateIntervals);
        }
        if (getOffsetTimer() % getTicksPerCycle() == 0) {
            if (isAutoPull()) {
                refreshItemList();
            }
            syncItemME();
            syncFluidME();
        }
    }

    protected void syncFluidME() {
        MEStorage networkInv = this.getMainNode().getGrid().getStorageService().getInventory();
        for (ExportOnlyAEFluidSlot slot : this.aeFluidHandler.getInventory()) {
            var config = slot.getConfig();
            if (config != null) {
                var key = config.what();
                long extracted = networkInv.extract(key, Long.MAX_VALUE, Actionable.SIMULATE, actionSource);
                if (extracted >= getMinStackSize()) {
                    slot.setStock(new GenericStack(key, extracted));
                    continue;
                }
            }
            slot.setStock(null);
        }
    }

    protected void syncItemME() {
        MEStorage networkInv = this.getMainNode().getGrid().getStorageService().getInventory();
        for (ExportOnlyAEItemSlot slot : this.aeItemHandler.getInventory()) {
            var config = slot.getConfig();
            if (config != null) {
                var key = config.what();
                long extracted = networkInv.extract(key, Long.MAX_VALUE, Actionable.SIMULATE, actionSource);
                if (extracted >= getMinStackSize()) {
                    slot.setStock(new GenericStack(key, extracted));
                    continue;
                }
            }
            slot.setStock(null);
        }
    }

    private void refreshItemList() {
        IGrid grid = this.getMainNode().getGrid();
        if (grid == null) {
            aeItemHandler.clearInventory(0);
            return;
        }

        MEStorage networkStorage = grid.getStorageService().getInventory();
        var counter = networkStorage.getAvailableStacks();

        PriorityQueue<Object2LongMap.Entry<AEKey>> topItems = new PriorityQueue<>(
                Comparator.comparingLong(Object2LongMap.Entry<AEKey>::getLongValue));

        for (Object2LongMap.Entry<AEKey> entry : counter) {
            long amount = entry.getLongValue();
            AEKey what = entry.getKey();

            if (amount <= 0) continue;
            if (!(what instanceof AEItemKey)) continue;

            long request = networkStorage.extract(what, amount, Actionable.SIMULATE, actionSource);
            if (request == 0) continue;

            if (amount >= getMinStackSize()) {
                if (topItems.size() < CONFIG_SIZE) {
                    topItems.offer(entry);
                } else if (amount > topItems.peek().getLongValue()) {
                    topItems.poll();
                    topItems.offer(entry);
                }
            }
        }

        int index;
        int itemAmount = topItems.size();
        for (index = 0; index < CONFIG_SIZE; index++) {
            if (topItems.isEmpty()) break;
            Object2LongMap.Entry<AEKey> entry = topItems.poll();
            AEKey what = entry.getKey();

            long request = networkStorage.extract(what, entry.getLongValue(), Actionable.SIMULATE, actionSource);
            if (request == 0) continue;

            var slot = this.aeItemHandler.getInventory()[itemAmount - index - 1];
            slot.setConfig(new GenericStack(what, 1));
            slot.setStock(new GenericStack(what, request));
        }

        aeItemHandler.clearInventory(index);
    }

    @Override
    public void setAutoPull(boolean autoPull) {
        super.setAutoPull(autoPull);
        if (!isRemote()) {
            if (!autoPull) {
                this.aeItemHandler.clearInventory(0);
            } else if (updateMEStatus()) {
                this.refreshItemList();
                updateTankSubscription();
            }
        }
    }

    ///////////////////////////////
    // ********** GUI ***********//
    ///////////////////////////////

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(new Position(0, 0));
        group.addWidget(new LabelWidget(3, 0, () -> this.isOnline ?
                "gtceu.gui.me_network.online" :
                "gtceu.gui.me_network.offline"));
        group.addWidget(new STLongAEFluidConfigWidget(3, 10, this.aeFluidHandler));
        group.addWidget(new STLongAEItemConfigWidget(3, 92, this.aeItemHandler));
        return group;
    }

    ////////////////////////////////
    // ****** Configuration ******//
    ////////////////////////////////

    @Override
    protected CompoundTag writeConfigToTag() {
        CompoundTag tag = super.writeConfigToTag();
        tag.put("ItemConfigStacks", writeConfigStacks(this.aeItemHandler.getInventory()));
        return tag;
    }

    @Override
    protected void readConfigFromTag(CompoundTag tag) {
        super.readConfigFromTag(tag);
        if (tag.contains("ItemConfigStacks")) {
            readConfigStacks(tag.getCompound("ItemConfigStacks"), this.aeItemHandler.getInventory());
        }
    }

    private static CompoundTag writeConfigStacks(IConfigurableSlot[] slots) {
        CompoundTag configStacks = new CompoundTag();
        for (int i = 0; i < slots.length; i++) {
            GenericStack config = slots[i].getConfig();
            if (config == null) {
                continue;
            }
            configStacks.put(Integer.toString(i), GenericStack.writeTag(config));
        }
        return configStacks;
    }

    private static void readConfigStacks(CompoundTag configStacks, IConfigurableSlot[] slots) {
        for (int i = 0; i < slots.length; i++) {
            String key = Integer.toString(i);
            if (configStacks.contains(key)) {
                slots[i].setConfig(GenericStack.readTag(configStacks.getCompound(key)));
            } else {
                slots[i].setConfig(null);
            }
        }
    }

    private class ExportOnlyAEStockingItemList extends ExportOnlyAEItemList {

        public ExportOnlyAEStockingItemList(MetaMachine holder, int slots) {
            super(holder, slots, ExportOnlyAEStockingItemSlot::new);
        }

        @Override
        public boolean isAutoPull() {
            return MEDualStockingInputHatchPartMachine.this.isAutoPull();
        }

        @Override
        public boolean isStocking() {
            return true;
        }

        @Override
        public boolean hasStackInConfig(GenericStack stack, boolean checkExternal) {
            boolean inThisHatch = super.hasStackInConfig(stack, false);
            if (inThisHatch) return true;
            if (checkExternal) {
                return testConfiguredInOtherPart(stack);
            }
            return false;
        }
    }

    private class ExportOnlyAEStockingItemSlot extends ExportOnlyAEItemSlot {

        public ExportOnlyAEStockingItemSlot() {
            super();
        }

        public ExportOnlyAEStockingItemSlot(@Nullable GenericStack config, @Nullable GenericStack stock) {
            super(config, stock);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 0 && this.stock != null) {
                if (this.config != null) {
                    if (!isOnline()) return ItemStack.EMPTY;
                    MEStorage aeNetwork = getMainNode().getGrid().getStorageService().getInventory();

                    Actionable action = simulate ? Actionable.SIMULATE : Actionable.MODULATE;
                    var key = config.what();
                    long extracted = aeNetwork.extract(key, amount, action, actionSource);

                    if (extracted > 0) {
                        ItemStack resultStack = key instanceof AEItemKey itemKey ?
                                itemKey.toStack((int) extracted) : ItemStack.EMPTY;
                        if (!simulate) {
                            this.stock = ExportOnlyAESlot.copy(stock, stock.amount() - extracted);
                            if (this.stock.amount() == 0) {
                                this.stock = null;
                            }
                            if (this.onContentsChanged != null) {
                                this.onContentsChanged.run();
                            }
                        }
                        return resultStack;
                    }
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ExportOnlyAEItemSlot copy() {
            return new ExportOnlyAEStockingItemSlot(
                    this.config == null ? null : copy(this.config),
                    this.stock == null ? null : copy(this.stock));
        }
    }
}
