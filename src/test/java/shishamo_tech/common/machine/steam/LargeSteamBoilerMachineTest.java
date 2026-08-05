package shishamo_tech.common.machine.steam;

import org.junit.jupiter.api.Test;
import shishamo_tech.common.recipe.STOverclockingLogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LargeSteamBoilerMachineTest {

    private static final int[] COIL_MULTIPLIER_BY_TIER = {1, 2, 2, 4, 4, 8, 8, 16};

    @Test
    void testSteamOutputMultiplierMatchesCoilMachineBonus() {
        for (int coil = 0; coil < COIL_MULTIPLIER_BY_TIER.length; coil++) {
            int expected = STOverclockingLogic.getCoilBonus(coil);
            assertEquals(expected,
                    LargeSteamBoilerMachine.getSteamOutputPerTick(coil)
                            / LargeSteamBoilerMachine.STEAM_OUTPUT_PER_TICK,
                    "Multiplier mismatch at coil tier " + coil);
        }
    }

    @Test
    void testSteamOutputMultipliesBaseOutputByCoilTier() {
        for (int coil = 0; coil < COIL_MULTIPLIER_BY_TIER.length; coil++) {
            long expected = (long) LargeSteamBoilerMachine.STEAM_OUTPUT_PER_TICK
                    * COIL_MULTIPLIER_BY_TIER[coil];
            assertEquals(expected,
                    LargeSteamBoilerMachine.getSteamOutputPerTick(coil),
                    "Output mismatch at coil tier " + coil);
        }
    }

    @Test
    void testCoilTier0OutputIsBaseOutput() {
        assertEquals(LargeSteamBoilerMachine.STEAM_OUTPUT_PER_TICK,
                LargeSteamBoilerMachine.getSteamOutputPerTick(0));
    }

    @Test
    void testCoilTier7OutputIsSixteenFold() {
        assertEquals((long) LargeSteamBoilerMachine.STEAM_OUTPUT_PER_TICK * 16,
                LargeSteamBoilerMachine.getSteamOutputPerTick(7));
    }

    @Test
    void testWaterConsumptionEqualsBaseOutputRegardlessOfCoil() {
        int waterRequired = LargeSteamBoilerMachine.STEAM_OUTPUT_PER_TICK;
        for (int coil = 0; coil < COIL_MULTIPLIER_BY_TIER.length; coil++) {
            long steamOutput = LargeSteamBoilerMachine.getSteamOutputPerTick(coil);
            assertEquals(LargeSteamBoilerMachine.STEAM_OUTPUT_PER_TICK, waterRequired,
                    "Water consumption must not change with coil tier " + coil);
            assertTrue(steamOutput >= waterRequired,
                    "Steam output must not drop below water consumption at coil tier " + coil);
        }
    }
}
