package be.minelabs.entity;

import be.minelabs.Minelabs;
import be.minelabs.entity.decoration.painting.Paintings;
import be.minelabs.entity.effect.Effects;
import be.minelabs.entity.mob.BalloonEntity;
import be.minelabs.entity.mob.EntropyCreeperEntity;
import be.minelabs.entity.projectile.thrown.ParticleEntity;
import be.minelabs.entity.projectile.thrown.PointChargedEntity;
import be.minelabs.util.AtomConfiguration;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.SpawnSettings;

import java.util.function.Predicate;


public class Entities {
    // EntityTypes
    public static final EntityType<BohrBlueprintEntity> BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE = register(FabricEntityTypeBuilder.<BohrBlueprintEntity>create(SpawnGroup.MISC, BohrBlueprintEntity::new)
            .dimensions(EntityDimensions.fixed(1.5F, 1.5F)).trackRangeChunks(3).disableSummon().fireImmune().build(), "bohr_blueprint_entity");

    public static final EntityType<EntropyCreeperEntity> ENTROPY_CREEPER = register(FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntropyCreeperEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.7f)).build(), "entropy_creeper");

    public static final EntityType<BalloonEntity> BALLOON = register(FabricEntityTypeBuilder.create(SpawnGroup.MISC, BalloonEntity::new)
            .dimensions(EntityDimensions.fixed(1.0f, 1.0f)).build(), "balloon");
    public static final EntityType<CorrosiveEntity> CORROSIVE_ENTITY = register(FabricEntityTypeBuilder.create(SpawnGroup.MISC, CorrosiveEntity::new)
            .dimensions(EntityDimensions.fixed(1.0f, 1.0f)).disableSummon().fireImmune().build(), "corrosive");

    public static final EntityType<ParticleEntity> PARTICLE_ENTITY = register(FabricEntityTypeBuilder.<ParticleEntity>create(SpawnGroup.MISC, ParticleEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeChunks(3).forceTrackedVelocityUpdates(true).build(), "charged_entity");

    public static final EntityType<PointChargedEntity> POINT_CHARGED_ENTITY = register(FabricEntityTypeBuilder.<PointChargedEntity>create(SpawnGroup.MISC, PointChargedEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeChunks(3).forceTrackedVelocityUpdates(true).build(), "point_charged_entity");

    /**
     * Register a single entity
     * <p>
     *
     * @param entityType : EntityType to register
     * @param identifier : String name of the entity
     * @return registered EntityType
     */
    private static <T extends Entity> EntityType<T> register(EntityType<T> entityType, String identifier) {
        return Registry.register(Registries.ENTITY_TYPE, new Identifier(Minelabs.MOD_ID, identifier), entityType);
    }

    /**
     * Modify the Entity spawns
     * <p>
     *
     * @param entityType  : EntityType to add Spawns for
     * @param selector:   Predicate BiomeSelection, what biome(s) the entity can spawn in
     * @param spawnEntry: no documentation found
     *                    <p>
     *                    While testing set Selector to BiomeSelectors.all(), this will spawn you Entity in "The End"/"Nether" when entering
     */
    private static void registerEntitySpawns(EntityType<?> entityType, Predicate<BiomeSelectionContext> selector, SpawnSettings.SpawnEntry spawnEntry) {
        BiomeModifications.create(Registries.ENTITY_TYPE.getId(entityType))
                .add(ModificationPhase.ADDITIONS, selector, context -> context.getSpawnSettings().addSpawn(entityType.getSpawnGroup(), spawnEntry));
    }

    /**
     * Main class method
     * Register All entities
     */
    public static void onInitialize() {
        Paintings.onInitialize();
        Effects.onInitialize();

        FabricDefaultAttributeRegistry.register(ENTROPY_CREEPER, EntropyCreeperEntity.createCreeperAttributes());
        registerEntitySpawns(ENTROPY_CREEPER, BiomeSelectors.foundInOverworld().or(BiomeSelectors.foundInTheNether()),
                new SpawnSettings.SpawnEntry(ENTROPY_CREEPER, 100, 0, 1));
        FabricDefaultAttributeRegistry.register(BALLOON, BalloonEntity.createMobAttributes());

        // Register tracked data handlers for entities
        TrackedDataHandlerRegistry.register(AtomConfiguration.DATA_HANDLER);
    }
}
