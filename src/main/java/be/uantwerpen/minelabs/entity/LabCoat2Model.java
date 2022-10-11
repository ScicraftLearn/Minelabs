package be.uantwerpen.minelabs.entity;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.item.LabCoatArmorItem2;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LabCoat2Model extends AnimatedGeoModel<LabCoatArmorItem2> {
    @Override
    public Identifier getModelResource(LabCoatArmorItem2 object) {
        return new Identifier(Minelabs.MOD_ID, "geo/labcoat2.geo.json");
    }

    @Override
    public Identifier getTextureResource(LabCoatArmorItem2 object) {
        return new Identifier(Minelabs.MOD_ID, "textures/models/armor/labcoat2.png");
    }

    @Override
    public Identifier getAnimationResource(LabCoatArmorItem2 animatable) {
        return new Identifier(Minelabs.MOD_ID, "animations/armor_animation.json");
    }
}
