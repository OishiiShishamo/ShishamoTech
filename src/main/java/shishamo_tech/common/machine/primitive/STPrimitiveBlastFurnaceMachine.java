package shishamo_tech.common.machine.primitive;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.PrimitiveBlastFurnaceMachine;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import shishamo_tech.config.STConfig;

/**
 * Mega Primitive Blast Furnace.
 *
 * <p>A much stronger version of the {@link PrimitiveBlastFurnaceMachine}: it processes
 * {@code PRIMITIVE_BLAST_FURNACE_RECIPES} in parallel (base {@code 4 x parallelMultiplier}),
 * has a much larger firebrick structure and provides 16 internal input/output slots as well
 * as item hatch support for automation.
 */
public class STPrimitiveBlastFurnaceMachine extends PrimitiveBlastFurnaceMachine {

    public static final int INPUT_SLOTS = 16;
    public static final int OUTPUT_SLOTS = 16;
    public static final int BASE_PARALLEL = 4;

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STPrimitiveBlastFurnaceMachine.class, PrimitiveBlastFurnaceMachine.MANAGED_FIELD_HOLDER);

    public STPrimitiveBlastFurnaceMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public int getParallelCount() {
        return BASE_PARALLEL * STConfig.parallelMultiplier;
    }

    public static int getDisplayParallelCount() {
        return BASE_PARALLEL * STConfig.parallelMultiplier;
    }

    @Override
    protected NotifiableItemStackHandler createImportItemHandler(Object... args) {
        return new NotifiableItemStackHandler(this, INPUT_SLOTS, IO.IN, IO.NONE);
    }

    @Override
    protected NotifiableItemStackHandler createExportItemHandler(Object... args) {
        return new NotifiableItemStackHandler(this, OUTPUT_SLOTS, IO.OUT, IO.NONE);
    }

    /**
     * Recipe modifier that parallelizes Primitive Blast Furnace recipes up to the machine's
     * configured parallel count. No EU / steam is involved, so only item inputs limit parallelism.
     */
    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof STPrimitiveBlastFurnaceMachine pbf)) {
            return ModifierFunction.IDENTITY;
        }
        if (!STConfig.isSteamEnabled() || !STConfig.isMachineEnabled(machine)) {
            return ModifierFunction.NULL;
        }
        int actualParallel = ParallelLogic.getParallelAmountWithoutEU(machine, recipe, pbf.getParallelCount());
        if (actualParallel <= 0) return ModifierFunction.NULL;
        ContentModifier modifier = ContentModifier.multiplier(actualParallel);
        return ModifierFunction.builder()
                .parallels(actualParallel)
                .inputModifier(modifier)
                .outputModifier(modifier)
                .build();
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        var ui = new ModularUI(176, 216, this, entityPlayer)
                .background(GuiTextures.PRIMITIVE_BACKGROUND)
                .widget(new LabelWidget(5, 5, getBlockState().getBlock().getDescriptionId()));

        for (int i = 0; i < INPUT_SLOTS; i++) {
            int row = i / 4;
            int col = i % 4;
            ui.widget(new SlotWidget(importItems.storage, i, 7 + col * 18, 18 + row * 18, true, true)
                    .setBackgroundTexture(new GuiTextureGroup(GuiTextures.PRIMITIVE_SLOT,
                            GuiTextures.PRIMITIVE_FURNACE_OVERLAY)));
        }
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            int row = i / 4;
            int col = i % 4;
            ui.widget(new SlotWidget(exportItems.storage, i, 97 + col * 18, 18 + row * 18, true, false)
                    .setBackgroundTexture(new GuiTextureGroup(GuiTextures.PRIMITIVE_SLOT,
                            GuiTextures.PRIMITIVE_DUST_OVERLAY)));
        }

        ui.widget(new ProgressWidget(recipeLogic::getProgressPercent, 79, 54, 20, 15,
                GuiTextures.PRIMITIVE_BLAST_FURNACE_PROGRESS_BAR));
        ui.widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(),
                GuiTextures.PRIMITIVE_SLOT, 7, 134, true));
        return ui;
    }
}
