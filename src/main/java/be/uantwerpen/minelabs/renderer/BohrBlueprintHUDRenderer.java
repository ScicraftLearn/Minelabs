package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.util.AtomConfiguration;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

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

    // for block breaking effect on atom square
    private static final int DESTRUCTION_STAGES = ModelLoader.BLOCK_DESTRUCTION_STAGE_TEXTURES.size();


    /**
     * renders the text of the bohrplate status. Gets called from HUD render event callback.
     */
    public static void renderHud(MatrixStack matrixStack, BohrBlueprintEntity entity) {
        AtomConfiguration atomConfig = entity.getAtomConfig();
        float integrity = entity.getIntegrity();

        renderHud(matrixStack, atomConfig, integrity);
    }

    private static void renderHud(MatrixStack matrixStack, AtomConfiguration atomConfig, float integrity) {
        int y = 12;
        int x = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - 91;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BARS_TEXTURE);

        renderBars(matrixStack, atomConfig, x, y);
        if (atomConfig.getProtons() > 0)
            renderText(matrixStack, atomConfig, integrity, x, y);
    }

    private static void renderText(MatrixStack matrixStack, AtomConfiguration atomConfig, float integrity, int x, int y){
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        String atomName = atomConfig.getName().orElse("");
        String symbol = atomConfig.getSymbol().orElse("_");

        int charge = atomConfig.getProtons() - atomConfig.getElectrons();
        String ionicCharge = String.format("%+d", charge);

        int Ecolor = WHITE;
        int Zcolor = WHITE;

        if (atomConfig.getProtons() != atomConfig.getElectrons()) {
            Ecolor = YELLOW;
            atomName += " ION";
        }
        if (Math.abs(atomConfig.getProtons() - atomConfig.getElectrons()) > 5) {
            Ecolor = RED;
        }
        if (!atomConfig.isNucleusStable()) {
            Zcolor = RED;
        }

        /*
         * Rendering of text:
         */
        matrixStack.push();
        matrixStack.translate(x-45, y-6, 0);
        drawTexture(matrixStack, 0, 0, 0, 63, 34, 34, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        matrixStack.pop();


        TextRenderer TR = MinecraftClient.getInstance().textRenderer;
        matrixStack.push();
        matrixStack.scale(2, 2, 2);
        int width = TR.getWidth(symbol) - 1;
        TR.draw(matrixStack, symbol, (x - 32 - width / 2) / 2, (y + 4) / 2, WHITE);
        TR.draw(matrixStack, (int) (integrity * 100) + "%", (x - 96) / 2, (y + 4) / 2, WHITE);
        matrixStack.pop();
        //if (!neutronHelp.isEmpty() || !electronHelp.isEmpty()) {
        //  MinecraftClient.getInstance().textRenderer.draw(matrixStack, helpInfo, 10, 20, RED_COLOR);
        //}
        TR.draw(matrixStack, Integer.toString(atomConfig.getProtons()), x - 43, y + 19, WHITE);
        TR.draw(matrixStack, Integer.toString(atomConfig.getProtons() + atomConfig.getNeutrons()), x - 43, y - 4, Zcolor);
        if (!ionicCharge.equals("0")) {
            int width_e = TR.getWidth(ionicCharge);
            TR.draw(matrixStack, ionicCharge, x - 11 - width_e, y - 4, Ecolor);
        }
        if (atomConfig.isNucleusStable() && Math.abs(atomConfig.getProtons() - atomConfig.getElectrons()) <= 5) {
            TR.draw(matrixStack, atomName, x + 192, y + 7, Ecolor);
        }

        matrixStack.push();
        matrixStack.translate(x-44, y-5, 0);
        matrixStack.scale(2, 2, 2);
        renderIntegrity(matrixStack, atomConfig, integrity);
        matrixStack.pop();
    }

    private static int getDestructionStage(float integrity){
        int p = (int) MathHelper.clampedLerp(0, DESTRUCTION_STAGES, 1 - integrity);
        if (p >= DESTRUCTION_STAGES) p = DESTRUCTION_STAGES - 1;
        return p;
    }

    private static void renderIntegrity(MatrixStack matrixStack, AtomConfiguration atomConfig, float integrity){
        if (!atomConfig.isNucleusDecomposing()) return;

        int stage = getDestructionStage(integrity);
        Identifier crumblingTexture = ModelLoader.BLOCK_DESTRUCTION_STAGE_TEXTURES.get(stage);
        RenderLayer blockBreakingLayer = RenderLayer.getBlockBreaking(crumblingTexture);

        blockBreakingLayer.startDrawing();

        // override these settings to make it work on GUI rather than on blocks
        RenderSystem.setShaderColor(1, 1, 1, 0.5f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // z=1 makes sure it renders above other text
        drawTexture(matrixStack, 0, 0, 1, 0, 0, 16, 16, 16, 16);

        blockBreakingLayer.endDrawing();
    }


    private static void renderBars(MatrixStack matrixStack, AtomConfiguration atomConfig, int x, int y) {
        int n = Math.max(atomConfig.getProtons(), Math.max(atomConfig.getElectrons(), atomConfig.getNeutrons()));
        int scale = n > 39 ? 176 : n > 9 ? 40 : 10;

        drawTexture(matrixStack, x, y, 0, 0, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        drawTexture(matrixStack, x, y + 8, 0, 10, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        drawTexture(matrixStack, x, y + 16, 0, 20, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);

        int ratio_p = atomConfig.getProtons() * 182 / scale;
        int ratio_e = atomConfig.getElectrons() * 182 / scale;
        int ratio_n = atomConfig.getNeutrons() * 182 / scale;

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
        if (atomConfig.getProtons() > 0) {
            if (Math.abs(atomConfig.getProtons() - atomConfig.getElectrons()) > 5) {
                drawTexture(matrixStack, x, y + 8, 0, 33, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
            }
            if (!atomConfig.isNucleusStable()) {
                drawTexture(matrixStack, x, y + 16, 0, 33, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
            }
        }
        matrixStack.push();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        TextRenderer TR = MinecraftClient.getInstance().textRenderer;
        drawCenteredText(matrixStack, TR, Text.of(Integer.toString(atomConfig.getProtons())), (x + ratio_p) * 2, y * 2 + 1, WHITE);
        drawCenteredText(matrixStack, TR, Text.of(Integer.toString(atomConfig.getElectrons())), (x + ratio_e) * 2, (y + 8) * 2 + 1, WHITE);
        drawCenteredText(matrixStack, TR, Text.of(Integer.toString(atomConfig.getNeutrons())), (x + ratio_n) * 2, (y + 16) * 2 + 1, WHITE);

        matrixStack.pop();

        RenderSystem.setShaderTexture(0, BARS_TEXTURE);
    }

}
