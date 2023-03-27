package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class DissolveReaction extends Reaction {
    @Override
    protected void react(World world, double x, double y, double z, BlockHitResult hitResult) {
        // TODO: implement
        for(int i = 0; i < 10; i++) {
            MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.CLOUD,
                    x, y, z, 0, 0, 0);
        }
    }

    @Override
    protected void react(World world, double x, double y, double z, EntityHitResult hitResult) {
        for(int i = 0; i < 10; i++) {
            MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.CLOUD,
                    x, y, z, 0, 0, 0);
        }
    }
}
