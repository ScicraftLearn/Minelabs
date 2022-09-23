package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.block.entity.LabChestBlockEntity;
import be.uantwerpen.minelabs.renderer.model.LabChestModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class LabChestBlockEntityRenderer extends GeoBlockRenderer<LabChestBlockEntity> {

    public LabChestBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(new LabChestModel());
    }
}
