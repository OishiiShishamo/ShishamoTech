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
class STParallelMultiblockMachineTest {

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
        assertEquals(4, STOverclockingLogic.getParallelBonus(0));
    }

    @Test
    void testGetBaseParallelForTierStaticTier1() {
        assertEquals(8, STOverclockingLogic.getParallelBonus(1));
    }

    @Test
    void testGetBaseParallelForTierStaticTier6() {
        assertEquals(48, STOverclockingLogic.getParallelBonus(6));
    }

    @Test
    void testGetBaseParallelForTierStaticDefault() {
        assertEquals(64, STOverclockingLogic.getParallelBonus(10));
    }

    @Test
    void testGetDisplayParallelCount() {
        assertEquals(STOverclockingLogic.getParallelBonus(1) * STConfig.parallelMultiplier,
                STParallelMultiblockMachine.getDisplayParallelCount(1));
    }

    @Test
    void testGetDisplayParallelCountTier0() {
        assertEquals(STOverclockingLogic.getParallelBonus(0) * STConfig.parallelMultiplier,
                STParallelMultiblockMachine.getDisplayParallelCount(0));
    }

    @Test
    void testGetDisplayParallelCountTier4() {
        assertEquals(STOverclockingLogic.getParallelBonus(4) * STConfig.parallelMultiplier,
                STParallelMultiblockMachine.getDisplayParallelCount(4));
    }

    @Test
    void testRecipeModifierWithNonMachineReturnsIdentity() {
        MetaMachine nonMachine = mock(MetaMachine.class);
        ModifierFunction result = STParallelMultiblockMachine.recipeModifier(
                nonMachine, mockRecipe);
        assertSame(ModifierFunction.IDENTITY, result);
    }
}