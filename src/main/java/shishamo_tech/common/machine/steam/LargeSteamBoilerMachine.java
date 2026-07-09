package shishamo_tech.common.machine.steam;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LargeSteamBoilerMachine extends WorkableMultiblockMachine implements IDisplayUIMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            LargeSteamBoilerMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    public static final int STEAM_OUTPUT_PER_TICK = 6553600;
    private static final int TICKS_PER_STEAM_GENERATION = 5;

    @Nullable
    protected TickableSubscription steamSubs;

    public LargeSteamBoilerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onUnload() {
        if (steamSubs != null) {
            steamSubs.unsubscribe();
            steamSubs = null;
        }
        super.onUnload();
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        updateSteamSubscription();
        return value;
    }

    @Override
    public void onWaiting() {
        super.onWaiting();
        updateSteamSubscription();
    }

    protected void updateSteamSubscription() {
        if (isFormed() && recipeLogic.isWorking()) {
            steamSubs = subscribeServerTick(steamSubs, this::generateSteam);
        } else if (steamSubs != null) {
            steamSubs.unsubscribe();
            steamSubs = null;
        }
    }

    @SuppressWarnings("unchecked")
    protected void generateSteam() {
        if (getOffsetTimer() % TICKS_PER_STEAM_GENERATION != 0) return;
        if (!isFormed() || !recipeLogic.isWorking()) return;

        int steamToGenerate = STEAM_OUTPUT_PER_TICK * TICKS_PER_STEAM_GENERATION;
        int waterRequired = steamToGenerate;

        var drainWater = List.of(FluidIngredient.of(Fluids.WATER, waterRequired));
        List<IRecipeHandler<?>> inputTanks = new ArrayList<>();
        inputTanks.addAll(getCapabilitiesFlat(IO.IN, FluidRecipeCapability.CAP));
        for (var tank : inputTanks) {
            drainWater = (List<FluidIngredient>) tank.handleRecipe(IO.IN, null, drainWater, false);
            if (drainWater == null || drainWater.isEmpty()) break;
        }
        if (drainWater == null || drainWater.isEmpty()) return;
        int drained = waterRequired - drainWater.get(0).getAmount();
        if (drained <= 0) return;

        int steamProduced = drained;
        var fillSteam = List.of(FluidIngredient.of(GTMaterials.Steam.getFluid(steamProduced)));
        List<IRecipeHandler<?>> outputTanks = new ArrayList<>();
        outputTanks.addAll(getCapabilitiesFlat(IO.OUT, FluidRecipeCapability.CAP));
        for (var tank : outputTanks) {
            fillSteam = (List<FluidIngredient>) tank.handleRecipe(IO.OUT, null, fillSteam, false);
            if (fillSteam == null || fillSteam.isEmpty()) break;
        }
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        if (isFormed()) {
            if (recipeLogic.isWorking()) {
                int waterPerTick = STEAM_OUTPUT_PER_TICK;
                textList.add(Component.translatable("shishamo_tech.machine.steam_output", STEAM_OUTPUT_PER_TICK));
                textList.add(Component.translatable("shishamo_tech.machine.water_consumption", waterPerTick));
            }
        }
        IDisplayUIMachine.super.addDisplayText(textList);
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        return ModifierFunction.IDENTITY;
    }
}