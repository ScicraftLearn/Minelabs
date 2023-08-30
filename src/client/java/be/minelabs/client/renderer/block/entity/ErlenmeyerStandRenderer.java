package be.minelabs.client.renderer.block.entity;

import be.minelabs.block.entity.ErlenmeyerBlockEntity;
import be.minelabs.item.items.ErlenmeyerItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
                ErlenmeyerItem item = (ErlenmeyerItem) entity.getItem();
                int color = ColorProviderRegistry.ITEM.get(item).getColor(new ItemStack(item), 0); // Should get the color
                // TODO : something....
                //  continue ??
            }
        }
    }
}
