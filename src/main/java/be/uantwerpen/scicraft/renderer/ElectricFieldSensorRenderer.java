package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.block.entity.ElectricFieldSensorBlockEntity;
import be.uantwerpen.scicraft.item.Items;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class ElectricFieldSensorRenderer implements BlockEntityRenderer<ElectricFieldSensorBlockEntity> {

    private final BlockEntityRendererFactory.Context context;

    public ElectricFieldSensorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(ElectricFieldSensorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        Vec3f field = entity.getField();
        ItemStack stack = new ItemStack(Items.ELECTRON);
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(Quaternion.fromEulerXyz(field));

        itemRenderer.renderItem(stack, ModelTransformation.Mode.GUI,light,overlay,matrices,vertexConsumers,0);

//        RenderSystem.setShaderTexture(0, new Identifier("textures/block/andesite.png"));
//        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getCutout());
//        buffer.vertex(-0.5 + field.getX(),1 + field.getY(),0 + field.getZ()).color(1,1,1,1).texture(0,16).light(light).normal(-1,0,0).next();
//        buffer.vertex(0.5 + field.getX(),1 + field.getY(),0 + field.getZ()).color(1,1,1,1).texture(0,16).light(light).normal(-1,0,0).next();
//        buffer.vertex(-0.5,0,0).color(0,0,0,0).texture(0,16).light(light).normal(-1,0,0).next();
//        buffer.vertex(0.5,0,0).color(0,0,0,0).texture(0,16).light(light).normal(-1,0,0).next();
//        buffer.next();
        matrices.pop();
    }
}
