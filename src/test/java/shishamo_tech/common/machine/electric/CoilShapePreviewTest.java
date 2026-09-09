package shishamo_tech.common.machine.electric;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoilShapePreviewTest {

    @Test
    void testGroupAislesChunksLayerMajorOrder() {
        String[] flat = {"A1", "A2", "B1", "B2", "C1", "C2"};
        String[][] layers = STCoilParallelMultiblockMachine.groupAisles(flat, 2);
        assertEquals(3, layers.length);
        assertArrayEquals(new String[]{"A1", "A2"}, layers[0]);
        assertArrayEquals(new String[]{"B1", "B2"}, layers[1]);
        assertArrayEquals(new String[]{"C1", "C2"}, layers[2]);
    }

    @Test
    void testGroupAislesRejectsRaggedInput() {
        String[] flat = {"A1", "A2", "B1"};
        assertThrows(IllegalArgumentException.class,
                () -> STCoilParallelMultiblockMachine.groupAisles(flat, 2));
    }

    @Test
    void testGroupAislesRejectsNonPositiveRowsPerLayer() {
        String[] flat = {"A1", "A2"};
        assertThrows(IllegalArgumentException.class,
                () -> STCoilParallelMultiblockMachine.groupAisles(flat, 0));
        assertThrows(IllegalArgumentException.class,
                () -> STCoilParallelMultiblockMachine.groupAisles(flat, -1));
    }

    @Test
    void testGroupAislesRejectsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> STCoilParallelMultiblockMachine.groupAisles(new String[0], 13));
    }
}
