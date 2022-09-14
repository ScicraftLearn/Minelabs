package be.uantwerpen.minelabs.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ErlenmeyerBlock extends HorizontalFacingBlock {

    public ErlenmeyerBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case NORTH -> VoxelShapes.cuboid(0.375f, 0f, 0.3125f, 0.625f, 0.8f, 0.6875f);
            case SOUTH -> VoxelShapes.cuboid(0.375f, 0f, 0.3125f, 0.625f, 0.8f, 0.6875f);
            case EAST -> VoxelShapes.cuboid(0.3125f, 0f, 0.375f, 0.6875f, 0.8f, 0.625f);
            case WEST -> VoxelShapes.cuboid(0.3125f, 0f, 0.375f, 0.6875f, 0.8f, 0.625f);
            default -> VoxelShapes.fullCube();
        };
    }
}
