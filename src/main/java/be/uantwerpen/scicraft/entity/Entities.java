package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Entities {
    // EntityTypes
    public static final EntityType<ElectronEntity> ELECTRON_ENTITY = FabricEntityTypeBuilder.<ElectronEntity>create(SpawnGroup.MISC, ElectronEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build();
    public static final EntityType<ProtonEntity> PROTON_ENTITY = FabricEntityTypeBuilder.<ProtonEntity>create(SpawnGroup.MISC, ProtonEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build();

    public static final EntityType<EntropyCreeperEntity> ENTROPY_CREEPER = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntropyCreeperEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.7f)).build();


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
        FabricDefaultAttributeRegistry.register(ENTROPY_CREEPER, EntropyCreeperEntity.createCreeperAttributes());
        registerEntity(ENTROPY_CREEPER, "entropy_creeper");
        registerEntity(ELECTRON_ENTITY, "electron_entity");
        registerEntity(PROTON_ENTITY, "proton_entity");
    }
}
