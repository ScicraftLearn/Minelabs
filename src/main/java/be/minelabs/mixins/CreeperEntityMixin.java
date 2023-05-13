package be.minelabs.mixins;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CreeperEntityMixin change Base minecraft code for own use
 *
 * @author pixar02, JoeyDP
 * Define what class you want to Mixin (Mix in too)
 */
@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Method that is called before the creeper type mob explodes.
     * Can return true to continue with original explosion code or false to skip it.
     *
     * @return Whether to run original explode code.
     */
    @Unique
    public boolean preExplode() {
        return true;
    }

    /**
     * Inject a call to onExplode at the beginning of the explosion function.
     * This determines whether the body is executed.
     */
    @Inject(at = @At("HEAD"), method = "explode", cancellable = true)
    public void injectOnExplode(CallbackInfo info) {
        if (!preExplode()) {
            info.cancel();
        }
    }

    /**
     * Called when the creeper is primed.
     * Call to this method is redirected, so we can override it.
     */
    @Unique
    protected void playPrimedSound() {
        this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 0.5f);
    }

    /**
     * Redirect playSound call when the creeper is primed, so we can override it.
     */
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/CreeperEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
    public void injectPlayPrimedSound(CreeperEntity instance, SoundEvent soundEvent, float v, float p) {
        playPrimedSound();
    }

}
