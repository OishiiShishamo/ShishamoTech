package shishamo_tech.common.machine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

/**
 * MachineTrait for vent direction configuration on steam machines.
 *
 * <p>Steam machines require venting. This trait tracks which direction
 * the vent faces relative to the machine's front facing.
 *
 * <p>When structure is formed, the vent position is validated.
 * Vent blocks in the structure must be placed on the side indicated by this trait.
 */
public class STVentTrait extends MachineTrait {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            STVentTrait.class);

    private RelativeDirection ventDirection;

    public STVentTrait(MetaMachine machine, RelativeDirection ventDirection) {
        super(machine);
        this.ventDirection = ventDirection;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public RelativeDirection getVentDirection() {
        return ventDirection;
    }

    public void setVentDirection(RelativeDirection direction) {
        this.ventDirection = direction;
    }
}
