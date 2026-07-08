package shishamo_tech.common.machine;

import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.Set;

class STInscriberFilterUtilTest {

    private static ResourceLocation id(String path) {
        return ResourceLocation.parse(path.contains(":") ? path : "minecraft:" + path);
    }

    @Test
    void testPrivateConstructor() {
        var constructor = STInscriberFilterUtil.class.getDeclaredConstructors()[0];
        assertFalse(constructor.canAccess(null));
    }

    @Test
    void testInputsConflictBothContainEachOther() {
        var a = Set.of(id("stone"), id("iron_ingot"));
        var b = Set.of(id("stone"), id("iron_ingot"), id("diamond"));
        assertTrue(STInscriberFilterUtil.inputsConflict(a, b));
    }

    @Test
    void testInputsConflictBothContainEachOtherReverse() {
        var a = Set.of(id("stone"), id("iron_ingot"), id("diamond"));
        var b = Set.of(id("stone"), id("iron_ingot"));
        assertTrue(STInscriberFilterUtil.inputsConflict(a, b));
    }

    @Test
    void testInputsConflictNoOverlap() {
        var a = Set.of(id("stone"));
        var b = Set.of(id("diamond"));
        assertFalse(STInscriberFilterUtil.inputsConflict(a, b));
    }

    @Test
    void testInputsConflictPartialOverlap() {
        var a = Set.of(id("stone"), id("iron_ingot"));
        var b = Set.of(id("stone"), id("diamond"));
        assertFalse(STInscriberFilterUtil.inputsConflict(a, b));
    }

    @Test
    void testInputsConflictBothEmpty() {
        assertTrue(STInscriberFilterUtil.inputsConflict(Set.of(), Set.of()));
    }

    @Test
    void testInputsConflictOneEmpty() {
        assertTrue(STInscriberFilterUtil.inputsConflict(Set.of(id("stone")), Set.of()));
    }

    @Test
    void testInputsConflictSameSingle() {
        assertTrue(STInscriberFilterUtil.inputsConflict(
                Set.of(id("stone")), Set.of(id("stone"))));
    }

    @Test
    void testInputsConflictBothEqual() {
        var a = Set.of(id("stone"), id("iron_ingot"));
        var b = Set.of(id("stone"), id("iron_ingot"));
        assertTrue(STInscriberFilterUtil.inputsConflict(a, b));
    }

    @Test
    void testInputsConflictDisjointMultiple() {
        var a = Set.of(id("stone"), id("iron_ingot"), id("gold_ingot"));
        var b = Set.of(id("diamond"), id("emerald"));
        assertFalse(STInscriberFilterUtil.inputsConflict(a, b));
    }
}