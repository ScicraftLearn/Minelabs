package be.minelabs.client.renderer.block.entity;

import be.minelabs.block.entity.ErlenmeyerBlockEntity;
import be.minelabs.state.property.Properties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class ErlenmeyerStandRenderer implements BlockEntityRenderer<ErlenmeyerBlockEntity> {
    private final BlockEntityRendererFactory.Context context;

    public ErlenmeyerStandRenderer(BlockEntityRendererFactory.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(ErlenmeyerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity != null){
            if (entity.getItem() != null){
                matrices.push();

                matrices.translate(0f,-(float)entity.getCachedState().get(Properties.COUNTER)/16f, 0f); // Down for Counter
                matrices.translate(0.5f,0f,0.5f);
                switch (entity.getCachedState().get(net.minecraft.state.property.Properties.HORIZONTAL_FACING)){
                    default -> {} // includes south
                    case EAST -> matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                    case WEST -> matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
                    case NORTH -> matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                }
                matrices.translate(-0.5f,0f,-0.5f);

                int seed = entity.getCachedState().get(Properties.TYPE);
                renderErlenmeyer(seed, matrices);

                matrices.scale(0.5f,0.5f,0.5f);
                context.getItemRenderer().renderItem(new ItemStack(entity.getItem()), ModelTransformationMode.GROUND, light,
                        overlay, matrices, vertexConsumers, null, 0);

                matrices.pop();
            }
        }
    }

    private void renderErlenmeyer(int seed, MatrixStack matrices){
        switch (seed) {
            case 0 -> {
                matrices.translate(0.5f, 0f, 0.5f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-45));
                matrices.translate(-2f / 16f, 1 / 16f, 5 / 16f);
            }
            case 1 -> {
                matrices.translate(0.5f, 0f, 0.5f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-45));
                matrices.translate(-1f / 16f, 5 / 16f, -5 / 16f);
            }
            default -> {
                matrices.translate(8 / 16f, 8.4 / 16f, 9.5 / 16f);
            }
        }
    }
}
