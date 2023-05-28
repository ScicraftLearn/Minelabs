package be.minelabs.client.item;

import be.minelabs.fluid.Fluids;
import be.minelabs.item.Items;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class Erlenmeyers {

    public static void onInitializeClient(){
        // TODO - toevoegen van 02, N2, ... als extra texture voor kleurloze gassen.
        // TODO - enchantment visuals voor zeldzame stoffen

        // GASSES
        registerErlenmeyer(Items.ERLENMEYER_O2, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_N2, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CH4, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_H2, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_NO, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_NO2, 0x991c00, 0);
        registerErlenmeyer(Items.ERLENMEYER_Cl2, 0xE8F48C, 0);
        registerErlenmeyer(Items.ERLENMEYER_CO2, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CO, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_NH3, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_N2O, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_HCl, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_He, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_Ne, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_Ar, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CL2O,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_H2CO3,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CH4S,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CH2O,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_BH3,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_HF,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_SIH4,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_PH3,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_H2S,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CF4,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_BF3,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_BCL3,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_SO2,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CLF,0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_F2,0xCFCFFF, 0);

        //Fluids
        registerErlenmeyer(Items.ERLENMEYER_HNO3, 0xFFCC33, 0);
        registerErlenmeyer(Items.ERLENMEYER_H2O, 0x3495eb, 0);
        registerErlenmeyer(Items.ERLENMEYER_NCL3, 0xe8dc5a, 0);
        registerErlenmeyer(Items.ERLENMEYER_CS2, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CCL4, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_PCl3, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_SCl2, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_HCN, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CH4O, 0xCFCFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_SICL4, 0xCFCFFF, 0);

        registerErlenmeyerFluid(Fluids.STILL_HNO3, Fluids.FLOWING_HNO3, 0xA1FFCC33);
        registerErlenmeyerFluid(Fluids.STILL_H2O, Fluids.FLOWING_H2O, 0xA10084FF);
        registerErlenmeyerFluid(Fluids.STILL_NCl3, Fluids.FLOWING_NCl3, 0xA1E8DC5A);
        registerErlenmeyerFluid(Fluids.STILL_HCN, Fluids.FLOWING_HCN, 0xA1CFCFFF);
        registerErlenmeyerFluid(Fluids.STILL_NCl3, Fluids.FLOWING_NCl3, 0xA1e8dc5a); // Default
        registerErlenmeyerFluid(Fluids.STILL_CS2, Fluids.FLOWING_CS2, 0xA1CFCFFF);
        registerErlenmeyerFluid(Fluids.STILL_CCl4, Fluids.FLOWING_CCl4, 0xA1CFCFFF);
        registerErlenmeyerFluid(Fluids.STILL_PCl3, Fluids.FLOWING_PCl3, 0xA1CFCFFF);
        registerErlenmeyerFluid(Fluids.STILL_SCl2, Fluids.FLOWING_SCl2, 0xA1CFCFFF);
        registerErlenmeyerFluid(Fluids.STILL_CH4O, Fluids.FLOWING_CH4O, 0xA1CFCFFF);
        registerErlenmeyerFluid(Fluids.STILL_SiCl4, Fluids.FLOWING_SiCl4, 0xA1CFCFFF);

    }

    public static void registerErlenmeyer(Item item, int color, int index) {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == index) return color;
            return 0xFFFFFF;
        }, item);
    }

    /**
     * Fully register the Fluid rendering, water texture recolored and transparent
     *
     * @param still   :  Still Fluid
     * @param flowing : Flowing Fluid
     * @param tint    : Tint in ARGB (alpha / R / G /B) use as hex: 0x........
     */
    public static void registerErlenmeyerFluid(Fluid still, Fluid flowing, int tint) {
        FluidRenderHandlerRegistry.INSTANCE.register(still, flowing, new SimpleFluidRenderHandler(
                new Identifier("minecraft:block/water_still"),
                new Identifier("minecraft:block/water_flow"),
                tint));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), still, flowing);
    }

}
