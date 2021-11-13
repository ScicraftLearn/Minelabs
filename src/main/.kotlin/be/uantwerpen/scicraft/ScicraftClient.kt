package be.uantwerpen.scicraft

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.render.RenderLayer


@Suppress("UNUSED")
class ScicraftClient: ClientModInitializer {

    override fun onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Scicraft.PROTON_BLOCK, RenderLayer.getCutout());
    }


}