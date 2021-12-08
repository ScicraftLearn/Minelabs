package be.uantwerpen.scicraft.mixins;

import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * ExplosionMixin change Base minecraft code for own use
 *
 * @author JoeyDP
 * Define what class you want to Mixin (Mix in too)
 */
@Mixin(Explosion.class)
public interface ExplosionAccessor{
    @Accessor("x")
    public double getX();
    @Accessor("y")
    public double getY();
    @Accessor("z")
    public double getZ();

    @Accessor("power")
    public float getPower();

    @Accessor("behavior")
    public ExplosionBehavior getBehavior();

}
