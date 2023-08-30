package be.minelabs.client.renderer.block.entity;

import be.minelabs.block.entity.ErlenmeyerBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ErlenmeyerStandRenderer implements BlockEntityRenderer<ErlenmeyerBlockEntity> {
    private final BlockEntityRendererFactory.Context context;

    public ErlenmeyerStandRenderer(BlockEntityRendererFactory.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(ErlenmeyerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // TODO : something....
    }
}
