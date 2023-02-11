package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ExplosiveReaction extends Reaction {

    private final int power;
    private final boolean flammable;

    public ExplosiveReaction(int power, boolean flammable) {
        this.power = power;
        this.flammable = flammable;
    }
    @Override
    protected void react(World world, double x, double y, double z, BlockHitResult hitResult) {
        world.createExplosion(null, x, y, z, power, flammable, Explosion.DestructionType.DESTROY);
    }

    @Override
    protected void react(World world, double x, double y, double z, EntityHitResult hitResult) {

    }
}
