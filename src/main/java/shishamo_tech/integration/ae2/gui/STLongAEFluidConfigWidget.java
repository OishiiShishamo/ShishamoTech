package shishamo_tech.integration.ae2.gui;

import com.gregtechceu.gtceu.integration.ae2.gui.widget.AEFluidConfigWidget;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidList;

/**
 * A fluid config widget that allows configuring slot amounts up to
 * {@link Long#MAX_VALUE}.
 */
public class STLongAEFluidConfigWidget extends AEFluidConfigWidget {

    public STLongAEFluidConfigWidget(int x, int y, ExportOnlyAEFluidList list) {
        super(x, y, list);
        this.widgets.remove(this.amountSetWidget);
        this.removeWidget(this.amountSetWidget.getAmountText());
        this.amountSetWidget = new STLongAmountSetWidget(31, -50, this);
        this.addWidget(this.amountSetWidget);
        this.addWidget(this.amountSetWidget.getAmountText());
        this.amountSetWidget.setVisible(false);
        this.amountSetWidget.getAmountText().setVisible(false);
    }
}
