package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Entities {
    // EntityTypes
    public static final EntityType<ElectronEntity> ELECTRON_ENTITY = FabricEntityTypeBuilder.<ElectronEntity>create(SpawnGroup.MISC, ElectronEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking)
            .build();

    /**
     * Register a single entity
     *
     * @param entityType : EntityType to register
     * @param identifier : String name of the entity
     * @return registered EntityType
     */
    private static EntityType<?> registerEntity(EntityType<?> entityType, String identifier) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Scicraft.MOD_ID, identifier), entityType);
    }


    /**
     * Register All entities
     */
    public static void registerEntities() {
        registerEntity(ELECTRON_ENTITY, "electron_entity");
    }
}