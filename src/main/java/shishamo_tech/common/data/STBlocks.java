package shishamo_tech.common.data;

import com.gregtechceu.gtceu.common.data.models.GTModels;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import shishamo_tech.ShishamoTech;
import shishamo_tech.STRegistration;

public class STBlocks {
    public static final BlockEntry<Block> CASING_NOUF_GENERAL = createCasingBlock(
            "non_omnipotent_trancendent_industrial_infinity_tesseract_ultimate_overpower_general_casing",
            ShishamoTech.id("block/casings/nouf/general"));
    public static final BlockEntry<Block> CASING_NOUF_MAGICAL = createCasingBlock(
            "non_omnipotent_trancendent_industrial_infinity_tesseract_ultimate_overpower_general_starmatter_whirlpool_magical_casing",
            ShishamoTech.id("block/casings/nouf/magical"));
    public static final BlockEntry<Block> CASING_NOUF_WAVE = createCasingBlock(
            "non_omnipotent_trancendent_industrial_infinity_tesseract_ultimate_overpower_general_psychotic_wave_forging_casing",
            ShishamoTech.id("block/casings/nouf/wave"));
    public static final BlockEntry<Block> CASING_SOLID_MIRACLE_FUMETSU = createCasingBlock(
            "miracle_theoretical_fumetsu_casing",
            ShishamoTech.id("block/casings/solid/miracle_fumetsu"));
    public static final BlockEntry<Block> CASING_SOLID_MIRACLE_METEOR = createCasingBlock(
            "miracle_theoretical_meteor_casing",
            ShishamoTech.id("block/casings/solid/miracle_meteor"));
    public static final BlockEntry<Block> CASING_SOLID_SPACETIME = createCasingBlock(
            "spacetime_stabilization_casing",
            ShishamoTech.id("block/casings/solid/spacetime"));

    private static BlockEntry<Block> createCasingBlock(String name, net.minecraft.resources.ResourceLocation texture) {
        return STRegistration.REGISTRATE
                .block(name, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::solid)
                .exBlockstate(GTModels.cubeAllModel(texture))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
    }
}
