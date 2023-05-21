package be.minelabs.client.renderer.geo;

import be.minelabs.item.items.LabCoatArmorItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

@Environment(EnvType.CLIENT)
public class LabCoatRenderer extends GeoArmorRenderer<LabCoatArmorItem> {
    public LabCoatRenderer() {
        super(new LabCoatModel());
    }
}
