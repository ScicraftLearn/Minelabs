package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class DissolveReaction extends Reaction {

    @Override
    protected void react(World world, BlockPos pos) {
        for (int i = 0; i < 10; i++) {
            MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.CLOUD,
                    pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
        }
    }

    @Override
    public void react(LivingEntity entity) {

    }
}
