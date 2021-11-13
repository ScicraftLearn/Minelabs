package be.uantwerpen.scicraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;


@SuppressWarnings("UNUSED")
public class ScicraftClient implements ClientModInitializer {

    @Override()
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Scicraft.PROTON_BLOCK, RenderLayer.getCutout());
    }
}
