package be.minelabs.renderer;

import be.minelabs.Minelabs;
import be.minelabs.entity.BohrBlueprintEntity;
import be.minelabs.util.AtomConfiguration;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BohrBlueprintHUDRenderer extends DrawableHelper {
    // range in blocks from where the HUD is rendered
    public static final int HUD_RENDER_RADIUS = 9;
    // maximal angle in radians between player camera and entity where HUD is still rendered
    public static final double MAX_RENDER_ANGLE = Math.PI / 2;

    private static final Identifier BARS_TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/bohr_bars.png");
    private static final int BARS_TEXTURE_SIZE = 256;

    // Color for rendering the text
    private static final int WHITE = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    private static final int YELLOW = ColorHelper.Argb.getArgb(255, 240, 225, 45);
    private static final int RED = ColorHelper.Argb.getArgb(255, 227, 23, 98);

    private static final int SHADOW_COLOR = ColorHelper.Argb.getArgb(255, 62, 62, 62);

    // for block breaking effect on atom square
    private static final int DESTRUCTION_STAGES = ModelLoader.BLOCK_DESTRUCTION_STAGE_TEXTURES.size();

    // settings of texture
    private static final int ELEMENT_SQUARE_SIZE = 38;  // limiting factor is height: 2 texts of 8, one of 16 and 4 gaps
    private static final int BARS_WIDTH = 182;
    private static final int BARS_HEIGHT = 5;
    private static final int BARS_RED_OUTLINE_V = 33;
    private static final int MC_TEXT_HEIGHT = 8;

    // period for red bar indicator in ticks
    private static final int BARS_FLICKER_PERIOD = 20;
    private static final float BAR_TEXT_SCALE = 0.5f;
    private static final int BARS_POSITION_OFFSET = 8;
    private static final int TOP_PADDING = 12;      // top padding of bars. Square is a bit higher
    private static final int HORIZONTAL_PADDING = 8;
    private static final int NAME_OFFSET_Y = 6;

    /**
     * renders the text of the bohrplate status. Gets called from HUD render event callback.
     */
    public static void renderHud(MatrixStack matrixStack, BohrBlueprintEntity entity) {
        AtomConfiguration atomConfig = entity.getAtomConfig();
        float integrity = entity.getIntegrity();

        renderHud(matrixStack, atomConfig, integrity);
    }

    public static void renderHud(MatrixStack matrixStack, AtomConfiguration atomConfig, float integrity) {
        MinecraftClient.getInstance().getProfiler().push("bohr_hud");
        int xBars = (MinecraftClient.getInstance().getWindow().getScaledWidth() - BARS_WIDTH) / 2;

        matrixStack.push();
        MinecraftClient.getInstance().getProfiler().push("bars");
        matrixStack.translate(xBars, TOP_PADDING, 0);
        renderBars(matrixStack, atomConfig);
        if (atomConfig.getProtons() > 0 || atomConfig.getNeutrons() > 0) {
            MinecraftClient.getInstance().getProfiler().swap("square");
            // shift to left of bars and center for element square
            matrixStack.push();
            int xOffset = -(HORIZONTAL_PADDING + ELEMENT_SQUARE_SIZE);
            int yOffset = -(ELEMENT_SQUARE_SIZE - (2 * BARS_POSITION_OFFSET + BARS_HEIGHT)) / 2;
            matrixStack.translate(xOffset, yOffset, 0);
            renderElementSquare(matrixStack, atomConfig, integrity);
            matrixStack.pop();

            MinecraftClient.getInstance().getProfiler().swap("name");
            // shift to right of bars for name
            matrixStack.push();
            xOffset = HORIZONTAL_PADDING + BARS_WIDTH;
            matrixStack.translate(xOffset, NAME_OFFSET_Y, 0);
            renderElementName(matrixStack, atomConfig);
            matrixStack.pop();
        }
        MinecraftClient.getInstance().getProfiler().pop();

        // DEBUG: render stability as percent left of element square
//        String stability = (int) ((1f - atomConfig.getNucleusInstability()) * 100) + "%";
//        drawCenteredText(matrixStack, MinecraftClient.getInstance().textRenderer, stability, -80, 8, WHITE);

        matrixStack.pop();
        MinecraftClient.getInstance().getProfiler().pop();
    }

    private static void renderElementSquare(MatrixStack matrixStack, AtomConfiguration atomConfig, float integrity) {
        String symbol = atomConfig.getSymbol().orElse("?");
        String protons = Integer.toString(atomConfig.getProtons());
        String mass = Integer.toString(atomConfig.getProtons() + atomConfig.getNeutrons());

        int charge = atomConfig.getProtons() - atomConfig.getElectrons();
        String ionicCharge = String.format("%+d", charge);

        int eColor = WHITE;
        if (atomConfig.isElectronDecomposing())
            eColor = RED;
        else if (!atomConfig.isElectronStable())
            eColor = YELLOW;

        int zColor = WHITE;
        if (atomConfig.isNucleusDecomposing())
            zColor = RED;
        else if (!atomConfig.isNucleusStable())
            zColor = YELLOW;

        // can be made dynamic, but isn't needed for now
        int squareSize = ELEMENT_SQUARE_SIZE;
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;

        // draw frame
        drawSquare(matrixStack, squareSize, WHITE);

        // draw symbol
        matrixStack.push();
        int squareCenter = squareSize / 2;
        matrixStack.translate(squareCenter, squareCenter, 0);
        matrixStack.scale(2, 2, 2);
        matrixStack.translate(0, (double) -MC_TEXT_HEIGHT / 2, 0);
        drawCenteredTextWithShadow(matrixStack, tr, symbol, 0, 0, WHITE);
        matrixStack.pop();

        // draw numbers
        drawTextWithShadow(matrixStack, tr, protons, 2, 2, WHITE);
        drawTextWithShadow(matrixStack, tr, mass, 2, squareSize - 2 - MC_TEXT_HEIGHT, zColor);

        if (!atomConfig.isElectronStable()) {
            int textWidth = tr.getWidth(ionicCharge);
            drawTextWithShadow(matrixStack, tr, ionicCharge, squareSize - 2 - textWidth, 2, eColor);
        }

        // overlay when decomposing
        drawIntegrity(matrixStack, squareSize, atomConfig, integrity);
    }

    /**
     * drawRectangle with shadow=true
     */
    private static void drawRectangle(MatrixStack matrixStack, int width, int height, int color) {
        fill(matrixStack, 1, 0, width - 1, 1, color);           // top
        fill(matrixStack, 1, height - 1, width - 1, height, color);         // bottom
        fill(matrixStack, 0, 1, 1, height - 1, color);          // left
        fill(matrixStack, width - 1, 1, width, height - 1, color);          // right
    }

    /**
     * Draw outline of a rectangle of width and height (including edges) and corners empty/
     * Shadow is not included in size.
     */
    private static void drawRectangle(MatrixStack matrixStack, int width, int height, int color, boolean shadow) {
        if (shadow) {
            // alternative: all edges have shadow (if used: give functions x and y params please)
//            matrixStack.push();
//            matrixStack.translate(1, 1, 0);
//            drawRectangle(matrixStack, width, height, SHADOW_COLOR);
//            matrixStack.pop();

            fill(matrixStack, 2, height, width, height + 1, SHADOW_COLOR);         // bottom
            fill(matrixStack, width, 2, width + 1, height, SHADOW_COLOR);          // right
        }

        drawRectangle(matrixStack, width, height, color);
    }

    /**
     * drawSquare with shadow.
     */
    private static void drawSquare(MatrixStack matrixStack, int size, int color) {
        drawSquare(matrixStack, size, color, true);
    }

    /**
     * Draw outline of a square with given size (including edges) and corners empty
     */
    private static void drawSquare(MatrixStack matrixStack, int size, int color, boolean shadow) {
        drawRectangle(matrixStack, size, size, color, shadow);
    }

    private static void renderElementName(MatrixStack matrixStack, AtomConfiguration atomConfig) {
        if (atomConfig.getName().isEmpty())
            return;

        String atomName = atomConfig.getName().get();
        if (!atomConfig.isElectronStable())
            atomName += " ion";

        int color = WHITE;
        if (atomConfig.isNucleusDecomposing())
            color = RED;
        else if (!atomConfig.isNucleusStable() || !atomConfig.isElectronStable())
            color = YELLOW;

        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
//        if (atomConfig.isNucleusStable() && !atomConfig.isElectronDecomposing())
        drawTextWithShadow(matrixStack, tr, atomName, 0, 0, color);
    }

    private static int getDestructionStage(float integrity) {
        int p = (int) MathHelper.clampedLerp(0, DESTRUCTION_STAGES, 1 - integrity);
        if (p >= DESTRUCTION_STAGES) p = DESTRUCTION_STAGES - 1;
        return p;
    }

    private static void drawIntegrity(MatrixStack matrixStack, int size, AtomConfiguration atomConfig, float integrity) {
        if (!atomConfig.isNucleusDecomposing()) return;

        int stage = getDestructionStage(integrity);
        Identifier crumblingTexture = ModelLoader.BLOCK_DESTRUCTION_STAGE_TEXTURES.get(stage);
        RenderLayer blockBreakingLayer = RenderLayer.getBlockBreaking(crumblingTexture);

        blockBreakingLayer.startDrawing();

        // override these settings to make it work on GUI rather than on blocks
        RenderSystem.setShaderColor(1, 1, 1, 0.5f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // 1 pixel padding for border
        float scale = (size - 2) / 16f;
        matrixStack.push();
        matrixStack.translate(1, 1, 0);
        matrixStack.scale(scale, scale, scale);
        // z=1 makes sure it renders above other text
        drawTexture(matrixStack, 0, 0, 1, 0, 0, 16, 16, 16, 16);
        matrixStack.pop();

        blockBreakingLayer.endDrawing();
    }

    private static void renderBars(MatrixStack matrixStack, AtomConfiguration atomConfig) {
        BarCapacity cap = BarCapacity.get(atomConfig);

        drawFilledBar(matrixStack, 0, 0, atomConfig.getProtons(), cap);
        drawFilledBar(matrixStack, BARS_POSITION_OFFSET, 4 * BARS_HEIGHT, atomConfig.getNeutrons(), cap, atomConfig.isNucleusStable());
        drawFilledBar(matrixStack, 2 * BARS_POSITION_OFFSET, 2 * BARS_HEIGHT, atomConfig.getElectrons(), cap, atomConfig.isElectronStable());
    }

    // assumes texture is already set
    private static void drawBar(MatrixStack matrixStack, int y, int v, int width) {
        drawTexture(matrixStack, 0, y, -10, 0, v, width, BARS_HEIGHT, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
    }

    // assumes texture is already set
    private static void drawBar(MatrixStack matrixStack, int y, int v) {
        drawBar(matrixStack, y, v, BARS_WIDTH);
    }

    // assumes texture is already set
    private static void drawRedOutlineBar(MatrixStack matrixStack, int y) {
        long time = MinecraftClient.getInstance().world != null ? MinecraftClient.getInstance().world.getTime() : 0;
        if ((time % BARS_FLICKER_PERIOD) < BARS_FLICKER_PERIOD / 2)
            drawBar(matrixStack, y, BARS_RED_OUTLINE_V, BARS_WIDTH);
    }

    // assumes texture is already set
    private static void drawFilledBar(MatrixStack matrixStack, int y, int vEmpty, float progress) {
        int vFilled = vEmpty + BARS_HEIGHT;       // see texture: filled bar is below empty one

        drawBar(matrixStack, y, vEmpty);
        drawBar(matrixStack, y, vFilled, (int) (BARS_WIDTH * progress));
    }

    // assumes texture is already set
    private static void drawBarTicks(MatrixStack matrixStack, int y, BarCapacity cap) {
        if (cap == BarCapacity.CAP_MAX)
            return;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        drawBar(matrixStack, y, cap.vTicks);
        RenderSystem.disableBlend();
    }

    private static void drawBarText(MatrixStack matrixStack, int y, int amount, float progress) {
        if (amount == 0)
            return;
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        int x = (int) (progress * BARS_WIDTH);
        String label = Integer.toString(amount);
        int textWidth = tr.getWidth(label);

        matrixStack.push();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(BAR_TEXT_SCALE, BAR_TEXT_SCALE, BAR_TEXT_SCALE);
        drawTextWithShadow(matrixStack, tr, label, -textWidth - 1, 1, WHITE);
        matrixStack.pop();
    }

    /**
     * Renders filled bar with ticks and text. Texture does not need to be set beforehand.
     */
    private static void drawFilledBar(MatrixStack matrixStack, int y, int vEmpty, int amount, BarCapacity cap) {
        drawFilledBar(matrixStack, y, vEmpty, amount, cap, true);
    }

    /**
     * Renders filled bar with ticks, text and instability indicator. Texture does not need to be set beforehand.
     */
    private static void drawFilledBar(MatrixStack matrixStack, int y, int vEmpty, int amount, BarCapacity cap, boolean stable) {
        float progress = (float) amount / cap.capacity;

        RenderSystem.setShaderTexture(0, BARS_TEXTURE);
        drawFilledBar(matrixStack, y, vEmpty, progress);
        drawBarTicks(matrixStack, y, cap);

        if (!stable) {
            drawRedOutlineBar(matrixStack, y);
        }

        // this call changes texture
        drawBarText(matrixStack, y, amount, progress);
    }

    /**
     * Constants for representing the ticks and max capacity of the bars.
     */
    private enum BarCapacity {
        CAP_10(10, 45),
        CAP_40(40, 55),
        // no ticks rendered
        CAP_MAX(BohrBlueprintEntity.MAX_NEUTRONS, 0);

        final int capacity;
        final int vTicks;

        BarCapacity(int capacity, int vTicks) {
            this.capacity = capacity;
            this.vTicks = vTicks;
        }

        static BarCapacity get(AtomConfiguration atomConfig) {
            int n = Math.max(atomConfig.getProtons(), Math.max(atomConfig.getElectrons(), atomConfig.getNeutrons()));
            if (n > 39) return CAP_MAX;
            else if (n > 9) return CAP_40;
            else return CAP_10;
        }
    }
}
