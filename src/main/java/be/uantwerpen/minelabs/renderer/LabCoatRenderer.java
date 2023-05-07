package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.item.LabCoatArmorItem;
import be.uantwerpen.minelabs.model.LabCoatModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class LabCoatRenderer extends GeoArmorRenderer<LabCoatArmorItem> {
    public LabCoatRenderer() {
        super(new LabCoatModel());

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
