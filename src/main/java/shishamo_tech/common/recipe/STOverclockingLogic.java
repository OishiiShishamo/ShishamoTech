package shishamo_tech.common.recipe;

import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import shishamo_tech.config.STConfig;

public final class STOverclockingLogic {

    private STOverclockingLogic() {}

    public static final OverclockingLogic TRIPLE_OVERCLOCK = OverclockingLogic.create(1.0 / 3.0, 3.0, false);
    public static final OverclockingLogic TRIPLE_OVERCLOCK_SUBTICK = OverclockingLogic.create(1.0 / 3.0, 3.0, true);

    public static int getParallelBonus(int tier) {
        return switch (tier) {
            case 0 -> 4;
            case 1 -> 8;
            case 2 -> 12;
            case 3 -> 16;
            case 4 -> 24;
            case 5 -> 32;
            case 6 -> 48;
            default -> 64;
        };
    }

    public static int computeParallelCount(int machineTier, int definitionTier) {
        return getParallelBonus(machineTier) * getParallelBonus(definitionTier) * STConfig.parallelMultiplier;
    }

    public static int computeParallelCount(int machineTier, int definitionTier, int coilBonus) {
        return computeParallelCount(machineTier, definitionTier) * coilBonus;
    }

    public static int computeParallelCountSafe(int machineTier, int definitionTier) {
        return Math.max(1, computeParallelCount(machineTier, definitionTier));
    }

    public static int computeDisplayParallel(int tier) {
        return getParallelBonus(tier) * STConfig.parallelMultiplier;
    }

    public static int computeDisplayParallel(int tier, int coilTier) {
        return computeDisplayParallel(tier) * switch (coilTier) {
            case 0 -> 1;
            case 1, 2 -> 2;
            case 3, 4 -> 4;
            case 5, 6 -> 8;
            default -> 16;
        };
    }

    public static int getCoilBonus(int coilTier) {
        return switch (coilTier) {
            case 0 -> 1;
            case 1, 2 -> 2;
            case 3, 4 -> 4;
            case 5, 6 -> 8;
            default -> 16;
        };
    }
}