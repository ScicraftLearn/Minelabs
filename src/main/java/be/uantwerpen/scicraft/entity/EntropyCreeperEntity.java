package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;

public class EntropyCreeperEntity extends CreeperEntity {

    public EntropyCreeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Hook into explode method
     * Thanks Mixin!
     */
    public boolean preExplode() {
        // BOOM !!
        this.dead = true;
        this.discard();
        Scicraft.LOGGER.info("Entropy creeper exploded");

        // TODO: Custom exposion

        return false;   // make sure the original 'explode' function doesn't run.
    }


}
