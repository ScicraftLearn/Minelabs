package be.uantwerpen.scicraft.fluid;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Fluids {

    // Fluids
    // TODO - https://fabricmc.net/wiki/tutorial:fluids
    public static final AbstractFluid STILL_HNO3 = register(new FluidHNO3.Still(), "still_hno3");
    public static final AbstractFluid STILL_H2O = register(new FluidH2O.Still(), "still_h2o");
    public static final AbstractFluid FLOWING_HNO3 = register(new FluidHNO3.Flowing(), "flowing_hno3");
    public static final AbstractFluid FLOWING_H2O = register(new FluidH2O.Flowing(), "flowing_h2o");

    /**
     * Register a Fluid
     * <p>
     *
     * @param fluid      : Block Object to register
     * @param identifier : String name of the Item
     * @return {@link AbstractFluid}
     */
    private static AbstractFluid register(AbstractFluid fluid, String identifier) {
        return Registry.register(Registry.FLUID, new Identifier(Scicraft.MOD_ID, identifier), fluid);
    }

    /**
     * Main class method
     * Registers all Fluids
     */
    public static void registerFluids() {
        Scicraft.LOGGER.info("registering fluids");

        FluidRenderHandlerRegistry.INSTANCE.register(STILL_HNO3, FLOWING_HNO3, new SimpleFluidRenderHandler(
                new Identifier("minecraft:block/water_still"),
                new Identifier("minecraft:block/water_flow"),
                0xFFCC33
        ));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), STILL_HNO3, FLOWING_HNO3);

        FluidRenderHandlerRegistry.INSTANCE.register(STILL_H2O, FLOWING_H2O, new SimpleFluidRenderHandler(
                new Identifier("minecraft:block/water_still"),
                new Identifier("minecraft:block/water_flow"),
                0x001AFF
        ));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), STILL_H2O, FLOWING_H2O);
    }
}
