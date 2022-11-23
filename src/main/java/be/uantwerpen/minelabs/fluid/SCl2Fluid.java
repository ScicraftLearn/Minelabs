package be.uantwerpen.minelabs.fluid;

import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.item.Items;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;

public abstract class SCl2Fluid extends AbstractFluid {
    @Override
    public Fluid getFlowing() {
        return Fluids.FLOWING_SCl2;
    }

    @Override
    public Fluid getStill() {
        return Fluids.STILL_SCl2;
    }

    public Item getBucketItem() {
        return Items.ERLENMEYER_SCl2;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        // getBlockStateLevel converts the LEVEL_1_8 of the fluid state to the LEVEL_15 the fluid block uses
        return Blocks.SCl2.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    public static class Flowing extends SCl2Fluid {
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

    public static class Still extends SCl2Fluid {
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
