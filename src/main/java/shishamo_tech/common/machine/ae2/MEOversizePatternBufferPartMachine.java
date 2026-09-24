package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.ButtonConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyInvConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyTankConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.AETextInputButtonWidget;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.slot.AEPatternViewSlotWidget;
import com.gregtechceu.gtceu.integration.ae2.machine.MEBusPartMachine;

import com.gregtechceu.gtceu.utils.GTMath;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.IPatternDetails.IInput;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.api.storage.StorageHelper;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.helpers.patternprovider.PatternContainer;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.BitSet;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An oversized ME Pattern Buffer with 9x8 = 72 pattern slots per page, expanded
 * to {@value #PAGE_COUNT} pages ({@value #MAX_PATTERN_COUNT} slots total) instead
 * of the vanilla GTCEu 3x9 = 27 slots.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEOversizePatternBufferPartMachine extends MEBusPartMachine
                                                implements ICraftingProvider, PatternContainer,
                                                           IDataStickInteractable {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MEOversizePatternBufferPartMachine.class, MEBusPartMachine.MANAGED_FIELD_HOLDER);
    public static final int PAGE_SIZE = 72; // 9 columns * 8 rows, kept as-is per page
    public static final int PAGE_COUNT = 16;
    public static final int MAX_PATTERN_COUNT = PAGE_SIZE * PAGE_COUNT;

    private final InternalInventory internalPatternInventory = new InternalInventory() {

        @Override
        public int size() {
            return MAX_PATTERN_COUNT;
        }

        @Override
        public ItemStack getStackInSlot(int slotIndex) {
            return patternInventory.getStackInSlot(slotIndex);
        }

        @Override
        public void setItemDirect(int slotIndex, ItemStack stack) {
            patternInventory.setStackInSlot(slotIndex, stack);
            patternInventory.onContentsChanged(slotIndex);
            onPatternChange(slotIndex);
        }
    };

    @Persisted
    private final CustomItemStackHandler patternInventory = new CustomItemStackHandler(MAX_PATTERN_COUNT);

    @Persisted
    protected final NotifiableItemStackHandler shareInventory;

    @Persisted
    protected final NotifiableFluidTank shareTank;

    @Persisted
    protected final InternalSlot[] internalInventory = new InternalSlot[MAX_PATTERN_COUNT];

    private final BiMap<IPatternDetails, InternalSlot> detailsSlotMap = HashBiMap.create(MAX_PATTERN_COUNT);

    // O(1) occupancy tracking for isOperating(): slot callback maintains this, no full scan.
    private final BitSet occupiedSlots = new BitSet(MAX_PATTERN_COUNT);
    // Cached pattern list for AE2 queries; invalidated on pattern change.
    private List<IPatternDetails> availablePatternsCache = null;

    @DescSynced
    @Persisted
    private String customName = "";

    @Persisted
    @DescSynced
    private int currentCircuit = -1;

    @Persisted
    @DescSynced
    private int page = 0;

    private boolean needPatternSync;

    @Persisted
    private final Set<BlockPos> proxies = new ObjectOpenHashSet<>();
    private final Set<MEOversizePatternBufferProxyPartMachine> proxyMachines = new ReferenceOpenHashSet<>();

    protected final STInternalSlotRecipeHandler internalRecipeHandler;

    @Nullable
    protected TickableSubscription updateSubs;

    public MEOversizePatternBufferPartMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, IO.IN, args);
        this.patternInventory.setFilter(stack -> stack.getItem() instanceof ProcessingPatternItem);
        for (int i = 0; i < this.internalInventory.length; i++) {
            this.internalInventory[i] = new InternalSlot();
        }
        getMainNode().addService(ICraftingProvider.class, this);
        this.shareInventory = new NotifiableItemStackHandler(this, 9, IO.IN, IO.NONE);
        this.shareTank = new NotifiableFluidTank(this, 9, 8 * FluidType.BUCKET_VOLUME, IO.IN, IO.NONE);
        this.internalRecipeHandler = new STInternalSlotRecipeHandler(this, internalInventory);
        for (int i = 0; i < this.internalInventory.length; i++) {
            final int idx = i;
            var slot = this.internalInventory[idx];
            var prev = slot.getOnContentsChanged();
            slot.setOnContentsChanged(() -> {
                prev.run();
                updateOccupancy(idx);
            });
        }
    }

    private void updateOccupancy(int index) {
        var slot = internalInventory[index];
        if (!slot.isItemEmpty() || !slot.isFluidEmpty()) {
            occupiedSlots.set(index);
        } else {
            occupiedSlots.clear(index);
        }
    }

    public STInternalSlotRecipeHandler getInternalRecipeHandler() {
        return internalRecipeHandler;
    }

    public CustomItemStackHandler getPatternInventory() {
        return patternInventory;
    }

    public NotifiableItemStackHandler getShareInventory() {
        return shareInventory;
    }

    public NotifiableFluidTank getShareTank() {
        return shareTank;
    }

    public InternalSlot[] getInternalInventory() {
        return internalInventory;
    }

    public void setCustomName(String customName) {
        this.customName = customName == null ? "" : customName;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(1, () -> {
                for (int i = 0; i < patternInventory.getSlots(); i++) {
                    var pattern = patternInventory.getStackInSlot(i);
                    var patternDetails = PatternDetailsHelper.decodePattern(pattern, getLevel());
                    if (patternDetails != null) {
                        this.internalInventory[i].circuitNumber = extractCircuit(patternDetails);
                        this.detailsSlotMap.put(new CircuitlessPatternDetails(patternDetails),
                                this.internalInventory[i]);
                    }
                    updateOccupancy(i);
                }
                availablePatternsCache = null;
                needPatternSync = true;
            }));
        }
    }

    @Override
    public List<RecipeHandlerList> getRecipeHandlers() {
        return internalRecipeHandler.getSlotHandlers();
    }

    @Override
    public boolean isWorkingEnabled() {
        return true;
    }

    @Override
    public void setWorkingEnabled(boolean ignored) {}

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public void setDistinct(boolean ignored) {}

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        this.updateSubscription();
    }

    protected void updateSubscription() {
        if (getMainNode().isOnline()) {
            updateSubs = subscribeServerTick(updateSubs, this::update);
        } else if (updateSubs != null) {
            updateSubs.unsubscribe();
            updateSubs = null;
        }
    }

    protected void update() {
        if (needPatternSync) {
            ICraftingProvider.requestUpdate(getMainNode());
            this.needPatternSync = false;
        }
    }

    public void addProxy(MEOversizePatternBufferProxyPartMachine proxy) {
        proxies.add(proxy.getPos());
        proxyMachines.add(proxy);
    }

    public void removeProxy(MEOversizePatternBufferProxyPartMachine proxy) {
        proxies.remove(proxy.getPos());
        proxyMachines.remove(proxy);
    }

    @UnmodifiableView
    public Set<MEOversizePatternBufferProxyPartMachine> getProxies() {
        if (proxyMachines.size() != proxies.size()) {
            proxyMachines.clear();
            for (var pos : proxies) {
                if (MetaMachine.getMachine(getLevel(), pos) instanceof MEOversizePatternBufferProxyPartMachine proxy) {
                    proxyMachines.add(proxy);
                }
            }
        }
        return Collections.unmodifiableSet(proxyMachines);
    }

    private void refundAll(ClickData clickData) {
        if (!clickData.isRemote) {
            for (InternalSlot internalSlot : internalInventory) {
                internalSlot.refund();
            }
        }
    }

    private void onPatternChange(int index) {
        if (isRemote()) return;

        // remove old if applicable
        var internalInv = internalInventory[index];
        var newPattern = patternInventory.getStackInSlot(index);
        var newPatternDetails = PatternDetailsHelper.decodePattern(newPattern, getLevel());
        var oldPatternDetails = detailsSlotMap.inverse().get(internalInv);
        if (newPatternDetails == null) {
            detailsSlotMap.inverse().remove(internalInv);
            if (oldPatternDetails != null) {
                internalInv.refund();
            }
        } else {
            internalInv.circuitNumber = extractCircuit(newPatternDetails);
            var newWrapped = new CircuitlessPatternDetails(newPatternDetails);
            detailsSlotMap.forcePut(newWrapped, internalInv);
            if (oldPatternDetails != null && !oldPatternDetails.equals(newWrapped)) {
                internalInv.refund();
            }
        }

        availablePatternsCache = null;
        needPatternSync = true;
    }

    //////////////////////////////////////
    // ********** GUI ***********//
    //////////////////////////////////////
    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        configuratorPanel.attachConfigurators(new ButtonConfigurator(
                new GuiTextureGroup(GuiTextures.BUTTON, GuiTextures.REFUND_OVERLAY), this::refundAll)
                .setTooltips(List.of(Component.translatable("gui.gtceu.refund_all.desc"))));
        if (isHasCircuitSlot() && isCircuitSlotEnabled()) {
            configuratorPanel.attachConfigurators(new CircuitFancyConfigurator(circuitInventory.storage));
        }
        configuratorPanel.attachConfigurators(new FancyInvConfigurator(
                shareInventory.storage, Component.translatable("gui.gtceu.share_inventory.title"))
                .setTooltips(List.of(
                        Component.translatable("gui.gtceu.share_inventory.desc.0"),
                        Component.translatable("gui.gtceu.share_inventory.desc.1"))));
        configuratorPanel.attachConfigurators(new FancyTankConfigurator(
                shareTank.getStorages(), Component.translatable("gui.gtceu.share_tank.title"))
                .setTooltips(List.of(
                        Component.translatable("gui.gtceu.share_tank.desc.0"),
                        Component.translatable("gui.gtceu.share_inventory.desc.1"))));
    }

    @Override
    public Widget createUIWidget() {
        int rowSize = 9;
        int colSize = 8;
        int perPage = rowSize * colSize;
        int totalPages = MAX_PATTERN_COUNT / perPage;

        var group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * colSize + 16 + 18);

        List<AEPatternViewSlotWidget> slots = new ArrayList<>();
        List<int[]> absIndexes = new ArrayList<>();
        for (int p = 0; p < perPage; p++) {
            int[] absIndex = { page * perPage + p };
            absIndexes.add(absIndex);
            int x = p % rowSize;
            int y = p / rowSize;
            AEPatternViewSlotWidget slot = new AEPatternViewSlotWidget(patternInventory, absIndex[0], 8 + x * 18, 14 + y * 18);
            slot.setOccupiedTexture(GuiTextures.SLOT)
                    .setItemHook(stack -> {
                        if (!stack.isEmpty() && stack.getItem() instanceof EncodedPatternItem iep) {
                            final ItemStack out = iep.getOutput(stack);
                            if (!out.isEmpty()) {
                                return out;
                            }
                        }
                        return stack;
                    })
                    .setChangeListener(() -> onPatternChange(absIndex[0]))
                    .setBackground(GuiTextures.SLOT, GuiTextures.PATTERN_OVERLAY);
            slots.add(slot);
            group.addWidget(slot);
        }
        // ME Network status
        group.addWidget(new LabelWidget(
                8,
                2,
                () -> this.isOnline ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));

        group.addWidget(new AETextInputButtonWidget(18 * rowSize + 8 - 70, 2, 70, 10)
                .setText(customName)
                .setOnConfirm(this::setCustomName)
                .setButtonTooltips(Component.translatable("gui.gtceu.rename.desc")));

        // page controls
        int pageBarY = 14 + colSize * 18 + 2;
        group.addWidget(new LabelWidget(8, pageBarY,
                () -> "Page " + (page + 1) + " / " + totalPages));
        group.addWidget(new ButtonWidget(120, pageBarY, 18, 18, GuiTextures.BUTTON,
                cd -> { if (page > 0) changePage(-1, slots, absIndexes); })
                .setHoverTooltips(Component.literal("Previous Page")));
        group.addWidget(new ButtonWidget(140, pageBarY, 18, 18, GuiTextures.BUTTON,
                cd -> { if (page < totalPages - 1) changePage(1, slots, absIndexes); })
                .setHoverTooltips(Component.literal("Next Page")));

        return group;
    }

    private void changePage(int delta, List<AEPatternViewSlotWidget> slots, List<int[]> absIndexes) {
        int perPage = 9 * 8;
        int totalPages = MAX_PATTERN_COUNT / perPage;
        int newPage = Math.floorMod(page + delta, totalPages);
        page = newPage;
        for (int p = 0; p < slots.size(); p++) {
            int abs = page * perPage + p;
            absIndexes.get(p)[0] = abs;
            slots.get(p).setHandlerSlot(patternInventory, abs);
        }
    }

    @Override
    public List<IPatternDetails> getAvailablePatterns() {
        if (availablePatternsCache == null) {
            availablePatternsCache = detailsSlotMap.keySet().stream().filter(Objects::nonNull).toList();
        }
        return availablePatternsCache;
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        if (!isFormed() || !getMainNode().isActive() || !detailsSlotMap.containsKey(patternDetails) ||
                !checkInput(inputHolder)) {
            return false;
        }

        var slot = detailsSlotMap.get(patternDetails);
        if (slot != null) {
            // a gtceu:programmed_circuit is excluded from the autocraft calculation; it only configures
            // the buffer's own circuit number, read from the stored pattern data
            int circuitNumber = slot.circuitNumber;

            // while the machine is operating, only accept input whose circuit number matches the
            // currently configured one
            if (isOperating() && circuitNumber != -1 && circuitNumber != currentCircuit) {
                return false;
            }

            if (circuitNumber != -1) {
                currentCircuit = circuitNumber;
                circuitInventory.storage.setStackInSlot(0, IntCircuitBehaviour.stack(circuitNumber));
            }

            slot.pushPattern(patternDetails, inputHolder);
            return true;
        }
        return false;
    }

    private boolean isOperating() {
        return !occupiedSlots.isEmpty();
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    private boolean checkInput(KeyCounter[] inputHolder) {
        for (KeyCounter input : inputHolder) {
            var illegal = input.keySet().stream()
                    .map(AEKey::getType)
                    .map(AEKeyType::getId)
                    .anyMatch(id -> !id.equals(AEKeyType.items().getId()) && !id.equals(AEKeyType.fluids().getId()));
            if (illegal) return false;
        }
        return true;
    }

    /**
     * Reads the configured number of a {@code gtceu:programmed_circuit} from a decoded pattern's inputs.
     * Returns -1 when the pattern contains no programmable circuit.
     */
    private static int extractCircuit(IPatternDetails details) {
        for (var input : details.getInputs()) {
            for (var possible : input.getPossibleInputs()) {
                if (possible.what() instanceof AEItemKey itemKey) {
                    var stack = itemKey.toStack();
                    if (IntCircuitBehaviour.isIntegratedCircuit(stack)) {
                        return IntCircuitBehaviour.getCircuitConfiguration(stack);
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Wraps an {@link IPatternDetails} so that a {@code gtceu:programmed_circuit} is removed from the
     * autocrafting calculation (it is only used to configure the buffer's own circuit number). All other
     * behaviour is delegated to the wrapped pattern.
     */
    private static final class CircuitlessPatternDetails implements IPatternDetails {

        private final IPatternDetails delegate;
        // Filtered inputs are immutable (delegate never changes); compute once.
        private final IInput[] inputs;

        private CircuitlessPatternDetails(IPatternDetails delegate) {
            this.delegate = delegate;
            var raw = delegate.getInputs();
            var kept = new ArrayList<IInput>(raw.length);
            for (var input : raw) {
                boolean isCircuit = false;
                for (var possible : input.getPossibleInputs()) {
                    if (possible.what() instanceof AEItemKey itemKey &&
                            IntCircuitBehaviour.isIntegratedCircuit(itemKey.toStack())) {
                        isCircuit = true;
                        break;
                    }
                }
                if (!isCircuit) {
                    kept.add(input);
                }
            }
            this.inputs = kept.toArray(new IInput[0]);
        }

        @Override
        public AEItemKey getDefinition() {
            return delegate.getDefinition();
        }

        @Override
        public GenericStack[] getOutputs() {
            return delegate.getOutputs();
        }

        @Override
        public IInput[] getInputs() {
            return inputs.clone();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof IPatternDetails that)) return false;
            // identity is the encoded pattern item, matching AE2's own pattern equality so that
            // crafting jobs resume correctly
            return getDefinition().equals(that.getDefinition());
        }

        @Override
        public int hashCode() {
            return getDefinition().hashCode();
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public @Nullable IGrid getGrid() {
        return getMainNode().getGrid();
    }

    @Override
    public InternalInventory getTerminalPatternInventory() {
        return internalPatternInventory;
    }

    @Override
    public PatternContainerGroup getTerminalGroup() {
        // Has controller
        if (isFormed()) {
            IMultiController controller = getControllers().first();
            MultiblockMachineDefinition controllerDefinition = controller.self().getDefinition();
            // has customName
            if (!customName.isEmpty()) {
                return new PatternContainerGroup(
                        AEItemKey.of(controllerDefinition.asStack()),
                        Component.literal(customName),
                        Collections.emptyList());
            } else {
                ItemStack circuitStack = isHasCircuitSlot() ? circuitInventory.storage.getStackInSlot(0) :
                        ItemStack.EMPTY;
                int circuitConfiguration = circuitStack.isEmpty() ? -1 :
                        IntCircuitBehaviour.getCircuitConfiguration(circuitStack);

                Component groupName = circuitConfiguration != -1 ?
                        Component.translatable(controllerDefinition.getDescriptionId())
                                .append(" - " + circuitConfiguration) :
                        Component.translatable(controllerDefinition.getDescriptionId());

                return new PatternContainerGroup(
                        AEItemKey.of(controllerDefinition.asStack()), groupName, Collections.emptyList());
            }
        } else {
            if (!customName.isEmpty()) {
                return new PatternContainerGroup(
                        AEItemKey.of(STAE2PartMachines.ME_OVERSIZE_PATTERN_BUFFER.getItem()),
                        Component.literal(customName),
                        Collections.emptyList());
            } else {
                return new PatternContainerGroup(
                        AEItemKey.of(STAE2PartMachines.ME_OVERSIZE_PATTERN_BUFFER.getItem()),
                        STAE2PartMachines.ME_OVERSIZE_PATTERN_BUFFER.get().getDefinition().getItem().getDescription(),
                        Collections.emptyList());
            }
        }
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(patternInventory);
        clearInventory(shareInventory);
    }

    @Override
    public InteractionResult onDataStickShiftUse(Player player, ItemStack dataStick) {
        dataStick.getOrCreateTag().putIntArray("pos", new int[] { getPos().getX(), getPos().getY(), getPos().getZ() });
        return InteractionResult.SUCCESS;
    }

    public class InternalSlot implements ITagSerializable<CompoundTag>, IContentChangeAware {

        // circuit number taken from the pattern's gtceu:programmed_circuit; -1 means none
        private int circuitNumber = -1;

        private Runnable onContentsChanged = () -> {};

        @Override
        public Runnable getOnContentsChanged() {
            return onContentsChanged;
        }

        @Override
        public void setOnContentsChanged(Runnable onContentsChanged) {
            this.onContentsChanged = onContentsChanged == null ? () -> {} : onContentsChanged;
        }

        private final Object2LongOpenCustomHashMap<ItemStack> itemInventory = new Object2LongOpenCustomHashMap<>(
                ItemStackHashStrategy.comparingAllButCount());
        private final Object2LongOpenHashMap<FluidStack> fluidInventory = new Object2LongOpenHashMap<>();
        private List<ItemStack> itemStacks = null;
        private List<FluidStack> fluidStacks = null;
        private Set<Item> itemTypes = null;
        private Set<Fluid> fluidTypes = null;

        public InternalSlot() {}

        public boolean isItemEmpty() {
            return itemInventory.isEmpty();
        }

        public boolean isFluidEmpty() {
            return fluidInventory.isEmpty();
        }

        public void onContentsChanged() {
            itemStacks = null;
            fluidStacks = null;
            itemTypes = null;
            fluidTypes = null;
            onContentsChanged.run();
        }

        /** Cached item types for the recipe-match prefilter; invalidated on contents change. */
        public Set<Item> getItemTypes() {
            Set<Item> cached = itemTypes;
            if (cached == null) {
                cached = new ReferenceOpenHashSet<>(itemInventory.size());
                for (ItemStack stack : itemInventory.keySet()) {
                    cached.add(stack.getItem());
                }
                itemTypes = cached;
            }
            return cached;
        }

        /** Cached fluid types for the recipe-match prefilter; invalidated on contents change. */
        public Set<Fluid> getFluidTypes() {
            Set<Fluid> cached = fluidTypes;
            if (cached == null) {
                cached = new ReferenceOpenHashSet<>(fluidInventory.size());
                for (FluidStack stack : fluidInventory.keySet()) {
                    cached.add(stack.getFluid());
                }
                fluidTypes = cached;
            }
            return cached;
        }

        private void add(AEKey what, long amount) {
            if (amount <= 0L) return;
            if (what instanceof AEItemKey itemKey) {
                var stack = itemKey.toStack();
                itemInventory.addTo(stack, amount);
            } else if (what instanceof AEFluidKey fluidKey) {
                var stack = fluidKey.toStack(1);
                fluidInventory.addTo(stack, amount);
            }
        }

        public List<ItemStack> getItems() {
            if (itemStacks == null) {
                itemStacks = new ArrayList<>();
                itemInventory.object2LongEntrySet().stream()
                        .map(e -> GTMath.splitStacks(e.getKey(), e.getLongValue()))
                        .forEach(itemStacks::addAll);
            }
            return itemStacks;
        }

        public List<FluidStack> getFluids() {
            if (fluidStacks == null) {
                fluidStacks = new ArrayList<>();
                fluidInventory.object2LongEntrySet().stream()
                        .map(e -> GTMath.splitFluidStacks(e.getKey(), e.getLongValue()))
                        .forEach(fluidStacks::addAll);
            }
            return fluidStacks;
        }

        public void refund() {
            var network = getMainNode().getGrid();
            if (network != null) {
                MEStorage networkInv = network.getStorageService().getInventory();
                var energy = network.getEnergyService();

                for (var it = itemInventory.object2LongEntrySet().iterator(); it.hasNext();) {
                    var entry = it.next();
                    var stack = entry.getKey();
                    var count = entry.getLongValue();
                    if (stack.isEmpty() || count == 0) {
                        it.remove();
                        continue;
                    }

                    var key = AEItemKey.of(stack);
                    if (key == null) continue;

                    long inserted = StorageHelper.poweredInsert(energy, networkInv, key, count, actionSource);
                    if (inserted > 0) {
                        count -= inserted;
                        if (count == 0) it.remove();
                        else entry.setValue(count);
                    }
                }

                for (var it = fluidInventory.object2LongEntrySet().iterator(); it.hasNext();) {
                    var entry = it.next();
                    var stack = entry.getKey();
                    var amount = entry.getLongValue();
                    if (stack.isEmpty() || amount == 0) {
                        it.remove();
                        continue;
                    }

                    var key = AEFluidKey.of(stack);
                    if (key == null) continue;

                    long inserted = StorageHelper.poweredInsert(energy, networkInv, key, amount, actionSource);
                    if (inserted > 0) {
                        amount -= inserted;
                        if (amount == 0) it.remove();
                        else entry.setValue(amount);
                    }
                }
                onContentsChanged();
            }
        }

        public void pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
            patternDetails.pushInputsToExternalInventory(inputHolder, this::add);
            onContentsChanged();
        }

        public @Nullable List<Ingredient> handleItemInternal(List<Ingredient> left, boolean simulate) {
            boolean changed = false;
            for (var it = left.listIterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }

                var items = ingredient.getItems();
                if (items.length == 0 || items[0].isEmpty()) {
                    it.remove();
                    continue;
                }

                int amount = items[0].getCount();
                for (var it2 = itemInventory.object2LongEntrySet().iterator(); it2.hasNext();) {
                    var entry = it2.next();
                    var stack = entry.getKey();
                    var count = entry.getLongValue();
                    if (stack.isEmpty() || count == 0) {
                        it2.remove();
                        continue;
                    }
                    if (!ingredient.test(stack)) continue;
                    int extracted = Math.min(GTMath.saturatedCast(count), amount);
                    if (!simulate && extracted > 0) {
                        changed = true;
                        count -= extracted;
                        if (count == 0) it2.remove();
                        else entry.setValue(count);
                    }
                    amount -= extracted;

                    if (amount <= 0) {
                        it.remove();
                        break;
                    }
                }

                if (amount > 0) {
                    if (ingredient instanceof SizedIngredient si) {
                        si.setAmount(amount);
                    } else {
                        items[0].setCount(amount);
                    }
                }
            }
            if (changed) onContentsChanged();
            return left.isEmpty() ? null : left;
        }

        public @Nullable List<FluidIngredient> handleFluidInternal(List<FluidIngredient> left, boolean simulate) {
            boolean changed = false;
            for (var it = left.listIterator(); it.hasNext();) {
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

                int amount = fluids[0].getAmount();
                for (var it2 = fluidInventory.object2LongEntrySet().iterator(); it2.hasNext();) {
                    var entry = it2.next();
                    var stack = entry.getKey();
                    var count = entry.getLongValue();
                    if (stack.isEmpty() || count == 0) {
                        it2.remove();
                        continue;
                    }
                    if (!ingredient.test(stack)) continue;
                    int extracted = Math.min(GTMath.saturatedCast(count), amount);
                    if (!simulate && extracted > 0) {
                        changed = true;
                        count -= extracted;
                        if (count == 0) it2.remove();
                        else entry.setValue(count);
                    }
                    amount -= extracted;

                    if (amount <= 0) {
                        it.remove();
                        break;
                    }
                }

                if (amount > 0) {
                    ingredient.setAmount(amount);
                }
            }

            if (changed) onContentsChanged();
            return left.isEmpty() ? null : left;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();

            ListTag itemsTag = new ListTag();
            for (var entry : itemInventory.object2LongEntrySet()) {
                var ct = entry.getKey().serializeNBT();
                ct.putLong("real", entry.getLongValue());
                itemsTag.add(ct);
            }
            if (!itemsTag.isEmpty()) tag.put("inventory", itemsTag);

            ListTag fluidsTag = new ListTag();
            for (var entry : fluidInventory.object2LongEntrySet()) {
                var ct = entry.getKey().writeToNBT(new CompoundTag());
                ct.putLong("real", entry.getLongValue());
                fluidsTag.add(ct);
            }
            if (!fluidsTag.isEmpty()) tag.put("fluidInventory", fluidsTag);

            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            ListTag items = tag.getList("inventory", Tag.TAG_COMPOUND);
            for (Tag t : items) {
                if (!(t instanceof CompoundTag ct)) continue;
                var stack = ItemStack.of(ct);
                var count = ct.getLong("real");
                if (!stack.isEmpty() && count > 0) {
                    itemInventory.put(stack, count);
                }
            }

            ListTag fluids = tag.getList("fluidInventory", Tag.TAG_COMPOUND);
            for (Tag t : fluids) {
                if (!(t instanceof CompoundTag ct)) continue;
                var stack = FluidStack.loadFluidStackFromNBT(ct);
                var amount = ct.getLong("real");
                if (!stack.isEmpty() && amount > 0) {
                    fluidInventory.put(stack, amount);
                }
            }
            itemTypes = null;
            fluidTypes = null;
        }
    }
}
