package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.LewisBlockEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.SpawnSettings;

import java.util.function.Predicate;

//import static be.uantwerpen.scicraft.block.Blocks.LEWIS_BLOCK;


public class Entities {
    // EntityTypes
    public static final EntityType<ElectronEntity> ELECTRON_ENTITY = register(FabricEntityTypeBuilder.<ElectronEntity>create(SpawnGroup.MISC, ElectronEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build(), "electron_entity");
    public static final EntityType<ProtonEntity> PROTON_ENTITY = register(FabricEntityTypeBuilder.<ProtonEntity>create(SpawnGroup.MISC, ProtonEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build(), "proton_entity");
    public static final EntityType<NeutronEntity> NEUTRON_ENTITY = register(FabricEntityTypeBuilder.<NeutronEntity>create(SpawnGroup.MISC, NeutronEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build(), "neutron_entity");
    public static final EntityType<EntropyCreeperEntity> ENTROPY_CREEPER = register(FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntropyCreeperEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.7f)).build(), "entropy_creeper");

    public static final BlockEntityType<LewisBlockEntity> LEWIS_BLOCK_ENTITY = register(
            FabricBlockEntityTypeBuilder.create(LewisBlockEntity::new, Blocks.LEWIS_BLOCK).build(null), "lewis_block");

    /**
     * Register a single entity
     * <p>
     *
     * @param entityType : EntityType to register
     * @param identifier : String name of the entity
     * @return registered EntityType
     */
    private static <T extends Entity> EntityType<T> register(EntityType<T> entityType, String identifier) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Scicraft.MOD_ID, identifier), entityType);
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
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Scicraft.MOD_ID, identifier), blockEntityType);
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
        BiomeModifications.create(Registry.ENTITY_TYPE.getId(entityType))
                .add(ModificationPhase.ADDITIONS, selector, context -> {
                    context.getSpawnSettings().addSpawn(entityType.getSpawnGroup(), spawnEntry);
                });
    }

    /**
     * Main class method
     * Register All entities
     */
    public static void registerEntities() {
        Scicraft.LOGGER.info("registering entities");
        FabricDefaultAttributeRegistry.register(ENTROPY_CREEPER, EntropyCreeperEntity.createCreeperAttributes());
        registerEntitySpawns(ENTROPY_CREEPER, BiomeSelectors.all(),
                new SpawnSettings.SpawnEntry(ENTROPY_CREEPER, 100, 1, 1));
    }
}
