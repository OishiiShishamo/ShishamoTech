package shishamo_tech.data.recipe;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import shishamo_tech.common.recipe.STRecipeTypes;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * Generates Integrated Ore Processing recipes for every material that has a raw ore.
 * The programmed circuit number selects the GTCEu ore-processing chain (input = 1 raw ore).
 * Each route is built by chaining GTCEu's ACTUAL processing recipes for the corresponding
 * steps (Macerator / Ore Washer / Thermal Centrifuge / Centrifuge / Sifter / Chemical Bath /
 * Forge Hammer). The guaranteed main output follows the real chain yields for 1 raw ore,
 * so JEI always shows the true count: routes 1/2/3/5/6 guarantee oreMultiplier x2 Dust
 * (1 raw -> x2 crushed -> x2 dust), route 7 guarantees oreMultiplier x1 Dust (hammer chain),
 * and route 4 (sifter) guarantees no Dust at all -- only stone plus chanced gems.
 * The chanced byproducts approximate what those real recipes would yield. The washing /
 * chemical-bathing steps require the same fluids (Water or the ore's chemical bath fluid)
 * as the real recipes.
 *   1 = Macerate -> Ore Washer -> Thermal Centrifuge -> Macerate
 *   2 = Macerate -> Ore Washer -> Macerate -> Centrifuge
 *   3 = Macerate -> Macerate -> Centrifuge
 *   4 = Macerate -> Ore Washer -> Sifter            (only branch that can yield gems)
 *   5 = Macerate -> Chemical Bathing -> Macerate -> Centrifuge
 *   6 = Macerate -> Chemical Bathing -> Thermal Centrifuge -> Macerate
 *   7 = Forge Hammer -> Forge Hammer -> Simple Washer
 */
public final class IntegratedOreProcessingLoader {

    private IntegratedOreProcessingLoader() {}

    public static void init(Consumer<FinishedRecipe> consumer) {
        long lvEUt = GTValues.VA[GTValues.LV];
        for (Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if (!material.hasProperty(PropertyKey.ORE)) continue;
            ItemStack rawOre = ChemicalHelper.get(TagPrefix.rawOre, material);
            if (rawOre.isEmpty()) continue;
            ItemStack dust = ChemicalHelper.get(TagPrefix.dust, material);
            if (dust.isEmpty()) continue;

            OreProperty prop = material.getProperty(PropertyKey.ORE);
            String name = sanitize(material.getName());
            int bpm = Math.max(1, prop.getByProductMultiplier());
            // Guaranteed yields for 1 raw ore, matching GTCEu's real chain ratios:
            // macerating raw ore gives oreMultiplier x2 crushed, hammering gives x1,
            // and every downstream step (washer/bath/thermal/macerate/centrifuge) is 1:1.
            int oreMult = Math.max(1, prop.getOreMultiplier());
            ItemStack mainMacerate = dust.copyWithCount(oreMult * 2);
            ItemStack mainHammer = dust.copyWithCount(oreMult);

            Material bp0 = prop.getOreByProduct(0, material);
            Material bp1 = prop.getOreByProduct(1, material);
            Material bp2 = prop.getOreByProduct(2, material);
            Material bp3 = prop.getOreByProduct(3, material);

            ItemStack stone = ChemicalHelper.get(TagPrefix.dust, GTMaterials.Stone);
            ItemStack washerStone = stone.isEmpty() ? stone : stone.copyWithCount(oreMult * 2);
            FluidStack water = GTMaterials.Water.getFluid(1000);
            ObjectIntPair<Material> washedIn = prop.getWashedIn();
            FluidStack chem = washedIn.first().isNull() ? null : washedIn.first().getFluid(washedIn.secondInt());
            if (chem != null && chem.isEmpty()) chem = null;

            boolean isGem = material.hasProperty(PropertyKey.GEM);
            boolean highSift = material.hasFlag(MaterialFlags.HIGH_SIFTER_OUTPUT);

            // 1 - Macerate -> Ore Washer -> Thermal Centrifuge -> Macerate (guaranteed: dust x oreMult*2)
            route(consumer, name, rawOre, mainMacerate, 1, water, lvEUt, 160, b -> {
                chDust(b, bp0, bpm, 1400, 0);   // Macerate raw ore
                chDust(b, bp0, bpm, 3333, 0);   // Ore Washer
                stone(b, washerStone);          // Ore Washer (1 stone per crushed)
                chDust(b, bp1, bpm, 3333, 0);   // Thermal Centrifuge (purified)
                chDust(b, bp2, bpm, 1400, 0);   // Macerate (refined)
            });

            // 2 - Macerate -> Ore Washer -> Macerate -> Centrifuge (guaranteed: dust x oreMult*2)
            route(consumer, name, rawOre, mainMacerate, 2, water, lvEUt, 140, b -> {
                chDust(b, bp0, bpm, 1400, 0);   // Macerate raw ore
                chDust(b, bp0, bpm, 3333, 0);   // Ore Washer
                stone(b, washerStone);          // Ore Washer (1 stone per crushed)
                chDust(b, bp1, bpm, 1400, 0);   // Macerate (purified)
                chDust(b, bp1, bpm, 1111, 0);   // Centrifuge (pure dust)
            });

            // 3 - Macerate -> Macerate -> Centrifuge  (no fluid, no stone; guaranteed: dust x oreMult*2)
            route(consumer, name, rawOre, mainMacerate, 3, null, lvEUt, 90, b -> {
                chDust(b, bp0, bpm, 1400, 0);   // Macerate raw ore
                chDust(b, bp0, bpm, 1400, 0);   // Macerate (crushed -> impure dust)
                chDust(b, bp0, bpm, 1111, 0);   // Centrifuge (impure dust)
            });

            // 4 - Macerate -> Ore Washer -> Sifter (gem ores only: the sifter has NO
            // guaranteed output, so this route guarantees no dust -- JEI shows only stone + chanced gems).
            // Non-gem ores have no sifter recipe, so circuit 4 does nothing for them.
            if (isGem) {
            route(consumer, name, rawOre, null, 4, water, lvEUt, 130, b -> {
                chDust(b, bp0, bpm, 1400, 0);   // Macerate raw ore
                chDust(b, bp0, bpm, 3333, 0);   // Ore Washer
                stone(b, washerStone);          // Ore Washer (1 stone per crushed)
                int exq = highSift ? 500 : 300;
                int flw = highSift ? 1500 : 1000;
                int gem = highSift ? 5000 : 3500;
                int fla = highSift ? 2000 : 2500;
                int chi = highSift ? 3000 : 3500;
                chanced(b, ChemicalHelper.get(TagPrefix.gemExquisite, material), exq, 0);
                chanced(b, ChemicalHelper.get(TagPrefix.gemFlawless, material), flw, 0);
                chanced(b, ChemicalHelper.get(TagPrefix.gem, material), gem, 0);
                chanced(b, dust.copy(), highSift ? 2500 : 5000, 0);
                chanced(b, ChemicalHelper.get(TagPrefix.gemFlawed, material), fla, 0);
                chanced(b, ChemicalHelper.get(TagPrefix.gemChipped, material), chi, 0);
            });
            } // isGem

            // 5 - Macerate -> Chemical Bathing -> Macerate -> Centrifuge
            // 6 - Macerate -> Chemical Bathing -> Thermal Centrifuge -> Macerate
            // (skipped when the ore has no chemical-bath fluid: a fluid-less route
            // would hand out the bath byproducts for free)
            if (chem != null) {
            route(consumer, name, rawOre, mainMacerate, 5, chem, lvEUt, 150, b -> {
                chDust(b, bp0, bpm, 1400, 0);   // Macerate raw ore
                chDust(b, bp3, bpm, 7000, 0);   // Chemical Bath (washedIn byproduct)
                chDust(b, GTMaterials.Stone, bpm, 4000, 0); // Chemical Bath stone
                chDust(b, bp1, bpm, 1400, 0);   // Macerate (purified)
                chDust(b, bp1, bpm, 1111, 0);   // Centrifuge (pure dust)
            });
            } // chem != null

            if (chem != null) {
            // 6 - Macerate -> Chemical Bathing -> Thermal Centrifuge -> Macerate
            route(consumer, name, rawOre, mainMacerate, 6, chem, lvEUt, 170, b -> {
                chDust(b, bp0, bpm, 1400, 0);   // Macerate raw ore
                chDust(b, bp3, bpm, 7000, 0);   // Chemical Bath (washedIn byproduct)
                chDust(b, GTMaterials.Stone, bpm, 4000, 0); // Chemical Bath stone (chanced, like route 5)
                chDust(b, bp1, bpm, 3333, 0);   // Thermal Centrifuge (purified)
                chDust(b, bp2, bpm, 1400, 0);   // Macerate (refined)
            });
            } // chem != null

            // 7 - Forge Hammer -> Forge Hammer -> Simple Washer (guaranteed: dust x oreMult;
            // hammering raw ore gives x1 crushed, and the washer step yields no stone/byproducts)
            route(consumer, name, rawOre, mainHammer, 7, water, lvEUt, 100, b -> {
            });
        }
    }

    private static void route(Consumer<FinishedRecipe> consumer, String materialName, ItemStack rawOre,
                              @org.jetbrains.annotations.Nullable ItemStack mainOutput,
                              int circuit, FluidStack fluid, long eut, int duration, Consumer<GTRecipeBuilder> configure) {
        GTRecipeBuilder b = STRecipeTypes.INTEGRATED_ORE_PROCESSING.recipeBuilder("iop_route" + circuit + "_" + materialName)
                .inputItems(rawOre.copy())
                .circuitMeta(circuit)
                .EUt(eut)
                .duration(duration);
        // Guaranteed main output (null = none, e.g. the gem sifter route); JEI shows exactly this count.
        if (mainOutput != null && !mainOutput.isEmpty()) b.outputItems(mainOutput.copy());
        if (fluid != null) b.inputFluids(fluid);
        configure.accept(b);
        b.save(consumer);
    }

    private static void chDust(GTRecipeBuilder b, Material m, int count, int chance, int boost) {
        if (m == null || m.isNull()) return;
        ItemStack s = ChemicalHelper.get(TagPrefix.dust, m, count);
        if (!s.isEmpty()) b.chancedOutput(s, chance, boost);
    }

    private static void stone(GTRecipeBuilder b, ItemStack stone) {
        if (!stone.isEmpty()) b.outputItems(stone.copy());
    }

    private static void chanced(GTRecipeBuilder b, ItemStack s, int chance, int boost) {
        if (!s.isEmpty()) b.chancedOutput(s, chance, boost);
    }

    private static String sanitize(String name) {
        int slash = name.indexOf(':');
        if (slash >= 0) name = name.substring(slash + 1);
        return name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_");
    }
}
