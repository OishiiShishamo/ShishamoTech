package shishamo_tech;

import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public final class STRegistration {
    private STRegistration() {}

    public static final GTRegistrate REGISTRATE = GTRegistrate.create(ShishamoTech.MOD_ID);

    public static void init() {
        var tab = REGISTRATE.defaultCreativeTab("machine", builder -> builder
                .title(Component.translatable("itemGroup.shishamo_tech"))
                .icon(() -> new ItemStack(net.minecraft.world.item.Items.BRICKS))
                .displayItems((parameters, output) -> {
                    var creativeTab = REGISTRATE.creativeModeTab();
                    for (var entry : REGISTRATE.getAll(Registries.BLOCK)) {
                        if (!REGISTRATE.isInCreativeTab(entry, creativeTab)) continue;
                        var block = (Block) entry.get();
                        var item = block.asItem();
                        if (item == net.minecraft.world.item.Items.AIR) continue;
                        if (item instanceof IComponentItem componentItem) {
                            var items = NonNullList.<ItemStack>create();
                            componentItem.fillItemCategory((CreativeModeTab) creativeTab.get(), items);
                            items.forEach(output::accept);
                        } else {
                            output.accept(new ItemStack(item));
                        }
                    }
                })).register();
        REGISTRATE.creativeModeTab(tab);
    }
}
