package shishamo_tech.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shishamo_tech.STTestHelper;

import static org.junit.jupiter.api.Assertions.*;

class STConfigTest {

    @BeforeEach
    void setUp() {
        STTestHelper.resetConfigDefaults();
    }

    @AfterEach
    void tearDown() {
        STTestHelper.resetConfigDefaults();
    }

    @Test
    void testDefaultStaticValues() {
        assertEquals(64, STConfig.parallelMultiplier);
        assertTrue(STConfig.enableSteamMachines);
        assertTrue(STConfig.enableElectricMachines);
        assertTrue(STConfig.enableAE2Integration);
    }

    @Test
    void testSpecIsBuilt() {
        assertNotNull(STConfig.SPEC);
    }

    @Test
    void testSpecValuesAreConfigured() {
        assertNotNull(STConfig.PARALLEL_MULTIPLIER);
        assertNotNull(STConfig.ENABLE_STEAM_MACHINES);
        assertNotNull(STConfig.ENABLE_ELECTRIC_MACHINES);
        assertNotNull(STConfig.ENABLE_AE2_INTEGRATION);
    }

    @Test
    void testRefreshWhenNotLoadedDoesNothing() {
        STConfig.parallelMultiplier = 999;
        STConfig.refresh();
        assertEquals(999, STConfig.parallelMultiplier);
    }
}