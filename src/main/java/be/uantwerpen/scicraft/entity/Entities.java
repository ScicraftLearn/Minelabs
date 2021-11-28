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

    public static final EntityType<EntropyCreeperEntity> ENTROPY_CREEPER = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntropyCreeperEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.7f)).build();


    private static EntityType<?> registerEntity(EntityType<?> entityType, String identifier) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Scicraft.MOD_ID, identifier), entityType);
    }

    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(ENTROPY_CREEPER, EntropyCreeperEntity.createCreeperAttributes());
        registerEntity(ENTROPY_CREEPER, "entropy_creeper");

    }
}
