package be.uantwerpen.minelabs.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class LabBlock extends BlockWithEntity {

    protected static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_N = VoxelShapes.union(
            Block.createCuboidShape(0.0, 0.0, 1.0, 16.0, 13.0, 16.0), //base
            Block.createCuboidShape(0.0, 14.0, 15.0, 16.0, 15.0, 16.0), //edge
            Block.createCuboidShape(0.0, 13.0, 0.0, 16.0, 14.0, 16.0)); // top
    private static final VoxelShape SHAPE_E = VoxelShapes.union(
            Block.createCuboidShape(0.0, 0.0, 0.0, 15.0, 13.0, 16.0), //base
            Block.createCuboidShape(0.0, 14.0, 0.0, 1.0, 15.0, 16.0), //edge
            Block.createCuboidShape(0.0, 13.0, 0.0, 16.0, 14.0, 16.0)); // top
    private static final VoxelShape SHAPE_S = VoxelShapes.union(
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 13.0, 15.0), //base
            Block.createCuboidShape(0.0, 14.0, 0.0, 16.0, 15.0, 1.0), //edge
            Block.createCuboidShape(0.0, 13.0, 0.0, 16.0, 14.0, 16.0)); // top
    private static final VoxelShape SHAPE_W = VoxelShapes.union(
            Block.createCuboidShape(1.0, 0.0, 0.0, 16.0, 13.0, 16.0), //base
            Block.createCuboidShape(15.0, 14.0, 0.0, 16.0, 15.0, 16.0), //edge
            Block.createCuboidShape(0.0, 13.0, 0.0, 16.0, 14.0, 16.0)); // top

    public LabBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            default -> SHAPE_N;
            case SOUTH -> SHAPE_S;
            case EAST -> SHAPE_E;
            case WEST -> SHAPE_W;
        };
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }
}
