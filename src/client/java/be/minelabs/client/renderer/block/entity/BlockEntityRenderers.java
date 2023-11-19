package be.minelabs.client.renderer.block.entity;

import be.minelabs.block.entity.BlockEntities;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BlockEntityRenderers {
    public static void onInitializeClient(){
        BlockEntityRendererFactories.register(BlockEntities.MOLOGRAM_BLOCK_ENTITY, MologramBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntities.ELECTRIC_FIELD_SENSOR, ElectricFieldSensorRenderer::new);
        BlockEntityRendererFactories.register(BlockEntities.ERLENMEYER_STAND_BLOCK_ENTITY, ErlenmeyerStandRenderer::new);
    }
}
