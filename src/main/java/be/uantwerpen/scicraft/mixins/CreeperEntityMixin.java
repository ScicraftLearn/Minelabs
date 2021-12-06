package be.uantwerpen.scicraft.mixins;

import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CreeperEntityMixin change Base minecraft code for own use
 *
 * @author pixar02, JoeyDP
 * Define what class you want to Mixin (Mix in too)
 */
@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {

    /**
     * Method that is called before the creeper type mob explodes.
     * Can return true to continue with original explosion code or false to skip it.
     *
     * @return Whether to run original explode code.
     */
    public boolean preExplode() {
        return true;
    }

    /**
     * Inject a call to onExplode at the beginning of the explode function.
     * This determines whether the body is executed.
     */
    @Inject(at = @At("HEAD"), method = "explode", cancellable = true)
    public void injectOnExplode(CallbackInfo info) {
        if (!preExplode()) {
            info.cancel();
        }
    }

}
