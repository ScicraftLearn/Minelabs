package be.minelabs.client.renderer.entity;

import be.minelabs.block.Blocks;
import be.minelabs.entity.ChargedEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ChargedEntityRenderer extends EntityRenderer<ChargedEntity> {

    private final BlockRenderManager blockRenderManager;

    protected ChargedEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        blockRenderManager = ctx.getBlockRenderManager();
    }

    @Override
    public Identifier getTexture(ChargedEntity entity) {
        return null;
    }

    @Override
    public void render(ChargedEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        boolean isPositive = entity.getCharge() >= 0;
        BlockState renderState = isPositive ? Blocks.POSITRON.getDefaultState() : Blocks.ELECTRON.getDefaultState();

        matrices.push();
        matrices.translate(-0.5, -0.25, -0.5);

        blockRenderManager.renderBlockAsEntity(renderState, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();

        Vec3d velocity = entity.getVelocity().multiply(30);

        matrices.push();
        matrices.translate(0, entity.getHeight() / 2, 0);
        // velocity
        drawLine(matrices, vertexConsumers, velocity);

        matrices.pop();

    }

    private void drawLine(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Vec3d vector){
        VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLines());

        int LINE_GRAYSCALE_COLOR = 0;
        Vec3d normal = vector.normalize();


        MatrixStack.Entry matrixEntry = matrices.peek();
        lineBuffer.vertex(matrixEntry.getPositionMatrix(), 0, 0, 0)
                .color(LINE_GRAYSCALE_COLOR, LINE_GRAYSCALE_COLOR, LINE_GRAYSCALE_COLOR, 255)
                .normal(matrixEntry.getNormalMatrix(), (float)normal.x, (float)normal.y, (float)normal.z)
                .next();

        lineBuffer.vertex(matrixEntry.getPositionMatrix(), (float) vector.x, (float) vector.y, (float) vector.z)
                .color(LINE_GRAYSCALE_COLOR, LINE_GRAYSCALE_COLOR, LINE_GRAYSCALE_COLOR, 255)
                .normal(matrixEntry.getNormalMatrix(), (float)normal.x, (float)normal.y, (float)normal.z)
                .next();
    }

}
