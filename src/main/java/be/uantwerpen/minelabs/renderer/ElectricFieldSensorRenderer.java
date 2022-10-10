package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.block.entity.ElectricFieldSensorBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class ElectricFieldSensorRenderer implements BlockEntityRenderer<ElectricFieldSensorBlockEntity> {

    private final BlockEntityRendererFactory.Context context;

    public ElectricFieldSensorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(ElectricFieldSensorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        Vec3f field = entity.getField().copy();
        ItemStack arrow = new ItemStack(net.minecraft.item.Items.ARROW);

        matrices.push();

        // Center arrow in block
        matrices.translate(0.5, 0.5, 0.5);

        // We want the arrows to point the other direction, so we turn the field vector around.
        field.scale(-1);

        if(field.equals(Vec3f.ZERO)) {
            // default field should be north iso east.
            // positive x is east, so we want to rotate -90 degrees along the y-axis.
            matrices.multiply(Direction.UP.getUnitVector().getDegreesQuaternion(90));
        }else{
            /*
             * This algorithm determines the normal vector of the plane described by the original orientation of the arrow (v) and the target direction (field).
             * It then rotates around this vector with the angle theta between the two vectors to point the arrow in the direction of the field.
             */
            // By default the arrow points in positive x (EAST)
            Vec3f v = new Vec3f(1,0,0);

            // Compute theta with cosine formula.
            double theta = Math.acos(v.dot(field) / Math.sqrt(Math.pow(field.getX(), 2) + Math.pow(field.getY(), 2) + Math.pow(field.getZ(), 2)));

            if(theta == 0 || theta == Math.PI) {
                // When the two vectors are parallel, their cross product does not produce the normal vector of the plane.
                // Instead we set in to one of the infinite valid normal vectors: positive Y.
                v = Direction.UP.getUnitVector();
            } else {
                v.cross(field);
                v.normalize();
            }

            matrices.multiply(v.getRadialQuaternion((float)theta));
        }

        // correct for default arrow inclination of 45 degrees.
        Vec3f x_rotation = new Vec3f(0, 0, 1);
        matrices.multiply(x_rotation.getDegreesQuaternion(-45));

        // make sure arrows are larger when near charged blocks and smaller when further away
        float reference = Math.abs(field.getX()) + Math.abs(field.getY()) + Math.abs(field.getZ());
        if (reference > 1) {
            matrices.scale(1,1,1);
            //matrices.scale(reference, reference, reference);
        } else {
            matrices.scale(Math.min(reference+0.2f, 0.99f),Math.min(reference+0.2f, 0.99f),Math.min(reference+0.2f, 0.99f));
        }

        itemRenderer.renderItem(arrow, ModelTransformation.Mode.GUI,light,overlay,matrices,vertexConsumers,0);

        matrices.pop();
    }
}
