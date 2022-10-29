package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.MinelabsProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
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
    private static final IntProperty COUNTER = MinelabsProperties.COUNTER;

    public MicroscopeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH).with(ZOOM, false).with(COUNTER, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(ZOOM);
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
                .with(ZOOM, false)
                .with(COUNTER, base);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && hand == Hand.MAIN_HAND) {
            ItemStack stack = player.getStackInHand(hand);
            if (state.get(ZOOM) && stack.getItem() == Items.LENS) {
                world.setBlockState(pos, state.cycle(ZOOM));
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BIG_LENS)));
                return ActionResult.SUCCESS;
            }
            if (!state.get(ZOOM) && stack.getItem() == Items.BIG_LENS) {
                world.setBlockState(pos, state.cycle(ZOOM));
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.LENS)));
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.FAIL;
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
            case NORTH, SOUTH -> VoxelShapes.cuboid(0.375f, (0f - offset), 0.250f, 0.625f, (0.6f - offset), 0.750f);
            case EAST, WEST -> VoxelShapes.cuboid(0.250f, (0f - offset), 0.375f, 0.750f, (0.6f - offset), 0.625f);
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
