package be.uantwerpen.scicraft.data;

import java.util.ArrayList;
import java.util.Random;

import com.google.gson.JsonArray;

public class MolecularMaterial {
	ArrayList<String> molecules = new ArrayList<>();
	ArrayList<Integer> distribution = new ArrayList<>();

	public MolecularMaterial(JsonArray molecules, JsonArray distribution) {
		for (int i = 0; i < molecules.size(); i++) {
			this.molecules.add(molecules.get(i).getAsString());
			this.distribution.add(distribution.get(i).getAsInt());
		}
	}
	
	public String getMolecule() {
		int r = new Random().nextInt(100);
		int chance = 0;
		for (int i = 0; i < distribution.size(); i++) {
			chance += distribution.get(i);
			if (r < chance) {
				return molecules.get(i);
			}
		}
		return molecules.get(molecules.size()); //should not happen, But if it does, return the last molecule.
	}
}
