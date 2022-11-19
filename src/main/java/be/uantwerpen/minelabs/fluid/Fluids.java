package be.uantwerpen.minelabs.fluid;

import be.uantwerpen.minelabs.Minelabs;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Fluids {

    // Fluids
    // TODO - https://fabricmc.net/wiki/tutorial:fluids
    public static final FlowableFluid STILL_ACID = register(new AcidFluid.Still(), "acid");
    public static final FlowableFluid FLOWING_ACID = register(new AcidFluid.Flowing(), "flowing_acid");

    public static final FlowableFluid STILL_CS2 = register(new CS2Fluid.Still(), "cs2");
    public static final FlowableFluid FLOWING_CS2 = register(new CS2Fluid.Flowing(), "flowing_cs2");

    public static final FlowableFluid STILL_CCl4 = register(new CCl4Fluid.Still(), "ccl4");
    public static final FlowableFluid FLOWING_CCl4 = register(new CCl4Fluid.Flowing(), "flowing_ccl4");

    public static final FlowableFluid STILL_PCl3 = register(new PCl3Fluid.Still(), "pcl3");
    public static final FlowableFluid FLOWING_PCl3 = register(new PCl3Fluid.Flowing(), "flowing_pcl3");

    public static final FlowableFluid STILL_SCl2 = register(new SCl2Fluid.Still(), "pcl3");
    public static final FlowableFluid FLOWING_SCl2 = register(new SCl2Fluid.Flowing(), "flowing_scl2");

    public static final FlowableFluid STILL_NCl3 = register(new NCl3Fluid.Still(), "ncl3");
    public static final FlowableFluid FLOWING_NCl3 = register(new NCl3Fluid.Flowing(), "flowing_ncl3");

    public static final FlowableFluid STILL_HCN = register(new HCNFluid.Still(), "ncl3");
    public static final FlowableFluid FLOWING_HCN = register(new HCNFluid.Flowing(), "flowing_ncl3");

    public static final FlowableFluid STILL_CH4O = register(new CH4OFluid.Still(), "ncl3");
    public static final FlowableFluid FLOWING_CH4O = register(new CH4OFluid.Flowing(), "flowing_ncl3");

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
