package shishamo_tech.common.machine.primitive;

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
class STPrimitiveBlastFurnaceMachineTest {

    @Mock
    private GTRecipe mockRecipe;

    @BeforeEach
    void setUp() {
        STTestHelper.resetConfigDefaults();
        STConfig.enableSteamMachines = true;
        STConfig.parallelMultiplier = 2;
    }

    @AfterEach
    void tearDown() {
        STTestHelper.resetConfigDefaults();
    }

    @Test
    void testGetDisplayParallelCountUsesConfig() {
        assertEquals(4 * 2, STPrimitiveBlastFurnaceMachine.getDisplayParallelCount());
    }

    @Test
    void testGetDisplayParallelCountUsesConfigMultiplier() {
        STConfig.parallelMultiplier = 64;
        assertEquals(4 * 64, STPrimitiveBlastFurnaceMachine.getDisplayParallelCount());
    }

    @Test
    void testRecipeModifierWithNonMachineReturnsIdentity() {
        MetaMachine nonMachine = mock(MetaMachine.class);
        ModifierFunction result = STPrimitiveBlastFurnaceMachine.recipeModifier(
                nonMachine, mockRecipe);
        assertSame(ModifierFunction.IDENTITY, result);
    }

    @Test
    void testRecipeModifierDisabledWhenSteamMachinesDisabled() {
        STConfig.enableSteamMachines = false;
        STPrimitiveBlastFurnaceMachine machine = mock(STPrimitiveBlastFurnaceMachine.class);
        ModifierFunction result = STPrimitiveBlastFurnaceMachine.recipeModifier(
                machine, mockRecipe);
        assertSame(ModifierFunction.NULL, result);
    }
}
