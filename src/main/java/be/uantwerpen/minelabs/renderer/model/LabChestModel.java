package be.uantwerpen.minelabs.renderer.model;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.block.entity.LabChestBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LabChestModel extends AnimatedGeoModel<LabChestBlockEntity> {
    @Override
    public Identifier getModelResource(LabChestBlockEntity object) {
        if (object.getCachedState().getBlock() == Blocks.LAB_DRAWER) { // DRAWER ANIMATION
            return new Identifier(Minelabs.MOD_ID, "animations/lab_drawer.animation.json");
        } else { // CABIN ANIMATION
            return new Identifier(Minelabs.MOD_ID, "animations/lab_cabin.animation.json");
        }
    }

    @Override
    public Identifier getTextureResource(LabChestBlockEntity object) {
        if (object.getCachedState().getBlock() == Blocks.LAB_DRAWER) { // DRAWER MODEL
            return new Identifier(Minelabs.MOD_ID, "geo/lab_drawer.geo.json");
        } else { // CABIN ANIMATION
            return new Identifier(Minelabs.MOD_ID, "geo/lab_cabin.geo.json");
        }
    }

    @Override
    public Identifier getAnimationResource(LabChestBlockEntity animatable) {
        // TODO MERGE TEXTURES
        return new Identifier(Minelabs.MOD_ID, "textures/block/lab_chest.png");
        /*
        if (animatable.getCachedState().getBlock() == Blocks.LAB_DRAWER){ // DRAWER MODEL
            return new Identifier(Minelabs.MOD_ID, "textures/block/lab_drawer.png");
        } else { // CABIN ANIMATION
            return new Identifier(Minelabs.MOD_ID, "textures/block/lab_cabin.png");
        }*/
    }
}
