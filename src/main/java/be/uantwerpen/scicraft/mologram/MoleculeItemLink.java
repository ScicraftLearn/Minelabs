package be.uantwerpen.scicraft.mologram;

import be.uantwerpen.scicraft.lewisrecipes.MoleculeRecipe;
import com.google.gson.JsonObject;

public enum MoleculeItemLink {
    WATER("water_bucket",new JsonObject())
    ;

    private final String name;
    private final JsonObject molculeFile;

    MoleculeItemLink(String name, JsonObject object) {
        this.name = name;
        this.molculeFile = object;
    }

    public String getName() {
        return name;
    }

    public JsonObject getMolculeFile() {
        return molculeFile;
    }

}
