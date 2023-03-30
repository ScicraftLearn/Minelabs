package be.uantwerpen.minelabs.particle;

import be.uantwerpen.minelabs.Minelabs;
import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

public class Particles {

//    public static final DefaultParticleType GAS;


//    private static DefaultParticleType register(String name, boolean alwaysShow) {
//        return (DefaultParticleType) Registry.register(Registry.PARTICLE_TYPE, name);
//    }

//    static {
//        GAS = register("gas", true);
//    }

    public static void registerParticles() {
        Minelabs.LOGGER.info("registering particles");
    }
}