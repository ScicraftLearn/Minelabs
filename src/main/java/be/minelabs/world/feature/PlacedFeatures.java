package be.minelabs.world.feature;

import be.minelabs.Minelabs;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

public class PlacedFeatures {

    public static final RegistryKey<PlacedFeature> SALT_ORE_PLACED_KEY = registryKey("salt_ore_placed");

    public static void bootstrap(Registerable<PlacedFeature> context){
        var configureFeatureRegistryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);


        register(context, SALT_ORE_PLACED_KEY,
                configureFeatureRegistryEntryLookup.getOrThrow(ConfiguredFeatures.SALT_ORE_KEY),
                OrePlacement.modifiersWithCount(7, // VEINS PER CHUNK
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
    }

    public static RegistryKey<PlacedFeature> registryKey(String name){
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(Minelabs.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                                 RegistryEntry<ConfiguredFeature<?, ?>> config, List<PlacementModifier> modifiers){
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }

    private static  <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                                                                                  RegistryEntry<ConfiguredFeature<?, ?>> config,
                                                                                  PlacementModifier... modifiers){
        register(context, key, config, List.of(modifiers));
    }
}
