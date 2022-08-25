package be.uantwerpen.minelabs.fluid;

import be.uantwerpen.minelabs.Minelabs;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Fluids {

    // Fluids
    // TODO - https://fabricmc.net/wiki/tutorial:fluids
    public static final AbstractFluid STILL_ACID = register(new AcidFluid.Still(), "acid");
    public static final AbstractFluid FLOWING_ACID = register(new AcidFluid.Flowing(), "flowing_acid");

    /**
     * Register a Fluid
     * <p>
     *
     * @param fluid      : Block Object to register
     * @param identifier : String name of the Item
     * @return {@link AbstractFluid}
     */
    private static AbstractFluid register(AbstractFluid fluid, String identifier) {
        return Registry.register(Registry.FLUID, new Identifier(Minelabs.MOD_ID, identifier), fluid);
    }

    /**
     * Main class method
     * Registers all Fluids
     */
    public static void registerFluids() {
        Minelabs.LOGGER.info("registering fluids");
    }
}
