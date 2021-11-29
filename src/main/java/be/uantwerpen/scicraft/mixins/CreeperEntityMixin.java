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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.Iterator;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {

    @Shadow
    protected abstract void explode();

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/CreeperEntity;explode()V"))
    public void redirectExplode(CreeperEntity creeperEntity) {
        this.onExplode(creeperEntity);
    }

    public void onExplode(CreeperEntity entity) {
        if (!entity.world.isClient) {
            if (entity instanceof EntropyCreeperEntity) {
                EntropyCreeperEntity entropyCreeper = (EntropyCreeperEntity) entity;
                entropyCreeper.onExplode();
            } else {
                Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
                float f = entity.shouldRenderOverlay() ? 2.0F : 1.0F;
                this.dead = true;
                this.world.createExplosion(entity, entity.getX(), entity.getY(), entity.getZ(), (float) 3 * f, destructionType);
                this.discard();
            }
            this.spawnEffectsCloud();
        }
    }

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
