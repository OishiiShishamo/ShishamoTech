package shishamo_tech.common.machine.ae2;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingHatchPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MESteamHatchPartMachine extends MEStockingHatchPartMachine {

    public MESteamHatchPartMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        setAutoPullTest(stack -> isSteam(stack));
        setAutoPull(true);
    }

    @Override
    public void setAutoPullTest(Predicate<GenericStack> test) {
        super.setAutoPullTest(stack -> isSteam(stack) && test.test(stack));
    }

    @Override
    protected NotifiableFluidTank createTank(int initialCapacity, int slots, Object... args) {
        return super.createTank(initialCapacity, slots, args)
                .setFilter(fluidStack -> fluidStack.getFluid().is(GTMaterials.Steam.getFluidTag()));
    }

    private static boolean isSteam(@Nullable GenericStack stack) {
        return stack != null && stack.what() instanceof AEFluidKey fluidKey
                && fluidKey.getFluid().is(GTMaterials.Steam.getFluidTag());
    }
}
