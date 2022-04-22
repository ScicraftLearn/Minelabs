package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.block.entity.AnimatedChargedBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ChargedBlockEntityRenderer<T extends AnimatedChargedBlockEntity> implements BlockEntityRenderer<T> {
	
    private Context context;

	public ChargedBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    	this.context = ctx;
    }

	@Override
	public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		double offset = (blockEntity.getWorld().getTime() + tickDelta - blockEntity.time) / blockEntity.time_move_ticks;
		matrices.translate(blockEntity.movement_direction.getX() * offset, blockEntity.movement_direction.getY() * offset, blockEntity.movement_direction.getZ() * offset);
		int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
		MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(blockEntity.render_state, matrices, vertexConsumers, lightAbove, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }
}
