package be.uantwerpen.scicraft.data;

import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;

public interface MoleculeData {
	public void serialize(JsonObject var1);

    default public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        this.serialize(jsonObject);
        return jsonObject;
    }

    public Identifier getId();
}


