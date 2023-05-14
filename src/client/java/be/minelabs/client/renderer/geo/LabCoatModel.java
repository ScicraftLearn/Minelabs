package be.minelabs.client.renderer.geo;

import be.minelabs.Minelabs;
import be.minelabs.item.LabCoatArmorItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
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
