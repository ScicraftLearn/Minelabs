package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.block.entity.ParticleBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ElectronBlockEntityRenderer<T extends ParticleBlockEntity> implements BlockEntityRenderer<T> {
	
    private Context context;

	public ElectronBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    	this.context = ctx;
    }

	@Override
	public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		if (blockEntity.is_moving) {
			double offset = (blockEntity.getWorld().getTime() + tickDelta - blockEntity.time) / 40;
			matrices.translate(0, 0 + offset, 0);
			MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(blockEntity.getCachedState(), matrices, vertexConsumers, light, overlay);
        }
        matrices.pop();

    }
}
