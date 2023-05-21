package be.minelabs.fluid;

import be.minelabs.block.Blocks;
import be.minelabs.item.Items;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;

public abstract class CCl4Fluid extends AbstractFluid {
    @Override
    public Fluid getFlowing() {
        return Fluids.FLOWING_CCl4;
    }

    @Override
    public Fluid getStill() {
        return Fluids.STILL_CCl4;
    }

    public Item getBucketItem() {
        return Items.ERLENMEYER_CCL4;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        // getBlockStateLevel converts the LEVEL_1_8 of the fluid state to the LEVEL_15 the fluid block uses
        return Blocks.CCL4.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    public static class Flowing extends CCl4Fluid {
        @Override
        protected void appendProperties(net.minecraft.state.StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends CCl4Fluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 2;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
