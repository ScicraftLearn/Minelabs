package be.uantwerpen.scicraft.mixins;

import be.uantwerpen.scicraft.entity.EntropyCreeperEntity;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.Iterator;

/**
 * CreeperEntityMixin change Base minecraft code for own use
 *
 * @author pixar02
 * <p>
 * Define what class you want to Mixin (Mix in too)
 */
@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {

    /**
     * Hostile entity constructor
     * Just super call, allows for access to public/protected fields/methods
     *
     * @param entityType : EntityType that is of HostileEntity
     * @param world : world location of entity
     */
    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Get the Explosion Radius from Creeper Entity
     * private field, using Mixin accessor
     *
     * @return int: private explosionRadius from CreeperEntity
     */
    @Accessor("explosionRadius")
    public abstract int getExplosionRadius();

    /**
     * RedirectExplode
     *
     * Redirect the explosion call from CreeperEntity in tick() to onExplode(...)
     * Allows a change in the code
     *
     * @param creeperEntity : creeper entity that is about to explode
     */
    @Redirect(method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/CreeperEntity;explode()V"))
    public void redirectExplode(CreeperEntity creeperEntity) {
        this.onExplode(creeperEntity);
    }

    /**
     * onExplode is called when a creeper is going to explode (Mixin redirect)
     *
     * @param entity : CreeperEntity or any derived class of it
     */
    public void onExplode(CreeperEntity entity) {
        if (!entity.world.isClient) {
            if (entity instanceof EntropyCreeperEntity entropyCreeper) {
                entropyCreeper.onExplode();
            } else {
                Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
                float f = entity.shouldRenderOverlay() ? 2.0F : 1.0F;
                this.dead = true;
                this.world.createExplosion(entity, this.getX(), this.getY(), this.getZ(), (float) getExplosionRadius() * f, destructionType);
            }
            this.discard();
            this.spawnEffectsCloud();
        }
    }

    /**
     * SpawnEffectsCloud from CreeperEntity
     * Private in default class ('overriding like this')
     */
    public void spawnEffectsCloud() {
        Collection<StatusEffectInstance> collection = this.getStatusEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.world, this.getX(), this.getY(), this.getZ());
            areaEffectCloudEntity.setRadius(2.5F);
            areaEffectCloudEntity.setRadiusOnUse(-0.5F);
            areaEffectCloudEntity.setWaitTime(10);
            areaEffectCloudEntity.setDuration(areaEffectCloudEntity.getDuration() / 2);
            areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
            Iterator var3 = collection.iterator();

            while (var3.hasNext()) {
                StatusEffectInstance statusEffectInstance = (StatusEffectInstance) var3.next();
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffectInstance));
            }

            this.world.spawnEntity(areaEffectCloudEntity);
        }
    }
}
