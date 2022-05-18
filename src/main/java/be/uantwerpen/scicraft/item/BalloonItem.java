package be.uantwerpen.scicraft.item;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.world.World;

public class BalloonItem extends Item {
    public BalloonItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);
        if (selected && slot == 1) {
            if (world.isClient()) {
                if (entity instanceof  PlayerEntity) {
                    ((PlayerEntity) entity).getAbilities().flying = true;
                    ((ClientPlayerEntity) entity).sendAbilitiesUpdate();
                }
            }
        }
    }
}
