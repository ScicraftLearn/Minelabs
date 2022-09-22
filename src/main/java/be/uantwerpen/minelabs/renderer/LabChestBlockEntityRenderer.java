package be.uantwerpen.minelabs.renderer;


import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.block.entity.LabChestBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class LabChestBlockEntityRenderer implements BlockEntityRenderer<LabChestBlockEntity> {
    private final BlockEntityRendererFactory.Context context;

    public LabChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        context = ctx;
    }

    public void render(LabChestBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        //World world = entity.getWorld();
        boolean isDrawer = entity.getCachedState().getBlock() == Blocks.LAB_DRAWER;
        float g = entity.getAnimationProgress(tickDelta);

        //TODO FINISH

        //RENDER NORMAL BASE BLOCK


        //SpriteIdentifier spriteIdentifier = TexturedRenderLayers.getChestTexture(entity, chestType, this.christmas);
        //VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
        if (isDrawer) {
            //renderDrawer(matrices, vertexConsumers, g, light, overlay);
        } else {
            //renderCabin(matrices, vertexConsumers, g, light, overlay);

        }
    }

    private void renderDrawer(MatrixStack matrices, VertexConsumer vertices, float openFactor, int light, int overlay) {

    }

    private void renderCabin(MatrixStack matrices, VertexConsumer vertices, float openFactor, int light, int overlay) {

    }
}
