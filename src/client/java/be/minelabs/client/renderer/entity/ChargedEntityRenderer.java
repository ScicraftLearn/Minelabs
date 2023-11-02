package be.minelabs.client.renderer.entity;

import be.minelabs.entity.projectile.thrown.ChargedEntity;
import be.minelabs.item.Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class ChargedEntityRenderer<T extends ChargedEntity> extends FlyingItemEntityRenderer<T> {

    protected ChargedEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, 1.5f, false);
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(0, 0.05f, 0);
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();

        Vec3d velocity = Vec3d.ZERO;
        Vec3d velb = Vec3d.ZERO;
        Vec3d velf = Vec3d.ZERO;

        if (MinecraftClient.getInstance().player.getInventory().getArmorStack(3).isOf(Items.FORCE_GLASSES)) {
            velocity = entity.getEField().multiply(30);
            velb = entity.getBField().multiply(1);
            velf = entity.getForce().multiply(30);
        }

        matrices.push();
        matrices.translate(0, entity.getHeight() / 2, 0);
        // velocity
        if (velocity.length() > 0.1) {
            drawLine(matrices, vertexConsumers, velocity, 0);
        }
        if (velb.length() > 0.1) {
            drawLine(matrices, vertexConsumers, velb, 100);
        }
        if (velf.length() > 0.1) {
            drawLine(matrices, vertexConsumers, velf, 200);
        }

        matrices.pop();
    }

    private void drawLine(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Vec3d vector, int line_color) {
        VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLines());

        Vec3d normal = vector.normalize();


        MatrixStack.Entry matrixEntry = matrices.peek();
        lineBuffer.vertex(matrixEntry.getPositionMatrix(), 0, 0, 0)
                .color(line_color, line_color, line_color, 255)
                .normal(matrixEntry.getNormalMatrix(), (float) normal.x, (float) normal.y, (float) normal.z)
                .next();

        lineBuffer.vertex(matrixEntry.getPositionMatrix(), (float) vector.x, (float) vector.y, (float) vector.z)
                .color(line_color, line_color, line_color, 255)
                .normal(matrixEntry.getNormalMatrix(), (float) normal.x, (float) normal.y, (float) normal.z)
                .next();
    }

}
