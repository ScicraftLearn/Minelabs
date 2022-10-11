package be.uantwerpen.minelabs.entity;

import be.uantwerpen.minelabs.item.LabCoatArmorItem2;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class LabCoat2Renderer extends GeoArmorRenderer<LabCoatArmorItem2> {
    public LabCoat2Renderer() {
        super(new LabCoat2Model());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorLeftBoot";
        this.leftLegBone = "armorRightBoot";
    }
}
