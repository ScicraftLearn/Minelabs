package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.util.MinelabsProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class LabCornerBlock extends LabBlock {

    private static final VoxelShape table_top = VoxelShapes.cuboid(0f, 0.8125f, 0f, 1f, 0.875f, 1f);
    private static final EnumProperty<CornerShape> CONNECT = MinelabsProperties.CONNECT;

    public LabCornerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(CONNECT, CornerShape.STRAIGHT));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CONNECT);
        super.appendProperties(builder);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(CONNECT) == CornerShape.STRAIGHT) {
            return super.getOutlineShape(state, world, pos, context);
        } else {
            Direction dir = state.get(FACING);
            if (state.get(CONNECT) == CornerShape.LEFT) {
                return switch (dir) {
                    case NORTH ->
                            VoxelShapes.union(table_top, VoxelShapes.cuboid(0.0625f, 0f, 0.0625f, 1f, 0.8125f, 1f));
                    case SOUTH ->
                            VoxelShapes.union(table_top, VoxelShapes.cuboid(0f, 0f, 0f, 0.9375f, 0.8125f, 0.9375f));
                    case WEST ->
                            VoxelShapes.union(table_top, VoxelShapes.cuboid(0.0625f, 0f, 0f, 1f, 0.8125f, 0.9375f));
                    case EAST -> VoxelShapes.union(table_top, VoxelShapes.cuboid(0f, 0f, 0.0625f, 0.9375f, 0.8125f, 1));
                    default -> VoxelShapes.cuboid(0f, 0f, 0f, 0.9375f, 0.8125f, 0.9375f);
                };
            } else {
                return switch (dir) {
                    case NORTH ->
                            VoxelShapes.union(table_top, VoxelShapes.cuboid(0f, 0f, 0.0625f, 0.9375f, 0.8125f, 1f));
                    case SOUTH ->
                            VoxelShapes.union(table_top, VoxelShapes.cuboid(0.0625f, 0f, 0f, 1f, 0.8125f, 0.9375f));
                    case WEST ->
                            VoxelShapes.union(table_top, VoxelShapes.cuboid(0.0625f, 0f, 0.0625f, 1f, 0.8125f, 1f));//
                    case EAST ->
                            VoxelShapes.union(table_top, VoxelShapes.cuboid(0f, 0f, 0f, 0.9375f, 0.8125f, 0.9375f));//
                    default -> VoxelShapes.cuboid(0f, 0f, 0f, 0.9375f, 0.8125f, 0.9375f);
                };
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        assert state != null;
        if (ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getPlayerFacing())).getBlock() != Blocks.LAB_CENTER) {
            boolean invert = ctx.getPlayerFacing().getDirection() == Direction.AxisDirection.NEGATIVE;
            if (ctx.getPlayerFacing().getAxis() == Direction.Axis.X) {
                if (ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getPlayerFacing())).getBlock() instanceof LabBlock) {
                    if (ctx.getWorld().getBlockState(ctx.getBlockPos().offset(Direction.Axis.Z, 1)).getBlock() instanceof LabBlock) {
                        return state.with(CONNECT, invert ? CornerShape.LEFT : CornerShape.RIGHT);
                    } else if (ctx.getWorld().getBlockState(ctx.getBlockPos().offset(Direction.Axis.Z, -1)).getBlock() instanceof LabBlock) {
                        return state.with(CONNECT, invert ? CornerShape.RIGHT : CornerShape.LEFT);
                    }
                    return state.with(CONNECT, CornerShape.STRAIGHT);
                }
                return state.with(CONNECT, CornerShape.STRAIGHT);

            } else if (ctx.getPlayerFacing().getAxis() == Direction.Axis.Z) {
                if (ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getPlayerFacing())).getBlock() instanceof LabBlock) {
                    if (ctx.getWorld().getBlockState(ctx.getBlockPos().offset(Direction.Axis.X, 1)).getBlock() instanceof LabBlock) {
                        return state.with(CONNECT, invert ? CornerShape.RIGHT : CornerShape.LEFT);
                    } else if (ctx.getWorld().getBlockState(ctx.getBlockPos().offset(Direction.Axis.X, -1)).getBlock() instanceof LabBlock) {
                        return state.with(CONNECT, invert ? CornerShape.LEFT : CornerShape.RIGHT);
                    }
                    return state.with(CONNECT, CornerShape.STRAIGHT);
                }
                return state.with(CONNECT, CornerShape.STRAIGHT);
            }
        }
        return state.with(CONNECT, CornerShape.STRAIGHT);
    }
}
