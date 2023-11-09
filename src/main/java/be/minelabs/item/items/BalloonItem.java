package be.minelabs.item.items;

import be.minelabs.entity.mob.BalloonEntity;
import be.minelabs.entity.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Iterator;
import java.util.List;


public class BalloonItem extends Item {
    public BalloonItem(Item.Settings settings) {
        super(settings);
    }

    private BalloonEntity summon(World world, Entity entity) {
        BalloonEntity balloon = Entities.BALLOON.create(world);
        balloon.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), 0.0F, 0.0F);
        world.spawnEntity(balloon);
        return balloon;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);
        if (!world.isClient()) {
            if (entity instanceof PlayerEntity pe) {
                if (!(pe.getAbilities().creativeMode)) {
                    if (pe.getOffHandStack().getItem() instanceof BalloonItem
                            || pe.getMainHandStack().getItem() instanceof BalloonItem) {
                        // TODO FIX custom effect (temp fix: use levitation)
                        //System.out.println("Try to fly: call effect");
                        //StatusEffectInstance sei = new StatusEffectInstance(Effects.FLYING, 2, 2, false, false);

                        StatusEffectInstance sei = new StatusEffectInstance(StatusEffects.LEVITATION, 4, 2, false, false);
                        pe.addStatusEffect(sei);
                    }
                }
            }
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(!(entity instanceof PlayerEntity) && !(entity instanceof BalloonEntity)) {
            World world = user.getWorld();
            if(!world.isClient) {
                if(!((MobEntity)entity).isLeashed()) {
                    BalloonEntity be = summon(world, entity);
                    be.attachLeash(entity, true);
                    stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isIn(BlockTags.FENCES)) {
            PlayerEntity playerEntity = context.getPlayer();
            if (!world.isClient && playerEntity != null) {
                LeashKnotEntity leashKnotEntity = LeashKnotEntity.getOrCreate(world, blockPos);
                leashKnotEntity.onPlace();
                BalloonEntity be = summon(world, leashKnotEntity);
                be.attachLeash(leashKnotEntity, true);

                world.emitGameEvent(GameEvent.BLOCK_ATTACH, blockPos, GameEvent.Emitter.of(playerEntity));
            }

            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }
}
