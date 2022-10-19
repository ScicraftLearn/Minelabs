package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.MologramBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MologramBlock extends BlockWithEntity {

    public static final BooleanProperty LIT = BooleanProperty.of("lit");

    public MologramBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
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
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (!(blockEntity instanceof MologramBlockEntity)) {
                return ActionResult.PASS;
            }
            Inventory blockInventory = ((MologramBlockEntity) blockEntity);
            if (!player.getStackInHand(hand).isEmpty()) { // insert
                if (((MologramBlockEntity) blockEntity).canInsert(0, player.getStackInHand(hand), null)) { // WHAT IS ALLOWED IN THE INVENTORY
                    // Check what is the first open slot and put an item from the player's hand there
                    if (blockInventory.getStack(0).isEmpty()) {
                        // Put the stack the player is holding into the inventory
                        blockInventory.setStack(0, player.getStackInHand(hand).copy());
                        world.setBlockState(blockPos, blockState.with(LIT, true));
                        blockInventory.getStack(0).setCount(1);
                        // Decrement the stack from the player's hand
                        player.getStackInHand(hand).decrement(1);
                    }
                } else {
                    // If the inventory is full we'll print it's contents
                    System.out.println("The first slot holds " + blockInventory.getStack(0));
                }
            } else { // extract
                // If the player is not holding anything we'll get give him the items in the block entity one by one
                // Find the first slot that has an item and give it to the player
                if (!blockInventory.getStack(0).isEmpty()) {
                    player.getInventory().offerOrDrop(blockInventory.getStack(0));
                    world.setBlockState(blockPos, blockState.with(LIT, false));
                    blockInventory.removeStack(0);
                }
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
}
