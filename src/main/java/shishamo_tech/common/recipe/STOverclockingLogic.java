package shishamo_tech.common.recipe;

import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;

public final class STOverclockingLogic {

    private STOverclockingLogic() {}

    public static final OverclockingLogic TRIPLE_OVERCLOCK = OverclockingLogic.create(1.0 / 3.0, 3.0, false);
    public static final OverclockingLogic TRIPLE_OVERCLOCK_SUBTICK = OverclockingLogic.create(1.0 / 3.0, 3.0, true);
}