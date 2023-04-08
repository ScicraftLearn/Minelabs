package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.crafting.molecules.Atom;
import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.util.NucleusState;
import be.uantwerpen.minelabs.util.NuclidesTable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.client.gui.DrawableHelper.drawCenteredText;
import static net.minecraft.client.gui.DrawableHelper.drawTexture;

public class BohrBlueprintHUDRenderer {
    // range in blocks from where the HUD is rendered
    public static final int HUD_RENDER_RADIUS = 9;

    private static final Identifier BARS_TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/bohr_bars.png");
    private static final int BARS_TEXTURE_SIZE = 256;

    // Color for rendering the text
    private static final int WHITE = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    private static final int YELLOW = ColorHelper.Argb.getArgb(255, 240, 225, 45);
    private static final int RED = ColorHelper.Argb.getArgb(255, 227, 23, 98);

    /**
     * renders the text of the bohrplate status. Gets called from HUD render event callback.
     */
    public static void renderHud(MatrixStack matrixStack, BohrBlueprintEntity entity) {
        int nP = entity.getProtons();
        int nN = entity.getNeutrons();
        int nE = entity.getElectrons();

        float integrity = entity.getIntegrity();
        NucleusState nucleusState = entity.getNucleusState();
        renderHud(matrixStack, nP, nE, nN, integrity, nucleusState);
    }

    private static void renderHud(MatrixStack matrixStack, int nP, int nE, int nN, float integrity, @Nullable NucleusState nucleusState) {
        int y = 12;
        int x = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - 91;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BARS_TEXTURE);

        renderBars(matrixStack, nP, nE, nN, x, y, nucleusState);

        if (nucleusState == null)
            return;

        if (nP > 0) {
            String atomName = "";
            String symbol = "_";
            Atom a = Atom.getByNumber(nP);
            if (a!=null){
                atomName = Text.translatable(a.getItem().getTranslationKey()).getString();
                symbol = a.getSymbol();
            } else {
                // TODO ideally not needed when all atoms are implemented
                // This should return symbol based on nP, regardless if it is in the nuclides file
                symbol = nucleusState.getSymbol();
            }

            String ionicCharge = NuclidesTable.calculateIonicCharge(nP, nE);

            int Ecolor = WHITE;
            int Zcolor = WHITE;

            if (nP != nE) {
                Ecolor = YELLOW;
                atomName += " ION";
            }
            if (Math.abs(nP - nE) > 5) {
                Ecolor = RED;
            }
            if (!nucleusState.isStable()) {
                Zcolor = RED;
            }

            /*
             * Rendering of text:
             */
            drawTexture(matrixStack, x-45, y-6, 0, 63, 34, 34, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);

            TextRenderer TR = MinecraftClient.getInstance().textRenderer;
            matrixStack.push();
            matrixStack.scale(2, 2, 2);
            int width = TR.getWidth(symbol);
            TR.draw(matrixStack, symbol, (x - 32 - width / 2) / 2, (y+4) / 2, WHITE);
            TR.draw(matrixStack, (int)integrity + "%", (x - 96) / 2, (y+4) / 2, WHITE);
            matrixStack.pop();
            //if (!neutronHelp.isEmpty() || !electronHelp.isEmpty()) {
            //  MinecraftClient.getInstance().textRenderer.draw(matrixStack, helpInfo, 10, 20, RED_COLOR);
            //}
            TR.draw(matrixStack, Integer.toString(nP), x - 43, y +19, WHITE);
            TR.draw(matrixStack, Integer.toString(nP + nN), x - 43, y - 4, Zcolor);
            if(!ionicCharge.equals("0")) {
                int width_e = TR.getWidth(ionicCharge);
                TR.draw(matrixStack, ionicCharge, x-11 - width_e, y-4, Ecolor);
            }
            if (nucleusState.isStable() && Math.abs(nP - nE) <= 5) {
                TR.draw(matrixStack, atomName, x + 192, y + 7, Ecolor);
            }
        }
    }


    private static void renderBars(MatrixStack matrixStack, int nP, int nE, int nN, int x, int y, NucleusState nucleusState) {
        int n = Math.max(nP, Math.max(nE, nN));
        int scale = n > 39 ? 176 : n > 9 ? 40 : 10;

        drawTexture(matrixStack, x, y, 0, 0, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        drawTexture(matrixStack, x, y + 8, 0, 10, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        drawTexture(matrixStack, x, y + 16, 0, 20, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);

        int ratio_p = nP * 182 / scale;
        int ratio_e = nE * 182 / scale;
        int ratio_n = nN * 182 / scale;

        drawTexture(matrixStack, x, y, 0, 5, ratio_p, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        drawTexture(matrixStack, x, y + 8, 0, 15, ratio_e, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        drawTexture(matrixStack, x, y + 16, 0, 25, ratio_n, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);

        if (scale != 176) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            int s;
            if (scale == 10) {
                s = 45;
            } else {
                s = 55;
            }
            drawTexture(matrixStack, x, y, 0, s, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
            drawTexture(matrixStack, x, y + 8, 0, s, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
            drawTexture(matrixStack, x, y + 16, 0, s, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);

            RenderSystem.disableBlend();

        }
        if (nP > 0) {
            if (Math.abs(nP - nE) > 5) {
                drawTexture(matrixStack, x, y + 8, 0, 33, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
            }
            if (nucleusState==null|| !nucleusState.isStable()) {
                drawTexture(matrixStack, x, y + 16, 0, 33, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
            }
        }
        matrixStack.push();
        matrixStack.scale(0.5f,0.5f,0.5f);
        TextRenderer TR = MinecraftClient.getInstance().textRenderer;
        drawCenteredText(matrixStack, TR, Text.of(Integer.toString(nP)), (x+ratio_p)*2, y*2+1, WHITE);
        drawCenteredText(matrixStack, TR, Text.of(Integer.toString(nE)), (x+ratio_e)*2, (y+8)*2+1, WHITE);
        drawCenteredText(matrixStack, TR, Text.of(Integer.toString(nN)), (x+ratio_n)*2, (y+16)*2+1, WHITE);

        matrixStack.pop();

        RenderSystem.setShaderTexture(0, BARS_TEXTURE);
    }

}
