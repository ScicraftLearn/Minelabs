package be.minelabs.client.renderer.block.entity;

import be.minelabs.Minelabs;
import be.minelabs.block.entity.ElectricFieldSensorBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class ElectricFieldSensorRenderer implements BlockEntityRenderer<ElectricFieldSensorBlockEntity> {

    private final BlockEntityRendererFactory.Context context;
    private final static float max_field = 3;

    public ElectricFieldSensorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(ElectricFieldSensorBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Minelabs.LOGGER.info("enity: " + entity.getEField());
        Vector3f field = new Vector3f(entity.getEField().toVector3f());
        Minelabs.LOGGER.info("render:" + field);
        matrices.push();

        ItemStack arrow;
        float reference = 0.2f;

        // Center arrow in block
        matrices.translate(0.5, 0.5, 0.5);

        if (field.equals(new Vector3f())) {
            // Don't display arrow if the field is zero
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
            // arrow = gray arrow
            arrow = new ItemStack(net.minecraft.item.Items.DARK_OAK_BUTTON);
        } else {
            // We want the arrows to point the other direction, so we turn the field vector around.
            field.mul(-1);

            // By default, the arrow points in negative x (WEST)
            Vector3f v = Direction.WEST.getUnitVector();

            // Rotate towards field
            Quaternionf rotation = v.rotationTo(field, new Quaternionf());
            matrices.multiply(rotation);

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
                light, overlay, matrices, vertexConsumers, null, 0);
        matrices.pop();
    }
}
