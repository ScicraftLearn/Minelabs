package be.minelabs.client.renderer.entity;

import be.minelabs.entity.Entities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class EntityRenderers {
    public static void onInitializeClient() {
        EntityRendererRegistry.register(Entities.SUBATOMIC_PARTICLE_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, BohrBlueprintEntityRenderer::new);
        EntityRendererRegistry.register(Entities.ENTROPY_CREEPER, EntropyCreeperEntityRenderer::new);
        EntityRendererRegistry.register(Entities.BALLOON, BalloonEntityRenderer::new);
    }

}
