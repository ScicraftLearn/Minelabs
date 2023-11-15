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
    private static final Identifier FORCE_GLASS = new Identifier(Minelabs.MOD_ID, "textures/misc/force_glass.png");

    private void renderOverlay(MatrixStack matrices, Identifier texture, float opacity, int scaledWidth,int scaledHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();
        InGameHud.drawTexture(matrices, 0, 0, -90, 0.0f, 0.0f, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        ItemStack itemStack = MinecraftClient.getInstance().player.getInventory().getArmorStack(3);
        int scaledWith = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int scaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

        if (itemStack.isOf(Items.SAFETY_GLASSES)) {
            renderOverlay(matrices, SAFETY_GLASS, 1.0F, scaledWith, scaledHeight);
        } else if (itemStack.isOf(Items.FORCE_GLASSES)) {
            renderOverlay(matrices, FORCE_GLASS, 1.0f, scaledWith, scaledHeight);
        }
    }
}
