package be.uantwerpen.scicraft.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.Identifier;



public class ScientificVillagerEntityRenderer extends VillagerEntityRenderer {
    public ScientificVillagerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);

    }

    /**
     * Get the texture for the Entity
     *
     * @param villagerEntity : Entity to get the texture for
     * @return Identifier
     */
    @Override
    public Identifier getTexture(VillagerEntity villagerEntity) {
        return new Identifier("scicraft:textures/entity/scientific_villager/scientific_villager.png");
    }
}
