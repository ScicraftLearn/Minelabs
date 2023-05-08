package be.uantwerpen.minelabs;

import be.uantwerpen.minelabs.data.DutchLangProvider;
import be.uantwerpen.minelabs.data.FlemishLangProvider;
import be.uantwerpen.minelabs.data.LangProvider;
import be.uantwerpen.minelabs.data.LaserToolDataProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MinelabsDatagen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(LaserToolDataProvider::new);
		pack.addProvider(LangProvider::new);
		pack.addProvider(DutchLangProvider::new);
		pack.addProvider(FlemishLangProvider::new);
	}

}
