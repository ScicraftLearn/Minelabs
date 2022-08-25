package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.block.entity.ChargedPlaceholderBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ChargedPlaceholderBlockEntityRenderer<T extends ChargedPlaceholderBlockEntity> implements BlockEntityRenderer<T> {

    private final Context context;

	public ChargedPlaceholderBlockEntityRenderer(Context ctx) {
    	this.context = ctx;
    }

	@Override
	public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
        matrices.pop();
    }
}
