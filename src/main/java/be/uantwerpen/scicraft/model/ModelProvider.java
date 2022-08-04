package be.uantwerpen.scicraft.model;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

@Environment(EnvType.CLIENT)
public class ModelProvider implements ModelResourceProvider {
    public static final Identifier SPHERE_BLOCK_MODEL = new Identifier(Scicraft.MOD_ID, "block/sphere");
    public static final Identifier SPHERE_ITEM_MODEL = new Identifier(Scicraft.MOD_ID, "item/sphere");
    private final ResourceManager resourceManager;

    public ModelProvider(ResourceManager rm) {
        this.resourceManager = rm;
    }

    @Override
    public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) throws ModelProviderException {
        if(identifier.equals(SPHERE_BLOCK_MODEL) || identifier.equals(SPHERE_ITEM_MODEL)) {
            try {
                Reader reader = this.resourceManager.openAsReader(new Identifier(identifier.getNamespace(), "models/" + "molecules/ch4" + ".json"));
                return SphereModel.deserialize(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}