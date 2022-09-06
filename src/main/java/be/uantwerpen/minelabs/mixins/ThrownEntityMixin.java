package be.uantwerpen.minelabs.mixins;

import be.uantwerpen.minelabs.entity.SubatomicParticle;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ThrownEntity.class)
public abstract class ThrownEntityMixin extends ProjectileEntity {
    protected ThrownEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    public float getSlowdownFactor() {
        if (this.isTouchingWater()) {
            return 0.8f;
        }
        return 0.99f;
    }

    /**
     * Mixin that hooks into the tick() method of ThrownEntity
     * This method injects before the following function call: this.setVelocity(blockPos.multiply((double)g));
     * First a check is done if the entity is of type ElectronEntity
     * If this is the case we update the position without changing the velocity after that we cancel the original method.
     * Else just continue running the original method.
     */
    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/ThrownEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void injected(CallbackInfo ci, HitResult hitResult, boolean bl, Vec3d blockPos, double blockState, double d, double e, float g) {
        ProjectileEntity self = this;
        // IDE might say the following expression is always false.
        // This is not the case, I tested it ingame.

        if (self instanceof SubatomicParticle) {
            // I used command: /tp @p 0 100 0 0 0
            // This teleports player to block x=0 y=100 z=0 and sets the looking direction of the player
            // Then don't move and use an electron. It is easy to verify that the electron flies straight and constant speed.
//            Minelabs.LOGGER.info(this.getVelocity());
//            Minelabs.LOGGER.info(this.getPos());
            this.setVelocity(blockPos.multiply(getSlowdownFactor()));
            // Gravity gets applied here in the tick method of ThrownEntity that code isn't needed for entities of this class
            // If modified/original gravity code is needed it can be inserted here
            this.setPosition(blockState, d, e);
            ci.cancel();
        }
    }
}
