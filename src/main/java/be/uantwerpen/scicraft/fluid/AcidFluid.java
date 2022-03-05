package be.uantwerpen.scicraft.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class AcidFluid extends AbstractFluid {
//    @Override
//    public Fluid getStill() {
//        return YOUR_STILL_FLUID_HERE;
//    }
//
//    @Override
//    public Fluid getFlowing() {
//        return YOUR_FLOWING_FLUID_HERE;
//    }
//
//    @Override
//    public Item getBucketItem() {
//        return YOUR_BUCKET_ITEM_HERE;
//    }
//
//    @Override
//    protected BlockState toBlockState(FluidState fluidState) {
//        return YOUR_FLUID_BLOCK_HERE.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
//    }
//
//    public static class Flowing extends AcidFluid {
//        @Override
//        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
//            super.appendProperties(builder);
//            builder.add(LEVEL);
//        }
//
//        @Override
//        public int getLevel(FluidState fluidState) {
//            return fluidState.get(LEVEL);
//        }
//
//        @Override
//        public boolean isStill(FluidState fluidState) {
//            return false;
//        }
//    }
//
//    public static class Still extends AcidFluid {
//        @Override
//        public int getLevel(FluidState fluidState) {
//            return 8;
//        }
//
//        @Override
//        public boolean isStill(FluidState fluidState) {
//            return true;
//        }
//    }
}
