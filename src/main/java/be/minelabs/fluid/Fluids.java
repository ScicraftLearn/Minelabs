package be.minelabs.fluid;

import be.minelabs.Minelabs;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Fluids {

    // Fluids
    // TODO - https://fabricmc.net/wiki/tutorial:fluids
    public static final FlowableFluid STILL_HNO3 = register(new HNO3Fluid.Still(), "chemical/hno3");
    public static final FlowableFluid FLOWING_HNO3 = register(new HNO3Fluid.Flowing(), "chemical/flowing_hno3");

    public static final FlowableFluid STILL_H2O = register(new H2OFluid.Still(), "chemical/h2o");
    public static final FlowableFluid FLOWING_H2O = register(new H2OFluid.Flowing(), "chemical/flowing_h2o");

    public static final FlowableFluid STILL_CS2 = register(new CS2Fluid.Still(), "chemical/cs2");
    public static final FlowableFluid FLOWING_CS2 = register(new CS2Fluid.Flowing(), "chemical/flowing_cs2");

    public static final FlowableFluid STILL_CCl4 = register(new CCl4Fluid.Still(), "chemical/ccl4");
    public static final FlowableFluid FLOWING_CCl4 = register(new CCl4Fluid.Flowing(), "chemical/flowing_ccl4");

    public static final FlowableFluid STILL_PCl3 = register(new PCl3Fluid.Still(), "chemical/pcl3");
    public static final FlowableFluid FLOWING_PCl3 = register(new PCl3Fluid.Flowing(), "chemical/flowing_pcl3");

    public static final FlowableFluid STILL_SCl2 = register(new SCl2Fluid.Still(), "chemical/scl2");
    public static final FlowableFluid FLOWING_SCl2 = register(new SCl2Fluid.Flowing(), "chemical/flowing_scl2");

    public static final FlowableFluid STILL_NCl3 = register(new NCl3Fluid.Still(), "chemical/ncl3");
    public static final FlowableFluid FLOWING_NCl3 = register(new NCl3Fluid.Flowing(), "chemical/flowing_ncl3");

    public static final FlowableFluid STILL_HCN = register(new HCNFluid.Still(), "chemical/hcn");
    public static final FlowableFluid FLOWING_HCN = register(new HCNFluid.Flowing(), "chemical/flowing_hcn");

    public static final FlowableFluid STILL_CH4O = register(new CH4OFluid.Still(), "chemical/ch4o");
    public static final FlowableFluid FLOWING_CH4O = register(new CH4OFluid.Flowing(), "chemical/flowing_ch4o");

    public static final FlowableFluid STILL_SiCl4 = register(new SiCl4Fluid.Still(), "chemical/sicl4");
    public static final FlowableFluid FLOWING_SiCl4 = register(new SiCl4Fluid.Flowing(), "chemical/flowing_sicl4");

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
    public static void onInitialize() {
    }
}
