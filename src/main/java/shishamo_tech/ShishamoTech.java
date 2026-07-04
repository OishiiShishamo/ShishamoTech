package shishamo_tech;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shishamo_tech.common.machine.electric.STElectricMachines;
import shishamo_tech.common.machine.steam.STSteamMachines;
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

        context.getModEventBus().addListener(this::onConfigLoad);
        context.getModEventBus().addListener(this::onConfigReload);
    }

    private void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        STRecipeTypes.init();
    }

    private void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        STSteamMachines.init();
        STElectricMachines.init();
        if (isModLoaded("ae2") && STConfig.isAE2Enabled()) {
            LOGGER.info("AE2 detected — registering Press-Free Inscriber multiblocks");
            shishamo_tech.common.machine.ae2.STAE2Machines.init();
        } else {
            LOGGER.info("AE2 not detected or disabled — skipping AE2 multiblocks");
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