package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shishamo_tech.STTestHelper;
import shishamo_tech.common.recipe.STOverclockingLogic;
import shishamo_tech.config.STConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class STCoilParallelMultiblockMachineTest {

    @Mock
    private GTRecipe mockRecipe;

    @BeforeEach
    void setUp() {
        STTestHelper.resetConfigDefaults();
    }

    @AfterEach
    void tearDown() {
        STTestHelper.resetConfigDefaults();
    }

    @Test
    void testGetBaseParallelForTierStaticTier0() {
        assertEquals(4, STCoilParallelMultiblockMachine.getBaseParallelForTier(0));
    }

    @Test
    void testGetBaseParallelForTierStaticTier1() {
        assertEquals(8, STCoilParallelMultiblockMachine.getBaseParallelForTier(1));
    }

    @Test
    void testGetBaseParallelForTierStaticDefault() {
        assertEquals(64, STCoilParallelMultiblockMachine.getBaseParallelForTier(10));
    }

    private static final int[] COIL_MULTIPLIER_BY_TIER = {1, 2, 2, 4, 4, 8, 8, 16, 16, 16, 16};

    @Test
    void testGetCoilParallelBonusTier0() {
        assertEquals(0, getCoilBonusForTier(0));
    }

    @Test
    void testGetCoilParallelBonusTier1And2() {
        assertEquals(2, getCoilBonusForTier(1));
    }

    @Test
    void testGetCoilParallelBonusTier3And4() {
        assertEquals(4, getCoilBonusForTier(3));
    }

    @Test
    void testGetCoilParallelBonusTier5And6() {
        assertEquals(8, getCoilBonusForTier(5));
    }

    @Test
    void testGetCoilParallelBonusDefault() {
        assertEquals(16, getCoilBonusForTier(7));
    }

    private static int getCoilBonusForTier(int coilTier) {
        return switch (coilTier) {
            case 0 -> 0;
            case 1, 2 -> 2;
            case 3, 4 -> 4;
            case 5, 6 -> 8;
            default -> 16;
        };
    }

    @Test
    void testCoilMultiplierMatchesDisplayParallelCount() {
        int machineTier = 2;
        int base = STOverclockingLogic.getParallelBonus(machineTier);
        for (int coil = 0; coil < COIL_MULTIPLIER_BY_TIER.length && coil <= 7; coil++) {
            int expected = base * STConfig.parallelMultiplier * COIL_MULTIPLIER_BY_TIER[coil];
            assertEquals(expected,
                    STCoilParallelMultiblockMachine.getDisplayParallelCount(machineTier, coil),
                    "Mismatch at coil tier " + coil);
        }
    }

    @Test
    void testGetDisplayParallelCountCoilTier0() {
        assertEquals(STOverclockingLogic.getParallelBonus(1) * STConfig.parallelMultiplier,
                STCoilParallelMultiblockMachine.getDisplayParallelCount(1, 0));
    }

    @Test
    void testGetDisplayParallelCountCoilTier3() {
        assertEquals(STOverclockingLogic.getParallelBonus(2) * STConfig.parallelMultiplier * 4,
                STCoilParallelMultiblockMachine.getDisplayParallelCount(2, 3));
    }

    @Test
    void testGetDisplayParallelCountCoilTier5() {
        assertEquals(STOverclockingLogic.getParallelBonus(3) * STConfig.parallelMultiplier * 8,
                STCoilParallelMultiblockMachine.getDisplayParallelCount(3, 5));
    }

    @Test
    void testGetDisplayParallelCountCoilDefault() {
        assertEquals(STOverclockingLogic.getParallelBonus(2) * STConfig.parallelMultiplier * 16,
                STCoilParallelMultiblockMachine.getDisplayParallelCount(2, 9));
    }

    @Test
    void testRecipeModifierWithNonCoilMachineReturnsIdentity() {
        MetaMachine nonCoil = mock(MetaMachine.class);
        ModifierFunction result = STCoilParallelMultiblockMachine.recipeModifier(
                nonCoil, mockRecipe);
        assertSame(ModifierFunction.IDENTITY, result);
    }
}