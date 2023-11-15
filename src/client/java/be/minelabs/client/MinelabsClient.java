package be.minelabs.client;

import be.minelabs.client.block.BlockColors;
import be.minelabs.client.block.BlockRenderLayers;
import be.minelabs.client.event.ClientModsEvents;
import be.minelabs.client.gui.screen.Screens;
import be.minelabs.client.item.ItemModels;
import be.minelabs.client.network.ClientNetworking;
import be.minelabs.client.particle.Particles;
import be.minelabs.client.renderer.block.entity.BlockEntityRenderers;
import be.minelabs.client.renderer.entity.EntityRenderers;
import be.minelabs.client.renderer.entity.model.EntityModelLayers;
import be.minelabs.client.renderer.model.ModelProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;


@SuppressWarnings("UNUSED")
@Environment(EnvType.CLIENT)
public class MinelabsClient implements ClientModInitializer {
    @Override()
    public void onInitializeClient() {

        ItemModels.onInitializeClient();
        BlockRenderLayers.onInitializeClient();
        BlockColors.onInitializeClient();

        EntityRenderers.onInitializeClient();
        EntityModelLayers.onInitializeClient();
        BlockEntityRenderers.onInitializeClient();

        Screens.onInitializeClient();
        Particles.onInitializeClient();

        ModelLoadingRegistry.INSTANCE.registerModelProvider(new ModelProvider.Requester());
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(ModelProvider::new);

        ClientModsEvents.onInitializeClient();
        ClientNetworking.onInitializeClient();
    }

}

