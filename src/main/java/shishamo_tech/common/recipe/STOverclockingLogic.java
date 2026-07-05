package shishamo_tech.common.recipe;

import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;

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
}