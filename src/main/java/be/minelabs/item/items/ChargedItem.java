package be.minelabs.item.items;

import be.minelabs.entity.projectile.thrown.ChargedEntity;
import be.minelabs.entity.projectile.thrown.ParticleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ChargedItem extends Item {

    public ChargedItem(Settings settings) {
        super(settings);
    }

    /**
     * Place the item on the floor
     *
     * @param context the usage context
     * @return
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (!canPlaceEntity(world, context.getBlockPos(), context.getSide(), player, context.getStack())) {
            return ActionResult.FAIL;
        }
        if (!world.isClient) {
            ProjectileEntity entity = createPlacedEntity(world, context.getBlockPos().offset(context.getSide()), context.getStack());
            world.spawnEntity(entity);
            world.emitGameEvent(player, GameEvent.ENTITY_PLACE, entity.getPos());
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!context.getPlayer().getAbilities().creativeMode) {
            context.getStack().decrement(1);
        }

        return ActionResult.success(context.getWorld().isClient);
    }

    /**
     * Right-clicked in the air (throwing the item)
     *
     * @param world the world the item was used in
     * @param user  the player who used the item
     * @param hand  the hand used
     * @return
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            ProjectileEntity entity = createThrownEntity(user, world, stack);
            entity.setVelocity(user, user.getPitch(), user.getYaw(), user.getRoll(), ParticleEntity.DEFAULT_SPEED, 0f);
            world.spawnEntity(entity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    private boolean canPlaceEntity(World world, BlockPos pos, Direction direction, PlayerEntity player, ItemStack stack) {
        boolean bl = player.canPlaceOn(pos, direction, stack); // Allows ADVENTURE mode checking
        return bl && world.getEntitiesByClass(ChargedEntity.class, Box.of(pos.offset(direction).toCenterPos(), 0.5, 0.5, 0.5), chargedEntity -> true).isEmpty();
    }

    protected ProjectileEntity createThrownEntity(PlayerEntity user, World world, ItemStack stack) {
        return new ParticleEntity(user, world, stack);
    }

    protected ProjectileEntity createPlacedEntity(World world, BlockPos pos, ItemStack stack) {
        return new ParticleEntity(world, pos, stack);
    }
}
