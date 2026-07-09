package shishamo_tech.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static shishamo_tech.common.machine.electric.STCompressedMachines.*;

public class CompressedRecipeLoader {

    public static void init(Consumer<FinishedRecipe> provider) {
        for (int tier : new int[]{GTValues.LV, GTValues.MV, GTValues.HV, GTValues.EV, GTValues.IV,
                GTValues.LuV, GTValues.ZPM, GTValues.UV}) {
            addCompressedCrafting(provider, "compressed_macerator", tier,
                    GTMachines.MACERATOR[tier], COMPRESSED_MACERATOR[tier]);
            addCompressedCrafting(provider, "compressed_furnace", tier,
                    GTMachines.ELECTRIC_FURNACE[tier], COMPRESSED_FURNACE[tier]);
            addCompressedCrafting(provider, "compressed_alloy_smelter", tier,
                    GTMachines.ALLOY_SMELTER[tier], COMPRESSED_ALLOY_SMELTER[tier]);
            addCompressedCrafting(provider, "compressed_arc_furnace", tier,
                    GTMachines.ARC_FURNACE[tier], COMPRESSED_ARC_FURNACE[tier]);
            addCompressedCrafting(provider, "compressed_assembler", tier,
                    GTMachines.ASSEMBLER[tier], COMPRESSED_ASSEMBLER[tier]);
            addCompressedCrafting(provider, "compressed_autoclave", tier,
                    GTMachines.AUTOCLAVE[tier], COMPRESSED_AUTOCLAVE[tier]);
            addCompressedCrafting(provider, "compressed_bender", tier,
                    GTMachines.BENDER[tier], COMPRESSED_BENDER[tier]);
            addCompressedCrafting(provider, "compressed_canner", tier,
                    GTMachines.CANNER[tier], COMPRESSED_CANNER[tier]);
            addCompressedCrafting(provider, "compressed_centrifuge", tier,
                    GTMachines.CENTRIFUGE[tier], COMPRESSED_CENTRIFUGE[tier]);
            addCompressedCrafting(provider, "compressed_chemical_bath", tier,
                    GTMachines.CHEMICAL_BATH[tier], COMPRESSED_CHEMICAL_BATH[tier]);
            addCompressedCrafting(provider, "compressed_chemical_reactor", tier,
                    GTMachines.CHEMICAL_REACTOR[tier], COMPRESSED_CHEMICAL_REACTOR[tier]);
            addCompressedCrafting(provider, "compressed_compressor", tier,
                    GTMachines.COMPRESSOR[tier], COMPRESSED_COMPRESSOR[tier]);
            addCompressedCrafting(provider, "compressed_cutter_saw", tier,
                    GTMachines.CUTTER[tier], COMPRESSED_CUTTER[tier]);
            addCompressedCrafting(provider, "compressed_distillery", tier,
                    GTMachines.DISTILLERY[tier], COMPRESSED_DISTILLERY[tier]);
            addCompressedCrafting(provider, "compressed_electrolyzer", tier,
                    GTMachines.ELECTROLYZER[tier], COMPRESSED_ELECTROLYZER[tier]);
            addCompressedCrafting(provider, "compressed_electromagnetic_separator", tier,
                    GTMachines.ELECTROMAGNETIC_SEPARATOR[tier], COMPRESSED_ELECTROMAGNETIC_SEPARATOR[tier]);
            addCompressedCrafting(provider, "compressed_extractor", tier,
                    GTMachines.EXTRACTOR[tier], COMPRESSED_EXTRACTOR[tier]);
            addCompressedCrafting(provider, "compressed_extruder", tier,
                    GTMachines.EXTRUDER[tier], COMPRESSED_EXTRUDER[tier]);
            addCompressedCrafting(provider, "compressed_fermenter", tier,
                    GTMachines.FERMENTER[tier], COMPRESSED_FERMENTING[tier]);
            addCompressedCrafting(provider, "compressed_fluid_heater", tier,
                    GTMachines.FLUID_HEATER[tier], COMPRESSED_FLUID_HEATER[tier]);
            addCompressedCrafting(provider, "compressed_fluid_solidifier", tier,
                    GTMachines.FLUID_SOLIDIFIER[tier], COMPRESSED_FLUID_SOLIDFICATION[tier]);
            addCompressedCrafting(provider, "compressed_forge_hammer", tier,
                    GTMachines.FORGE_HAMMER[tier], COMPRESSED_FORGE_HAMMER[tier]);
            addCompressedCrafting(provider, "compressed_forming_press", tier,
                    GTMachines.FORMING_PRESS[tier], COMPRESSED_FORMING_PRESS[tier]);
            addCompressedCrafting(provider, "compressed_lathe", tier,
                    GTMachines.LATHE[tier], COMPRESSED_LATHE[tier]);
            addCompressedCrafting(provider, "compressed_mixer", tier,
                    GTMachines.MIXER[tier], COMPRESSED_MIXER[tier]);
            addCompressedCrafting(provider, "compressed_ore_washer", tier,
                    GTMachines.ORE_WASHER[tier], COMPRESSED_ORE_WASHER[tier]);
            addCompressedCrafting(provider, "compressed_packer", tier,
                    GTMachines.PACKER[tier], COMPRESSED_PACKER[tier]);
            addCompressedCrafting(provider, "compressed_polarizer", tier,
                    GTMachines.POLARIZER[tier], COMPRESSED_POLARIZER[tier]);
            addCompressedCrafting(provider, "compressed_laser_engraver", tier,
                    GTMachines.LASER_ENGRAVER[tier], COMPRESSED_LASER_ENGRAVER[tier]);
            addCompressedCrafting(provider, "compressed_sifter", tier,
                    GTMachines.SIFTER[tier], COMPRESSED_SIFTER[tier]);
            addCompressedCrafting(provider, "compressed_thermal_centrifuge", tier,
                    GTMachines.THERMAL_CENTRIFUGE[tier], COMPRESSED_THERMAL_CENTRIFUGE[tier]);
            addCompressedCrafting(provider, "compressed_wiremill", tier,
                    GTMachines.WIREMILL[tier], COMPRESSED_WIREMILL[tier]);
            addCompressedCrafting(provider, "compressed_circuit_assembler", tier,
                    GTMachines.CIRCUIT_ASSEMBLER[tier], COMPRESSED_CIRCUIT_ASSEMBLER[tier]);
            addCompressedCrafting(provider, "compressed_rock_breaker", tier,
                    GTMachines.ROCK_CRUSHER[tier], COMPRESSED_ROCK_BREAKER[tier]);
            addCompressedCrafting(provider, "compressed_scanner", tier,
                    GTMachines.SCANNER[tier], COMPRESSED_SCANNER[tier]);
        }
    }

    private static void addCompressedCrafting(Consumer<FinishedRecipe> provider,
                                              String baseName, int tier,
                                              MachineDefinition baseMachine,
                                              MachineDefinition compressedMachine) {
        if (baseMachine == null || compressedMachine == null) return;
        VanillaRecipeHelper.addShapedRecipe(provider,
                baseName + "_" + GTValues.VN[tier].toLowerCase(),
                compressedMachine.asStack(),
                "XXX",
                "XXX",
                "XXX",
                'X', baseMachine.asStack());
    }
}
