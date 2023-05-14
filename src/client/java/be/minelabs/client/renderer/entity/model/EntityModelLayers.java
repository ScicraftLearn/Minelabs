package be.minelabs.client.renderer.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntityModelLayers {
    public static final EntityModelLayer BALLOON_MODEL =
            new EntityModelLayer(new Identifier("minelabs:balloon"), "main");
}

