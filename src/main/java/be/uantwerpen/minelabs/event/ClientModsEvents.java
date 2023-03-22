package be.uantwerpen.minelabs.event;

import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.entity.Entities;
import be.uantwerpen.minelabs.renderer.BohrBlueprintEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ClientModsEvents {
    public static void registerEvents() {
        // render HUD of the closest bohr blueprint
        HudRenderCallback.EVENT.register(
            (matrixStack, delta) -> {
                MinecraftClient client = MinecraftClient.getInstance();

                if (client.world == null || client.player == null)
                    return;

                int diameter = BohrBlueprintEntityRenderer.HUD_RENDER_RADIUS * 2;
                Box box = Box.of(client.player.getPos(), diameter, diameter, diameter);
                // TODO: frustrum?
                List<BohrBlueprintEntity> entities = client.world.getEntitiesByType(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, box, e -> e.isInRange(client.player, BohrBlueprintEntityRenderer.HUD_RENDER_RADIUS));
                Optional<BohrBlueprintEntity> closestBohrBlockEntity = entities.stream().min(Comparator.comparing(e -> e.distanceTo(client.player)));
                closestBohrBlockEntity.ifPresent(bohrBlueprintEntity -> BohrBlueprintEntityRenderer.renderHud(matrixStack, bohrBlueprintEntity));
            }
        );
    }
}
