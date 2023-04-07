package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

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
    protected void react(World world, Vec3d pos, BlockPos blockPos) {
        if (this.pyrophoric || Utils.isFlameNearby(world, blockPos, power))
            world.createExplosion(null, pos.x, pos.y, pos.z, power, flammable, Explosion.DestructionType.DESTROY);
    }
}
