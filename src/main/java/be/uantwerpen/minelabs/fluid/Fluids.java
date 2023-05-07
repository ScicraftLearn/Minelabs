package be.uantwerpen.minelabs.fluid;

import be.uantwerpen.minelabs.Minelabs;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Fluids {

    // Fluids
    // TODO - https://fabricmc.net/wiki/tutorial:fluids
    public static final FlowableFluid STILL_HNO3 = register(new HNO3Fluid.Still(), "hno3");
    public static final FlowableFluid FLOWING_HNO3 = register(new HNO3Fluid.Flowing(), "flowing_hno3");

    public static final FlowableFluid STILL_H2O = register(new H2OFluid.Still(), "h2o");
    public static final FlowableFluid FLOWING_H2O = register(new H2OFluid.Flowing(), "flowing_h2o");

    public static final FlowableFluid STILL_CS2 = register(new CS2Fluid.Still(), "cs2");
    public static final FlowableFluid FLOWING_CS2 = register(new CS2Fluid.Flowing(), "flowing_cs2");

    public static final FlowableFluid STILL_CCl4 = register(new CCl4Fluid.Still(), "ccl4");
    public static final FlowableFluid FLOWING_CCl4 = register(new CCl4Fluid.Flowing(), "flowing_ccl4");

    public static final FlowableFluid STILL_PCl3 = register(new PCl3Fluid.Still(), "pcl3");
    public static final FlowableFluid FLOWING_PCl3 = register(new PCl3Fluid.Flowing(), "flowing_pcl3");

    public static final FlowableFluid STILL_SCl2 = register(new SCl2Fluid.Still(), "scl2");
    public static final FlowableFluid FLOWING_SCl2 = register(new SCl2Fluid.Flowing(), "flowing_scl2");

    public static final FlowableFluid STILL_NCl3 = register(new NCl3Fluid.Still(), "ncl3");
    public static final FlowableFluid FLOWING_NCl3 = register(new NCl3Fluid.Flowing(), "flowing_ncl3");

    public static final FlowableFluid STILL_HCN = register(new HCNFluid.Still(), "hcn");
    public static final FlowableFluid FLOWING_HCN = register(new HCNFluid.Flowing(), "flowing_hcn");

    public static final FlowableFluid STILL_CH4O = register(new CH4OFluid.Still(), "ch4o");
    public static final FlowableFluid FLOWING_CH4O = register(new CH4OFluid.Flowing(), "flowing_ch4o");

    public static final FlowableFluid STILL_SiCl4 = register(new SiCl4Fluid.Still(), "sicl4");
    public static final FlowableFluid FLOWING_SiCl4 = register(new SiCl4Fluid.Flowing(), "flowing_sicl4");

    /**
     * Register a Fluid
     * <p>
     *
     * @param fluid      : Block Object to register
     * @param identifier : String name of the Item
     * @return {@link AbstractFluid}
     */
    private static FlowableFluid register(AbstractFluid fluid, String identifier) {
        return Registry.register(Registries.FLUID, new Identifier(Minelabs.MOD_ID, identifier), fluid);
    }

    /**
     * Main class method
     * Registers all Fluids
     */
    public static void registerFluids() {
        Minelabs.LOGGER.info("registering fluids");
    }
}
