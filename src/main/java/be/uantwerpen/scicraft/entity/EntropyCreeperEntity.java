package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;

public class EntropyCreeperEntity extends CreeperEntity {

    public EntropyCreeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    public void onExplode() {
        Scicraft.LOGGER.debug("I'm exploding!");
        this.dead = true;
        //TODO custom explosion

    }

}
