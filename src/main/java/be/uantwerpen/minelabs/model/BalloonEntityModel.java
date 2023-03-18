package be.uantwerpen.minelabs.model;

import be.uantwerpen.minelabs.entity.BalloonEntity;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

public class BalloonEntityModel extends EntityModel<BalloonEntity> {

    private final ModelPart bb_main;

    public BalloonEntityModel(ModelPart root) {
        this.bb_main = root.getChild("main");
    }

    @Override
    public void setAngles(BalloonEntity entity, float limbAngle, float limbDistance,
                          float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay,
                       float red, float green, float blue, float alpha) {
        ImmutableList.of(this.bb_main).forEach((modelRenderer) -> {
            modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        });
    }

    public static TexturedModelData getTexturedModelData() {
        // TODO: fix graphical issues
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData2 = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.NONE);


//        modelPartData2.addChild("bone0", ModelPartBuilder.create().uv(0, 0)
//                .cuboid(-1.0F, 20.0F, -1.0F, 2.0F, 0.0F, 2.0F),
//                ModelTransform.pivot(0F, 0F, 0F));
        modelPartData2.addChild("bone1", ModelPartBuilder.create().uv(0, 2)
                        .cuboid(-0.5F, 23.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                ModelTransform.pivot(0F, 0F, 0F));
        modelPartData2.addChild("bone2", ModelPartBuilder.create().uv(0, 31)
                        .cuboid(-2.0F, 21, -2.0F, 4.0F, 2.0F, 4.0F),
                ModelTransform.pivot(0F, 0F, 0F));
        modelPartData2.addChild("bone3", ModelPartBuilder.create().uv(0, 20)
                        .cuboid(-4.0F, 8.0F, -4.0F, 8.0F, 13.0F, 8.0F),
                ModelTransform.pivot(0F, 0F, 0F));
        modelPartData2.addChild("bone4", ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-6.0F, 10.0F, -6.0F, 12.0F, 8.0F, 12.0F),
                ModelTransform.pivot(0F, 0F, 0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}