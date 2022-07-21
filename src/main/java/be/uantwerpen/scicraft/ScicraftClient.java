package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.entity.AnimatedChargedBlockEntity;
import be.uantwerpen.scicraft.block.entity.BlockEntities;
import be.uantwerpen.scicraft.renderer.MologramBlockRenderer;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.gui.LewisScreen;
import be.uantwerpen.scicraft.gui.Screens;
import be.uantwerpen.scicraft.item.ItemModels;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.network.NetworkingConstants;
import be.uantwerpen.scicraft.particle.HologramParticle;
import be.uantwerpen.scicraft.particle.Particles;
import be.uantwerpen.scicraft.renderer.ChargedBlockEntityRenderer;
import be.uantwerpen.scicraft.renderer.ChargedPlaceholderBlockEntityRenderer;
import be.uantwerpen.scicraft.renderer.EntropyCreeperEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;


@SuppressWarnings("UNUSED")
@Environment(EnvType.CLIENT)
public class ScicraftClient implements ClientModInitializer {
    @Override()
    public void onInitializeClient() {
        //Register ItemModels
        ItemModels.registerModels();

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_NUL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_MINUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_PLUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.WEAK_BOSON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEUTRINO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ANTINEUTRINO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.POSITRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ELECTRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEUTRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PROTON, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GREEN_FIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.HELIUM, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MOLOGRAM_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MOLOGRAM_BLOCK, RenderLayer.getTranslucent());
        BlockEntityRendererRegistry.register(BlockEntities.MOLOGRAM_BLOCK_ENTITY, MologramBlockRenderer::new);

        // Register rendering for electron entity
        EntityRendererRegistry.register(Entities.ELECTRON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.PROTON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.NEUTRON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.ENTROPY_CREEPER, EntropyCreeperEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(Particles.HOLOGRAM_PARTICLE, HologramParticle.Factory::new);

        BlockEntityRendererRegistry.register(BlockEntities.ANIMATED_CHARGED_BLOCK_ENTITY, ChargedBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntities.CHARGED_PLACEHOLDER_BLOCK_ENTITY, ChargedPlaceholderBlockEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.CHARGED_MOVE_STATE, (client, handler, buf, responseSender) -> {
            BlockPos target = buf.readBlockPos();
            String block_name = buf.readString();
            boolean annihilation = buf.readBoolean();
            client.execute(() -> {
                if (client.world != null) {
                    if (client.world.getBlockEntity(target) instanceof AnimatedChargedBlockEntity particle2) {
                        particle2.render_state = particle2.string2BlockState(block_name);
                        particle2.annihilation = annihilation;
                    }
                }
            });
        });

        // Register rendering lewis crafting table inventory
        HandledScreens.register(Screens.LEWIS_SCREEN_HANDLER, LewisScreen::new);

//        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LEWIS_BLOCK, RenderLayer.getTranslucent());

        // Gas

        // TODO - toevoegen van 02, N2, ... als extra texture voor kleurloze gassen.
        // TODO - enchantment visuals voor zeldzame stoffen

        // Tier 1
        registerErlenmeyer(Items.ERLENMEYER_02, 0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_N2, 0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_CH4, 0x7F7F7F, 2);

        // Tier 2
        registerErlenmeyer(Items.ERLENMEYER_H2, 0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_N0, 0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_N02, 0x991c00, 2);
        registerErlenmeyer(Items.ERLENMEYER_Cl2, 0xE8F48C, 2);
        registerErlenmeyer(Items.ERLENMEYER_CO2, 0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_CO, 0x7F7F7F, 2);

        // Tier 3
        registerErlenmeyer(Items.ERLENMEYER_NH3, 0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_N2O, 0x7F7F7F, 2);
        registerErlenmeyer(Items.ERLENMEYER_HCl, 0x7F7F7F, 2);

        //Fluids
        registerErlenmeyer(Items.ERLENMEYER_HNO3, 0xFFCC33, 2);
//        public static Block ACID = Registry.register(Registry.BLOCK, new Identifier(Scicraft.MOD_ID, "acid"), new FluidBlock(be.uantwerpen.scicraft.item.Items.STILL_ACID, FabricBlockSettings.copy(net.minecraft.block.)){});
    }

    public void registerErlenmeyer(Item item, int color, int index) {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == index) return color;
            return 0x7F7F7F;
        }, item);
    }
}
