package be.uantwerpen.scicraft.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class AtomPortalScreen extends HandledScreen<AtomPortalScreenHandler> {
    public AtomPortalScreen(AtomPortalScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        System.out.println("misschien hier");
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices,mouseX,mouseY);
    }

    @Override
    protected void init() {
        super.init();
        //Berekenen van plaatsing titel
        titleX=(backgroundWidth-textRenderer.getWidth(title))/2;
    }
}
