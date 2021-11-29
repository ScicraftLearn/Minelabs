package be.uantwerpen.scicraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;

public class EntropyCreeperEntity extends CreeperEntity {

    public EntropyCreeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * New On Explode method
     * Thanks Mixin!
     * <p>
     * Make sure to "kill" the entity
     * TODO custom explosion
     */
    public void onExplode() {
        this.dead = true;
        // BOOM !!
    }

}
