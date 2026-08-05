package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;
import com.gregtechceu.gtceu.utils.GTMath;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.utils.Position;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;

import appeng.api.config.Actionable;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import org.jetbrains.annotations.Nullable;

import shishamo_tech.integration.ae2.gui.STLongAEFluidConfigWidget;
import shishamo_tech.integration.ae2.gui.STLongAEItemConfigWidget;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An ME hatch that can import both items and fluids from the ME network in a
 * single block, with configurable slot amounts up to {@link Long#MAX_VALUE}.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEDualLongInputHatchPartMachine extends MEInputHatchPartMachine {

    protected ExportOnlyAEItemList aeItemHandler;
    @Nullable
    protected ISubscription itemSubs;

    public MEDualLongInputHatchPartMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, IO.IN, args);
        this.aeItemHandler = new ExportOnlyAEItemList(this, CONFIG_SIZE);
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

    @Override
    public void onMachineRemoved() {
        flushInventory();
    }

    /////////////////////////////////
    // ********** Sync ME *********//
    /////////////////////////////////

    @Override
    protected void autoIO() {
        if (!this.isWorkingEnabled()) return;
        if (!this.shouldSyncME()) return;

        if (this.updateMEStatus()) {
            this.syncME();
            this.syncItemME();
            this.updateTankSubscription();
        }
    }

    protected void syncItemME() {
        MEStorage networkInv = this.getMainNode().getGrid().getStorageService().getInventory();
        for (ExportOnlyAEItemSlot aeSlot : this.aeItemHandler.getInventory()) {
            // Try to clear the wrong item
            GenericStack exceedItem = aeSlot.exceedStack();
            if (exceedItem != null) {
                long total = exceedItem.amount();
                long inserted = networkInv.insert(exceedItem.what(), exceedItem.amount(), Actionable.MODULATE,
                        this.actionSource);
                if (inserted > 0) {
                    aeSlot.extractItem(0, GTMath.saturatedCast(inserted), false);
                    continue;
                } else {
                    aeSlot.extractItem(0, GTMath.saturatedCast(total), false);
                }
            }
            // Fill it
            GenericStack reqItem = aeSlot.requestStack();
            if (reqItem != null) {
                long extracted = networkInv.extract(reqItem.what(), reqItem.amount(), Actionable.MODULATE,
                        this.actionSource);
                if (extracted != 0) {
                    aeSlot.addStack(new GenericStack(reqItem.what(), extracted));
                }
            }
        }
    }

    @Override
    protected void flushInventory() {
        super.flushInventory();
        var grid = getMainNode().getGrid();
        if (grid != null) {
            for (var aeSlot : aeItemHandler.getInventory()) {
                GenericStack stock = aeSlot.getStock();
                if (stock != null) {
                    grid.getStorageService().getInventory().insert(stock.what(), stock.amount(),
                            Actionable.MODULATE, actionSource);
                }
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
}
