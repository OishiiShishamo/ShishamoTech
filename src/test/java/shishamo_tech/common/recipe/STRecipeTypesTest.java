package shishamo_tech.common.recipe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class STRecipeTypesTest {

    @Test
    void testPrivateConstructor() {
        var constructor = STRecipeTypes.class.getDeclaredConstructors()[0];
        assertFalse(constructor.canAccess(null));
    }

    @Test
    void testRecipeAssignmentRecord() {
        var assignment = new STRecipeTypes.RecipeAssignment(null, 1);
        assertEquals(1, assignment.circuit());
    }

    @Test
    void testRecipeAssignmentRecordCircuit5() {
        var assignment = new STRecipeTypes.RecipeAssignment(null, 5);
        assertEquals(5, assignment.circuit());
    }

    @Test
    void testRecipeAssignmentRecordWithRecipe() {
        var assignment = new STRecipeTypes.RecipeAssignment(null, 3);
        assertNull(assignment.recipe());
        assertEquals(3, assignment.circuit());
    }
}