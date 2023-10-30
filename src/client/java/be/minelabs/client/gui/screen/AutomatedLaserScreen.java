package be.minelabs.client.gui.screen;

import be.minelabs.Minelabs;
import be.minelabs.screen.AutomatedLaserScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AutomatedLaserScreen extends HandledScreen<AutomatedLaserScreenHandler> implements ScreenHandlerProvider<AutomatedLaserScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/automated_laser_gui.png");


    public AutomatedLaserScreen(AutomatedLaserScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundHeight = 200;
    }

    @Override
    protected void init() {
        super.init();
        playerInventoryTitleY += 30;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        renderProgress(matrices, x, y);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    private void renderProgress(MatrixStack matrices, int x, int y) {
        if (handler.isCrafting()) {
            drawTexture(matrices, x + 80, y + 39, 176, 3, 17, handler.getScaledProgress());
        }
    }
}
