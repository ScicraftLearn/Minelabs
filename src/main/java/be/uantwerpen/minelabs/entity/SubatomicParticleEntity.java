package be.uantwerpen.minelabs.entity;

import be.uantwerpen.minelabs.item.Items;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Instances of this class are used for building atoms.
 * They include: Proton, Neutron, Electron, Anti-proton, Anti-neutron and positron.
 */
public class SubatomicParticleEntity extends ThrownItemEntity {
    private int itemAge;

    public SubatomicParticleEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(true);
    }

    public SubatomicParticleEntity(double d, double e, double f, World world, ItemStack itemStack) {
        super(Entities.SUBATOMIC_PARTICLE_ENTITY_TYPE, d, e, f, world);
        setItem(itemStack);
        setNoGravity(true);
    }

    public SubatomicParticleEntity(LivingEntity owner, World world, ItemStack itemStack) {
        super(Entities.SUBATOMIC_PARTICLE_ENTITY_TYPE, owner, world);
        setItem(itemStack);
        setNoGravity(true);
    }

    /**
     * Prevent slowdown over time from SubatomicParticle. Defined in {@link be.uantwerpen.minelabs.mixins.ThrownEntityMixin}.
     */
    public float getSlowdownFactor() {
        return 1f;
    }

    /**
     * Change getDespawnAge() to change the time after which ProtonEntity will despawn
     * Game normally runs at 20 ticks per second, so return 100 -> ProtonEntity despawns after 5 seconds
     */
    protected int getDespawnAge() {
        return 100;
    }

    @Environment(EnvType.CLIENT)
    @Nullable
    protected ParticleEffect getParticleParameters() { // Needed for particles on collision with the world
        ItemStack itemStack = this.getStack();
        return itemStack.isEmpty() ? null : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        // status 3 -> generate particles onCollision
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();
            if (particleEffect == null) return;

            for (int i = 0; i < 8; ++i) {
                double velocityX = (Math.random() * 2.0 - 1.0) * (double)0.3f;
                double velocityY = (Math.random() * 2.0 - 1.0) * (double)0.3f;
                double velocityZ = (Math.random() * 2.0 - 1.0) * (double)0.3f;
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), velocityX, velocityY, velocityZ);
            }
        }
    }

    /**
     * Return gravity = 0 -> no gravity effect on SubatomicParticle
     *
     * @return float
     */
    @Override
    protected float getGravity() {
        return 0F;
    }

    /**
     * Updates the SubatomicParticle every tick
     */
    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof PlayerEntity && !entity.isAlive()) {
            // Removes the SubatomicParticle when throwing player dies
            this.discard();
        } else {
            super.tick();

            // Logic to despawn SubatomicParticle
            ++this.itemAge;
            if (!this.world.isClient && this.itemAge >= getDespawnAge()) {
                this.discard();
            }
        }
    }

//    /**
//     * If we set {@link BohrBlueprintEntity#canHit} to false, with this function we can still hit with particles.
//     * Override checking `canHit` of bohrBlueprintEntity so we can set it to false.
//     * `canHit` is used both for projectiles and targetting and we only want one of the two.
//     */
//    protected boolean canHit(Entity entity) {
//        if (entity instanceof BohrBlueprintEntity) {
//            // Note: did not copy over owner and vehicle check as these situations shouldn't occur.
//            return !entity.isSpectator() && entity.isAlive();
//        }
//        return super.canHit(entity);
//    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        // notify bohr plate of collision
        if (entityHitResult.getEntity() instanceof BohrBlueprintEntity bohrBlueprintEntity) {
            bohrBlueprintEntity.onParticleCollision(this, (ServerPlayerEntity) getOwner());
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.world.isClient) {
            // Generates clientside particles
            this.world.sendEntityStatus(this, (byte) 3);
            // Remove Entity from the server
            this.kill();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ELECTRON;
    }
}
