package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.util.MinelabsProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ErlenmeyerBlock extends HorizontalFacingBlock {

    private static final IntProperty COUNTER = MinelabsProperties.COUNTER;

    public ErlenmeyerBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(COUNTER, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(COUNTER);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        int base = 0;
        if (ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock() instanceof LabBlock) {
            base = 2;
        } else if (ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock() instanceof LabCenterBlock) {
            base = 1;
        }
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite())
                .with(COUNTER, base);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        float offset = 0.0f;
        int i = state.get(COUNTER);
        Direction dir = state.get(FACING);
        switch (i) {
            case 0 -> offset = 0f;
            case 1 -> offset = 0.0625f;
            case 2 -> offset = 0.125f;
        }
        return switch (dir) {
            case NORTH, SOUTH -> VoxelShapes.cuboid(0.3750f, (0f - offset), 0.3125f, 0.625f, (0.8f - offset), 0.6875f);
            case EAST, WEST -> VoxelShapes.cuboid(0.3125f, (0f - offset), 0.375f, 0.6875f, (0.8f - offset), 0.625f);
            default -> VoxelShapes.fullCube();
        };
    }
}
