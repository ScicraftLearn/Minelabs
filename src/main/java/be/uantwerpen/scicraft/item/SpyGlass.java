package be.uantwerpen.scicraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.SpyglassItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.hit.HitResult;



public class SpyGlass extends SpyglassItem {
    public SpyGlass(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        HitResult lookingAt = MinecraftClient.getInstance().crosshairTarget;
        BlockPos blockPos = null;
        //This looks if what you are pointing at is a block.
        if (lookingAt != null && lookingAt.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) lookingAt;
            blockPos = blockHit.getBlockPos();
            BlockState blockState = MinecraftClient.getInstance().world.getBlockState(blockPos);
            Block block = blockState.getBlock();
        }
        //If lookingAt is not a block within range, do normal SpyGlass.
        if(blockPos != null && blockPos.isWithinDistance(user.getPos(), 1.0)){
            //Show picture of molecule model.
            user.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0F, 1.0F);
            return ItemUsage.consumeHeldItem(world, user, hand);
        }
            user.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0F, 1.0F);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return ItemUsage.consumeHeldItem(world, user, hand);
        }
    }
