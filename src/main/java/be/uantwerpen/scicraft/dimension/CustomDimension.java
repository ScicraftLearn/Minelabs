package be.uantwerpen.scicraft.dimension;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class CustomDimension {
    // The dimension options refer to the JSON-file in the dimension subfolder of the datapack,
    // which will always share it's ID with the world that is created from it
    private static final RegistryKey<DimensionOptions> DIMENSION_KEY = RegistryKey.of(
            Registry.DIMENSION_KEY,
            new Identifier("fabric_dimension", "void")
    );

    private static RegistryKey<World> WORLD_KEY = RegistryKey.of(
            Registry.WORLD_KEY,
            DIMENSION_KEY.getValue()
    );

    private static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY = RegistryKey.of(
            Registry.DIMENSION_TYPE_KEY,
            new Identifier("fabric_dimension", "void_type")
    );

    public void onInitialize() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("fabric_dimension", "void"), VoidChunkGenerator.CODEC);

        WORLD_KEY = RegistryKey.of(Registry.WORLD_KEY, new Identifier("fabric_dimension", "void"));
    }

    public static ServerWorld getModWorld(MinecraftServer server) {
        return getWorld(server, WORLD_KEY);
    }

    public static ServerWorld getWorld(MinecraftServer server, RegistryKey<World> dimensionRegistryKey) {
        return server.getWorld(dimensionRegistryKey);
    }
}
