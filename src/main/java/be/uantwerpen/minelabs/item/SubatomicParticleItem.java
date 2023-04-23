package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.entity.Entities;
import be.uantwerpen.minelabs.entity.SubatomicParticleEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SubatomicParticleItem extends BlockItem {

    public SubatomicParticleItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    /**
     * When SubatomicParticleItem is right-clicked, use up the item if necessary and spawn the entity
     *
     * @param world minecraft world
     * @param user  player invoking the right click action
     * @param hand  the hand of the user
     * @return TypedActionResult, indicates if the use of the item succeeded or not
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand); // creates a new ItemStack instance of the user's itemStack in-hand

        /* TODO sound effect of SubatomicParticleItem throw
         * Example with snowball sound
         * world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 1F);
         */

        /* TODO cooldown on the throw of an SubatomicParticleItem (like the cooldown on Ender Pearls)
         * Example cooldown of 5 ticks
         * user.getItemCooldownManager().set(this, 5);
         */
        if (!world.isClient) {
            // Spawns the subatomicParticle entity with correct initial velocity (velocity has the same direction as the players looking direction)
            SubatomicParticleEntity subPart = new SubatomicParticleEntity(user, world, itemStack);
            subPart.setVelocity(user, user.getPitch(), user.getYaw(), user.getRoll(), SubatomicParticleEntity.DEFAULT_SPEED, 0F);
            world.spawnEntity(subPart);

            List<BohrBlueprintEntity> entities = world.getEntitiesByType(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE,
                    Box.of(subPart.getPos(), 1, 1, 1), bohrE -> bohrE.isInRange(subPart, 2.0));
            Optional<BohrBlueprintEntity> closest = entities.stream().min(Comparator.comparing(e -> e.distanceTo(user)));
            closest.ifPresent(bohrBlueprintEntity -> subPart.onEntityHit(new EntityHitResult(bohrBlueprintEntity)));

        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1); // decrements itemStack if user is not in creative mode
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
