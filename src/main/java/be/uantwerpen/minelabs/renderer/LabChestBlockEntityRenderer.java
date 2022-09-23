package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.block.entity.LabChestBlockEntity;
import be.uantwerpen.minelabs.renderer.model.LabChestModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class LabChestBlockEntityRenderer extends GeoBlockRenderer<LabChestBlockEntity> {

    public LabChestBlockEntityRenderer() {
        super(new LabChestModel());
    }

}
