package be.minelabs.item.items;

import be.minelabs.Minelabs;
import be.minelabs.entity.ChargedEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ChargedItem extends Item {

    private final int charge;

    private final float mass;

    public ChargedItem(Settings settings, int charge, float mass) {
        super(settings);
        this.charge = charge;
        this.mass = mass;
    }

    /**
     * Place the item on the floor
     *
     * @param context the usage context
     * @return
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient) {
            ChargedEntity entity = new ChargedEntity(context.getWorld(), context.getBlockPos().up(), this.charge, this.mass);
            context.getWorld().spawnEntity(entity);
        }
        return super.useOnBlock(context);
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
            ChargedEntity entity = new ChargedEntity(user, world, charge, mass);
            entity.setVelocity(user, user.getPitch(), user.getYaw(), user.getRoll(), 0.5f, 0f);
            world.spawnEntity(entity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (user.getAbilities().creativeMode) {
            stack.decrement(1);

        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
