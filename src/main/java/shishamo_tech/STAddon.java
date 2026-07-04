package shishamo_tech;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

@GTAddon
public class STAddon implements IGTAddon {
    @Override
    public GTRegistrate getRegistrate() {
        return STRegistration.REGISTRATE;
    }

    @Override
    public void initializeAddon() {}

    @Override
    public String addonModId() {
        return ShishamoTech.MOD_ID;
    }
}
