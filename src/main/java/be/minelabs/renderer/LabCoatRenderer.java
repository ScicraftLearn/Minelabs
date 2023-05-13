package be.minelabs.renderer;

import be.minelabs.item.LabCoatArmorItem;
import be.minelabs.model.LabCoatModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class LabCoatRenderer extends GeoArmorRenderer<LabCoatArmorItem> {
    public LabCoatRenderer() {
        super(new LabCoatModel());
    }
}
