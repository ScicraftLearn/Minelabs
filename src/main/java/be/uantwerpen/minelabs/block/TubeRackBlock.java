package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.state.MinelabsProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class TubeRackBlock extends Block {

    private static final BooleanProperty COUNTER = MinelabsProperties.COUNTER;
    private static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    public TubeRackBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH).with(COUNTER, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(COUNTER);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite())
                .with(COUNTER, ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock() instanceof LabBlock);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Boolean bl = state.get(COUNTER);
        Direction dir = state.get(FACING);
        return switch (dir) {
            case NORTH, SOUTH ->
                    VoxelShapes.cuboid(0.375f, bl ? -0.125f : 0f, 0.250f, 0.625f, bl ? 0.25f : 0.4f, 0.750f);
            case EAST, WEST -> VoxelShapes.cuboid(0.250f, bl ? -0.125f : 0f, 0.375f, 0.750f, bl ? 0.25f : 0.4f, 0.625f);
            default -> VoxelShapes.fullCube();
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
}
