package be.minelabs.client.gui.hud;

import be.minelabs.Minelabs;
import be.minelabs.item.Items;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class HudOverlayCallBack implements HudRenderCallback {

    private static final Identifier SAFETY_GLASS = new Identifier(Minelabs.MOD_ID, "textures/misc/safety_glass.png");

    private void renderOverlay(MatrixStack matrices, Identifier texture, float opacity, int scaledWidth,int scaledHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
        RenderSystem.setShaderTexture(0, texture);
        InGameHud.drawTexture(matrices, 0, 0, -90, 0.0f, 0.0f, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        ItemStack itemStack = MinecraftClient.getInstance().player.getInventory().getArmorStack(3);

        if (itemStack.isOf(Items.SAFETY_GLASSES)) {
            renderOverlay(matrices, SAFETY_GLASS, 1.0F,
                    MinecraftClient.getInstance().getWindow().getScaledWidth(),
                    MinecraftClient.getInstance().getWindow().getScaledHeight());
        }
    }
}
