package be.uantwerpen.scicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ErlenmeyerBlock extends HorizontalFacingBlock {
    public ErlenmeyerBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        // TODO USE ROTATION AS IN SIGNBLOCK CLASS -> this.stateManager.getDefaultState()).with(ROTATION, 0))
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case NORTH:
                return VoxelShapes.cuboid(0.375f,0f,0.3125f,0.625f,0.8f,0.6875f);
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
}