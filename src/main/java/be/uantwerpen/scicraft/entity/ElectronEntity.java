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
import net.minecraft.network.Packet;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
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
    }

    public ElectronEntity(World world, LivingEntity owner) {
        super(Entities.ELECTRON_ENTITY, owner, world);
    }

    public ElectronEntity(World world, double x, double y, double z) {
        super(Entities.ELECTRON_ENTITY, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ELECTRON_ITEM;
    }

    /**
     * Packet to inform integrated or dedicated server about spawned ThrownItemEntity
     * @return Packet
     */
    @Override
    public Packet createSpawnPacket() {
        return EntitySpawnPacket.create(this, new Identifier(Scicraft.MOD_ID, "spawn_packet"));
    }

    /**
     * TODO change ths snowball particle effect to custom particle effect?
     * @return ParticleEffect used on collision
     */
    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() { // Needed for particles on collision with the world
        ItemStack itemStack = this.getItem();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    /**
     * Handle status 3 -> generate particles onCollision
     * @param status byte with status to handle
     */
    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) { // Needed for particles on collision with the world
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    /**
     * Removes the initial random adjustment on throw of projectiles in minecraft
     * @param x distance along x-axis
     * @param y distance along y-axis
     * @param z distance along z-axis
     * @param speed velocity of the item
     * @param divergence unused parameter
     */
    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        Vec3d vec3d = (new Vec3d(x, y, z)).normalize().multiply((double)speed);
        this.setVelocity(vec3d);
        double d = vec3d.horizontalLength();
        this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D));
        this.setPitch((float)(MathHelper.atan2(vec3d.y, d) * 57.2957763671875D));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }

    /**
     * Return gravity = 0 -> no gravity effect on ElectronEntity
     * @return float
     */
    @Override
    protected float getGravity() {
        return 0.0F;
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
            if (!this.world.isClient && this.itemAge >= DESPAWN_AGE) {
                this.discard();
            }
        }
    }

    /** TODO custom EntityHit
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
            this.world.sendEntityStatus(this, (byte)3);
            // Remove Entity from the server
            this.kill();
        }
    }
}
