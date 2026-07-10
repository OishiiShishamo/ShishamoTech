package shishamo_tech.common.machine.botany;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shishamo_tech.ShishamoTech;
import shishamo_tech.common.recipe.STOverclockingLogic;
import shishamo_tech.common.recipe.STRecipeModifierUtil;
import shishamo_tech.common.recipe.STRecipeTypes;
import shishamo_tech.config.STConfig;
import shishamo_tech.integration.botany.BotanyPotsRecipeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class GreenHouseMachine extends WorkableElectricMultiblockMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger("ShishamoTech/GreenHouse");

    private static final int WATER_PER_SLOT_PER_TICK = 2;

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            GreenHouseMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    private final NotifiableItemStackHandler seedTray;

    public GreenHouseMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.seedTray = new NotifiableItemStackHandler(this, getSeedSlotCount(), IO.IN, IO.BOTH);
    }

    public int getSeedSlotCount() {
        return 1024;
    }

    public int getParallelCount() {
        return STOverclockingLogic.computeParallelCountSafe(getTier(), getDefinition().getTier());
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new GreenHouseRecipeLogic(this);
    }

    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof GreenHouseMachine)) return ModifierFunction.IDENTITY;
        if (!ShishamoTech.isModLoaded("botanypots")) return ModifierFunction.NULL;
        if (!STConfig.isBotanyEnabled()) return ModifierFunction.NULL;

        long voltage = STRecipeModifierUtil.getOverclockVoltage(machine);
        if (voltage <= 0 || RecipeHelper.getRealEUt(recipe).getTotalEU() <= 0) {
            return ModifierFunction.IDENTITY;
        }
        return STOverclockingLogic.TRIPLE_OVERCLOCK.getModifier(machine, recipe, voltage);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            int occupied = 0;
            for (int i = 0; i < seedTray.getSlots(); i++) {
                if (!seedTray.getStackInSlot(i).isEmpty()) occupied++;
            }
            textList.add(Component.translatable("shishamo_tech.machine.parallel_count",
                    getParallelCount()));
            textList.add(Component.translatable("shishamo_tech.machine.seed_slots",
                    occupied, seedTray.getSlots()));
        }
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfigurator() {
            @Override
            public Component getTitle() {
                return Component.translatable("shishamo_tech.gui.seed_tray");
            }

            @Override
            public IGuiTexture getIcon() {
                return new ItemStackTexture(Items.WHEAT);
            }

            @Override
            public Widget createConfigurator() {
                int slots = seedTray.getSlots();
                int rowSize = Math.min(slots, 9);
                int colSize = (slots + rowSize - 1) / rowSize;
                int slotWidth = 18 * rowSize;
                int slotHeight = 18 * colSize;
                int maxHeight = Math.min(slotHeight, 72);

                var scroll = new DraggableScrollableWidgetGroup(0, 0, slotWidth + 20, maxHeight + 8);
                var container = new WidgetGroup(4, 4, slotWidth + 8, slotHeight + 8);
                container.setBackground(GuiTextures.BACKGROUND_INVERSE);
                for (int i = 0; i < slots; i++) {
                    int x = 4 + (i % rowSize) * 18;
                    int y = 4 + (i / rowSize) * 18;
                    container.addWidget(new SlotWidget((IItemHandlerModifiable) seedTray, i, x, y, true, true)
                            .setBackgroundTexture(GuiTextures.SLOT));
                }
                scroll.addWidget(container);
                return scroll;
            }
        });
    }

    private static GTRecipe applyModifier(GreenHouseMachine machine, GTRecipe recipe) {
        var modifier = recipeModifier(machine, recipe);
        if (modifier == null || modifier == ModifierFunction.NULL || modifier == ModifierFunction.IDENTITY) {
            return recipe;
        }
        var modified = modifier.apply(recipe);
        return modified != null ? modified : recipe;
    }

    protected class GreenHouseRecipeLogic extends RecipeLogic {

        private final List<GTRecipe> recipePool;
        private final GTRecipe[] slotRecipes;
        private final int[] slotProgress;
        private int sharedDuration;
        private ISubscription trayListenerSub;

        public GreenHouseRecipeLogic(GreenHouseMachine machine) {
            super(machine);
            int slots = machine.getSeedSlotCount();
            this.slotRecipes = new GTRecipe[slots];
            this.slotProgress = new int[slots];
            this.sharedDuration = 0;
            this.recipePool = new ArrayList<>();
        }

        @Override
        public void onMachineLoad() {
            super.onMachineLoad();
            var level = getMachine().getLevel();
            LOGGER.debug("GreenHouse onMachineLoad: side={}, level={}, isClientSide={}, thread={}", 
                level != null ? (level.isClientSide ? "CLIENT" : "SERVER") : "NULL", 
                level, 
                level != null ? level.isClientSide() : "N/A",
                Thread.currentThread().getName());
            if (level != null && !level.isClientSide) {
                LOGGER.debug("GreenHouse: Building recipe pool on SERVER side");
                buildRecipePool(level);
                syncSlotsFromTray();
                GreenHouseMachine machine = (GreenHouseMachine) getMachine();
                trayListenerSub = machine.seedTray.addChangedListener(this::onTrayChanged);
            } else {
                LOGGER.debug("GreenHouse: Skipping recipe pool build - client side or null level");
            }
        }

        @Override
        public void onMachineUnLoad() {
            super.onMachineUnLoad();
            if (trayListenerSub != null) {
                trayListenerSub.unsubscribe();
                trayListenerSub = null;
            }
        }

        private void buildRecipePool(Level level) {
            if (!ShishamoTech.isModLoaded("botanypots") || !STConfig.isBotanyEnabled()) return;
            if (level == null || level.isClientSide) return;
            if (!recipePool.isEmpty()) return;

            var crops = BotanyPotsRecipeHelper.readAllCropRecipes(level);
            LOGGER.debug("GreenHouse buildRecipePool: found {} crops", crops.size());
            if (crops.isEmpty()) return;

            GreenHouseMachine machine = (GreenHouseMachine) getMachine();

            var gtRecipes = BotanyPotsRecipeHelper.convertToGTRecipes(crops, STRecipeTypes.GREEN_HOUSE_RECIPES);
            LOGGER.debug("GreenHouse buildRecipePool: converted {} gtRecipes", gtRecipes.size());
            for (var raw : gtRecipes) {
                var modified = applyModifier(machine, raw);
                if (modified != null) {
                    recipePool.add(modified);
                }
            }
            LOGGER.debug("GreenHouse buildRecipePool: final pool size={}", recipePool.size());
        }

        private void onTrayChanged() {
            syncSlotsFromTray();
            recalcDuration();
        }

        private void syncSlotsFromTray() {
            // Ensure recipe pool is loaded
            if (recipePool.isEmpty()) {
                var level = getMachine().getLevel();
                if (level != null && !level.isClientSide) {
                    buildRecipePool(level);
                }
            }
            
            GreenHouseMachine machine = (GreenHouseMachine) getMachine();
            for (int slot = 0; slot < machine.seedTray.getSlots(); slot++) {
                ItemStack seed = machine.seedTray.getStackInSlot(slot);
                if (seed.isEmpty()) {
                    slotRecipes[slot] = null;
                    slotProgress[slot] = 0;
                } else {
                    if (slotRecipes[slot] == null || !itemMatchesRecipe(seed, slotRecipes[slot])) {
                        LOGGER.debug("GreenHouse syncSlotsFromTray: slot={}, seed={}, finding recipe...", slot, seed);
                        slotRecipes[slot] = findRecipeForItem(seed);
                        if (slotRecipes[slot] != null) {
                            LOGGER.debug("GreenHouse syncSlotsFromTray: slot={} found recipe={}", slot, slotRecipes[slot].getId());
                        } else {
                            LOGGER.debug("GreenHouse syncSlotsFromTray: slot={} NO RECIPE FOUND for seed={}", slot, seed);
                        }
                        slotProgress[slot] = 0;
                    }
                }
            }
        }

        private boolean itemMatchesRecipe(ItemStack seed, GTRecipe recipe) {
            var inputs = recipe.inputs.get(ItemRecipeCapability.CAP);
            if (inputs == null) return false;
            for (var content : inputs) {
                if (content.getContent() instanceof Ingredient ing) {
                    if (ing.test(seed)) return true;
                }
            }
            return false;
        }

        @Nullable
        private GTRecipe findRecipeForItem(ItemStack seed) {
            LOGGER.debug("GreenHouse findRecipeForItem: testing seed={} against pool size={}", seed, recipePool.size());
            for (var recipe : recipePool) {
                var inputs = recipe.inputs.get(ItemRecipeCapability.CAP);
                if (inputs == null) continue;
                for (var content : inputs) {
                    if (content.getContent() instanceof Ingredient ing) {
                        if (ing.test(seed)) {
                            LOGGER.debug("GreenHouse findRecipeForItem: MATCH for seed={} with recipe={}", seed, recipe.getId());
                            return recipe;
                        }
                    }
                }
            }
            LOGGER.debug("GreenHouse findRecipeForItem: NO MATCH for seed={}", seed);
            return null;
        }

        private void recalcDuration() {
            sharedDuration = 0;
            for (var recipe : slotRecipes) {
                if (recipe != null) {
                    sharedDuration = recipe.duration;
                    LOGGER.debug("GreenHouse recalcDuration: sharedDuration={}, recipe={}", sharedDuration, recipe.getId());
                    break;
                }
            }
        }

        @Override
        public void serverTick() {
            if (getMachine().getLevel().isClientSide) return;

            // Sync tray contents to slot recipes (also builds recipe pool if needed)
            syncSlotsFromTray();

            if (!isWorkingEnabled()) {
                if (isWorking()) {
                    setStatus(Status.IDLE);
                    progress = 0;
                    duration = 0;
                    isActive = false;
                }
                return;
            }

            if (!ShishamoTech.isModLoaded("botanypots") || !STConfig.isBotanyEnabled()) {
                if (isWorking()) {
                    setStatus(Status.IDLE);
                    progress = 0;
                    duration = 0;
                    isActive = false;
                }
                return;
            }

            GreenHouseMachine machine = (GreenHouseMachine) getMachine();

            long totalEUt = 0;
            int activeSlots = 0;
            boolean hasWork = false;

            for (int slot = 0; slot < machine.seedTray.getSlots(); slot++) {
                if (slotRecipes[slot] == null) continue;

                totalEUt += Math.abs(RecipeHelper.getRealEUt(slotRecipes[slot]).getTotalEU());
                activeSlots++;
                hasWork = true;
            }

            LOGGER.debug("GreenHouse serverTick: activeSlots={}, totalEUt={}, hasWork={}, seedTray occupied={}", 
                activeSlots, totalEUt, hasWork, 
                (int) IntStream.range(0, machine.seedTray.getSlots())
                    .filter(i -> !machine.seedTray.getStackInSlot(i).isEmpty()).count());

            if (!hasWork) {
                if (isWorking()) {
                    setStatus(Status.IDLE);
                    progress = 0;
                    duration = 0;
                    isActive = false;
                }
                return;
            }

            int waterPerTick = activeSlots * WATER_PER_SLOT_PER_TICK;
            LOGGER.debug("GreenHouse drainFluid: waterPerTick={}, fluidHandlers={}", 
                waterPerTick, 
                ((GreenHouseMachine) getMachine()).getCapabilitiesFlat(IO.IN, FluidRecipeCapability.CAP).size());
            if (waterPerTick > 0 && !drainFluid(waterPerTick)) {
                LOGGER.debug("GreenHouse waiting: insufficient water");
                setWaiting(Component.translatable("gtceu.recipe_logic.insufficient_in"));
                return;
            }

            LOGGER.debug("GreenHouse drainEnergy: totalEUt={}, euHandlers={}", 
                totalEUt, 
                ((GreenHouseMachine) getMachine()).getCapabilitiesFlat(IO.IN, EURecipeCapability.CAP).size());
            if (totalEUt > 0 && !drainEnergy(totalEUt)) {
                LOGGER.debug("GreenHouse waiting: insufficient energy");
                setWaiting(Component.translatable("gtceu.recipe_logic.insufficient_energy"));
                return;
            }

            setStatus(Status.WORKING);
            isActive = true;
            duration = Math.max(sharedDuration, 1);
            LOGGER.debug("GreenHouse serverTick: duration set to sharedDuration={}, progress={}", sharedDuration, progress);

            for (int slot = 0; slot < machine.seedTray.getSlots(); slot++) {
                if (slotRecipes[slot] == null) continue;

                slotProgress[slot]++;
                if (slotProgress[slot] >= slotRecipes[slot].duration) {
                    completeSlot(slot);
                }
            }
            progress++;
            if (progress >= duration) {
                progress = 0;
            }
        }

        private boolean drainEnergy(long amount) {
            if (amount <= 0) return true;

            var euHandlers = ((GreenHouseMachine) getMachine()).getCapabilitiesFlat(IO.IN, EURecipeCapability.CAP);
            if (euHandlers.isEmpty()) return false;

            long totalDrained = 0;
            for (IRecipeHandler<?> handler : euHandlers) {
                if (handler instanceof IEnergyContainer container) {
                    long canDrain = Math.min(container.getEnergyStored(), amount - totalDrained);
                    if (canDrain > 0) {
                        container.changeEnergy(-canDrain);
                        totalDrained += canDrain;
                    }
                }
                if (totalDrained >= amount) break;
            }

            return totalDrained >= amount;
        }

        @SuppressWarnings("unchecked")
        private boolean drainFluid(int amount) {
            if (amount <= 0) return true;

            var fluidHandlers = ((GreenHouseMachine) getMachine()).getCapabilitiesFlat(IO.IN, FluidRecipeCapability.CAP);
            LOGGER.debug("GreenHouse drainFluid: amount={}, fluidHandlers={}", amount, fluidHandlers.size());
            if (fluidHandlers.isEmpty()) {
                LOGGER.debug("GreenHouse drainFluid: NO fluid handlers found!");
                return false;
            }

            var toDrain = new ArrayList<>(List.of(
                    FluidIngredient.of(Fluids.WATER, amount)));
            int needed = amount;

            for (IRecipeHandler<?> handler : fluidHandlers) {
                LOGGER.debug("GreenHouse drainFluid: handler={}", handler.getClass().getSimpleName());
                var result = (List<FluidIngredient>)
                        handler.handleRecipe(IO.IN, null, toDrain, false);
                if (result == null || result.isEmpty()) {
                    LOGGER.debug("GreenHouse drainFluid: fully drained via {}", handler.getClass().getSimpleName());
                    return true;
                }
                toDrain.clear();
                toDrain.addAll(result);
                LOGGER.debug("GreenHouse drainFluid: remaining after {}: {}", handler.getClass().getSimpleName(), toDrain.isEmpty() ? 0 : toDrain.get(0).getAmount());
            }

            int remaining = toDrain.isEmpty() ? 0 : toDrain.get(0).getAmount();
            LOGGER.debug("GreenHouse drainFluid: needed={}, remaining={}", needed, remaining);
            return remaining <= 0;
        }

        private void completeSlot(int slot) {
            GTRecipe recipe = slotRecipes[slot];
            if (recipe == null) return;

            LOGGER.debug("GreenHouse completeSlot: slot={}, recipe={}, outputs={}", 
                slot, recipe.getId(), recipe.outputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList()));

            var itemsOutput = recipe.outputs.get(ItemRecipeCapability.CAP);
            if (itemsOutput == null || itemsOutput.isEmpty()) {
                LOGGER.debug("GreenHouse completeSlot: no item outputs for recipe {}", recipe.getId());
                slotProgress[slot] = 0;
                recalcDuration();
                return;
            }

            GreenHouseMachine machine = (GreenHouseMachine) getMachine();
            int parallel = machine.getParallelCount();

            // Build a temporary recipe with multiplied outputs and dispatch via standard GTCEu IO
            var builder = GTRecipeBuilder.of(ShishamoTech.id("green_house_output"), STRecipeTypes.GREEN_HOUSE_RECIPES)
                    .duration(1);

            for (var content : itemsOutput) {
                ItemStack outputStack = extractOutputStack(content);
                if (outputStack != null && !outputStack.isEmpty()) {
                    outputStack.setCount(outputStack.getCount() * parallel);
                    builder.outputItems(outputStack);
                }
            }

            var outputRecipe = builder.buildRawRecipe();
            var result = RecipeHelper.handleRecipeIO(machine, outputRecipe, IO.OUT, Collections.emptyMap());

            if (result.isSuccess()) {
                LOGGER.debug("GreenHouse completeSlot: slot={} output OK (parallel={})", slot, parallel);
                slotProgress[slot] = 0;
                recalcDuration();
            } else {
                LOGGER.warn("GreenHouse completeSlot: slot={} failed to output all items", slot);
                slotProgress[slot] = 0;
            }
        }

        private static ItemStack extractOutputStack(Content content) {
            if (content.getContent() instanceof ItemStack directStack) {
                return directStack.copy();
            } else if (content.getContent() instanceof SizedIngredient si) {
                var stacks = si.getItems();
                if (stacks.length > 0 && !stacks[0].isEmpty()) {
                    var stack = stacks[0].copy();
                    stack.setCount(si.getAmount());
                    return stack;
                }
            } else if (content.getContent() instanceof Ingredient ing) {
                var stacks = ing.getItems();
                if (stacks.length > 0 && !stacks[0].isEmpty()) {
                    return stacks[0].copy();
                }
            }
            return null;
        }

        @Override
        public @NotNull Iterator<GTRecipe> searchRecipe() {
            return Collections.emptyIterator();
        }
    }
}