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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.function.Supplier;

/**
 * Instances of this class are used for building atoms.
 * They include: Proton, Neutron, Electron, Anti-proton, Anti-neutron and positron.
 */
public class SubatomicParticleBase extends ThrownItemEntity {
    private int itemAge;
    private final Item item;

    public SubatomicParticleBase(EntityType<? extends ThrownItemEntity> entityType, World world, ItemStack itemStack) {
        super(entityType, world);
        item = itemStack.getItem();
        setItem(itemStack);
        setNoGravity(true);
    }

    public SubatomicParticleBase(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world, ItemStack itemStack) {
        super(entityType, d, e, f, world);
        item = itemStack.getItem();
        setItem(itemStack);
        setNoGravity(true);
    }

    public SubatomicParticleBase(EntityType<? extends ThrownItemEntity> entityType, LivingEntity owner, World world, ItemStack itemStack) {
        super(entityType, owner, world);
        item = itemStack.getItem();
        setItem(itemStack);
        setNoGravity(true);
    }

    // Prevent slowdown over time from SubatomicParticle
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

    /**
     * TODO change ths snowball particle effect to custom particle effect?
     *
     * @return ParticleEffect used on collision
     */
    @Environment(EnvType.CLIENT)
    protected ParticleEffect getParticleParameters() { // Needed for particles on collision with the world
        ItemStack itemStack = this.getStack();
        return itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
    }

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

    /**
     * TODO custom EntityHit
     * implement custom behaviour for hitting entities like other electrons, protons, neutrons, ...
     */
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        // notify bohr plate of collision
        if (entityHitResult.getEntity() instanceof BohrBlueprintEntity bohrBlueprintEntity){
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

    @Override
    protected Item getDefaultItem() {
        return item;
    }
}
