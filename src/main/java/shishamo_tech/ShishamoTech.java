package shishamo_tech;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shishamo_tech.common.data.STMultiMachines;
import shishamo_tech.common.machine.electric.STCompressedMachines;
import shishamo_tech.common.recipe.STRecipeTypes;
import shishamo_tech.config.STConfig;

@Mod(ShishamoTech.MOD_ID)
public class ShishamoTech {
    public static final String MOD_ID = "shishamo_tech";
    public static final String MOD_NAME = "ShishamoTech";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public ShishamoTech(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, STConfig.SPEC);
        STRegistration.REGISTRATE.registerEventListeners(context.getModEventBus());
        STRegistration.init();

        context.getModEventBus().addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        context.getModEventBus().addGenericListener(MachineDefinition.class, this::registerMachines);

        context.getModEventBus().addListener(this::onGatherData);
        context.getModEventBus().addListener(this::onConfigLoad);
        context.getModEventBus().addListener(this::onConfigReload);
    }

    private void onGatherData(GatherDataEvent event) {
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.parallel", "§7Parallel: §a%d");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.parallel_count", "§7Parallel: §a%d");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.steam_output", "§7Steam Output: §a%d mb/t");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.water_consumption", "§7Water Consumption: §a%d mb/t");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.coil_tier", "§7Coil: §a%s");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.inscriber.circuit", "§7Use Integrated Circuit to select output variant when multiple recipes share the same input");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.inscriber.jei_circuit", "§7Circuit: §a%d");
        STRegistration.REGISTRATE.addRawLang("itemGroup.shishamo_tech", "ShishamoTech");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.recipe.category.st_inscriber", "Press-Free Inscriber");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.compressed.8x_speed", "§a8x §7processing speed");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.recipe.category.green_house", "Green House");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.seed_slots", "§7Seeds: §a%d§7/§a%d");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.gui.seed_tray", "Seed Tray");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.void_miner.tooltip", "§7Generates random resources from the Void");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.void_miner.resources", "§7Available Resources: §a%d");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.recipe.category.void_resource_mining", "Void Resource Mining");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.ultimate_universal_storage", "Ultimate Universal Storage");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.ultimate_universal_storage.tooltip", "§7Ultimate storage with §a8 §7sections. Each section holds either items or fluids with §aunlimited §7(BigInteger) amounts, and the controller acts as §aAE2 native storage§7 on the ME network.");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.ultimate_universal_storage.pipe_tip", "§7Connect item and fluid pipes directly to the controller, or attach it to the ME network. Form the §a3x3x3 §7multiblock with Steel Solid Casings first.");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.gui.ultimate.item_count", "§7Item Count");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.gui.ultimate.fluid_amount", "§7Fluid Amount");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.me_long.slot_capacity", "§7Configurable slot amounts up to §aLong.MAX_VALUE");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.me_long.buffer_capacity", "§7Internal buffer up to §aLong.MAX_VALUE");
        STRegistration.REGISTRATE.addRawLang("shishamo_tech.machine.me_dual.item_fluid", "§7Accepts both items and fluids in a single block");
    }

    private void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        STRecipeTypes.init();
    }

    private void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        STMultiMachines.steamInit();
        STMultiMachines.primitiveInit();
        STMultiMachines.electricInit();
        STCompressedMachines.init();
        if (isModLoaded("ae2")) {
            LOGGER.info("AE2 detected — registering Press-Free Inscriber multiblocks");
            STMultiMachines.AEInit();
        } else {
            LOGGER.info("AE2 not detected — skipping AE2 multiblocks");
        }
        STMultiMachines.botanyInit();
        if (isModLoaded("ae2")) {
            STMultiMachines.storageInit();
        }
        if (isModLoaded("botanypots")) {
            LOGGER.info("Botany Pots detected — Green House will process crop recipes");
        } else {
            LOGGER.info("Botany Pots not detected — Green House will be non-functional (same as config-disabled behavior)");
        }
    }

    private void onConfigLoad(ModConfigEvent.Loading event) {
        STConfig.refresh();
    }

    private void onConfigReload(ModConfigEvent.Reloading event) {
        STConfig.refresh();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.tryParse(MOD_ID + ":" + path);
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get() != null && ModList.get().isLoaded(modId);
    }
}