package be.uantwerpen.minelabs;

import be.uantwerpen.minelabs.entity.*;
import be.uantwerpen.minelabs.item.Items;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ExtraDispenserBehavior {
    /**
     * Main class method
     * Registers all dispenser behaviors
     */
    public static void registerBehaviors() {
        /**
         * Register dispenser behavior for shooting out subatomic particles
         * Implements abstract class ProjectileDispenserBehavior to create the correct entity
         *
         * The entity is shot up slight as defined in dispenseSilently in ProjectileDispenserBehavior:
         * direction.getOffsetY() + 0.1F
         */
        registerSubatomicParticle(Items.ELECTRON, Entities.ELECTRON_ENTITY);
        registerSubatomicParticle(Items.POSITRON, Entities.POSITRON_ENTITY);
        registerSubatomicParticle(Items.NEUTRON, Entities.NEUTRON_ENTITY);
        registerSubatomicParticle(Items.ANTI_NEUTRON, Entities.ANTI_NEUTRON_ENTITY);
        registerSubatomicParticle(Items.PROTON, Entities.PROTON_ENTITY);
        registerSubatomicParticle(Items.ANTI_PROTON, Entities.ANTI_PROTON_ENTITY);

        /**
         * Register dispenser behavior for using Entropy Creeper Spawn Egg
         * Implements abstract class {@link ItemDispenserBehavior} to summon the correct entity
         */
        DispenserBlock.registerBehavior(Items.ENTROPY_CREEPER_SPAWN_EGG, new ItemDispenserBehavior() {
            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
                EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getEntityType(stack.getNbt());

                try {
                    entityType.spawnFromItemStack(pointer.getWorld(), stack, null, pointer.getPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
                } catch (Exception var6) {
                    LOGGER.error("Error while dispensing spawn egg from dispenser at {}", pointer.getPos(), var6);
                    return ItemStack.EMPTY;
                }

                stack.decrement(1);
                pointer.getWorld().emitGameEvent(null, GameEvent.ENTITY_PLACE, pointer.getPos());
                return stack;
            }
        });
    }

    /**
     * Register a single SubatomicParticle for the dispenser
     *
     * @param item   : Item that should be used
     * @param entity : Entity that should spawn
     **/
    private static void registerSubatomicParticle(Item item, EntityType<SubatomicParticleBase> entity) {
        DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return new SubatomicParticleBase(entity, position.getX(), position.getY(), position.getZ(), world, stack);
            }
        });
    }
}
