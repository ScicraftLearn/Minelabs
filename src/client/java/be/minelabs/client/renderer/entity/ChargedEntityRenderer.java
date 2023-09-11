package be.minelabs.client.renderer.entity;

import be.minelabs.entity.ChargedEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class ChargedEntityRenderer extends EntityRenderer<ChargedEntity> {
    protected ChargedEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(ChargedEntity entity) {
        return null;
    }
}
