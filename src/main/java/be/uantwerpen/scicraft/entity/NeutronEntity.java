package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class NeutronEntity extends SubatomicParticle {
    public NeutronEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public NeutronEntity(World world, LivingEntity owner) {
        super(Entities.NEUTRON_ENTITY, owner, world);
    }

    public NeutronEntity(World world, double x, double y, double z) {
        super(Entities.NEUTRON_ENTITY, x, y, z, world);
    }

    /**
     * Change getDespawnAge() to change the time after which NeutronEntity will despawn
     * Game normally runs at 20 ticks per second, so return 100 -> NeutronEntity despawns after 5 seconds
     */
    protected int getDespawnAge() {
        return 100;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.NEUTRON;
    }

    /**
     * TODO change ths snowball particle effect to custom particle effect?
     *
     * @return ParticleEffect used on collision
     */
    @Environment(EnvType.CLIENT)
    protected ParticleEffect getParticleParameters() { // Needed for particles on collision with the world
        ItemStack itemStack = this.getItem();
        return (ParticleEffect) (itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    /**
     * Updates the NeutronEntity every tick
     */
    public void tick() {
        super.tick();
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
    }
}
