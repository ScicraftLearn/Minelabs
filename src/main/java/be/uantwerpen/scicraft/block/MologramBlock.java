package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.entity.BlockEntities;
import be.uantwerpen.scicraft.block.entity.MologramBlockEntity;
import be.uantwerpen.scicraft.particle.Particles;
import net.minecraft.block.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;


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
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (!(blockEntity instanceof MologramBlockEntity)) {
            return ActionResult.PASS;
        }
        Inventory blockInventory = ((MologramBlockEntity) blockEntity).getInventory();
        //Onderstaande toevoegen aan if statement zodat er geen blocks ingestoken kunnen worden
        //&&Block.getBlockFromItem(player.getStackInHand(hand).getItem()) == Blocks.AIR
        if (!player.getStackInHand(hand).isEmpty()) {
            // Check what is the first open slot and put an item from the player's hand there
            if (blockInventory.getStack(0).isEmpty()) {
                // Put the stack the player is holding into the inventory
                blockInventory.setStack(0, player.getStackInHand(hand).copy());
                world.setBlockState(blockPos, blockState.with(LIT,true));
                blockInventory.getStack(0).setCount(1);
                // Decrement the stack from the player's hand
                player.getStackInHand(hand).decrement(1);
            } else {
                // If the inventory is full we'll print it's contents
                System.out.println("The first slot holds " + blockInventory.getStack(0));
            }
        } else { // open
            // If the player is not holding anything we'll get give him the items in the block entity one by one
            // Find the first slot that has an item and give it to the player
            if (!blockInventory.getStack(0).isEmpty()) {
                player.getInventory().offerOrDrop(blockInventory.getStack(0));
                world.setBlockState(blockPos, blockState.with(LIT,false));
                blockInventory.removeStack(0);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? HopperBlock.checkType(type, BlockEntities.MOLOGRAM_BLOCK_ENTITY, MologramBlockEntity::clientTick) : null;
    }

//    @Override
//    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
//        if (!state.get(LIT)) return;
//        for(int i = 0;i < 3;i++){
//            world.addParticle(Particles.HOLOGRAM_PARTICLE, pos.getX()+ 0.5d, pos.getY() + 0.75d, pos.getZ() + 0.5d,
//                    0,0,0);
//        }
//    }

}
