package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.block.entity.ElectricFieldSensorBlockEntity;
import be.uantwerpen.scicraft.item.Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
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
        ItemStack arrow = new ItemStack(net.minecraft.item.Items.ARROW);

        // first make sure the settings are correct for the electron field
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        //Quaternion quaternion = new Quaternion(field.getX(), field.getY(), field.getZ(), 0);
        //matrices.multiply(Quaternion.fromEulerXyz(quaternion.toEulerXyz()));
        Vec3f x = new Vec3f(1,0,0);
        Vec3f y = new Vec3f(0,1,0);
        Vec3f z = new Vec3f(0,0,1);
        double x_angle = Math.acos((x.dot(field)) / (Math.sqrt(Math.pow(x.getX(), 2) + Math.pow(x.getY(), 2) + Math.pow(x.getZ(), 2)) * Math.sqrt(Math.pow(field.getX(), 2) + Math.pow(field.getY(), 2) + Math.pow(field.getZ(), 2))));
        double y_angle = Math.acos((y.dot(field)) / (Math.sqrt(Math.pow(y.getX(), 2) + Math.pow(y.getY(), 2) + Math.pow(y.getZ(), 2)) * Math.sqrt(Math.pow(field.getX(), 2) + Math.pow(field.getY(), 2) + Math.pow(field.getZ(), 2))));
        double z_angle = Math.acos((z.dot(field)) / (Math.sqrt(Math.pow(z.getX(), 2) + Math.pow(z.getY(), 2) + Math.pow(z.getZ(), 2)) * Math.sqrt(Math.pow(field.getX(), 2) + Math.pow(field.getY(), 2) + Math.pow(field.getZ(), 2))));
        Vec3f euler = new Vec3f((float) x_angle * 57.2957795f, (float) y_angle * 57.2957795f, (float) z_angle * 57.2957795f);
        matrices.multiply(Quaternion.fromEulerXyzDegrees(euler));

        // now render the electron with the field
        itemRenderer.renderItem(stack, ModelTransformation.Mode.GUI,light,overlay,matrices,vertexConsumers,0);

        // the field related things are calculated and rendered so now we start with the arrow
        // for this, we use the same matrices as above and we can modify them since we don't need the old values

        //matrices.multiply(quaternion);
//        if (field.getX() != 0 || field.getY() != 0 || field.getZ() != 0) {
//            System.out.println("-----");
//            System.out.println(field.getX());
//            System.out.println(field.getY());
//            System.out.println(field.getZ());
//            System.out.println("-----");
//        }
        Vec3f y_rotation = new Vec3f(0, -1, 0);
        matrices.multiply(new Quaternion(y_rotation, 90.f, true));

        Vec3f x_rotation = new Vec3f(0, 0, -1);
        matrices.multiply(new Quaternion(x_rotation, 45.f, true));
        //matrices.translate(0.5,1,0);

        // now also draw the arrow
        itemRenderer.renderItem(arrow, ModelTransformation.Mode.GUI,light,overlay,matrices,vertexConsumers,0);


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
