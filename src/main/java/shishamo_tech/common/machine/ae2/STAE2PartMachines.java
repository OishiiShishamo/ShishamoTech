package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;

import net.minecraft.network.chat.Component;

import shishamo_tech.STRegistration;
import shishamo_tech.config.STConfig;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.ZPM;

public final class STAE2PartMachines {
    private STAE2PartMachines() {}

    public static MachineDefinition ME_LONG_OUTPUT_HATCH;
    public static MachineDefinition ME_LONG_OUTPUT_BUS;
    public static MachineDefinition ME_DUAL_LONG_INPUT_HATCH;
    public static MachineDefinition ME_DUAL_LONG_OUTPUT_HATCH;
    public static MachineDefinition ME_DUAL_STOCKING_INPUT_HATCH;
    public static MachineDefinition ME_STEAM_HATCH;

    public static void init() {
        ME_DUAL_STOCKING_INPUT_HATCH = STRegistration.REGISTRATE
                .machine("me_dual_stocking_input_hatch", MEDualStockingInputHatchPartMachine::new)
                .langValue("ME Dual Stocking Input Hatch")
                .tier(ZPM)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
                .colorOverlayTieredHullModel(GTCEu.id("block/overlay/appeng/me_input_hatch"))
                .tooltipBuilder((stack, tooltips) -> {
                    STConfig.checkMachineDisabledTooltip("me_dual_stocking_input_hatch", tooltips);
                    tooltips.add(Component.translatable("shishamo_tech.machine.me_dual.item_fluid"));
                    tooltips.add(Component.translatable("gtceu.machine.me.stocking_fluid.tooltip.0"));
                    tooltips.add(Component.translatable("gtceu.machine.me.stocking_fluid.tooltip.1"));
                    tooltips.add(Component.translatable("gtceu.machine.me.stocking_item.tooltip.0"));
                    tooltips.add(Component.translatable("gtceu.machine.me.stocking_item.tooltip.1"));
                    tooltips.add(Component.translatable("gtceu.part_sharing.enabled"));
                })
                .register();

        ME_STEAM_HATCH = STRegistration.REGISTRATE
                .machine("me_steam_hatch", MESteamHatchPartMachine::new)
                .langValue("ME Steam Hatch")
                .tier(LuV)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.STEAM)
                .colorOverlayTieredHullModel(GTCEu.id("block/overlay/appeng/me_input_hatch"))
                .tooltipBuilder((stack, tooltips) -> {
                    STConfig.checkMachineDisabledTooltip("me_steam_hatch", tooltips);
                    tooltips.add(Component.translatable("gtceu.machine.steam.steam_hatch.tooltip"));
                    tooltips.add(Component.translatable("gtceu.machine.me.stocking_fluid.tooltip.0"));
                    tooltips.add(Component.translatable("gtceu.machine.me.stocking_fluid.tooltip.1"));
                    tooltips.add(Component.translatable("gtceu.machine.me.copy_paste.tooltip"));
                    tooltips.add(Component.translatable("gtceu.part_sharing.enabled"));
                })
                .register();

        ME_LONG_OUTPUT_HATCH = STRegistration.REGISTRATE
                .machine("me_long_output_hatch", MELongOutputHatchPartMachine::new)
                .langValue("ME Long Output Hatch")
                .tier(LuV)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.EXPORT_FLUIDS)
                .colorOverlayTieredHullModel(GTCEu.id("block/overlay/appeng/me_output_hatch"))
                .tooltipBuilder((stack, tooltips) -> {
                    STConfig.checkMachineDisabledTooltip("me_long_output_hatch", tooltips);
                    tooltips.add(Component.translatable("gtceu.machine.fluid_hatch.export.tooltip"));
                    tooltips.add(Component.translatable("gtceu.machine.me.fluid_export.tooltip"));
                    tooltips.add(Component.translatable("gtceu.machine.me.export.tooltip"));
                    tooltips.add(Component.translatable("shishamo_tech.machine.me_long.buffer_capacity"));
                    tooltips.add(Component.translatable("gtceu.part_sharing.enabled"));
                })
                .register();

        ME_LONG_OUTPUT_BUS = STRegistration.REGISTRATE
                .machine("me_long_output_bus", MELongOutputBusPartMachine::new)
                .langValue("ME Long Output Bus")
                .tier(LuV)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.EXPORT_ITEMS)
                .colorOverlayTieredHullModel(GTCEu.id("block/overlay/appeng/me_output_bus"))
                .tooltipBuilder((stack, tooltips) -> {
                    STConfig.checkMachineDisabledTooltip("me_long_output_bus", tooltips);
                    tooltips.add(Component.translatable("gtceu.machine.item_bus.export.tooltip"));
                    tooltips.add(Component.translatable("gtceu.machine.me.item_export.tooltip"));
                    tooltips.add(Component.translatable("gtceu.machine.me.export.tooltip"));
                    tooltips.add(Component.translatable("shishamo_tech.machine.me_long.buffer_capacity"));
                    tooltips.add(Component.translatable("gtceu.part_sharing.enabled"));
                })
                .register();

        ME_DUAL_LONG_OUTPUT_HATCH = STRegistration.REGISTRATE
                .machine("me_dual_long_output_hatch", MEDualLongOutputHatchPartMachine::new)
                .langValue("ME Dual Long Output Hatch")
                .tier(ZPM)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.EXPORT_ITEMS, PartAbility.EXPORT_FLUIDS)
                .colorOverlayTieredHullModel(GTCEu.id("block/overlay/appeng/me_output_hatch"))
                .tooltipBuilder((stack, tooltips) -> {
                    STConfig.checkMachineDisabledTooltip("me_dual_long_output_hatch", tooltips);
                    tooltips.add(Component.translatable("shishamo_tech.machine.me_dual.item_fluid"));
                    tooltips.add(Component.translatable("gtceu.machine.fluid_hatch.export.tooltip"));
                    tooltips.add(Component.translatable("gtceu.machine.me.fluid_export.tooltip"));
                    tooltips.add(Component.translatable("gtceu.machine.item_bus.export.tooltip"));
                    tooltips.add(Component.translatable("gtceu.machine.me.item_export.tooltip"));
                    tooltips.add(Component.translatable("gtceu.machine.me.export.tooltip"));
                    tooltips.add(Component.translatable("shishamo_tech.machine.me_long.buffer_capacity"));
                    tooltips.add(Component.translatable("gtceu.part_sharing.enabled"));
                })
                .register();
    }
}
