package be.minelabs.block.blocks;

import be.minelabs.block.entity.BlockEntities;
import be.minelabs.block.entity.MologramBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class MologramBlock extends BlockWithEntity implements Waterloggable {

    public static final BooleanProperty LIT = BooleanProperty.of("lit");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public MologramBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(LIT, false).with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MologramBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        //With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(1f/16f, 0, 1f/16f, 15f/16f, 12f / 16f, 15f/16f);
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (!(blockEntity instanceof MologramBlockEntity)) {
            return ActionResult.PASS;
        }
        Inventory blockInventory = ((MologramBlockEntity) blockEntity);

        if (!blockInventory.getStack(0).isEmpty()) { //remove molecule
            player.getInventory().offerOrDrop(blockInventory.getStack(0));
            world.setBlockState(blockPos, blockState.with(LIT, false));
            blockInventory.removeStack(0);
            if (!world.isClient){((ServerWorld) world).getChunkManager().markForUpdate(blockPos);}

        } else if (!player.getStackInHand(hand).isEmpty()) { //insert molecule
            if (((MologramBlockEntity) blockEntity).canInsert(0, player.getStackInHand(hand), null)) {
                blockInventory.setStack(0, player.getStackInHand(hand).copy());
                world.setBlockState(blockPos, blockState.with(LIT, true));
                blockInventory.getStack(0).setCount(1);
                player.getStackInHand(hand).decrement(1);
                if (!world.isClient){((ServerWorld) world).getChunkManager().markForUpdate(blockPos);}
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MologramBlockEntity mologramBlockEntity) {
                ItemScatterer.spawn(world, pos, mologramBlockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockEntities.MOLOGRAM_BLOCK_ENTITY, MologramBlockEntity::tick);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return state.get(LIT);
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(LIT) ? 15 : 0;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            // This is for 1.17 and below: world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
