package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.renderer.EntropyCreeperEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;


@SuppressWarnings("UNUSED")
public class ScicraftClient implements ClientModInitializer {

    @Override()
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ELECTRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PROTON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_NUL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_MINUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_PLUS, RenderLayer.getCutout());

        EntityRendererRegistry.register(Entities.ENTROPY_CREEPER, EntropyCreeperEntityRenderer::new);
    }
}
