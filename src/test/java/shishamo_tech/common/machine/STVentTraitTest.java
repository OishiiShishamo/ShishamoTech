package shishamo_tech.common.machine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class STVentTraitTest {

    @Mock
    private MetaMachine mockMachine;

    @Test
    void testConstructorStoresDirection() {
        STVentTrait trait = new STVentTrait(mockMachine, RelativeDirection.UP);
        assertEquals(RelativeDirection.UP, trait.getVentDirection());
    }

    @Test
    void testConstructorDefaultDirection() {
        STVentTrait trait = new STVentTrait(mockMachine, RelativeDirection.LEFT);
        assertEquals(RelativeDirection.LEFT, trait.getVentDirection());
    }

    @Test
    void testSetVentDirection() {
        STVentTrait trait = new STVentTrait(mockMachine, RelativeDirection.UP);
        trait.setVentDirection(RelativeDirection.RIGHT);
        assertEquals(RelativeDirection.RIGHT, trait.getVentDirection());
    }

    @Test
    void testSetVentDirectionToNull() {
        STVentTrait trait = new STVentTrait(mockMachine, RelativeDirection.UP);
        trait.setVentDirection(null);
        assertNull(trait.getVentDirection());
    }

    @Test
    void testGetFieldHolder() {
        STVentTrait trait = new STVentTrait(mockMachine, RelativeDirection.FRONT);
        ManagedFieldHolder holder = trait.getFieldHolder();
        assertNotNull(holder);
        assertSame(holder, trait.getFieldHolder());
    }

    @Test
    void testMachineReference() {
        STVentTrait trait = new STVentTrait(mockMachine, RelativeDirection.UP);
        assertSame(mockMachine, trait.getMachine());
    }

    @Test
    void testAllDirections() {
        for (var dir : RelativeDirection.values()) {
            STVentTrait trait = new STVentTrait(mockMachine, dir);
            assertEquals(dir, trait.getVentDirection());
        }
    }
}