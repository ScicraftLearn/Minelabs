package be.minelabs.client.renderer.entity;

import be.minelabs.Minelabs;
import be.minelabs.client.renderer.entity.model.BalloonEntityModel;
import be.minelabs.client.renderer.entity.model.EntityModelLayers;
import be.minelabs.entity.mob.BalloonEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BalloonEntityRenderer extends MobEntityRenderer<BalloonEntity, BalloonEntityModel> {
    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/entity/balloon/balloon.png");

    public BalloonEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BalloonEntityModel(context.getPart(EntityModelLayers.BALLOON_MODEL)), 0.7F);
    }

    @Override
    public Identifier getTexture(BalloonEntity entity) {
        return TEXTURE;
    }
}