package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.api.capability.recipe.IFilteredHandler;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.IRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerGroupDistinctness;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import com.lowdragmc.lowdraglib.syncdata.ISubscription;

import net.minecraft.world.item.crafting.Ingredient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Proxy-side recipe handlers for {@link MEOversizePatternBufferProxyPartMachine}.
 *
 * <p>Previously one {@link RecipeHandlerList} per pattern slot (1152), each subscribing
 * its own listeners to the buffer's shared handlers — a notify storm on every shared
 * inventory change. Now a single {@link ProxyRHL} delegates consumption to the buffer's
 * aggregated handler and only subscribes five listeners total.
 */
public final class STProxySlotRecipeHandler {

    private final ProxyRHL proxyRHL;
    private final List<RecipeHandlerList> proxySlotHandlers;

    public STProxySlotRecipeHandler(MEOversizePatternBufferProxyPartMachine machine) {
        proxyRHL = new ProxyRHL(machine);
        proxySlotHandlers = List.of(proxyRHL);
    }

    public List<RecipeHandlerList> getProxySlotHandlers() {
        return proxySlotHandlers;
    }

    public void updateProxy(MEOversizePatternBufferPartMachine patternBuffer) {
        proxyRHL.setBuffer(patternBuffer);
    }

    public void clearProxy() {
        proxyRHL.clearBuffer();
    }

    protected static class ProxyRHL extends RecipeHandlerList {

        private final ProxyItemRecipeHandler circuit;
        private final ProxyItemRecipeHandler sharedItem;
        private final ProxyItemRecipeHandler slotItem;
        private final ProxyFluidRecipeHandler sharedFluid;
        private final ProxyFluidRecipeHandler slotFluid;

        private @Nullable MEOversizePatternBufferPartMachine buffer;

        public ProxyRHL(MEOversizePatternBufferProxyPartMachine machine) {
            super(IO.IN);
            circuit = new ProxyItemRecipeHandler(machine);
            sharedItem = new ProxyItemRecipeHandler(machine);
            slotItem = new ProxyItemRecipeHandler(machine);
            sharedFluid = new ProxyFluidRecipeHandler(machine);
            slotFluid = new ProxyFluidRecipeHandler(machine);
            addHandlers(circuit, sharedItem, slotItem, sharedFluid, slotFluid);
            this.setGroup(RecipeHandlerGroupDistinctness.BUS_DISTINCT);
        }

        public void setBuffer(MEOversizePatternBufferPartMachine buffer) {
            if (this.buffer == buffer) return;
            this.buffer = buffer;
            var handler = buffer.getInternalRecipeHandler();
            circuit.setProxy(buffer.getCircuitInventory());
            sharedItem.setProxy(buffer.getShareInventory());
            sharedFluid.setProxy(buffer.getShareTank());
            slotItem.setProxy(handler.getAggregateItemHandler());
            slotFluid.setProxy(handler.getAggregateFluidHandler());
        }

        public void clearBuffer() {
            if (this.buffer == null) return;
            this.buffer = null;
            circuit.setProxy(null);
            sharedItem.setProxy(null);
            sharedFluid.setProxy(null);
            slotItem.setProxy(null);
            slotFluid.setProxy(null);
        }

        @Override
        public Map<RecipeCapability<?>, List<Object>> handleRecipe(IO io, GTRecipe recipe,
                                                                   Map<RecipeCapability<?>, List<Object>> contents,
                                                                   boolean simulate) {
            var target = buffer;
            if (target == null) {
                return contents;
            }
            return target.getInternalRecipeHandler().getBufferHandler().handleRecipe(io, recipe, contents, simulate);
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public void setDistinct(boolean ignored, boolean notify) {}
    }

    private static class ProxyItemRecipeHandler extends NotifiableRecipeHandlerTrait<Ingredient> {

        private IRecipeHandlerTrait<Ingredient> proxy = null;
        private ISubscription proxySub = null;

        private final IO handlerIO = IO.IN;
        private final RecipeCapability<Ingredient> capability = ItemRecipeCapability.CAP;

        @Override
        public IO getHandlerIO() {
            return handlerIO;
        }

        @Override
        public RecipeCapability<Ingredient> getCapability() {
            return capability;
        }

        public ProxyItemRecipeHandler(MetaMachine machine) {
            super(machine);
        }

        public void setProxy(IRecipeHandlerTrait<Ingredient> proxy) {
            if (this.proxy == proxy) return;
            this.proxy = proxy;
            if (proxySub != null) {
                proxySub.unsubscribe();
                proxySub = null;
            }
            if (proxy != null) {
                proxySub = proxy.addChangedListener(this::notifyListeners);
            }
        }

        @Override
        public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
            if (proxy == null) return left;
            return proxy.handleRecipeInner(io, recipe, left, simulate);
        }

        @Override
        public int getSize() {
            if (proxy == null) return 0;
            return proxy.getSize();
        }

        @Override
        public @NotNull List<Object> getContents() {
            if (proxy == null) return Collections.emptyList();
            return proxy.getContents();
        }

        @Override
        public double getTotalContentAmount() {
            if (proxy == null) return 0;
            return proxy.getTotalContentAmount();
        }

        @Override
        public int getPriority() {
            if (proxy == null) return IFilteredHandler.LOW;
            return proxy.getPriority();
        }
    }

    private static class ProxyFluidRecipeHandler extends NotifiableRecipeHandlerTrait<FluidIngredient> {

        private IRecipeHandlerTrait<FluidIngredient> proxy = null;
        private ISubscription proxySub = null;

        private final IO handlerIO = IO.IN;
        private final RecipeCapability<FluidIngredient> capability = FluidRecipeCapability.CAP;

        @Override
        public IO getHandlerIO() {
            return handlerIO;
        }

        @Override
        public RecipeCapability<FluidIngredient> getCapability() {
            return capability;
        }

        public ProxyFluidRecipeHandler(MetaMachine machine) {
            super(machine);
        }

        public void setProxy(IRecipeHandlerTrait<FluidIngredient> proxy) {
            if (this.proxy == proxy) return;
            this.proxy = proxy;
            if (proxySub != null) {
                proxySub.unsubscribe();
                proxySub = null;
            }
            if (proxy != null) {
                proxySub = proxy.addChangedListener(this::notifyListeners);
            }
        }

        @Override
        public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left,
                                                       boolean simulate) {
            if (proxy == null) return left;
            return proxy.handleRecipeInner(io, recipe, left, simulate);
        }

        @Override
        public int getSize() {
            if (proxy == null) return 0;
            return proxy.getSize();
        }

        @Override
        public @NotNull List<Object> getContents() {
            if (proxy == null) return Collections.emptyList();
            return proxy.getContents();
        }

        @Override
        public double getTotalContentAmount() {
            if (proxy == null) return 0;
            return proxy.getTotalContentAmount();
        }

        @Override
        public int getPriority() {
            if (proxy == null) return IFilteredHandler.LOW;
            return proxy.getPriority();
        }
    }
}
