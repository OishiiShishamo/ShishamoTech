package shishamo_tech.common.machine.ae2;

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
class STInscriberMultiblockMachineTest {

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
    void testGetDisplayParallelCount() {
        assertEquals(STOverclockingLogic.getParallelBonus(2) * STConfig.parallelMultiplier,
                STInscriberMultiblockMachine.getDisplayParallelCount(2));
    }

    @Test
    void testGetDisplayParallelCountTier0() {
        assertEquals(STOverclockingLogic.getParallelBonus(0) * STConfig.parallelMultiplier,
                STInscriberMultiblockMachine.getDisplayParallelCount(0));
    }

    @Test
    void testGetDisplayParallelCountTier4() {
        assertEquals(STOverclockingLogic.getParallelBonus(4) * STConfig.parallelMultiplier,
                STInscriberMultiblockMachine.getDisplayParallelCount(4));
    }

    @Test
    void testRecipeModifierWithNonInscriberMachineReturnsIdentity() {
        MetaMachine nonInscriber = mock(MetaMachine.class);
        ModifierFunction result = STInscriberMultiblockMachine.recipeModifier(
                nonInscriber, mockRecipe);
        assertSame(ModifierFunction.IDENTITY, result);
    }
}