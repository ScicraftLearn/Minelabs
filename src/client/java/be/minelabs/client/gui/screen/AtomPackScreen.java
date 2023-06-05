package be.minelabs.client.gui.screen;

import be.minelabs.Minelabs;
import be.minelabs.screen.AtomPackScreenHandler;
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
public class AtomPackScreen extends HandledScreen<AtomPackScreenHandler> implements ScreenHandlerProvider<AtomPackScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/atom_pack/atom_pack_inventory.png");

    public AtomPackScreen(AtomPackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
    }
}
