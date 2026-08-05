package shishamo_tech.config;

import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shishamo_tech.STTestHelper;

import java.util.ArrayList;
import java.util.List;

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
        assertTrue(STConfig.enableBotanyIntegration);
        assertTrue(STConfig.enableMegaSteamVoidResourceMiner);
        assertTrue(STConfig.enableUltimateUniversalStorage);
        assertTrue(STConfig.enableCompressedSingleblockRecipes);
        assertTrue(STConfig.voidMinerExtraItems.isEmpty());
        assertTrue(STConfig.voidMinerExtraFluids.isEmpty());
        assertTrue(STConfig.voidMinerBlacklist.isEmpty());
    }

    @Test
    void testMachinesDefaultEnabledWhenSpecNotLoaded() {
        assertTrue(STConfig.isMachineEnabled("mega_steam_grinder"));
        assertTrue(STConfig.isMachineEnabled("compressed_macerator"));
        assertTrue(STConfig.isMachineEnabled("unknown_machine"));
        assertTrue(STConfig.isMegaSteamVoidResourceMinerEnabled());
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
        assertNotNull(STConfig.VOID_MINER_BLACKLIST);
        assertNotNull(STConfig.VOID_MINER_EXTRA_ITEMS);
        assertNotNull(STConfig.VOID_MINER_EXTRA_FLUIDS);
    }

    @Test
    void testRefreshWhenNotLoadedDoesNothing() {
        STConfig.parallelMultiplier = 999;
        STConfig.refresh();
        assertEquals(999, STConfig.parallelMultiplier);
    }

    @Test
    void testMachinesFullyEnabledByDefaultWhenSpecNotLoaded() {
        assertTrue(STConfig.isMachineFullyEnabled("compressed_macerator"));
        assertTrue(STConfig.isMachineFullyEnabled("press_free_inscriber_mv"));
        assertTrue(STConfig.isMachineFullyEnabled("green_house"));
        assertTrue(STConfig.isMachineFullyEnabled("mega_steam_void_resource_miner"));
        assertTrue(STConfig.isMachineFullyEnabled("ultimate_universal_storage"));
        assertTrue(STConfig.isMachineFullyEnabled("god_steam_boiler"));
        assertTrue(STConfig.isMachineFullyEnabled("hyper_primitive_blast_furnace"));
        assertTrue(STConfig.isMachineFullyEnabled("mega_steam_grinder"));
        assertTrue(STConfig.isMachineFullyEnabled("lcr_cluster"));
        assertTrue(STConfig.isMachineFullyEnabled("unknown_machine"));
    }

    @Test
    void testCheckMachineDisabledTooltipReturnsFalseWhenEnabled() {
        List<Component> tooltips = new ArrayList<>();
        assertFalse(STConfig.checkMachineDisabledTooltip("compressed_macerator", tooltips));
        assertFalse(STConfig.checkMachineDisabledTooltip("mega_steam_grinder", tooltips));
        assertTrue(tooltips.isEmpty());
    }
}