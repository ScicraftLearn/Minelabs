package be.uantwerpen.scicraft.model;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ModelProvider implements ModelResourceProvider {
    public static final Identifier SPHERE_BLOCK_MODEL = new Identifier(Scicraft.MOD_ID, "block/sphere");
    public static final Identifier SPHERE_ITEM_MODEL = new Identifier(Scicraft.MOD_ID, "item/sphere");

    @Override
    public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) throws ModelProviderException {
        if(identifier.equals(SPHERE_BLOCK_MODEL) || identifier.equals(SPHERE_ITEM_MODEL)) {
            return new SphereModel();
        } else {
            return null;
        }
    }
}