package shishamo_tech.config;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class STConfig {
    private STConfig() {}

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue PARALLEL_MULTIPLIER = BUILDER
            .comment("Parallel multiplier applied to all ShishamoTech machines (default 64)")
            .defineInRange("parallelMultiplier", 64, 1, 2147483647);

    public static final ForgeConfigSpec.BooleanValue ENABLE_STEAM_MACHINES = BUILDER
            .comment("Enable Steam-era mega multiblocks")
            .define("enableSteamMachines", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_ELECTRIC_MACHINES = BUILDER
            .comment("Enable Electric-era mega multiblocks")
            .define("enableElectricMachines", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_AE2_INTEGRATION = BUILDER
            .comment("Enable AE2 integration multiblocks (requires AE2 installed)")
            .define("enableAE2Integration", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_BOTANY_INTEGRATION = BUILDER
            .comment("Enable Botany Pots integration multiblocks (requires Botany Pots installed)")
            .define("enableBotanyIntegration", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_MEGA_STEAM_VOID_RESOURCE_MINER = BUILDER
            .comment("Enable Mega Steam Void Resource Miner functionality")
            .define("enableMegaSteamVoidResourceMiner", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_ULTIMATE_UNIVERSAL_STORAGE = BUILDER
            .comment("Enable Ultimate Universal Storage functionality")
            .define("enableUltimateUniversalStorage", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_COMPRESSED_SINGLEBLOCK_RECIPES = BUILDER
            .comment("Enable 8x speed compressed recipes on compressed single block machines")
            .define("enableCompressedSingleblockRecipes", true);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> VOID_MINER_EXTRA_ITEMS = BUILDER
            .comment("Additional item resources for the Mega Steam Void Resource Miner.",
                    "Format: \"<registry_id>|<count>|<weight>\" e.g. \"minecraft:diamond|1|5\"")
            .defineList("voidMinerExtraItems", List.of(), o -> o instanceof String);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> VOID_MINER_EXTRA_FLUIDS = BUILDER
            .comment("Additional fluid resources for the Mega Steam Void Resource Miner.",
                    "Format: \"<registry_id>|<amount_mb>|<weight>\" e.g. \"gtceu:oil|100|10\"")
            .defineList("voidMinerExtraFluids", List.of(), o -> o instanceof String);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> VOID_MINER_BLACKLIST = BUILDER
            .comment("Blacklisted resources for the Mega Steam Void Resource Miner.",
                    "Any item or fluid registry id in this list will never be rolled by the miner.",
                    "Format: \"<registry_id>\" e.g. \"minecraft:diamond\" or \"gtceu:oil\"")
            .defineList("voidMinerBlacklist", List.of(), o -> o instanceof String);

    private static final List<String> MACHINE_PATHS = List.of(
            // Steam era
            "mega_steam_grinder",
            "mega_steam_furnace",
            "mega_steam_compressor",
            "mega_steam_extractor",
            "mega_steam_hammer",
            "mega_steam_alloy_smelter",
            "mega_steam_rock_crusher",
            "mega_steam_void_resource_miner",
            "god_steam_boiler",
            "hyper_primitive_blast_furnace",
            // Electric era
            "superior_maceration_plant",
            "large_smelting_plant",
            "large_washing_plant",
            "hyper_tower_centrifuge",
            "large_electrolyzer",
            "lcr_cluster",
            "large_assembly_plant",
            "large_arc_furnace",
            "large_distillation_tower",
            "eternal_force_freezer",
            "non_omnipotent_universe_forge",
            // AE2
            "press_free_inscriber_mv",
            "press_free_inscriber_hv",
            "press_free_inscriber_ev",
            "press_free_inscriber_iv",
            "me_long_output_hatch",
            "me_long_output_bus",
            "me_dual_long_input_hatch",
            "me_dual_long_output_hatch",
            "me_dual_stocking_input_hatch",
            "me_steam_hatch",
            // Botany
            "green_house",
            // Storage
            "ultimate_universal_storage",
            // Compressed single block families
            "compressed_macerator",
            "compressed_furnace",
            "compressed_alloy_smelter",
            "compressed_arc_furnace",
            "compressed_assembler",
            "compressed_autoclave",
            "compressed_bender",
            "compressed_canner",
            "compressed_centrifuge",
            "compressed_chemical_bath",
            "compressed_chemical_reactor",
            "compressed_compressor",
            "compressed_cutter_saw",
            "compressed_distillery",
            "compressed_electrolyzer",
            "compressed_electromagnetic_separator",
            "compressed_extractor",
            "compressed_extruder",
            "compressed_fermenter",
            "compressed_fluid_heater",
            "compressed_fluid_solidifier",
            "compressed_forge_hammer",
            "compressed_forming_press",
            "compressed_lathe",
            "compressed_mixer",
            "compressed_ore_washer",
            "compressed_packer",
            "compressed_polarizer",
            "compressed_laser_engraver",
            "compressed_sifter",
            "compressed_thermal_centrifuge",
            "compressed_wiremill",
            "compressed_circuit_assembler",
            "compressed_rock_breaker",
            "compressed_scanner");

    private static final Map<String, ForgeConfigSpec.BooleanValue> MACHINE_TOGGLES = new LinkedHashMap<>();

    static {
        BUILDER.push("machines");
        for (String path : MACHINE_PATHS) {
            MACHINE_TOGGLES.put(path, BUILDER
                    .comment("Enable the '" + path + "' machine. Disabled machines will not run.")
                    .define(toConfigKey(path), true));
        }
        BUILDER.pop();
    }

    private static String toConfigKey(String path) {
        StringBuilder sb = new StringBuilder("enable");
        for (String part : path.split("_")) {
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0)));
                sb.append(part.substring(1));
            }
        }
        return sb.toString();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int parallelMultiplier = 64;
    public static boolean enableSteamMachines = true;
    public static boolean enableElectricMachines = true;
    public static boolean enableAE2Integration = true;
    public static boolean enableBotanyIntegration = true;
    public static boolean enableMegaSteamVoidResourceMiner = true;
    public static boolean enableUltimateUniversalStorage = true;
    public static boolean enableCompressedSingleblockRecipes = true;
    public static List<String> voidMinerExtraItems = List.of();
    public static List<String> voidMinerExtraFluids = List.of();
    public static List<String> voidMinerBlacklist = List.of();

    private static final Map<String, Boolean> MACHINE_ENABLED = new HashMap<>();

    public static void refresh() {
        if (!SPEC.isLoaded()) return;
        parallelMultiplier = PARALLEL_MULTIPLIER.get();
        enableSteamMachines = ENABLE_STEAM_MACHINES.get();
        enableElectricMachines = ENABLE_ELECTRIC_MACHINES.get();
        enableAE2Integration = ENABLE_AE2_INTEGRATION.get();
        enableBotanyIntegration = ENABLE_BOTANY_INTEGRATION.get();
        enableMegaSteamVoidResourceMiner = ENABLE_MEGA_STEAM_VOID_RESOURCE_MINER.get();
        enableUltimateUniversalStorage = ENABLE_ULTIMATE_UNIVERSAL_STORAGE.get();
        enableCompressedSingleblockRecipes = ENABLE_COMPRESSED_SINGLEBLOCK_RECIPES.get();
        voidMinerExtraItems = List.copyOf(VOID_MINER_EXTRA_ITEMS.get());
        voidMinerExtraFluids = List.copyOf(VOID_MINER_EXTRA_FLUIDS.get());
        voidMinerBlacklist = List.copyOf(VOID_MINER_BLACKLIST.get());
        MACHINE_ENABLED.clear();
        for (var entry : MACHINE_TOGGLES.entrySet()) {
            MACHINE_ENABLED.put(entry.getKey(), entry.getValue().get());
        }
        shishamo_tech.common.machine.steam.STVoidResourceMinerMachine.invalidateResourceTable();
    }

    public static boolean isSteamEnabled() {
        return !SPEC.isLoaded() || enableSteamMachines;
    }

    public static boolean isElectricEnabled() {
        return !SPEC.isLoaded() || enableElectricMachines;
    }

    public static boolean isAE2Enabled() {
        return !SPEC.isLoaded() || enableAE2Integration;
    }

    public static boolean isBotanyEnabled() {
        return !SPEC.isLoaded() || enableBotanyIntegration;
    }

    public static boolean isMegaSteamVoidResourceMinerEnabled() {
        return (!SPEC.isLoaded() || enableMegaSteamVoidResourceMiner)
                && isMachineEnabled("mega_steam_void_resource_miner");
    }

    public static boolean isUltimateUniversalStorageEnabled() {
        return (!SPEC.isLoaded() || enableUltimateUniversalStorage)
                && net.minecraftforge.fml.ModList.get().isLoaded("ae2")
                && isMachineEnabled("ultimate_universal_storage");
    }

    public static boolean isCompressedSingleblockRecipesEnabled() {
        return !SPEC.isLoaded() || enableCompressedSingleblockRecipes;
    }

    /**
     * Checks the per-machine config toggle. Unknown machine ids default to enabled.
     */
    public static boolean isMachineEnabled(String path) {
        if (!SPEC.isLoaded()) return true;
        var enabled = MACHINE_ENABLED.get(path);
        return enabled == null || enabled;
    }

    /**
     * Checks the per-machine config toggle for the given machine by deriving its
     * definition path (e.g. "mega_steam_grinder").
     */
    public static boolean isMachineEnabled(MetaMachine machine) {
        if (machine == null || machine.getDefinition() == null) return true;
        return isMachineEnabled(machine.getDefinition().getId().getPath());
    }

    /**
     * Checks whether a machine is effectively enabled, considering its category-level
     * config flag (e.g. {@code enableSteamMachines}) as well as the per-machine toggle.
     */
    public static boolean isMachineFullyEnabled(String path) {
        if (!SPEC.isLoaded()) return true;
        if (!isMachineEnabled(path)) return false;
        if (path.startsWith("compressed_")) {
            return enableCompressedSingleblockRecipes;
        }
        if (path.startsWith("press_free_inscriber_")) {
            return enableAE2Integration;
        }
        if (path.startsWith("me_long_") || path.startsWith("me_dual_") || path.equals("me_steam_hatch")) {
            return enableAE2Integration;
        }
        if (path.equals("green_house")) {
            return enableBotanyIntegration;
        }
        if (path.equals("mega_steam_void_resource_miner")) {
            return enableMegaSteamVoidResourceMiner && enableSteamMachines;
        }
        if (path.equals("ultimate_universal_storage")) {
            return enableUltimateUniversalStorage;
        }
        if (path.startsWith("mega_steam_") || path.equals("god_steam_boiler")
                || path.equals("hyper_primitive_blast_furnace")) {
            return enableSteamMachines;
        }
        return enableElectricMachines;
    }

    /**
     * Appends the "disabled by config" tooltip line when the machine is turned off.
     * Returns {@code true} if the machine is currently disabled.
     */
    public static boolean checkMachineDisabledTooltip(String path, List<Component> tooltips) {
        if (isMachineFullyEnabled(path)) return false;
        tooltips.add(Component.translatable("shishamo_tech.machine.disabled"));
        return true;
    }
}
