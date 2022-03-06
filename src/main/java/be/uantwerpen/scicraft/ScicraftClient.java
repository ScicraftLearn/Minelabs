package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.renderer.EntropyCreeperEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


@SuppressWarnings("UNUSED")
public class ScicraftClient implements ClientModInitializer {
    @Override()
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_NUL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_MINUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_PLUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.WEAK_BOSON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEUTRINO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ANTINEUTRINO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.POSITRON, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GREEN_FIRE, RenderLayer.getCutout());

        // Register rendering for electron entity
        EntityRendererRegistry.register(Entities.ELECTRON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.PROTON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.NEUTRON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.ENTROPY_CREEPER, EntropyCreeperEntityRenderer::new);

        // Gas


        registerErlenmeyer(Items.ERLENMEYER_GAS, 0x7F7F7F, 1);
        registerErlenmeyer(Items.ERLENMEYER_FLUID,0x000FF, 1);

        // TODO - toevoegen van 02, N2, ... als extra texture voor kleurloze gassen.
        // TODO - enchanement visuals voor zeldzame stoffen

        // Tier 1
        registerErlenmeyer(Items.ERLENMEYER_02,0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_N2,0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_CH4,0x7F7F7F, 2);

        // Tier 2
        registerErlenmeyer(Items.ERLENMEYER_H2,0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_N0,0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_N02,0x991c00, 2);

//        public static Block ACID = Registry.register(Registry.BLOCK, new Identifier(Scicraft.MOD_ID, "acid"), new FluidBlock(be.uantwerpen.scicraft.item.Items.STILL_ACID, FabricBlockSettings.copy(net.minecraft.block.)){});
    }

    public void registerErlenmeyer(Item item, int color, int index) {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == index) return color;
            return 0x7F7F7F;
        }, item);
    }
}
