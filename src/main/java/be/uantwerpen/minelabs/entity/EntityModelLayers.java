package be.uantwerpen.minelabs.entity;

import be.uantwerpen.minelabs.Minelabs;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityModelLayers {
    public static final EntityModelLayer BALLOON_MODEL =
            new EntityModelLayer(new Identifier("minelabs:balloon"), "main");
}

