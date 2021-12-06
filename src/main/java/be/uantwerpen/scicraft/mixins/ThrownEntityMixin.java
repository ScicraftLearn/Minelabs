package be.uantwerpen.scicraft.mixins;

import be.uantwerpen.scicraft.entity.ElectronEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin that hooks into the tick() method of ThrownEntity
 * This method redirects the following function call: this.setVelocity(blockPos.multiply((double)g));
 * An additional check is added if the entity(this) is of type ElectronEntity
 * Based on this check the setVelocity method is executed or not
 *
 * Not executing setVelocity makes the ThrownEntity not slowdown over time
 */
@Mixin(ThrownEntity.class)
public class ThrownEntityMixin {
    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/ThrownEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void injected(ThrownEntity entity, Vec3d velocity) {
        if (!(entity instanceof ElectronEntity)) {
            entity.setVelocity(velocity);
        }
    }
}
