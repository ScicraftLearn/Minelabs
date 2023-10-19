package be.minelabs.client.renderer.entity;

import be.minelabs.entity.Entities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EmptyEntityRenderer;

public class EntityRenderers {
    public static void onInitializeClient() {
        EntityRendererRegistry.register(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, BohrBlueprintEntityRenderer::new);
        EntityRendererRegistry.register(Entities.ENTROPY_CREEPER, EntropyCreeperEntityRenderer::new);
        EntityRendererRegistry.register(Entities.BALLOON, BalloonEntityRenderer::new);
        EntityRendererRegistry.register(Entities.CORROSIVE_ENTITY, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(Entities.PARTICLE_ENTITY, ChargedEntityRenderer::new);
        EntityRendererRegistry.register(Entities.POINT_CHARGED_ENTITY, ChargedEntityRenderer::new);
    }

}
