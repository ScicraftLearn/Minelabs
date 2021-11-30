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
     * Mixin Injected explosion method
     *
     * Make sure to "kill" the entity
     *
     * @return boolean: cancel default explosion or not
     */
    public boolean preExplode() {
        // BOOM !!
        if (!this.world.isClient) {
            this.dead = true;
            this.discard();
            Scicraft.LOGGER.info("Entropy creeper exploded");

            // TODO: Custom exposion
        }
        return false;   // make sure the original 'explode' function doesn't run.
    }


}
