package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.sound.SoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.*;


public class Scicraft implements ModInitializer {

    public static final String MOD_ID = "scicraft";

    // This logger is used to write text to the console and the log file.
    public static final Logger LOGGER = LogManager.getLogger("scicraft");

    private static final ConfiguredFeature<?, ?> OVERWORLD_SALT_ORE_CONFIGURED_FEATURE = Feature.ORE
            .configure(new OreFeatureConfig(
                    OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    Blocks.SALT_ORE.getDefaultState(),
                    9)); // vein size

    public static final PlacedFeature OVERWORLD_SALT_ORE_PLACED_FEATURE = OVERWORLD_SALT_ORE_CONFIGURED_FEATURE.withPlacement(
            CountPlacementModifier.of(20), // number of veins per chunk
            SquarePlacementModifier.of(), // spreading horizontally
            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64))); // height


    @Override
    public void onInitialize() {
        LOGGER.info("Hello Scicraft world!");

        Items.registerItems();
        Blocks.registerBlocks();
        Entities.registerEntities();
        ExtraDispenserBehavior.registerBehaviors();
        SoundEvents.registerSounds();

        // ore gen
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new Identifier("tutorial", "overworld_wool_ore"), OVERWORLD_SALT_ORE_CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("tutorial", "overworld_wool_ore"),
                OVERWORLD_SALT_ORE_PLACED_FEATURE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                        new Identifier("tutorial", "overworld_wool_ore")));
    }
}