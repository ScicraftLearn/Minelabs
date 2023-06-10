package be.minelabs.data;

import be.minelabs.data.lang.ExtendingLangProvider;
import be.minelabs.data.lang.ImputingLangProvider;
import be.minelabs.world.gen.feature.ConfiguredFeatures;
import be.minelabs.world.gen.feature.PlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourcePack;

public class Datagen implements DataGeneratorEntrypoint {

	private static final String EN = "en_us";
	private static final String BE = "nl_be";
	private static final String NL = "nl_nl";

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(LaserToolDataProvider::new);

		// Languages, note: order is important!
		// English (default) extends the en_us static file with placeholders for missing values based on code. Logs warnings for all missing translations.
		pack.addProvider(ImputingLangProvider.factory(EN));
		// Flemish extends the nl_be static file with defaults from generated en_us file. Logs warnings for all missing translations.
		pack.addProvider(ExtendingLangProvider.factory(BE, EN));
		// Dutch extends the nl_nl static file with defaults from generated nl_be file. Does not log warnings for missing values (intentional).
		pack.addProvider(ExtendingLangProvider.factory(NL, BE, false));

		// Try keeping this at the end.
		pack.addProvider(WorldGenerator::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ConfiguredFeatures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, PlacedFeatures::bootstrap);
	}
}
