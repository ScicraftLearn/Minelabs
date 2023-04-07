package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class DissolveReaction extends Reaction {

    public DissolveReaction() {}

    public DissolveReaction(List<Block> whiteList, List<Block> blackList) {
        super(whiteList, blackList);
    }

    @Override
    protected void react(World world, Vec3d pos, BlockPos blockPos) {
        for (int i = 0; i < 10; i++) {
            MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.CLOUD,
                    pos.x, pos.y, pos.z, 0, 0, 0);
        }
    }
}
