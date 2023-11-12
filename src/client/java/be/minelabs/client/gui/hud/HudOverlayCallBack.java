package be.minelabs.client.gui.hud;

import be.minelabs.Minelabs;
import be.minelabs.item.Items;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;


public class HudOverlayCallBack implements HudRenderCallback {
    private static final Identifier SAFETY_GLASS = new Identifier(Minelabs.MOD_ID, "textures/misc/safety_glass.png");

    private void renderOverlay(DrawContext context, Identifier texture, float opacity) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        context.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
        context.drawTexture(texture, 0, 0, -90, 0.0f, 0.0f,
                context.getScaledWindowWidth(), context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight());
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }


    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        ItemStack itemStack = MinecraftClient.getInstance().player.getInventory().getArmorStack(3);

        if (itemStack.isOf(Items.SAFETY_GLASSES)) {
            renderOverlay(context, SAFETY_GLASS, 1.0F);
        }
    }
}
