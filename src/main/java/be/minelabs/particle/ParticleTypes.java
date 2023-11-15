package be.minelabs.particle;

import be.minelabs.Minelabs;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ParticleTypes {

    public static final DefaultParticleType BOHR_PLATE_PARTICLE = register(FabricParticleTypes.simple(), "bohrplate");

    private static  DefaultParticleType register(DefaultParticleType particle, String path){
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(Minelabs.MOD_ID, path), particle);
    }


    public static void onInitialize() {
    }
}