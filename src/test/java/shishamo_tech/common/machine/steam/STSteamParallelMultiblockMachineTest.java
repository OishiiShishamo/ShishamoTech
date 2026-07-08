package shishamo_tech.common.machine.steam;

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
import shishamo_tech.config.STConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class STSteamParallelMultiblockMachineTest {

    @Mock
    private GTRecipe mockRecipe;

    @BeforeEach
    void setUp() {
        STTestHelper.resetConfigDefaults();
        STConfig.enableSteamMachines = true;
        STConfig.parallelMultiplier = 1;
    }

    @AfterEach
    void tearDown() {
        STTestHelper.resetConfigDefaults();
    }

    @Test
    void testGetDisplayParallelCount() {
        assertEquals(5, STSteamParallelMultiblockMachine.getDisplayParallelCount(5));
    }

    @Test
    void testGetDisplayParallelCountZero() {
        assertEquals(0, STSteamParallelMultiblockMachine.getDisplayParallelCount(0));
    }

    @Test
    void testRecipeModifierWithNonSteamMachineReturnsIdentity() {
        MetaMachine nonSteam = mock(MetaMachine.class);
        ModifierFunction result = STSteamParallelMultiblockMachine.recipeModifier(
                nonSteam, mockRecipe);
        assertSame(ModifierFunction.IDENTITY, result);
    }
}