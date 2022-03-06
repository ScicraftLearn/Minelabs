package be.uantwerpen.scicraft.world.features;

import be.uantwerpen.scicraft.block.Blocks;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.*;

public class ConfiguredFeatures {
    // Salt ore feature
    public static final ConfiguredFeature<?, ?> OVERWORLD_SALT_ORE_CONFIGURED_FEATURE = Feature.ORE
            .configure(new OreFeatureConfig(
                    OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    Blocks.SALT_ORE.getDefaultState(),
                    9)); // vein size

    public static final PlacedFeature OVERWORLD_SALT_ORE_PLACED_FEATURE = OVERWORLD_SALT_ORE_CONFIGURED_FEATURE.withPlacement(
            CountPlacementModifier.of(20), // number of veins per chunk
            SquarePlacementModifier.of(), // spreading horizontally
            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64))); // height

}
