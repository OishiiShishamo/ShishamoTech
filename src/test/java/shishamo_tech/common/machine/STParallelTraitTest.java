package shishamo_tech.common.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import shishamo_tech.STTestHelper;
import shishamo_tech.config.STConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class STParallelTraitTest {

    @Mock
    private IMachineBlockEntity mockBlockEntity;

    @Mock
    private GTRecipe mockRecipe;

    private STParallelTrait trait;
    private MetaMachine dummyMachine;

    static class TestHolderMachine extends MetaMachine implements STParallelTrait.STParallelTraitHolder {
        private final STParallelTrait parallelTrait;

        TestHolderMachine(IMachineBlockEntity holder, STParallelTrait trait) {
            super(holder);
            this.parallelTrait = trait;
        }

        @Override
        public STParallelTrait getParallelTrait() {
            return parallelTrait;
        }
    }

    @BeforeEach
    void setUp() {
        STTestHelper.resetConfigDefaults();
        dummyMachine = mock(MetaMachine.class);
        trait = new STParallelTrait(dummyMachine, 8);
    }

    @AfterEach
    void tearDown() {
        STTestHelper.resetConfigDefaults();
    }

    @Test
    void testConstructor() {
        assertNotNull(trait);
    }

    @Test
    void testGetFieldHolder() {
        ManagedFieldHolder holder = trait.getFieldHolder();
        assertNotNull(holder);
        assertSame(holder, trait.getFieldHolder());
    }

    @Test
    void testGetParallelCountWithDefaultMultiplier() {
        assertEquals(512, trait.getParallelCount());
    }

    @Test
    void testGetParallelCountWithCustomMultiplier() {
        STConfig.parallelMultiplier = 10;
        assertEquals(80, trait.getParallelCount());
    }

    @Test
    void testGetParallelCountWithMultiplier1() {
        STConfig.parallelMultiplier = 1;
        assertEquals(8, trait.getParallelCount());
    }

    @Test
    void testGetParallelCountZeroBase() {
        STParallelTrait zeroTrait = new STParallelTrait(dummyMachine, 0);
        assertEquals(0, zeroTrait.getParallelCount());
    }

    @Test
    void testRecipeModifierWithHolderPositiveParallel() {
        STConfig.parallelMultiplier = 1;
        var holderMachine = new TestHolderMachine(mockBlockEntity,
                new STParallelTrait(dummyMachine, 5));

        try (MockedStatic<ParallelLogic> pl = mockStatic(ParallelLogic.class)) {
            pl.when(() -> ParallelLogic.getParallelAmountWithoutEU(
                    any(), any(), anyInt())).thenReturn(3);

            ModifierFunction result = STParallelTrait.recipeModifier(
                    holderMachine, mockRecipe);
            assertNotNull(result);
            assertNotSame(ModifierFunction.NULL, result);
            assertNotSame(ModifierFunction.IDENTITY, result);
        }
    }

    @Test
    void testRecipeModifierWithHolderReturningNullTrait() {
        var holderMachine = new TestHolderMachine(mockBlockEntity, null);

        ModifierFunction result = STParallelTrait.recipeModifier(
                holderMachine, mockRecipe);
        assertSame(ModifierFunction.IDENTITY, result);
    }

    @Test
    void testRecipeModifierWithNonHolder() {
        MetaMachine nonHolder = mock(MetaMachine.class);
        ModifierFunction result = STParallelTrait.recipeModifier(nonHolder, mockRecipe);
        assertSame(ModifierFunction.IDENTITY, result);
    }

    @Test
    void testRecipeModifierWithZeroActualParallel() {
        STConfig.parallelMultiplier = 1;
        var holderMachine = new TestHolderMachine(mockBlockEntity,
                new STParallelTrait(dummyMachine, 4));

        try (MockedStatic<ParallelLogic> pl = mockStatic(ParallelLogic.class)) {
            pl.when(() -> ParallelLogic.getParallelAmountWithoutEU(
                    any(), any(), anyInt())).thenReturn(0);

            ModifierFunction result = STParallelTrait.recipeModifier(
                    holderMachine, mockRecipe);
            assertSame(ModifierFunction.NULL, result);
        }
    }

    @Test
    void testSTParallelTraitHolderInterface() {
        assertTrue(STParallelTrait.STParallelTraitHolder.class.isInterface());
    }

    @Test
    void testGetParallelTraitFromHolder() {
        var holderMachine = new TestHolderMachine(mockBlockEntity, trait);
        assertSame(trait, holderMachine.getParallelTrait());
    }
}