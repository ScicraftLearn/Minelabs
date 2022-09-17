package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.state.MinelabsProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MicroscopeBlock extends Block {

    private static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    private static final BooleanProperty ZOOM = MinelabsProperties.ZOOMED;
    private static final BooleanProperty COUNTER = MinelabsProperties.COUNTER;

    public MicroscopeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH).with(ZOOM, false).with(COUNTER, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(ZOOM);
        builder.add(COUNTER);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Boolean bl = state.get(COUNTER);
        Direction dir = state.get(FACING);
        return switch (dir) {
            case NORTH, SOUTH -> VoxelShapes.cuboid(0.375f, bl ? -0.2f : 0f, 0.250f, 0.625f, bl ? 0.4f : 0.6f, 0.750f);
            case EAST, WEST -> VoxelShapes.cuboid(0.250f, bl ? -0.2f : 0f, 0.375f, 0.750f, bl ? 0.4f : 0.6f, 0.625f);
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

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite())
                .with(ZOOM, false)
                .with(COUNTER, ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock() instanceof LabBlock);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && hand == Hand.MAIN_HAND) {
            world.setBlockState(pos, state.cycle(ZOOM));
        }
        return ActionResult.SUCCESS;
    }
}
