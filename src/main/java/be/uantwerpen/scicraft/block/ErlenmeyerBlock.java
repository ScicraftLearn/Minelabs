package be.uantwerpen.scicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;


public class ErlenmeyerBlock extends Block {
    public static IntProperty ROTATION;

    public ErlenmeyerBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(ROTATION, 0));
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(ROTATION, MathHelper.floor((double)((180.0F + ctx.getPlayerYaw()) * 8.0F / 360.0F) + 0.5D) & 15); //wtf doet die bitwise AND operator?
    }
    /*
    FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return (BlockState)((BlockState)this.getDefaultState().with(ROTATION, MathHelper.floor((double)((180.0F + ctx.getPlayerYaw()) * 16.0F / 360.0F) + 0.5D) & 15)).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }
     */
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        return VoxelShapes.cuboid(0.375f, 0f, 0.3125f, 0.625f, 0.8f, 0.6875f);
    }
    /*
        Direction dir = state.get(FACING);
        switch(dir) {
            case NORTH:

            case SOUTH:
                return VoxelShapes.cuboid(0.375f,0f,0.3125f,0.625f,0.8f,0.6875f);
            case EAST:
                return VoxelShapes.cuboid(0.3125f,0f,0.375f,0.6875f,0.8f,0.625f);
            case WEST:
                return VoxelShapes.cuboid(0.3125f,0f,0.375f,0.6875f,0.8f,0.625f);
            default:
                return VoxelShapes.fullCube();
        }

    }
    */
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(ROTATION, rotation.rotate((Integer)state.get(ROTATION), 8));
    }
    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return (BlockState)state.with(ROTATION, mirror.mirror((Integer)state.get(ROTATION), 8));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{ROTATION});
    }

    static {
        ROTATION = Properties.ROTATION;
    }
}