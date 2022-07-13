package be.uantwerpen.scicraft.mixins;

import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * ExplosionMixin change Base minecraft code for own use
 *
 * @author JoeyDP
 * Define what class you want to Mixin (Mix in too)
 */
@Mixin(Explosion.class)
public interface ExplosionAccessor {
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
