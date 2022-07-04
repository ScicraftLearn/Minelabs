package be.uantwerpen.scicraft.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class LewisScreen extends HandledScreen<LewisBlockScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("scicraft", "textures/block/lewiscrafting/lewis_block_inventory.png");

    public LewisScreen(LewisBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        // 3x18 for 3 inventory slots
        // +4 for extra offset to match the double chest
        backgroundHeight += (18 * 3 + 4);
    }

    /**
     * draw function is called every tick
     */
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        playerInventoryTitleX = (backgroundWidth - textRenderer.getWidth(playerInventoryTitle)) / 2;

        // move the title to the correct place
        playerInventoryTitleY += 56;


    }
}
