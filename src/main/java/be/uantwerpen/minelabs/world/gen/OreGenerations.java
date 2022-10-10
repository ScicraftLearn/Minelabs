package be.uantwerpen.minelabs.world.gen;

import be.uantwerpen.minelabs.world.feature.PlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class OreGenerations {

    public static void generateOres() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, PlacedFeatures.SALT_ORE_PLACED.getKey().get());
    }
}
