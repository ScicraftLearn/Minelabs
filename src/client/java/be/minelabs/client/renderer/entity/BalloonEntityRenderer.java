package be.minelabs.client.renderer.entity;

import be.minelabs.client.renderer.entity.model.BalloonEntityModel;
import be.minelabs.client.renderer.entity.model.EntityModelLayers;
import be.minelabs.entity.BalloonEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class BalloonEntityRenderer extends MobEntityRenderer<BalloonEntity, BalloonEntityModel> {


    public BalloonEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BalloonEntityModel(context.getPart(EntityModelLayers.BALLOON_MODEL)), 0.7F);
    }

    @Override
    public Identifier getTexture(BalloonEntity entity) {
        return new Identifier("minelabs:textures/entity/balloon/balloon.png");
    }

    @Override
    public void render(BalloonEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        Quaternionf rot = RotationAxis.POSITIVE_Y.rotationDegrees(mobEntity.getRotationY());
        matrixStack.multiply(rot);
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}