package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.data.MoleculeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ScicraftDatagen implements DataGeneratorEntrypoint{

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.addProvider(MoleculeProvider::new);
		
	}

}
