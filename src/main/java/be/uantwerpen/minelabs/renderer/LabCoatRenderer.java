package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.item.LabCoatArmorItem;
import be.uantwerpen.minelabs.model.LabCoatModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class LabCoatRenderer extends GeoArmorRenderer<LabCoatArmorItem> {
    public LabCoatRenderer() {
        super(new LabCoatModel());
    }
}
