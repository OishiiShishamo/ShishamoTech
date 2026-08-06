package shishamo_tech.common.cover;

import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.client.renderer.cover.ICoverRenderer;
import com.gregtechceu.gtceu.client.renderer.cover.SimpleCoverRenderer;
import com.gregtechceu.gtceu.common.item.CoverPlaceBehavior;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import shishamo_tech.ShishamoTech;
import shishamo_tech.STRegistration;
import shishamo_tech.config.STConfig;

import static shishamo_tech.ShishamoTech.isModLoaded;

public final class STCovers {

    private STCovers() {}

    public static CoverDefinition ME_STEAM_SUPPLY;
    public static ItemEntry<ComponentItem> ME_STEAM_SUPPLY_ITEM;
    @Nullable
    public static CoverDefinition ME_INDUCTION;
    @Nullable
    public static ItemEntry<ComponentItem> ME_INDUCTION_ITEM;

    /**
     * Called from {@code IGTAddon#registerCovers()}, while {@link GTRegistries#COVERS}
     * is unfrozen. Registers cover definitions and their items. The induction cover is
     * only registered while Applied Flux is present.
     */
    public static void init() {
        ME_STEAM_SUPPLY = register("me_steam_supply", MESteamSupplyCover::new);
        ME_STEAM_SUPPLY_ITEM = registerItem("me_steam_supply_cover", ME_STEAM_SUPPLY, "ME Steam Supply Cover",
                lines -> {
                    lines.add(Component.translatable("shishamo_tech.cover.me_steam_supply.tooltip"));
                    STConfig.checkCoverDisabledTooltip(STConfig.isMESteamSupplyCoverEnabled(), lines);
                });

        if (ShishamoTech.isModLoaded("appflux")) {
            ShishamoTech.LOGGER.info("Applied Flux detected — registering ME Induction Cover");
            ME_INDUCTION = register("me_induction", MEInductionCover::new);
            ME_INDUCTION_ITEM = registerItem("me_induction_cover", ME_INDUCTION, "ME Induction Cover",
                    lines -> {
                        lines.add(Component.translatable("shishamo_tech.cover.me_induction.tooltip"));
                        STConfig.checkCoverDisabledTooltip(STConfig.isMEInductionCoverEnabled(), lines);
                    });
        } else {
            ShishamoTech.LOGGER.info("Applied Flux not detected — skipping ME Induction Cover");
        }
    }

    private static CoverDefinition register(String id, CoverDefinition.CoverBehaviourProvider behaviorCreator) {
        return register(id, behaviorCreator, () -> () -> new SimpleCoverRenderer(ShishamoTech.id("block/cover/" + id)));
    }

    private static CoverDefinition register(String id, CoverDefinition.CoverBehaviourProvider behaviorCreator,
                                            Supplier<Supplier<ICoverRenderer>> coverRenderer) {
        return register(ShishamoTech.id(id), behaviorCreator, coverRenderer);
    }

    private static CoverDefinition register(ResourceLocation id, CoverDefinition.CoverBehaviourProvider behaviorCreator,
                                            Supplier<Supplier<ICoverRenderer>> coverRenderer) {
        var definition = new CoverDefinition(id, behaviorCreator, coverRenderer);
        GTRegistries.COVERS.register(definition.getId(), definition);
        return definition;
    }

    private static ItemEntry<ComponentItem> registerItem(String name, CoverDefinition definition, String displayName,
                                                java.util.function.Consumer<java.util.List<Component>> tooltipBuilder) {
        return STRegistration.REGISTRATE.item(name, ComponentItem::create)
                .lang(displayName)
                .onRegister(attach(new CoverPlaceBehavior(definition)))
                .onRegister(attach(new TooltipBehavior(tooltipBuilder)))
                .register();
    }

    private static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent... components) {
        return item -> item.attachComponents(components);
    }
}
