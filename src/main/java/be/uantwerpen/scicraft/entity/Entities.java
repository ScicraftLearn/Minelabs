package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.SpawnSettings;

import java.util.function.Predicate;

public class Entities {
    // EntityTypes
    public static final EntityType<ElectronEntity> ELECTRON_ENTITY = FabricEntityTypeBuilder.<ElectronEntity>create(SpawnGroup.MISC, ElectronEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build();
    public static final EntityType<ProtonEntity> PROTON_ENTITY = FabricEntityTypeBuilder.<ProtonEntity>create(SpawnGroup.MISC, ProtonEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build();
    public static final EntityType<NeutronEntity> NEUTRON_ENTITY = FabricEntityTypeBuilder.<NeutronEntity>create(SpawnGroup.MISC, NeutronEntity::new)
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
     * Modify the Entity spawns
     *
     * @param entityType  : EntityType to add Spawns for
     * @param selector:   Predicate BiomeSelection, what biome(s) the entity can spawn in
     * @param spawnEntry: no documentation found
     *                    <p>
     *                    While testing set Selector to BiomeSelectors.all(), this will spawn you Entity in "The End"/"Nether" when entering
     */
    private static void registerEntitySpawns(EntityType<?> entityType, Predicate<BiomeSelectionContext> selector, SpawnSettings.SpawnEntry spawnEntry) {
        BiomeModifications.create(Registry.ENTITY_TYPE.getId(entityType))
                .add(ModificationPhase.ADDITIONS, selector, context -> {
                    context.getSpawnSettings().addSpawn(entityType.getSpawnGroup(), spawnEntry);
                });
    }

    /**
     * Register All entities
     */
    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(ENTROPY_CREEPER, EntropyCreeperEntity.createCreeperAttributes());
        registerEntity(ENTROPY_CREEPER, "entropy_creeper");
        registerEntitySpawns(ENTROPY_CREEPER, BiomeSelectors.foundInOverworld(),
                new SpawnSettings.SpawnEntry(ENTROPY_CREEPER, 100, 1, 1)); // Same as normal creeper
        registerEntity(ELECTRON_ENTITY, "electron_entity");
        registerEntity(PROTON_ENTITY, "proton_entity");
        registerEntity(NEUTRON_ENTITY, "neutron_entity");

//        registerEntitySpawns(ENTROPY_CREEPER, BiomeSelectors.foundInOverworld(), new SpawnSettings.SpawnEntry(SCIENTIFIC_VILLAGER, 100, 1, 1));
    }
}
