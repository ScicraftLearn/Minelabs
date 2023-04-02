package be.uantwerpen.minelabs.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

/**
 * Instances of this class are used for building atoms.
 * They include: Proton, Neutron, Electron, Anti-proton, Anti-neutron and positron.
 */
public abstract class SubatomicParticle extends ThrownItemEntity {
    private int itemAge;

    public SubatomicParticle(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(true);
    }

    public SubatomicParticle(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
        setNoGravity(true);
    }

    public SubatomicParticle(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
        setNoGravity(true);
    }

    // Prevent slowdown over time from SubatomicParticle
    public float getSlowdownFactor() {
        return 1f;
    }

    protected abstract int getDespawnAge();

    @Environment(EnvType.CLIENT) // Needed for particles on collision with the world
    protected abstract ParticleEffect getParticleParameters();

    /**
     * Handle status 3 -> generate particles onCollision
     *
     * @param status byte with status to handle
     */
    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) { // Needed for particles on collision with the world
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
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
            bohrBlueprintEntity.onParticleCollision(this);
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
}
