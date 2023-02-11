package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ExplosiveReaction extends Reaction {

    private final int power;
    private final Block block;

    public ExplosiveReaction(Block block, int power) {
        this.power = power;
        this.block = block;
    }
    @Override
    protected void react(World world, double x, double y, double z, BlockHitResult hitResult) {
        BlockState blockState = world.getBlockState(hitResult.getBlockPos());
        if (blockState.getBlock() == this.block)
            world.createExplosion(null, x, y, z, power, false, Explosion.DestructionType.DESTROY);
    }

    @Override
    protected void react(World world, double x, double y, double z, EntityHitResult hitResult) {

    }
}
