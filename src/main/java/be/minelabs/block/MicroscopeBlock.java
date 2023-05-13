package be.minelabs.block;

import be.minelabs.item.Items;
import be.minelabs.util.MinelabsProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MicroscopeBlock extends CosmeticBlock {
    private static final BooleanProperty ZOOM = MinelabsProperties.ZOOMED;

    public MicroscopeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(ZOOM, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ZOOM);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(ZOOM, false);
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
        float offset = getYOffset(state);
        return switch (state.get(FACING)) {
            case NORTH, SOUTH -> VoxelShapes.cuboid(0.375f, (0f - offset), 0.250f, 0.625f, (0.6f - offset), 0.750f);
            case EAST, WEST -> VoxelShapes.cuboid(0.250f, (0f - offset), 0.375f, 0.750f, (0.6f - offset), 0.625f);
            default -> VoxelShapes.fullCube();
        };
    }
}
