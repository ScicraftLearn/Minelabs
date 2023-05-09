package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.block.entity.ElectricFieldSensorBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ElectricFieldSensorRenderer implements BlockEntityRenderer<ElectricFieldSensorBlockEntity> {

    private final BlockEntityRendererFactory.Context context;
    private final static float max_field = 3;

    public ElectricFieldSensorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(ElectricFieldSensorBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Vector3f field = new Vector3f(entity.getField());

        matrices.push();

        ItemStack arrow;
        float reference = 0.2f;

        // Center arrow in block
        matrices.translate(0.5, 0.5, 0.5);

        if(field.equals(new Vector3f())) {
            // Don't display arrow if the field is zero
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
            // arrow = gray arrow
            arrow = new ItemStack(net.minecraft.item.Items.DARK_OAK_BUTTON);
        } else {
            // We want the arrows to point the other direction, so we turn the field vector around.
            field.mul(-1);

            /*
             * This algorithm determines the normal vector of the plane described by the original orientation
             * of the arrow (v) and the target direction (field).
             * It then rotates around this vector with the angle theta between the two vectors to point
             * the arrow in the direction of the field.
             *
             * By default, the arrow points in negative x (WEST)
             */
            Vector3f v = Direction.WEST.getUnitVector();

            // Compute theta with cosine formula.
            double theta = Math.acos(v.dot(field) * MathHelper.fastInverseSqrt(field.dot(field)));
            v.cross(field);
            if (v.equals(new Vector3f())) {
                // When the two vectors are parallel, their cross product does not produce the normal vector of the plane.
                // Instead, we set in to one of the infinite valid normal vectors: positive Y.
                v = Direction.UP.getUnitVector();
            } else {
                v.normalize();
            }
            matrices.multiply(v.getRadialQuaternion((float)theta));

            // correct for default arrow inclination of 45 degrees.
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-45));

            // make sure arrows are larger when near charged blocks and smaller when further away
            reference = (float) Math.sqrt(field.dot(field)) / max_field + .2f;

            if (reference > .9f) {
                reference = .9f + reference / 640f;
                arrow = new ItemStack(net.minecraft.item.Items.SPECTRAL_ARROW);
            } else {
                arrow = new ItemStack(net.minecraft.item.Items.ARROW);
            }
        }

        matrices.scale(reference, reference, reference);
        context.getItemRenderer().renderItem(arrow, ModelTransformationMode.GUI,
                light, overlay, matrices, vertexConsumers,null,0);
        matrices.pop();
    }
}
