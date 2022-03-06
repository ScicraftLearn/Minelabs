package be.uantwerpen.scicraft.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;

import static be.uantwerpen.scicraft.world.features.ConfiguredFeatures.OVERWORLD_SALT_ORE_CONFIGURED_FEATURE;
import static be.uantwerpen.scicraft.world.features.ConfiguredFeatures.OVERWORLD_SALT_ORE_PLACED_FEATURE;

public class OreGen {
    public static void generateOres() {
        // SALT ORE
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new Identifier("scicraft", "overworld_salt_ore"), OVERWORLD_SALT_ORE_CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("scicraft", "overworld_salt_ore"),
                OVERWORLD_SALT_ORE_PLACED_FEATURE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                        new Identifier("scicraft", "overworld_salt_ore")));
    }
}
