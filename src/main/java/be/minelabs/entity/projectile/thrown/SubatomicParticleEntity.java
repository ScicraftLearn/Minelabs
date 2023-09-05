package be.minelabs.entity.projectile.thrown;

import be.minelabs.entity.BohrBlueprintEntity;
import be.minelabs.entity.Entities;
import be.minelabs.item.Items;
import be.minelabs.mixin.ThrownEntityMixin;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

/**
 * Instances of this class are used for building atoms.
 * They include: Proton, Neutron, Electron, Anti-proton, Anti-neutron and positron.
 */
public class SubatomicParticleEntity extends ThrownItemEntity {

    public static final float DEFAULT_SPEED = 1.5f;

    private int itemAge;

    public SubatomicParticleEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        onCreated(true);
    }

    public SubatomicParticleEntity(double d, double e, double f, World world, ItemStack itemStack) {
        this(d, e, f, world, itemStack, true);
    }

    public SubatomicParticleEntity(double d, double e, double f, World world, ItemStack itemStack, boolean checkCollisionOnSpawn) {
        super(Entities.SUBATOMIC_PARTICLE_ENTITY_TYPE, d, e, f, world);
        setItem(itemStack);
        onCreated(checkCollisionOnSpawn);
    }

    public SubatomicParticleEntity(LivingEntity owner, World world, ItemStack itemStack) {
        super(Entities.SUBATOMIC_PARTICLE_ENTITY_TYPE, owner, world);
        setItem(itemStack);
        onCreated(true);
    }

    /**
     * Called when the entity is created (from any constructor)
     */
    public void onCreated(boolean checkCollision) {
        setNoGravity(true);

        if (getWorld().isClient)
            return;

        if (checkCollision) {
            // on server check for collision with other entities upon spawning
            // when inside of entity from start this is not checked by the default collision logic
            List<Entity> entities = getWorld().getOtherEntities(this, getBoundingBox().expand(0.2f), Entity::canHit);
            for (Entity entity : entities.stream().sorted(Comparator.comparing(this::distanceTo)).toList()) {
                onEntityHit(new EntityHitResult(entity));
                if (isRemoved()) break;
            }
        }

    }

    /**
     * Prevent slowdown over time from SubatomicParticle. Defined in {@link ThrownEntityMixin}.
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
                double velocityX = (Math.random() * 2.0 - 1.0) * (double) 0.3f;
                double velocityY = (Math.random() * 2.0 - 1.0) * (double) 0.3f;
                double velocityZ = (Math.random() * 2.0 - 1.0) * (double) 0.3f;
                this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), velocityX, velocityY, velocityZ);
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
            if (!this.getWorld().isClient && this.itemAge >= getDespawnAge()) {
                this.discard();
            }
        }
    }

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
        if (!this.getWorld().isClient) {
            // Generates clientside particles
            this.getWorld().sendEntityStatus(this, (byte) 3);
            // Remove Entity from the server
            this.kill();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ELECTRON;
    }
}
