package be.uantwerpen.scicraft.particle;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Particles {

    public static final DefaultParticleType HOLOGRAM_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Scicraft.MOD_ID, "hologram_particle"),
                HOLOGRAM_PARTICLE);
    }
}
