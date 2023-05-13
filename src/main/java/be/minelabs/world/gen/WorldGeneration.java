package be.minelabs.world.gen;

import be.minelabs.world.feature.PlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class WorldGeneration {
    public static void generateCustomWorld(){
        generateOres();
    }

    private static void generateOres() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, PlacedFeatures.SALT_ORE_PLACED_KEY);
    }
}
