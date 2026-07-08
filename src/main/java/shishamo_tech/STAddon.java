package shishamo_tech;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import net.minecraft.data.recipes.FinishedRecipe;
import shishamo_tech.common.data.STRecipes;

import java.util.function.Consumer;

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

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        STRecipes.recipeAddition(provider);
    }
}
