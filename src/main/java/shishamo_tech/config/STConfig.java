package shishamo_tech.config;

import net.minecraftforge.common.ForgeConfigSpec;

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

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int parallelMultiplier = 64;
    public static boolean enableSteamMachines = true;
    public static boolean enableElectricMachines = true;
    public static boolean enableAE2Integration = true;
    public static boolean enableBotanyIntegration = true;

    public static void refresh() {
        if (!SPEC.isLoaded()) return;
        parallelMultiplier = PARALLEL_MULTIPLIER.get();
        enableSteamMachines = ENABLE_STEAM_MACHINES.get();
        enableElectricMachines = ENABLE_ELECTRIC_MACHINES.get();
        enableAE2Integration = ENABLE_AE2_INTEGRATION.get();
        enableBotanyIntegration = ENABLE_BOTANY_INTEGRATION.get();
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
}
