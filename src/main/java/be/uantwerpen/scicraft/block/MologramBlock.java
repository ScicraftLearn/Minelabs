package be.uantwerpen.scicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class MologramBlock extends Block {

    public ItemStack itemStack;

    public MologramBlock(Settings settings) {
        super(settings);
        itemStack = ItemStack.EMPTY;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        //With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if(world.isClient){
            return ActionResult.SUCCESS;
        }
        if (itemStack.isEmpty()) {
            itemStack = player.getStackInHand(hand).copy();
            itemStack.setCount(1);
            player.getStackInHand(hand).decrement(1);
        }
        else {
            System.out.println("test");
            world.spawnEntity(new ItemEntity(world,blockPos.getX(),blockPos.getY(),blockPos.getZ(),itemStack));
            itemStack = ItemStack.EMPTY;
        }
        player.getInventory().markDirty();

        return ActionResult.SUCCESS;
    }


}
