package be.uantwerpen.minelabs.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.Reader;

@Environment(EnvType.CLIENT)
public class ModelProvider implements ModelResourceProvider {
    private final ResourceManager resourceManager;

    public ModelProvider(ResourceManager rm) {
        this.resourceManager = rm;
    }

    @Override
    public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) throws ModelProviderException {
        if (identifier.getPath().startsWith("molecules")) {
            try {
                Reader reader = this.resourceManager.openAsReader(new Identifier(identifier.getNamespace(), "models/" + identifier.getPath() + ".json"));
                return MoleculeModel.deserialize(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}