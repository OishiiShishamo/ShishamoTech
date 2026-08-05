package shishamo_tech;

import shishamo_tech.config.STConfig;

import java.util.List;

public final class STTestHelper {
    private STTestHelper() {}

    public static final int DEFAULT_PARALLEL_MULTIPLIER = 64;
    public static final boolean DEFAULT_ENABLE_STEAM    = true;
    public static final boolean DEFAULT_ENABLE_ELECTRIC  = true;
    public static final boolean DEFAULT_ENABLE_AE2       = true;
    public static final boolean DEFAULT_ENABLE_BOTANY    = true;
    public static final boolean DEFAULT_ENABLE_VOID_MINER = true;
    public static final boolean DEFAULT_ENABLE_UUS       = true;
    public static final boolean DEFAULT_ENABLE_COMPRESSED = true;

    public static void resetConfigDefaults() {
        STConfig.parallelMultiplier = DEFAULT_PARALLEL_MULTIPLIER;
        STConfig.enableSteamMachines = DEFAULT_ENABLE_STEAM;
        STConfig.enableElectricMachines = DEFAULT_ENABLE_ELECTRIC;
        STConfig.enableAE2Integration = DEFAULT_ENABLE_AE2;
        STConfig.enableBotanyIntegration = DEFAULT_ENABLE_BOTANY;
        STConfig.enableMegaSteamVoidResourceMiner = DEFAULT_ENABLE_VOID_MINER;
        STConfig.enableUltimateUniversalStorage = DEFAULT_ENABLE_UUS;
        STConfig.enableCompressedSingleblockRecipes = DEFAULT_ENABLE_COMPRESSED;
        STConfig.voidMinerExtraItems = List.of();
        STConfig.voidMinerExtraFluids = List.of();
        STConfig.voidMinerBlacklist = List.of();
    }
}