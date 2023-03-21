package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.item.Items;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class BohrBlueprintEntityRenderer<T extends BohrBlueprintEntity> extends EntityRenderer<T> {

    private final ItemRenderer itemRenderer;
    private final BlockRenderManager blockRenderManager;

    public BohrBlueprintEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        int seed = entity.getId();

        ItemStack itemStack = new ItemStack(Items.PROTON, 1);
        this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, seed);
    }

    @Override
    public Identifier getTexture(T entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
