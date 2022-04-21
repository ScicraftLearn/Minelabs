package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.block.entity.AnimatedChargedBlockEntity;
import be.uantwerpen.scicraft.block.entity.ChargedPlaceholderBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ChargedPlaceholderBlockEntityRenderer<T extends ChargedPlaceholderBlockEntity> implements BlockEntityRenderer<T> {

    private Context context;

	public ChargedPlaceholderBlockEntityRenderer(Context ctx) {
    	this.context = ctx;
    }

	@Override
	public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
        matrices.pop();
    }
}
