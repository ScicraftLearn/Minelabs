package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.block.Blocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;


@SuppressWarnings("UNUSED")
public class ScicraftClient implements ClientModInitializer {

    @Override()
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PROTON_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_NUL_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_MINUS_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_PLUS_BLOCK, RenderLayer.getCutout());
    }
}
