package be.minelabs.particle;

import be.minelabs.Minelabs;


public class Particles {

//    public static final DefaultParticleType GAS;


//    private static DefaultParticleType register(String name, boolean alwaysShow) {
//        return (DefaultParticleType) Registry.register(Registry.PARTICLE_TYPE, name);
//    }

//    static {
//        GAS = register("gas", true);
//    }

    public static void onInitialize() {
        Minelabs.LOGGER.info("registering particles");
    }
}