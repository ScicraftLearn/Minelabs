package be.uantwerpen.scicraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.world.World;

public class ScientificVillagerEntity extends VillagerEntity {

    public ScientificVillagerEntity(EntityType<? extends VillagerEntity> entityType, World world) {
        super(entityType, world);
    }
}
