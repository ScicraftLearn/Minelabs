package be.minelabs.client.renderer.block.entity;

import be.minelabs.block.entity.BlockEntities;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BlockEntityRenderers {
    public static void onInitializeClient(){
        BlockEntityRendererFactories.register(BlockEntities.MOLOGRAM_BLOCK_ENTITY, MologramBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntities.ANIMATED_CHARGED_BLOCK_ENTITY, ChargedBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntities.CHARGED_PLACEHOLDER_BLOCK_ENTITY, ChargedPlaceholderBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntities.ELECTRIC_FIELD_SENSOR, ElectricFieldSensorRenderer::new);
    }
}
