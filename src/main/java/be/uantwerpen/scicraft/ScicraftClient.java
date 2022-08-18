package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.entity.BlockEntities;
import be.uantwerpen.scicraft.block.entity.BohrBlockEntity;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.event.ModEvents;
import be.uantwerpen.scicraft.network.NetworkingConstants;
import be.uantwerpen.scicraft.renderer.BohrBlockEntityRenderer;
import be.uantwerpen.scicraft.gui.ScreenHandlers;
import be.uantwerpen.scicraft.gui.ionic_gui.IonicScreen;
import be.uantwerpen.scicraft.gui.lewis_gui.LewisScreen;
import be.uantwerpen.scicraft.item.ItemModels;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.network.IonicDataPacket;
import be.uantwerpen.scicraft.network.LewisDataPacket;
import be.uantwerpen.scicraft.network.NetworkingConstants;
import be.uantwerpen.scicraft.renderer.ChargedBlockEntityRenderer;
import be.uantwerpen.scicraft.renderer.ChargedPlaceholderBlockEntityRenderer;
import be.uantwerpen.scicraft.renderer.ElectricFieldSensorRenderer;
import be.uantwerpen.scicraft.renderer.EntropyCreeperEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;


@SuppressWarnings("UNUSED")
@Environment(EnvType.CLIENT)
public class ScicraftClient implements ClientModInitializer {
    @Override()
    public void onInitializeClient() {


        ModEvents.registerEvents();

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ATOM_FLOOR, RenderLayer.getTranslucent());
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
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LARGE_SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MEDIUM_SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SMALL_SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SALT_WIRE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GREEN_FIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.HELIUM, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ELECTRON_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLUON_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.DOWNQUARK_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PHOTON_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEUTRINO_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.UPQUARK_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.WEAK_BOSON_QUANTUMFIELD, RenderLayer.getTranslucent());

        // Register rendering for electron entity
        EntityRendererRegistry.register(Entities.ELECTRON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.PROTON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.ANTI_PROTON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.NEUTRON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.ANTI_NEUTRON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.ENTROPY_CREEPER, EntropyCreeperEntityRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntities.BOHR_BLOCK_ENTITY, BohrBlockEntityRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntities.ANIMATED_CHARGED_BLOCK_ENTITY, ChargedBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntities.CHARGED_PLACEHOLDER_BLOCK_ENTITY, ChargedPlaceholderBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntities.ELECTRIC_FIELD_SENSOR, ElectricFieldSensorRenderer::new);

        // Register rendering lewis crafting table inventory
        HandledScreens.register(ScreenHandlers.LEWIS_SCREEN_HANDLER, LewisScreen::new);

        ScreenEvents.BEFORE_INIT.register((a, screen, b, c) -> {
            if (screen instanceof LewisScreen)
                ScreenMouseEvents.afterMouseRelease(screen).register((d, mouseX, mouseY, e) -> ((LewisScreen) screen).getButtonWidget().onClick(mouseX, mouseY));
        });


        //Register rendering ionic block inventory
        HandledScreens.register(ScreenHandlers.IONIC_SCREEN_HANDLER, IonicScreen::new);

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

        //Lewis Data Sync
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.LEWISDATASYNC, (c, h, b, s) -> LewisDataPacket.receive(c.world, b, s));

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.IONICDATASYNC, (c, h, b, s) -> IonicDataPacket.receive(c.world, b, s));
    }

    public void registerErlenmeyer(Item item, int color, int index) {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == index) return color;
            return 0x7F7F7F;
        }, item);
    }

}

