package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.item.Items;
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
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ElectronEntity extends ThrownItemEntity {
    /**
     * Change the DESPAWN_AGE variable to change the time after which ElectronEntity will despawn
     * Game runs at 20 ticks per second, so DESPAWN_AGE = 100 -> ElectronEntity despawns after 5 seconds
     */
    private static final int DESPAWN_AGE = 100;
    private int itemAge;

    public ElectronEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(true);
    }

    public ElectronEntity(World world, LivingEntity owner) {
        super(Entities.ELECTRON_ENTITY, owner, world);
        setNoGravity(true);
    }

    public ElectronEntity(World world, double x, double y, double z) {
        super(Entities.ELECTRON_ENTITY, x, y, z, world);
        setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ELECTRON_ITEM;
    }

    /**
     * TODO change ths snowball particle effect to custom particle effect?
     *
     * @return ParticleEffect used on collision
     */
    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() { // Needed for particles on collision with the world
        ItemStack itemStack = this.getItem();
        return (ParticleEffect) (itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
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
     * Return gravity = 0 -> no gravity effect on ElectronEntity
     *
     * @return float
     */
    @Override
    protected float getGravity() {
        return 0F;
    }

    /**
     * Updates the ElectronEntity every tick
     */
    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof PlayerEntity && !entity.isAlive()) {
            // Removes the electron when throwing player dies
            this.discard();
        } else {
            super.tick();

            // Logic to despawn electron after 5 seconds
            ++this.itemAge;
            Scicraft.LOGGER.info("item age: " + Integer.toString(this.itemAge));
            if (!this.world.isClient && this.itemAge >= DESPAWN_AGE) {
                Scicraft.LOGGER.info("Killing electron entity");
                this.discard();
            }
        }
    }

    /**
     * TODO custom EntityHit
     * implement custom behaviour for hitting entities like other electrons, protons, neutrons, ...
     */
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
    }

    //TODO implement custom collision for different kind of blocks
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            // Generates clientside particles
            this.world.sendEntityStatus(this, (byte) 3);
            // Remove Entity from the server
            this.kill();
        }
    }
}
