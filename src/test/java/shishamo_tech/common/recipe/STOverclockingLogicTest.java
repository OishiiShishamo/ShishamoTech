package shishamo_tech.common.recipe;

import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class STOverclockingLogicTest {

    @Test
    void testPrivateConstructor() {
        var constructor = STOverclockingLogic.class.getDeclaredConstructors()[0];
        assertFalse(constructor.canAccess(null));
    }

    @Test
    void testTripleOverclockConstant() {
        OverclockingLogic logic = STOverclockingLogic.TRIPLE_OVERCLOCK;
        assertNotNull(logic);
    }

    @Test
    void testTripleOverclockSubtickConstant() {
        OverclockingLogic logic = STOverclockingLogic.TRIPLE_OVERCLOCK_SUBTICK;
        assertNotNull(logic);
    }

    @Test
    void testTripleOverclockAndSubtickAreDifferent() {
        assertNotSame(STOverclockingLogic.TRIPLE_OVERCLOCK, STOverclockingLogic.TRIPLE_OVERCLOCK_SUBTICK);
    }

    @Test
    void testGetParallelBonusTier0() {
        assertEquals(4, STOverclockingLogic.getParallelBonus(0));
    }

    @Test
    void testGetParallelBonusTier1() {
        assertEquals(8, STOverclockingLogic.getParallelBonus(1));
    }

    @Test
    void testGetParallelBonusTier2() {
        assertEquals(12, STOverclockingLogic.getParallelBonus(2));
    }

    @Test
    void testGetParallelBonusTier3() {
        assertEquals(16, STOverclockingLogic.getParallelBonus(3));
    }

    @Test
    void testGetParallelBonusTier4() {
        assertEquals(24, STOverclockingLogic.getParallelBonus(4));
    }

    @Test
    void testGetParallelBonusTier5() {
        assertEquals(32, STOverclockingLogic.getParallelBonus(5));
    }

    @Test
    void testGetParallelBonusTier6() {
        assertEquals(48, STOverclockingLogic.getParallelBonus(6));
    }

    @Test
    void testGetParallelBonusDefault() {
        assertEquals(64, STOverclockingLogic.getParallelBonus(7));
    }

    @Test
    void testGetParallelBonusDefaultHighTier() {
        assertEquals(64, STOverclockingLogic.getParallelBonus(10));
    }

    @Test
    void testGetParallelBonusDefaultNegative() {
        assertEquals(64, STOverclockingLogic.getParallelBonus(-1));
    }

    @Test
    void testGetParallelBonusAllTiers() {
        int[] expected = {4, 8, 12, 16, 24, 32, 48, 64, 64};
        for (int tier = 0; tier <= 8; tier++) {
            assertEquals(expected[tier], STOverclockingLogic.getParallelBonus(tier),
                    "Mismatch at tier " + tier);
        }
    }
}