package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.state.MinelabsProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ErlenmeyerBlock extends HorizontalFacingBlock {

    private static final BooleanProperty COUNTER = MinelabsProperties.COUNTER;

    public ErlenmeyerBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(COUNTER, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(COUNTER);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite())
                .with(COUNTER, ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock() instanceof LabBlock);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Boolean bl = state.get(COUNTER);
        Direction dir = state.get(FACING);
        return switch (dir) {
            case NORTH, SOUTH ->
                    VoxelShapes.cuboid(0.3750f, bl ? -0.1f : 0f, 0.3125f, 0.625f, bl ? 0.6f : 0.8f, 0.6875f);
            case EAST, WEST -> VoxelShapes.cuboid(0.3125f, bl ? -0.1f : 0f, 0.375f, 0.6875f, bl ? 0.6f : 0.8f, 0.625f);
            default -> VoxelShapes.fullCube();
        };
    }
}
