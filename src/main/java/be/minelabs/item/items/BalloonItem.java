package be.minelabs.item.items;

import be.minelabs.entity.mob.BalloonEntity;
import be.minelabs.entity.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.include.com.google.common.base.Predicates;

import java.util.List;


public class BalloonItem extends Item {

    public BalloonItem(Item.Settings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, BalloonItem.DISPENSER_BEHAVIOR);
    }

    public static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            if(stack.getItem() instanceof BalloonItem bi) {
                return bi.dispense(pointer, stack) ? stack : super.dispenseSilently(pointer, stack);
            }
            return super.dispenseSilently(pointer, stack);
        }
    };

    public boolean dispense(BlockPointer pointer, ItemStack balloon) {
        BlockPos blockPos = pointer.getPos().offset((Direction)pointer.getBlockState().get(DispenserBlock.FACING));
        List<LivingEntity> list = pointer.getWorld().getEntitiesByClass(LivingEntity.class, new Box(blockPos), EntityPredicates.EXCEPT_SPECTATOR);
        if (list.isEmpty()) {
            return false;
        } else {
            LivingEntity livingEntity = (LivingEntity)list.get(0);
            useOnEntity(balloon, balloon.getHolder(), livingEntity);
            return true;
        }
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
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return useOnEntity(stack, user, entity);
    }

    public ActionResult useOnEntity(ItemStack stack, Entity user, LivingEntity entity) {
        if(!(entity instanceof PlayerEntity) && !(entity instanceof BalloonEntity)) {
            World world = user.getWorld();
            if(!world.isClient) {
                if(!((MobEntity)entity).isLeashed()) {
                    BalloonEntity be = summon(world, entity);
                    be.attachOwner(entity);
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
                be.attachOwner(leashKnotEntity);

                context.getStack().decrement(1);

                world.emitGameEvent(GameEvent.BLOCK_ATTACH, blockPos, GameEvent.Emitter.of(playerEntity));

                return ActionResult.SUCCESS;
            }

            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }
}
