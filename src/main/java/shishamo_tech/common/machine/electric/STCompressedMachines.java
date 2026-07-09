package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import net.minecraft.network.chat.Component;
import shishamo_tech.STRegistration;
import shishamo_tech.ShishamoTech;
import shishamo_tech.common.recipe.STCompressedRecipeModifier;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.*;
import static com.gregtechceu.gtceu.utils.FormattingUtil.toEnglishName;

public final class STCompressedMachines {

    public static MachineDefinition[] COMPRESSED_MACERATOR;
    public static MachineDefinition[] COMPRESSED_FURNACE;
    public static MachineDefinition[] COMPRESSED_ALLOY_SMELTER;
    public static MachineDefinition[] COMPRESSED_ARC_FURNACE;
    public static MachineDefinition[] COMPRESSED_ASSEMBLER;
    public static MachineDefinition[] COMPRESSED_AUTOCLAVE;
    public static MachineDefinition[] COMPRESSED_BENDER;
    public static MachineDefinition[] COMPRESSED_CANNER;
    public static MachineDefinition[] COMPRESSED_CENTRIFUGE;
    public static MachineDefinition[] COMPRESSED_CHEMICAL_BATH;
    public static MachineDefinition[] COMPRESSED_CHEMICAL_REACTOR;
    public static MachineDefinition[] COMPRESSED_COMPRESSOR;
    public static MachineDefinition[] COMPRESSED_CUTTER;
    public static MachineDefinition[] COMPRESSED_DISTILLERY;
    public static MachineDefinition[] COMPRESSED_ELECTROLYZER;
    public static MachineDefinition[] COMPRESSED_ELECTROMAGNETIC_SEPARATOR;
    public static MachineDefinition[] COMPRESSED_EXTRACTOR;
    public static MachineDefinition[] COMPRESSED_EXTRUDER;
    public static MachineDefinition[] COMPRESSED_FERMENTING;
    public static MachineDefinition[] COMPRESSED_FLUID_HEATER;
    public static MachineDefinition[] COMPRESSED_FLUID_SOLIDFICATION;
    public static MachineDefinition[] COMPRESSED_FORGE_HAMMER;
    public static MachineDefinition[] COMPRESSED_FORMING_PRESS;
    public static MachineDefinition[] COMPRESSED_LATHE;
    public static MachineDefinition[] COMPRESSED_MIXER;
    public static MachineDefinition[] COMPRESSED_ORE_WASHER;
    public static MachineDefinition[] COMPRESSED_PACKER;
    public static MachineDefinition[] COMPRESSED_POLARIZER;
    public static MachineDefinition[] COMPRESSED_LASER_ENGRAVER;
    public static MachineDefinition[] COMPRESSED_SIFTER;
    public static MachineDefinition[] COMPRESSED_THERMAL_CENTRIFUGE;
    public static MachineDefinition[] COMPRESSED_WIREMILL;
    public static MachineDefinition[] COMPRESSED_CIRCUIT_ASSEMBLER;
    public static MachineDefinition[] COMPRESSED_ROCK_BREAKER;
    public static MachineDefinition[] COMPRESSED_SCANNER;

    private STCompressedMachines() {}

    public static void init() {
        COMPRESSED_MACERATOR = registerCompressedSimpleMachines("compressed_macerator", MACERATOR_RECIPES, "macerator");
        COMPRESSED_FURNACE = registerCompressedSimpleMachines("compressed_furnace", FURNACE_RECIPES, "electric_furnace");
        COMPRESSED_ALLOY_SMELTER = registerCompressedSimpleMachines("compressed_alloy_smelter", ALLOY_SMELTER_RECIPES, "alloy_smelter");
        COMPRESSED_ARC_FURNACE = registerCompressedSimpleMachines("compressed_arc_furnace", ARC_FURNACE_RECIPES, "arc_furnace");
        COMPRESSED_ASSEMBLER = registerCompressedSimpleMachines("compressed_assembler", ASSEMBLER_RECIPES, "assembler");
        COMPRESSED_AUTOCLAVE = registerCompressedSimpleMachines("compressed_autoclave", AUTOCLAVE_RECIPES, "autoclave");
        COMPRESSED_BENDER = registerCompressedSimpleMachines("compressed_bender", BENDER_RECIPES, "bender");
        COMPRESSED_CANNER = registerCompressedSimpleMachines("compressed_canner", CANNER_RECIPES, "canner");
        COMPRESSED_CENTRIFUGE = registerCompressedSimpleMachines("compressed_centrifuge", CENTRIFUGE_RECIPES, "centrifuge");
        COMPRESSED_CHEMICAL_BATH = registerCompressedSimpleMachines("compressed_chemical_bath", CHEMICAL_BATH_RECIPES, "chemical_bath");
        COMPRESSED_CHEMICAL_REACTOR = registerCompressedSimpleMachines("compressed_chemical_reactor", CHEMICAL_RECIPES, "chemical_reactor");
        COMPRESSED_COMPRESSOR = registerCompressedSimpleMachines("compressed_compressor", COMPRESSOR_RECIPES, "compressor");
        COMPRESSED_CUTTER = registerCompressedSimpleMachines("compressed_cutter_saw", CUTTER_RECIPES, "cutter");
        COMPRESSED_DISTILLERY = registerCompressedSimpleMachines("compressed_distillery", DISTILLERY_RECIPES, "distillery");
        COMPRESSED_ELECTROLYZER = registerCompressedSimpleMachines("compressed_electrolyzer", ELECTROLYZER_RECIPES, "electrolyzer");
        COMPRESSED_ELECTROMAGNETIC_SEPARATOR = registerCompressedSimpleMachines("compressed_electromagnetic_separator", ELECTROMAGNETIC_SEPARATOR_RECIPES, "electromagnetic_separator");
        COMPRESSED_EXTRACTOR = registerCompressedSimpleMachines("compressed_extractor", EXTRACTOR_RECIPES, "extractor");
        COMPRESSED_EXTRUDER = registerCompressedSimpleMachines("compressed_extruder", EXTRUDER_RECIPES, "extruder");
        COMPRESSED_FERMENTING = registerCompressedSimpleMachines("compressed_fermenter", FERMENTING_RECIPES, "fermenter");
        COMPRESSED_FLUID_HEATER = registerCompressedSimpleMachines("compressed_fluid_heater", FLUID_HEATER_RECIPES, "fluid_heater");
        COMPRESSED_FLUID_SOLIDFICATION = registerCompressedSimpleMachines("compressed_fluid_solidifier", FLUID_SOLIDFICATION_RECIPES, "fluid_solidifier");
        COMPRESSED_FORGE_HAMMER = registerCompressedSimpleMachines("compressed_forge_hammer", FORGE_HAMMER_RECIPES, "forge_hammer");
        COMPRESSED_FORMING_PRESS = registerCompressedSimpleMachines("compressed_forming_press", FORMING_PRESS_RECIPES, "forming_press");
        COMPRESSED_LATHE = registerCompressedSimpleMachines("compressed_lathe", LATHE_RECIPES, "lathe");
        COMPRESSED_MIXER = registerCompressedSimpleMachines("compressed_mixer", MIXER_RECIPES, "mixer");
        COMPRESSED_ORE_WASHER = registerCompressedSimpleMachines("compressed_ore_washer", ORE_WASHER_RECIPES, "ore_washer");
        COMPRESSED_PACKER = registerCompressedSimpleMachines("compressed_packer", PACKER_RECIPES, "packer");
        COMPRESSED_POLARIZER = registerCompressedSimpleMachines("compressed_polarizer", POLARIZER_RECIPES, "polarizer");
        COMPRESSED_LASER_ENGRAVER = registerCompressedSimpleMachines("compressed_laser_engraver", LASER_ENGRAVER_RECIPES, "laser_engraver");
        COMPRESSED_SIFTER = registerCompressedSimpleMachines("compressed_sifter", SIFTER_RECIPES, "sifter");
        COMPRESSED_THERMAL_CENTRIFUGE = registerCompressedSimpleMachines("compressed_thermal_centrifuge", THERMAL_CENTRIFUGE_RECIPES, "thermal_centrifuge");
        COMPRESSED_WIREMILL = registerCompressedSimpleMachines("compressed_wiremill", WIREMILL_RECIPES, "wiremill");
        COMPRESSED_CIRCUIT_ASSEMBLER = registerCompressedSimpleMachines("compressed_circuit_assembler", CIRCUIT_ASSEMBLER_RECIPES, "circuit_assembler");
        COMPRESSED_ROCK_BREAKER = registerCompressedSimpleMachines("compressed_rock_breaker", ROCK_BREAKER_RECIPES, "rock_breaker");
        COMPRESSED_SCANNER = registerCompressedSimpleMachines("compressed_scanner", SCANNER_RECIPES, "scanner");
    }

    private static MachineDefinition[] registerCompressedSimpleMachines(
            String name, GTRecipeType recipeType, String baseMachineName) {
        return GTMachineUtils.registerTieredMachines(
                STRegistration.REGISTRATE,
                name,
                (holder, tier) -> new SimpleTieredMachine(holder, tier, defaultTankSizeFunction),
                (tier, builder) -> builder
                        .langValue("Compressed %s %s %s".formatted(VLVH[tier], toEnglishName(baseMachineName), VLVT[tier]))
                        .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(
                                ShishamoTech.id(name), recipeType))
                        .rotationState(RotationState.NON_Y_AXIS)
                        .recipeType(recipeType)
                        .recipeModifiers(STCompressedRecipeModifier.COMPRESSED, GTRecipeModifiers.OC_NON_PERFECT)
                        .workableTieredHullModel(GTCEu.id("block/machines/" + baseMachineName))
                        .tooltipBuilder((stack, tooltips) -> {
                            tooltips.add(Component.literal("§a8x §7processing speed"));
                            for (var t : workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64,
                                    recipeType, defaultTankSizeFunction.applyAsInt(tier), true)) {
                                tooltips.add(t);
                            }
                        })
                        .register(),
                GTMachineUtils.ELECTRIC_TIERS);
    }
}
