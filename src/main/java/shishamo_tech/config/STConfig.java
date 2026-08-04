package shishamo_tech.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

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
        return !SPEC.isLoaded() || enableMegaSteamVoidResourceMiner;
    }

    public static boolean isUltimateUniversalStorageEnabled() {
        return (!SPEC.isLoaded() || enableUltimateUniversalStorage)
                && net.minecraftforge.fml.ModList.get().isLoaded("ae2");
    }

    public static boolean isCompressedSingleblockRecipesEnabled() {
        return !SPEC.isLoaded() || enableCompressedSingleblockRecipes;
    }
}
