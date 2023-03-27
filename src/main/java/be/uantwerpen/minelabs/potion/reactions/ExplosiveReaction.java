package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
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
    protected void react(World world, double x, double y, double z, BlockHitResult hitResult) {
        if(this.pyrophoric || ReactionUtils.isBlockNearby(world, hitResult.getBlockPos(), Blocks.TORCH.getDefaultState(), 3))
            world.createExplosion(null, x, y, z, power, flammable, Explosion.DestructionType.DESTROY);
    }

    @Override
    protected void react(World world, double x, double y, double z, EntityHitResult hitResult) {
        if(this.pyrophoric || ReactionUtils.isBlockNearby(world, hitResult.getEntity().getBlockPos(), Blocks.TORCH.getDefaultState(), 3))
            world.createExplosion(null, x, y, z, power, flammable, Explosion.DestructionType.DESTROY);
    }
}
