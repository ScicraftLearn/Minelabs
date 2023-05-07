package be.uantwerpen.minelabs.model;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.item.LabCoatArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class LabCoatModel extends GeoModel<LabCoatArmorItem> {
    @Override
    public Identifier getModelResource(LabCoatArmorItem object) {
        return new Identifier(Minelabs.MOD_ID, "geo/labcoat.geo.json");
    }

    @Override
    public Identifier getTextureResource(LabCoatArmorItem object) {
        return new Identifier(Minelabs.MOD_ID, "textures/armor/lab_coat.png");
    }

    @Override
    public Identifier getAnimationResource(LabCoatArmorItem animatable) {
        return new Identifier(Minelabs.MOD_ID, "animations/armor_animation.json");
    }
}
