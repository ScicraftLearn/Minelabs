package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.entity.BalloonEntity;
import be.uantwerpen.minelabs.entity.Entities;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.World;

import java.util.UUID;

public class BalloonItem extends Item {
    public BalloonItem(Item.Settings settings) {
        super(settings);
    }

    private BalloonEntity summon(World world, LivingEntity entity) {
        BalloonEntity balloon = Entities.BALLOON.create(world);
        balloon.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), 0.0F, 0.0F);
        world.spawnEntity(balloon);
        balloon.attachLeash(entity, true);
        return balloon;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);
        if (world.isClient()) {
            if (entity instanceof PlayerEntity pe) {
                if (!(pe.getAbilities().creativeMode)) {
                    ItemStack offhand = pe.getInventory().getStack(PlayerInventory.OFF_HAND_SLOT);
                    if(offhand.isItemEqual(itemStack)) {
                        if(!offhand.isEmpty() && offhand.getItem() instanceof BalloonItem) {
                            pe.getAbilities().allowFlying = true;
                            ((ClientPlayerEntity) entity).sendAbilitiesUpdate();
                            // TODO item takes damage
                        } else {
                            pe.getAbilities().allowFlying = false;
                            pe.getAbilities().flying = false;
                            ((ClientPlayerEntity) entity).sendAbilitiesUpdate();
                        }
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
                summon(world, entity);
                stack.decrement(1);
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    // TODO: connect to fence? => see LeadItem
}
