package shishamo_tech.common.machine.storage;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.gui.widget.TankWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTMath;

import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.IStorageMounts;
import appeng.api.storage.IStorageProvider;
import appeng.api.storage.MEStorage;
import appeng.api.util.AECableType;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * A 3x3x3 storage multiblock with 8 independent sections. Each section holds
 * either items or fluids ({@code BigInteger} amounts internally) and the
 * controller itself is mounted as native storage on the AE2 ME network.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class STUltimateUniversalStorageMachine extends MultiblockControllerMachine implements IFancyUIMachine,
        MEStorage, IStorageProvider, IInWorldGridNodeHost, IGridNodeListener<STUltimateUniversalStorageMachine> {

    public static final int SECTIONS = 8;

    private static final Set<Direction> ALL_SIDES = EnumSet.allOf(Direction.class);
    private static final Set<Direction> NO_SIDES = Collections.emptySet();

    @DescSynced
    protected ItemStack[] storedItems = new ItemStack[SECTIONS];
    @DescSynced
    protected FluidStack[] storedFluids = new FluidStack[SECTIONS];
    @DescSynced
    protected long[] storedAmounts = new long[SECTIONS];

    protected final AEKey[] keys = new AEKey[SECTIONS];
    protected final BigInteger[] amounts = new BigInteger[SECTIONS];

    protected final UltimateItemStorage itemStorage;
    protected final UltimateFluidStorage fluidStorage;

    protected final IManagedGridNode gridNode = GridHelper.createManagedNode(this, this)
            .setInWorldNode(true)
            .setTagName("ue")
            .setIdlePowerUsage(1.0)
            .setFlags(GridFlags.REQUIRE_CHANNEL)
            .addService(IStorageProvider.class, this);

    public STUltimateUniversalStorageMachine(IMachineBlockEntity holder, Object... args) {
        super(holder);
        Arrays.fill(storedItems, ItemStack.EMPTY);
        Arrays.fill(storedFluids, FluidStack.EMPTY);
        Arrays.fill(amounts, BigInteger.ZERO);
        this.itemStorage = new UltimateItemStorage(this);
        this.fluidStorage = new UltimateFluidStorage(this);
    }

    //////////////////////////////////////
    // ***** LDLib SyncData ******//
    //////////////////////////////////////

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STUltimateUniversalStorageMachine.class, MultiblockControllerMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    //////////////////////////////////////
    // ****** Grid Node ********//
    //////////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() != null && !getLevel().isClientSide) {
            gridNode.create(getLevel(), getPos());
            gridNode.setExposedOnSides(isFormed() ? ALL_SIDES : NO_SIDES);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        gridNode.destroy();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        gridNode.setExposedOnSides(ALL_SIDES);
        refreshGridStorage();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        gridNode.setExposedOnSides(NO_SIDES);
    }

    @Override
    public @Nullable IGridNode getGridNode(Direction dir) {
        return isFormed() ? gridNode.getNode() : null;
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.SMART;
    }

    @Override
    public void onSaveChanges(STUltimateUniversalStorageMachine nodeOwner, IGridNode node) {
        this.onChanged();
    }

    @Override
    public void onStateChanged(STUltimateUniversalStorageMachine nodeOwner, IGridNode node, State state) {
        refreshGridStorage();
    }

    protected void refreshGridStorage() {
        if (gridNode.isOnline()) {
            IStorageProvider.requestUpdate(gridNode);
        }
    }

    //////////////////////////////////////
    // ***** IStorageProvider *****//
    //////////////////////////////////////

    @Override
    public void mountInventories(IStorageMounts storageMounts) {
        if (gridNode.isOnline()) {
            storageMounts.mount(this);
        }
    }

    //////////////////////////////////////
    // ****** MEStorage *********//
    //////////////////////////////////////

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        MEStorage.checkPreconditions(what, amount, mode, source);
        if (amount <= 0 || !isFormed()) return 0;
        return insertIntoSection(findSection(what), what, amount, mode == Actionable.SIMULATE, false);
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        MEStorage.checkPreconditions(what, amount, mode, source);
        if (amount <= 0 || !isFormed()) return 0;
        return extractFromSection(findSectionWith(what), amount, mode == Actionable.SIMULATE, false);
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        for (int i = 0; i < SECTIONS; i++) {
            if (keys[i] != null) {
                out.add(keys[i], saturate(amounts[i]));
            }
        }
    }

    @Override
    public Component getDescription() {
        return Component.translatable("shishamo_tech.machine.ultimate_universal_storage");
    }

    //////////////////////////////////////
    // ***** Unified Storage ******//
    //////////////////////////////////////

    protected int findSection(AEKey key) {
        for (int i = 0; i < SECTIONS; i++) {
            if (keys[i] != null && keys[i].equals(key)) return i;
        }
        for (int i = 0; i < SECTIONS; i++) {
            if (keys[i] == null) return i;
        }
        return -1;
    }

    protected int findSectionWith(AEKey key) {
        for (int i = 0; i < SECTIONS; i++) {
            if (keys[i] != null && keys[i].equals(key)) return i;
        }
        return -1;
    }

    protected long insertIntoSection(int slot, AEKey key, long amount, boolean simulate, boolean refresh) {
        if (slot < 0 || amount <= 0) return 0;
        if (!simulate) {
            if (keys[slot] == null) keys[slot] = key;
            amounts[slot] = amounts[slot].add(BigInteger.valueOf(amount));
            onStorageChanged(refresh);
        }
        return amount;
    }

    protected long extractFromSection(int slot, long amount, boolean simulate, boolean refresh) {
        if (slot < 0 || amount <= 0) return 0;
        long extracted = Math.min(saturate(amounts[slot]), amount);
        if (!simulate && extracted > 0) {
            amounts[slot] = amounts[slot].subtract(BigInteger.valueOf(extracted));
            if (amounts[slot].signum() <= 0) {
                amounts[slot] = BigInteger.ZERO;
                keys[slot] = null;
            }
            onStorageChanged(refresh);
        }
        return extracted;
    }

    protected static long saturate(BigInteger value) {
        if (value == null || value.signum() <= 0) return 0;
        return value.min(BigInteger.valueOf(Long.MAX_VALUE)).longValue();
    }

    protected void updateSyncData() {
        for (int i = 0; i < SECTIONS; i++) {
            if (keys[i] instanceof AEItemKey itemKey) {
                storedItems[i] = itemKey.toStack();
                storedFluids[i] = FluidStack.EMPTY;
                storedAmounts[i] = saturate(amounts[i]);
            } else if (keys[i] instanceof AEFluidKey fluidKey) {
                storedItems[i] = ItemStack.EMPTY;
                storedFluids[i] = fluidKey.toStack(GTMath.saturatedCast(saturate(amounts[i])));
                storedAmounts[i] = saturate(amounts[i]);
            } else {
                storedItems[i] = ItemStack.EMPTY;
                storedFluids[i] = FluidStack.EMPTY;
                storedAmounts[i] = 0;
            }
        }
    }

    protected void onStorageChanged(boolean refresh) {
        updateSyncData();
        onChanged();
        if (refresh) {
            refreshGridStorage();
        }
    }

    //////////////////////////////////////
    // ***** Persistence ********//
    //////////////////////////////////////

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        var sections = new ListTag();
        for (int i = 0; i < SECTIONS; i++) {
            if (keys[i] != null) {
                var section = new CompoundTag();
                section.put("k", keys[i].toTagGeneric());
                section.putString("a", amounts[i].toString());
                sections.add(section);
            }
        }
        tag.put("sections", sections);
        gridNode.saveToNBT(tag);
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        gridNode.loadFromNBT(tag);
        Arrays.fill(keys, null);
        Arrays.fill(amounts, BigInteger.ZERO);
        var sections = tag.getList("sections", Tag.TAG_COMPOUND);
        for (int i = 0; i < Math.min(sections.size(), SECTIONS); i++) {
            var section = sections.getCompound(i);
            AEKey key = AEKey.fromTagGeneric(section.getCompound("k"));
            if (key != null) {
                keys[i] = key;
                amounts[i] = new BigInteger(section.getString("a"));
            }
        }
        updateSyncData();
    }

    //////////////////////////////////////
    // ****** Capability ********//
    //////////////////////////////////////

    @Override
    public @Nullable IItemHandlerModifiable getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        if (isFormed()) {
            return super.getItemHandlerCap(side, useCoverCapability);
        }
        return null;
    }

    @Override
    public @Nullable IFluidHandlerModifiable getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        if (isFormed()) {
            return super.getFluidHandlerCap(side, useCoverCapability);
        }
        return null;
    }

    //////////////////////////////////////
    // *********** GUI ***********//
    //////////////////////////////////////

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 176, 166);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);

        group.addWidget(new ImageWidget(4, 4, 168, 10, GuiTextures.DISPLAY));
        group.addWidget(new LabelWidget(8, 6, "shishamo_tech.gui.ultimate.item_count"));
        group.addWidget(new LabelWidget(60, 6, this::getItemLabel).setTextColor(-1).setDropShadow(true));
        group.addWidget(new LabelWidget(100, 6, "shishamo_tech.gui.ultimate.fluid_amount"));
        group.addWidget(new LabelWidget(152, 6, this::getFluidLabel).setTextColor(-1).setDropShadow(true));

        for (int i = 0; i < SECTIONS; i++) {
            int y = 18 + i * 18;
            group.addWidget(new SlotWidget(itemStorage, i, 4, y, true, true)
                    .setBackgroundTexture(new GuiTextureGroup(GuiTextures.SLOT, GuiTextures.IN_SLOT_OVERLAY)));
            group.addWidget(new TankWidget(fluidStorage, i, 24, y, 18, 18, true, true)
                    .setBackground(GuiTextures.FLUID_SLOT));
            int slot = i;
            group.addWidget(new LabelWidget(48, y + 4, () -> FormattingUtil.formatNumbers(storedAmounts[slot]))
                    .setTextColor(-1).setDropShadow(true));
        }

        return group;
    }

    private long getTotalItems() {
        BigInteger total = BigInteger.ZERO;
        for (int i = 0; i < SECTIONS; i++) {
            if (keys[i] instanceof AEItemKey) {
                total = total.add(amounts[i]);
            }
        }
        return saturate(total);
    }

    private long getTotalFluids() {
        BigInteger total = BigInteger.ZERO;
        for (int i = 0; i < SECTIONS; i++) {
            if (keys[i] instanceof AEFluidKey) {
                total = total.add(amounts[i]);
            }
        }
        return saturate(total);
    }

    private String getItemLabel() {
        return FormattingUtil.formatNumbers(getTotalItems());
    }

    private String getFluidLabel() {
        return FormattingUtil.formatNumbers(getTotalFluids());
    }

    //////////////////////////////////////
    // ******* Item Storage *******//
    //////////////////////////////////////

    protected class UltimateItemStorage extends MachineTrait implements IItemHandlerModifiable {

        public UltimateItemStorage(MetaMachine holder) {
            super(holder);
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack stack) {
            if (slot < 0 || slot >= SECTIONS || !isFormed()) return;
            if (stack.isEmpty()) {
                keys[slot] = null;
                amounts[slot] = BigInteger.ZERO;
                onStorageChanged(true);
            } else {
                insertItem(slot, stack, false);
            }
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            if (slot < 0 || slot >= SECTIONS) return ItemStack.EMPTY;
            if (keys[slot] instanceof AEItemKey itemKey && saturate(amounts[slot]) > 0) {
                return itemKey.toStack(GTMath.saturatedCast(saturate(amounts[slot])));
            }
            return ItemStack.EMPTY;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot < 0 || slot >= SECTIONS || stack.isEmpty() || !isFormed()) return stack;
            AEItemKey key = AEItemKey.of(stack);
            if (keys[slot] != null && !keys[slot].equals(key)) return stack;
            long inserted = insertIntoSection(slot, key, stack.getCount(), simulate, true);
            return inserted >= stack.getCount() ? ItemStack.EMPTY
                    : stack.copyWithCount(stack.getCount() - (int) inserted);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot < 0 || slot >= SECTIONS || amount <= 0 || !isFormed()) return ItemStack.EMPTY;
            if (!(keys[slot] instanceof AEItemKey itemKey)) return ItemStack.EMPTY;
            long extracted = extractFromSection(slot, amount, simulate, true);
            return extracted > 0 ? itemKey.toStack(GTMath.saturatedCast(extracted)) : ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getSlots() {
            return SECTIONS;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot < 0 || slot >= SECTIONS || stack.isEmpty()) return false;
            AEItemKey key = AEItemKey.of(stack);
            return keys[slot] == null || keys[slot].equals(key);
        }

        @Override
        public ManagedFieldHolder getFieldHolder() {
            return MANAGED_FIELD_HOLDER;
        }
    }

    //////////////////////////////////////
    // ******* Fluid Storage *******//
    //////////////////////////////////////

    protected class UltimateFluidStorage extends MachineTrait implements IFluidHandlerModifiable {

        public UltimateFluidStorage(MetaMachine holder) {
            super(holder);
        }

        @Override
        public void setFluidInTank(int tank, @NotNull FluidStack stack) {
            if (tank < 0 || tank >= SECTIONS || !isFormed()) return;
            if (stack.isEmpty()) {
                keys[tank] = null;
                amounts[tank] = BigInteger.ZERO;
                onStorageChanged(true);
            } else {
                fillTank(tank, stack, FluidAction.EXECUTE);
            }
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            if (tank < 0 || tank >= SECTIONS) return FluidStack.EMPTY;
            if (keys[tank] instanceof AEFluidKey fluidKey && saturate(amounts[tank]) > 0) {
                return fluidKey.toStack(GTMath.saturatedCast(saturate(amounts[tank])));
            }
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            if (tank < 0 || tank >= SECTIONS || stack.isEmpty()) return false;
            AEFluidKey key = AEFluidKey.of(stack);
            return key != null && (keys[tank] == null || keys[tank].equals(key));
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || !isFormed()) return 0;
            AEFluidKey key = AEFluidKey.of(resource);
            if (key == null) return 0;
            return GTMath.saturatedCast(insertIntoSection(findSection(key), key, resource.getAmount(),
                    action == FluidAction.SIMULATE, true));
        }

        protected int fillTank(int tank, @NotNull FluidStack resource, FluidAction action) {
            if (tank < 0 || tank >= SECTIONS || resource.isEmpty() || !isFormed()) return 0;
            AEFluidKey key = AEFluidKey.of(resource);
            if (key == null) return 0;
            if (keys[tank] != null && !keys[tank].equals(key)) return 0;
            return GTMath.saturatedCast(
                    insertIntoSection(tank, key, resource.getAmount(), action == FluidAction.SIMULATE, true));
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || !isFormed()) return FluidStack.EMPTY;
            AEFluidKey key = AEFluidKey.of(resource);
            if (key == null) return FluidStack.EMPTY;
            int slot = findSectionWith(key);
            if (slot < 0) return FluidStack.EMPTY;
            long drained = extractFromSection(slot, resource.getAmount(), action == FluidAction.SIMULATE, true);
            return drained > 0 ? key.toStack(GTMath.saturatedCast(drained)) : FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (maxDrain <= 0 || !isFormed()) return FluidStack.EMPTY;
            for (int i = 0; i < SECTIONS; i++) {
                if (keys[i] instanceof AEFluidKey fluidKey) {
                    long drained = extractFromSection(i, maxDrain, action == FluidAction.SIMULATE, true);
                    return drained > 0 ? fluidKey.toStack(GTMath.saturatedCast(drained)) : FluidStack.EMPTY;
                }
            }
            return FluidStack.EMPTY;
        }

        @Override
        public int getTanks() {
            return SECTIONS;
        }

        @Override
        public ManagedFieldHolder getFieldHolder() {
            return MANAGED_FIELD_HOLDER;
        }
    }
}
