package be.minelabs.data;

import be.minelabs.data.lang.DutchLangProvider;
import be.minelabs.data.lang.FlemishLangProvider;
import be.minelabs.data.lang.LangProvider;
import be.minelabs.world.gen.feature.ConfiguredFeatures;
import be.minelabs.world.gen.feature.PlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class Datagen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(LaserToolDataProvider::new);

		pack.addProvider(LangProvider::new);
		pack.addProvider(FlemishLangProvider::new);
		pack.addProvider(DutchLangProvider::new);

		// Try keeping this at the end.
		pack.addProvider(WorldGenerator::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ConfiguredFeatures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, PlacedFeatures::bootstrap);
	}
}
