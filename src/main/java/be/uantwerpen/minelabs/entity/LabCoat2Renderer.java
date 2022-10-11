package be.uantwerpen.minelabs.entity;

import be.uantwerpen.minelabs.item.LabCoatArmorItem2;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class LabCoat2Renderer extends GeoArmorRenderer<LabCoatArmorItem2> {
    public LabCoat2Renderer(AnimatedGeoModel<LabCoatArmorItem2> modelProvider) {
        super(modelProvider);
    }
}
