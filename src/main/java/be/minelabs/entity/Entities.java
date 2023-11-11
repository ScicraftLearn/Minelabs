package be.minelabs.entity;

import be.minelabs.Minelabs;
import be.minelabs.entity.decoration.painting.Paintings;
import be.minelabs.entity.effect.Effects;
import be.minelabs.entity.mob.BalloonEntity;
import be.minelabs.entity.mob.EntropyCreeperEntity;
import be.minelabs.entity.projectile.thrown.SubatomicParticleEntity;
import be.minelabs.util.AtomConfiguration;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
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
    public static final EntityType<SubatomicParticleEntity> SUBATOMIC_PARTICLE_ENTITY_TYPE = register(FabricEntityTypeBuilder.<SubatomicParticleEntity>create(SpawnGroup.MISC, SubatomicParticleEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build(), "subatomic_particle_entity");

    public static final EntityType<BohrBlueprintEntity> BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE = register(FabricEntityTypeBuilder.<BohrBlueprintEntity>create(SpawnGroup.MISC, BohrBlueprintEntity::new)
            .dimensions(EntityDimensions.fixed(1.5F, 1.5F)).trackRangeChunks(3).disableSummon().fireImmune().build(), "bohr_blueprint_entity");

    public static final EntityType<EntropyCreeperEntity> ENTROPY_CREEPER = register(FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntropyCreeperEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.7f)).build(), "entropy_creeper");

    public static final EntityType<BalloonEntity> BALLOON = register(FabricEntityTypeBuilder.create(SpawnGroup.MISC, BalloonEntity::new)
            .dimensions(EntityDimensions.fixed(0.8f, 1.0f)).disableSummon().build(), "balloon");
    public static final EntityType<CorrosiveEntity> CORROSIVE_ENTITY = register(FabricEntityTypeBuilder.create(SpawnGroup.MISC, CorrosiveEntity::new)
            .dimensions(EntityDimensions.fixed(1.0f, 1.0f)).disableSummon().fireImmune().build(), "corrosive");


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
     * Register a single block entity
     * <p>
     *
     * @param blockEntityType : BlockEntityType to register
     * @param identifier      : String name of the entity
     * @return registered BlockEntityType
     */
    private static <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType<T> blockEntityType, String identifier) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Minelabs.MOD_ID, identifier), blockEntityType);
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
