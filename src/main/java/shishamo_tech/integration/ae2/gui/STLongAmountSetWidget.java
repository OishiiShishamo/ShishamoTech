package shishamo_tech.integration.ae2.gui;

import com.gregtechceu.gtceu.integration.ae2.gui.widget.AmountSetWidget;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.ConfigWidget;

import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;

/**
 * An {@link AmountSetWidget} whose amount text field accepts values up to
 * {@link Long#MAX_VALUE} instead of being capped at {@link Integer#MAX_VALUE}.
 */
public class STLongAmountSetWidget extends AmountSetWidget {

    private final TextFieldWidget amountText;

    public STLongAmountSetWidget(int x, int y, ConfigWidget widget) {
        super(x, y, widget);
        this.amountText = new TextFieldWidget(x + 3, y + 12, 65, 13, this::getAmountStr, this::setNewAmount)
                .setNumbersOnly(0L, Long.MAX_VALUE)
                .setMaxStringLength(19);
    }

    @Override
    public TextFieldWidget getAmountText() {
        return this.amountText;
    }
}
