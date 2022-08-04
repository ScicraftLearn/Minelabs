package be.uantwerpen.scicraft.fluid;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
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
        return Registry.register(Registry.FLUID, new Identifier(Scicraft.MOD_ID, identifier), fluid);
    }

    /**
     * Main class method
     * Registers all Fluids
     */
    public static void registerFluids() {
        Scicraft.LOGGER.info("registering fluids");
        FluidRenderHandlerRegistry.INSTANCE.register(STILL_ACID, FLOWING_ACID, new SimpleFluidRenderHandler(
                new Identifier("minecraft:block/water_still"),
                new Identifier("minecraft:block/water_flow"),
                0x4CC248
        ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), STILL_ACID, FLOWING_ACID);
    }
}
