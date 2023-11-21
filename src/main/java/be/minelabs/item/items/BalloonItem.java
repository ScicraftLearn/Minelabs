package be.minelabs.item.items;

import be.minelabs.entity.mob.BalloonEntity;
import be.minelabs.entity.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class BalloonItem extends Item {

    public BalloonItem(Item.Settings settings) {
        super(settings);
    }

    private BalloonEntity summon(World world, Entity entity) {
        BalloonEntity balloon = Entities.BALLOON.create(world);
        balloon.refreshPositionAndAngles(entity.getX(), entity.getY() + 1.0, entity.getZ(), 0.0F, 0.0F);
        world.spawnEntity(balloon);
        return balloon;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity pe) {
            Vec3d vel = pe.getVelocity();
            user.setVelocity(vel.x * 1.02, BalloonEntity.LEVITATION_SPEED, vel.z * 1.02);
            user.onLanding();
        }
    }

    public int getMaxUseTime(ItemStack stack) {
        return 7200;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(!(entity instanceof PlayerEntity) && !(entity instanceof BalloonEntity)) {
            World world = entity.getWorld();
            if(!world.isClient) {
                List<BalloonEntity> list = entity.getWorld().getEntitiesByClass(BalloonEntity.class,
                        new Box(entity.getBlockPos()).expand(10), EntityPredicates.EXCEPT_SPECTATOR);
                boolean isLeashed = false;
                for(BalloonEntity be: list) {
                    if(be.getOwner().equals(entity)) {
                        isLeashed = true;
                        break;
                    }
                }

                if(!isLeashed) {
                    BalloonEntity be = summon(world, entity);
                    be.attachOwner(entity);
                    stack.decrement(1);
                    return ActionResult.success(true);
                }
            }
            return ActionResult.success(false);
        }
        return super.useOnEntity(stack, user, entity, hand);
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
                be.attachOwner(leashKnotEntity);

                context.getStack().decrement(1);

                world.emitGameEvent(GameEvent.BLOCK_ATTACH, blockPos, GameEvent.Emitter.of(playerEntity));

                return ActionResult.CONSUME;
            }

            return ActionResult.success(world.isClient);
        } else {
            return super.useOnBlock(context);
        }
    }
}
