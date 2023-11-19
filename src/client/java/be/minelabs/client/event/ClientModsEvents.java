package be.minelabs.client.event;

import be.minelabs.client.gui.hud.BohrBlueprintHUDRenderer;
import be.minelabs.client.gui.hud.HudOverlayCallBack;
import be.minelabs.entity.BohrBlueprintEntity;
import be.minelabs.entity.Entities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ClientModsEvents {
    public static void onInitializeClient() {
        // render HUD of the closest bohr blueprint
        HudRenderCallback.EVENT.register(
                (matrixStack, delta) -> {
                    MinecraftClient client = MinecraftClient.getInstance();

                    if (client.world == null || client.player == null)
                        return;

                    Vec3d cameraPos = client.player.getCameraPosVec(delta);
                    Vec3d cameraDirection = client.player.getRotationVec(delta).normalize();

                    // function to compute angle between camera and entity in radians.
                    Function<Entity, Double> entityToRadiansAngle = e -> {
                        Vec3d directionToEntity = e.getEyePos().subtract(cameraPos).normalize();
                        return Math.acos(cameraDirection.dotProduct(directionToEntity));
                    };

                    // find all entities in range
                    int diameter = BohrBlueprintHUDRenderer.HUD_RENDER_RADIUS * 2;
                    Box box = Box.of(client.player.getPos(), diameter, diameter, diameter);
                    List<BohrBlueprintEntity> entities = client.world.getEntitiesByType(
                            Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE,
                            box,
                            e -> e.isInRange(client.player, BohrBlueprintHUDRenderer.HUD_RENDER_RADIUS)
                    );

                    // select the entity closest to where the player is looking (minimal angle between vectors)
                    Optional<BohrBlueprintEntity> closestBohrBlockEntity = entities.stream().min(Comparator.comparing(entityToRadiansAngle));

                    // render HUD if its entity is in viewport (approximated with maximal angle away from camera)
                    if (closestBohrBlockEntity.isPresent()) {
                        BohrBlueprintEntity bohrBlueprintEntity = closestBohrBlockEntity.get();
                        if (entityToRadiansAngle.apply(bohrBlueprintEntity) < BohrBlueprintHUDRenderer.MAX_RENDER_ANGLE) {
                            BohrBlueprintHUDRenderer.renderHud(matrixStack, bohrBlueprintEntity);
                        }
                    }
                }
        );
        HudRenderCallback.EVENT.register(new HudOverlayCallBack());
    }
}
