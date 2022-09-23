package be.uantwerpen.minelabs.renderer.model;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.entity.LabChestBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LabChestModel extends AnimatedGeoModel<LabChestBlockEntity> {
    @Override
    public Identifier getModelResource(LabChestBlockEntity object) {
        return new Identifier(Minelabs.MOD_ID, "animations/bike.animation.json");
    }

    @Override
    public Identifier getTextureResource(LabChestBlockEntity object) {
        return new Identifier(Minelabs.MOD_ID, "geo/bike.geo.json");
    }

    @Override
    public Identifier getAnimationResource(LabChestBlockEntity animatable) {
        return new Identifier(Minelabs.MOD_ID, "textures/model/entity/bike.png");
    }
}
