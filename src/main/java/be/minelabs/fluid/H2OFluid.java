package be.minelabs.fluid;

import be.minelabs.block.Blocks;
import be.minelabs.item.Items;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;

public abstract class H2OFluid extends AbstractFluid {
    @Override
    public Fluid getFlowing() {
        return Fluids.FLOWING_H2O;
    }

    @Override
    public Fluid getStill() {
        return Fluids.STILL_H2O;
    }

    @Override
    public Item getBucketItem() {
        return Items.ERLENMEYER_H2O;
    }

    protected BlockState toBlockState(FluidState fluidState) {
        // getBlockStateLevel converts the LEVEL_1_8 of the fluid state to the LEVEL_15 the fluid block uses
        return Blocks.H2O.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    public static class Flowing extends H2OFluid {
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

    public static class Still extends H2OFluid {
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
