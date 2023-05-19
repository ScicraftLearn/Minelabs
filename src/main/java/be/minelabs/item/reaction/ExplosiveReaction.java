package be.minelabs.item.reaction;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class ExplosiveReaction extends Reaction {

    private final int power;
    private final boolean flammable;
    private final boolean pyrophoric;

    public ExplosiveReaction(int power, boolean flammable, boolean pyrophoric) {
        this.power = power;
        this.flammable = flammable;
        this.pyrophoric = pyrophoric;
    }

    @Override
    protected void react(World world, Vec3d pos) {
        if (this.pyrophoric || Utils.isFlameNearby(world, pos, power))
            world.createExplosion(null, pos.x, pos.y, pos.z,
                    power, flammable, World.ExplosionSourceType.BLOCK);

    }

    @Override
    public void react(LivingEntity entity) {

    }
}
