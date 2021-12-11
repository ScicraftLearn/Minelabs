package be.uantwerpen.scicraft.renderer;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class SchrodingersBoxEntityRenderer extends ChestBlockEntityRenderer {
    public SchrodingersBoxEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }
}
