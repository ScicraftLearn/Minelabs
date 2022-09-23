package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.block.entity.LabChestBlockEntity;
import be.uantwerpen.minelabs.renderer.model.LabChestModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class LabChestBlockEntityRenderer extends GeoBlockRenderer<LabChestBlockEntity> {

    public LabChestBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(new LabChestModel());
    }

    @Override
    public RenderLayer getRenderType(LabChestBlockEntity animatable, float partialTicks, MatrixStack stack, @Nullable VertexConsumerProvider renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }
}
