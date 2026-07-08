package shishamo_tech;

import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShishamoTechTest {

    @Test
    void testModIdConstant() {
        assertEquals("shishamo_tech", ShishamoTech.MOD_ID);
    }

    @Test
    void testModNameConstant() {
        assertEquals("ShishamoTech", ShishamoTech.MOD_NAME);
    }

    @Test
    void testLoggerNotNull() {
        assertNotNull(ShishamoTech.LOGGER);
    }

    @Test
    void testIdWithSimplePath() {
        ResourceLocation id = ShishamoTech.id("test");
        assertEquals("shishamo_tech:test", id.toString());
    }

    @Test
    void testIdWithNestedPath() {
        ResourceLocation id = ShishamoTech.id("path/to/something");
        assertEquals("shishamo_tech:path/to/something", id.toString());
    }

    @Test
    void testIdWithEmptyPath() {
        ResourceLocation id = ShishamoTech.id("");
        assertNotNull(id);
    }

    @Test
    void testIsModLoadedReturnsFalseWhenModListNull() {
        assertFalse(ShishamoTech.isModLoaded("ae2"));
    }
}