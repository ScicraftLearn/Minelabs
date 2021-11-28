package be.uantwerpen.scicraft.renderer;

import net.minecraft.client.render.entity.CreeperEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Identifier;

public class EntropyCreeperEntityRenderer extends CreeperEntityRenderer {
    public EntropyCreeperEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(CreeperEntity creeperEntity) {
        return new Identifier("scicraft:textures/entity/entropy_creeper/entropy_creeper.png");
    }
}
