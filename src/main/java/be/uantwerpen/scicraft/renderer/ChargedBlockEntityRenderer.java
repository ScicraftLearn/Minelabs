package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.block.entity.ChargedBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ChargedBlockEntityRenderer<T extends ChargedBlockEntity> implements BlockEntityRenderer<T> {
	
    private Context context;

	public ChargedBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    	this.context = ctx;
    }

	@Override
	public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		if (blockEntity.is_moving) {
			double offset = (blockEntity.getWorld().getTime() + tickDelta - blockEntity.time) / blockEntity.time_move_ticks;
			matrices.translate(blockEntity.movement_direction.getX() * offset, blockEntity.movement_direction.getY() * offset, blockEntity.movement_direction.getZ() * offset);
			MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(blockEntity.getCachedState(), matrices, vertexConsumers, light, overlay);
        }
        matrices.pop();

    }
}
