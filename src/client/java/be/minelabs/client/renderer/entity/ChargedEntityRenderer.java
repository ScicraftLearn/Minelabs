package be.minelabs.client.renderer.entity;

import be.minelabs.entity.ChargedEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class ChargedEntityRenderer extends FlyingItemEntityRenderer<ChargedEntity> {

    protected ChargedEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, 1.5f, false);
    }

    @Override
    public void render(ChargedEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(0, 0.05f, 0);
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();

        Vec3d velocity = entity.getVelocity().multiply(30);

        matrices.push();
        matrices.translate(0, entity.getHeight() / 2, 0);
        // velocity
        drawLine(matrices, vertexConsumers, velocity);

        matrices.pop();
    }

    private void drawLine(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Vec3d vector) {
        VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLines());

        int LINE_GRAYSCALE_COLOR = 0;
        Vec3d normal = vector.normalize();


        MatrixStack.Entry matrixEntry = matrices.peek();
        lineBuffer.vertex(matrixEntry.getPositionMatrix(), 0, 0, 0)
                .color(LINE_GRAYSCALE_COLOR, LINE_GRAYSCALE_COLOR, LINE_GRAYSCALE_COLOR, 255)
                .normal(matrixEntry.getNormalMatrix(), (float) normal.x, (float) normal.y, (float) normal.z)
                .next();

        lineBuffer.vertex(matrixEntry.getPositionMatrix(), (float) vector.x, (float) vector.y, (float) vector.z)
                .color(LINE_GRAYSCALE_COLOR, LINE_GRAYSCALE_COLOR, LINE_GRAYSCALE_COLOR, 255)
                .normal(matrixEntry.getNormalMatrix(), (float) normal.x, (float) normal.y, (float) normal.z)
                .next();
    }

}
