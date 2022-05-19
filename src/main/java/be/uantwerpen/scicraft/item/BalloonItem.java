package be.uantwerpen.scicraft.item;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BalloonItem extends Item {
    public BalloonItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);
        if (world.isClient()) {
            if (entity instanceof  PlayerEntity) {
                if (!((PlayerEntity) entity).getAbilities().creativeMode) {
                    //System.out.println(Integer.toString(slot));
                    if ((selected || slot==0) && !((PlayerEntity) entity).getAbilities().allowFlying) {
                        ((PlayerEntity) entity).getAbilities().allowFlying = true;
                        ((ClientPlayerEntity) entity).sendAbilitiesUpdate();
                        // TODO item takes damage
                    } else if (!selected && slot!=0 && ((PlayerEntity) entity).getAbilities().allowFlying) {
                        ((PlayerEntity) entity).getAbilities().allowFlying = false;
                        ((PlayerEntity) entity).getAbilities().flying = false;
                        ((ClientPlayerEntity) entity).sendAbilitiesUpdate();
                    }
                }
            }
        }
    }
}
