package shishamo_tech.common.recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class STRecipeModifierUtilTest {

    @Mock
    private MetaMachine mockMachine;

    @Mock
    private GTRecipe mockRecipe;

    @Test
    void testPrivateConstructor() {
        var constructor = STRecipeModifierUtil.class.getDeclaredConstructors()[0];
        assertFalse(constructor.canAccess(null));
    }

    @Test
    void testCreateParallelModifierZeroParallelReturnsNull() {
        try (MockedStatic<ParallelLogic> pl = mockStatic(ParallelLogic.class)) {
            pl.when(() -> ParallelLogic.getParallelAmountWithoutEU(
                    eq(mockMachine), eq(mockRecipe), anyInt()))
                    .thenReturn(0);

            ModifierFunction result = STRecipeModifierUtil.createParallelModifier(
                    mockMachine, mockRecipe, 4, 120);
            assertSame(ModifierFunction.NULL, result);
        }
    }

    @Test
    void testCreateParallelModifierNegativeParallelReturnsNull() {
        try (MockedStatic<ParallelLogic> pl = mockStatic(ParallelLogic.class)) {
            pl.when(() -> ParallelLogic.getParallelAmountWithoutEU(
                    eq(mockMachine), eq(mockRecipe), anyInt()))
                    .thenReturn(-1);

            ModifierFunction result = STRecipeModifierUtil.createParallelModifier(
                    mockMachine, mockRecipe, 4, 120);
            assertSame(ModifierFunction.NULL, result);
        }
    }

    @Test
    void testCreateParallelModifierWithZeroVoltage() {
        try (MockedStatic<ParallelLogic> pl = mockStatic(ParallelLogic.class)) {
            pl.when(() -> ParallelLogic.getParallelAmountWithoutEU(
                    eq(mockMachine), eq(mockRecipe), eq(4)))
                    .thenReturn(4);

            ModifierFunction result = STRecipeModifierUtil.createParallelModifier(
                    mockMachine, mockRecipe, 4, 0);
            assertNotNull(result);
            assertNotSame(ModifierFunction.NULL, result);
        }
    }

    @Test
    void testGetOverclockVoltageWithRegularMachine() {
        assertEquals(0, STRecipeModifierUtil.getOverclockVoltage(mockMachine));
    }

    @Test
    void testGetOverclockVoltageWithNull() {
        assertEquals(0, STRecipeModifierUtil.getOverclockVoltage(null));
    }

    @Test
    void testGetOverclockVoltageWithOverclockMachine() {
        MetaMachine ocMachine = mock(MetaMachine.class, withSettings()
                .extraInterfaces(IOverclockMachine.class));
        when(((IOverclockMachine) ocMachine).getOverclockVoltage()).thenReturn(480L);

        long result = STRecipeModifierUtil.getOverclockVoltage(ocMachine);
        assertEquals(480L, result);
    }
}