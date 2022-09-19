package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.state.MinelabsProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class TubeRackBlock extends Block {

    private static final IntProperty COUNTER = MinelabsProperties.COUNTER;
    private static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    public TubeRackBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH).with(COUNTER, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(COUNTER);
    }

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
            case NORTH -> VoxelShapes.cuboid(0.1875f, (0f - offset), 0.3125f, 0.8125f, (0.4f - offset), 0.6250f);//
            case SOUTH -> VoxelShapes.cuboid(0.1875f, (0f - offset), 0.3750f, 0.8125f, (0.4f - offset), 0.6875f);//
            case EAST -> VoxelShapes.cuboid(0.3750f, (0f - offset), 0.1875f, 0.6875f, (0.4f - offset), 0.8125f);//
            case WEST -> VoxelShapes.cuboid(0.3125f, (0f - offset), 0.1875f, 0.6250f, (0.4f - offset), 0.8125f);
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
