package be.uantwerpen.scicraft.mixins;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.entity.ElectronEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Mixin that hooks into the tick() method of ThrownEntity
 * This method redirects the following function call: this.setVelocity(blockPos.multiply((double)g));
 * An additional check is added if the entity(this) is of type ElectronEntity
 * Based on this check the setVelocity method is executed or not
 *
 * Not executing setVelocity makes the ThrownEntity not slowdown over time
 */
@Mixin(ThrownEntity.class)
public abstract class ThrownEntityMixin extends ProjectileEntity{
    protected ThrownEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/ThrownEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void injected(CallbackInfo ci, HitResult hitResult, boolean bl, Vec3d blockPos, double blockState, double d, double e, float g) {
        ProjectileEntity self = this;
        if (self instanceof ElectronEntity) {
            Scicraft.LOGGER.info("Ding dong IDE you are wrong");
            this.setPosition(blockState, d, e);
            ci.cancel();
        }
    }
}
