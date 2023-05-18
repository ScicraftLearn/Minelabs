package be.minelabs.client;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.block.entity.BlockEntities;
import be.minelabs.client.event.ClientModsEvents;
import be.minelabs.client.gui.screen.ChargedPointScreen;
import be.minelabs.client.gui.screen.IonicScreen;
import be.minelabs.client.gui.screen.LabChestScreen;
import be.minelabs.client.gui.screen.LewisScreen;
import be.minelabs.client.item.ItemModels;
import be.minelabs.client.network.ClientNetworking;
import be.minelabs.client.renderer.block.entity.ChargedBlockEntityRenderer;
import be.minelabs.client.renderer.block.entity.ChargedPlaceholderBlockEntityRenderer;
import be.minelabs.client.renderer.block.entity.ElectricFieldSensorRenderer;
import be.minelabs.client.renderer.block.entity.MologramBlockEntityRenderer;
import be.minelabs.client.renderer.entity.BalloonEntityRenderer;
import be.minelabs.client.renderer.entity.BohrBlueprintEntityRenderer;
import be.minelabs.client.renderer.entity.EntropyCreeperEntityRenderer;
import be.minelabs.client.renderer.entity.model.BalloonEntityModel;
import be.minelabs.client.renderer.entity.model.EntityModelLayers;
import be.minelabs.client.renderer.model.ModelProvider;
import be.minelabs.entity.Entities;
import be.minelabs.fluid.Fluids;
import be.minelabs.item.Items;
import be.minelabs.screen.ScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.util.Map;


@SuppressWarnings("UNUSED")
@Environment(EnvType.CLIENT)
public class MinelabsClient implements ClientModInitializer {
    @Override()
    public void onInitializeClient() {
        ClientModsEvents.registerEvents();

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ATOM_FLOOR, RenderLayer.getTranslucent());
        //BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PORTAL_BLOCK, RenderLayer.getTranslucent());

        //Register ItemModels
        ItemModels.registerModels();

        ModelLoadingRegistry.INSTANCE.registerResourceProvider(ModelProvider::new);
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            Map<Identifier, Resource> molecules = manager.findResources("models/molecules", (i) -> true);
            for (Identifier resource: molecules.keySet()) {
                out.accept(new Identifier(resource.getNamespace(), resource.getPath().split("models/")[1].split(".json")[0]));
            }
            out.accept(new Identifier(Minelabs.MOD_ID,"block/mologram_beam"));
        });


        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_NUL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_MINUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_PLUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.WEAK_BOSON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEUTRINO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ANTINEUTRINO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.POSITRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ELECTRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEUTRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ANTI_NEUTRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PROTON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ANTI_PROTON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LARGE_SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MEDIUM_SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SMALL_SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SALT_WIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ELECTRIC_FIELD_SENSOR_BLOCK, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MOLOGRAM_BLOCK, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GREEN_FIRE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ERLENMEYER_STAND, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BURNER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MICROSCOPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TUBERACK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LAB_CABIN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LAB_DRAWER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LAB_SINK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LAB_CORNER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LAB_CENTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.CHARGED_POINT_BLOCK, RenderLayer.getCutout());
        // Register rendering for electron entity
        EntityRendererRegistry.register(Entities.SUBATOMIC_PARTICLE_ENTITY_TYPE, FlyingItemEntityRenderer::new);

        EntityRendererRegistry.register(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, BohrBlueprintEntityRenderer::new);

        EntityRendererRegistry.register(Entities.ENTROPY_CREEPER, EntropyCreeperEntityRenderer::new);

        EntityRendererRegistry.register(Entities.BALLOON, BalloonEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(EntityModelLayers.BALLOON_MODEL, BalloonEntityModel::getTexturedModelData);

        BlockEntityRendererFactories.register(BlockEntities.MOLOGRAM_BLOCK_ENTITY, MologramBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntities.ANIMATED_CHARGED_BLOCK_ENTITY, ChargedBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntities.CHARGED_PLACEHOLDER_BLOCK_ENTITY, ChargedPlaceholderBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntities.ELECTRIC_FIELD_SENSOR, ElectricFieldSensorRenderer::new);
        //BlockEntityRendererFactories.register(BlockEntities.LAB_CHEST_BLOCK_ENTITY, LabChestBlockEntityRenderer::new);

        // Register rendering lewis crafting table inventory
        HandledScreens.register(ScreenHandlers.LEWIS_SCREEN_HANDLER, LewisScreen::new);
        HandledScreens.register(ScreenHandlers.IONIC_SCREEN_HANDLER, IonicScreen::new);
        HandledScreens.register(ScreenHandlers.LAB_CHEST_SCREEN_HANDLER, LabChestScreen::new);
        HandledScreens.register(ScreenHandlers.CHARGED_POINT_SCREEN_HANDLER, ChargedPointScreen::new);

        ScreenEvents.BEFORE_INIT.register((a, screen, b, c) -> {
            if (screen instanceof LewisScreen)
                ScreenMouseEvents.afterMouseRelease(screen).register((d, mouseX, mouseY, e) -> ((LewisScreen) screen).getButtonWidget().onClick(mouseX, mouseY));
        });

         // Gas

        // TODO - toevoegen van 02, N2, ... als extra texture voor kleurloze gassen.
        // TODO - enchantment visuals voor zeldzame stoffen

        // GASSES
        registerErlenmeyer(Items.ERLENMEYER_O2, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_N2, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CH4, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_H2, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_NO, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_NO2, 0x991c00, 0);
        registerErlenmeyer(Items.ERLENMEYER_Cl2, 0xE8F48C, 0);
        registerErlenmeyer(Items.ERLENMEYER_CO2, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CO, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_NH3, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_N2O, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_HCl, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_He, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_Ne, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_Ar, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CL2O,0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_H2CO3,0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CH4S,0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CH2O,0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_BH3,0xFFFFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_HF,0xFFFFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_SIH4,0xFFFFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_PH3,0xFFFFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_H2S,0xFFFFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CF4,0xFFFFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_BF3,0xFFFFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_BCL3,0xFFFFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_SO2,0xFFFFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CLF,0xFFFFFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_F2,0xFFFFFF, 0);

        //Fluids
        registerErlenmeyer(Items.ERLENMEYER_HNO3, 0xFFCC33, 0);
        registerErlenmeyer(Items.ERLENMEYER_H2O, 0x3495eb, 0);
        registerErlenmeyer(Items.ERLENMEYER_NCL3, 0xe8dc5a, 0);
        registerErlenmeyer(Items.ERLENMEYER_CS2, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CCL4, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_PCl3, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_SCl2, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_HCN, 0xCCCCFF, 0);
        registerErlenmeyer(Items.ERLENMEYER_CH4O, 0xAFAFAF, 0);
        registerErlenmeyer(Items.ERLENMEYER_SICL4, 0xAFAFAF, 0);

        registerErlenmeyerFluid(Fluids.STILL_HNO3, Fluids.FLOWING_HNO3, 0xA1FFCC33);
        registerErlenmeyerFluid(Fluids.STILL_H2O, Fluids.FLOWING_H2O, 0xA10084FF);
        registerErlenmeyerFluid(Fluids.STILL_NCl3, Fluids.FLOWING_NCl3, 0xA1E8DC5A);
        registerErlenmeyerFluid(Fluids.STILL_HCN, Fluids.FLOWING_HCN, 0xA1CCCCFF);
        registerErlenmeyerFluid(Fluids.STILL_NCl3, Fluids.FLOWING_NCl3, 0xA1e8dc5a); // Default
        registerErlenmeyerFluid(Fluids.STILL_CS2, Fluids.FLOWING_CS2, 0xA1AFAFAF);
        registerErlenmeyerFluid(Fluids.STILL_CCl4, Fluids.FLOWING_CCl4, 0xA1AFAFAF);
        registerErlenmeyerFluid(Fluids.STILL_PCl3, Fluids.FLOWING_PCl3, 0xA1AFAFAF);
        registerErlenmeyerFluid(Fluids.STILL_SCl2, Fluids.FLOWING_SCl2, 0xA1AFAFAF);
        registerErlenmeyerFluid(Fluids.STILL_CH4O, Fluids.FLOWING_CH4O, 0xA1AFAFAF);
        registerErlenmeyerFluid(Fluids.STILL_SiCl4, Fluids.FLOWING_SiCl4, 0xA1AFAFAF);

        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0x3495eb, Blocks.LAB_SINK);

        ClientNetworking.registerHandlers();
    }

    public void registerErlenmeyer(Item item, int color, int index) {
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
    public void registerErlenmeyerFluid(Fluid still, Fluid flowing, int tint) {
        FluidRenderHandlerRegistry.INSTANCE.register(still, flowing, new SimpleFluidRenderHandler(
                new Identifier("minecraft:block/water_still"),
                new Identifier("minecraft:block/water_flow"),
                tint));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), still, flowing);
    }

}

