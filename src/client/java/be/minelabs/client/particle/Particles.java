package be.minelabs.client.particle;

import be.minelabs.particle.ParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;

@Environment(EnvType.CLIENT)
public class Particles {


    public static void onInitializeClient() {

        ParticleFactoryRegistry.getInstance().register(ParticleTypes.BOHR_PLATE_PARTICLE, FlameParticle.Factory::new);
    }
}
