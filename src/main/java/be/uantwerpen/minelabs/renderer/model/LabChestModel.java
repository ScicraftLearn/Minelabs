package be.uantwerpen.minelabs.renderer.model;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.entity.LabChestBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LabChestModel extends AnimatedGeoModel<LabChestBlockEntity> {
    @Override
    public Identifier getModelResource(LabChestBlockEntity object) {
        return new Identifier(Minelabs.MOD_ID, "animations/cabin.animation.json");

        // if (object.getCachedState().getBlock() == Blocks.LAB_DRAWER){ // DRAWER ANIMATION
        //    return  new Identifier(Minelabs.MOD_ID, "animations/drawer.animation.json");
        //} else { // CABIN ANIMATION
        //    return new Identifier(Minelabs.MOD_ID, "animations/cabin.animation.json");
        //}
    }

    @Override
    public Identifier getTextureResource(LabChestBlockEntity object) {
        return new Identifier(Minelabs.MOD_ID, "geo/lab_chest.geo.json");
    }

    @Override
    public Identifier getAnimationResource(LabChestBlockEntity animatable) {
        return new Identifier(Minelabs.MOD_ID, "textures/block/counter.png");
    }
}
