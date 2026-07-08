package shishamo_tech;

import shishamo_tech.config.STConfig;

public final class STTestHelper {
    private STTestHelper() {}

    public static final int DEFAULT_PARALLEL_MULTIPLIER = 64;
    public static final boolean DEFAULT_ENABLE_STEAM    = true;
    public static final boolean DEFAULT_ENABLE_ELECTRIC  = true;
    public static final boolean DEFAULT_ENABLE_AE2       = true;

    public static void resetConfigDefaults() {
        STConfig.parallelMultiplier = DEFAULT_PARALLEL_MULTIPLIER;
        STConfig.enableSteamMachines = DEFAULT_ENABLE_STEAM;
        STConfig.enableElectricMachines = DEFAULT_ENABLE_ELECTRIC;
        STConfig.enableAE2Integration = DEFAULT_ENABLE_AE2;
    }
}