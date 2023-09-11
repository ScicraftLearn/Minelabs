package be.minelabs.client.gui.hud;

import be.minelabs.Minelabs;
import be.minelabs.entity.BohrBlueprintEntity;
import be.minelabs.util.AtomConfiguration;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BohrBlueprintHUDRenderer {
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
    public static void renderHud(DrawContext context, BohrBlueprintEntity entity) {
        AtomConfiguration atomConfig = entity.getAtomConfig();
        float integrity = entity.getIntegrity();

        renderHud(context, atomConfig, integrity);
    }

    public static void renderHud(DrawContext context, AtomConfiguration atomConfig, float integrity) {
        MinecraftClient.getInstance().getProfiler().push("bohr_hud");
        int xBars = (MinecraftClient.getInstance().getWindow().getScaledWidth() - BARS_WIDTH) / 2;

        context.getMatrices().push();
        MinecraftClient.getInstance().getProfiler().push("bars");
        context.getMatrices().translate(xBars, TOP_PADDING, 0);
        renderBars(context, atomConfig);
        if (atomConfig.getProtons() > 0 || atomConfig.getNeutrons() > 0) {
            MinecraftClient.getInstance().getProfiler().swap("square");
            // shift to left of bars and center for element square
            context.getMatrices().push();
            int xOffset = -(HORIZONTAL_PADDING + ELEMENT_SQUARE_SIZE);
            int yOffset = -(ELEMENT_SQUARE_SIZE - (2 * BARS_POSITION_OFFSET + BARS_HEIGHT)) / 2;
            context.getMatrices().translate(xOffset, yOffset, 0);
            renderElementSquare(context, atomConfig, integrity);
            context.getMatrices().pop();

            MinecraftClient.getInstance().getProfiler().swap("name");
            // shift to right of bars for name
            context.getMatrices().push();
            xOffset = HORIZONTAL_PADDING + BARS_WIDTH;
            context.getMatrices().translate(xOffset, NAME_OFFSET_Y, 0);
            renderElementName(context, atomConfig);
            context.getMatrices().pop();
        }
        MinecraftClient.getInstance().getProfiler().pop();

        // DEBUG: render stability as percent left of element square
//        String stability = (int) ((1f - atomConfig.getNucleusInstability()) * 100) + "%";
//        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, stability, -80, 8, WHITE);


        context.getMatrices().pop();
        MinecraftClient.getInstance().getProfiler().pop();
    }

    private static void renderElementSquare(DrawContext context, AtomConfiguration atomConfig, float integrity) {
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
        drawSquare(context, squareSize, WHITE);

        // draw symbol
        context.getMatrices().push();
        int squareCenter = squareSize / 2;
        context.getMatrices().translate(squareCenter, squareCenter, 0);
        context.getMatrices().scale(2, 2, 2);
        context.getMatrices().translate(0, (double) -MC_TEXT_HEIGHT / 2, 0);
        context.drawCenteredTextWithShadow(tr, symbol, 0,0, WHITE);
        context.getMatrices().pop();

        // draw numbers
        context.drawText(tr, protons, 2,2, WHITE, true);
        context.drawText(tr, mass, 2, squareSize - 2 - MC_TEXT_HEIGHT, zColor,  true);

        if (!atomConfig.isElectronStable()) {
            int textWidth = tr.getWidth(ionicCharge);
            context.drawText(tr, ionicCharge, squareSize - 2 - textWidth, 2, eColor, true);
        }

        // overlay when decomposing
        drawIntegrity(context, squareSize, atomConfig, integrity);
    }

    /**
     * drawRectangle with shadow=true
     */
    private static void drawRectangle(DrawContext context, int width, int height, int color) {
        context.fill(1, 0, width - 1, 1, color);           // top
        context.fill(1, height - 1, width - 1, height, color);         // bottom
        context.fill(0, 1, 1, height - 1, color);          // left
        context.fill(width - 1, 1, width, height - 1, color);          // right
    }

    /**
     * Draw outline of a rectangle of width and height (including edges) and corners empty/
     * Shadow is not included in size.
     */
    private static void drawRectangle(DrawContext context, int width, int height, int color, boolean shadow) {
        if (shadow) {
            // alternative: all edges have shadow (if used: give functions x and y params please)
            //context.getMatrices().push();
            //context.getMatrices().translate(1, 1, 0);
            //drawRectangle(context, width, height, SHADOW_COLOR);
            //context.getMatrices().pop();

            context.fill(2, height, width, height + 1, SHADOW_COLOR);         // bottom
            context.fill(width, 2, width + 1, height, SHADOW_COLOR);          // right
        }

        drawRectangle(context, width, height, color);
    }

    /**
     * drawSquare with shadow.
     */
    private static void drawSquare(DrawContext context, int size, int color) {
        drawSquare(context, size, color, true);
    }

    /**
     * Draw outline of a square with given size (including edges) and corners empty
     */
    private static void drawSquare(DrawContext context, int size, int color, boolean shadow) {
        drawRectangle(context, size, size, color, shadow);
    }

    private static void renderElementName(DrawContext context, AtomConfiguration atomConfig) {
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
        context.drawText(tr, atomName, 0, 0, color, true);
    }

    private static int getDestructionStage(float integrity) {
        int p = (int) MathHelper.clampedLerp(0, DESTRUCTION_STAGES, 1 - integrity);
        if (p >= DESTRUCTION_STAGES) p = DESTRUCTION_STAGES - 1;
        return p;
    }

    private static void drawIntegrity(DrawContext context, int size, AtomConfiguration atomConfig, float integrity) {
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
        context.getMatrices().push();
        context.getMatrices().translate(1, 1, 0);
        context.getMatrices().scale(scale, scale, scale);
        // z=1 makes sure it renders above other text
        context.drawTexture(crumblingTexture, 0, 0, 1, 0, 0, 16, 16, 16, 16);
        context.getMatrices().pop();

        blockBreakingLayer.endDrawing();
    }

    private static void renderBars(DrawContext context, AtomConfiguration atomConfig) {
        BarCapacity cap = BarCapacity.get(atomConfig);

        drawFilledBar(context, 0, 0, atomConfig.getProtons(), cap);
        drawFilledBar(context, BARS_POSITION_OFFSET, 4 * BARS_HEIGHT, atomConfig.getNeutrons(), cap, atomConfig.isNucleusStable());
        drawFilledBar(context, 2 * BARS_POSITION_OFFSET, 2 * BARS_HEIGHT, atomConfig.getElectrons(), cap, atomConfig.isElectronStable());
    }

    // assumes texture is already set
    private static void drawBar(DrawContext context, int y, int v, int width) {
        context.drawTexture(BARS_TEXTURE, 0, y, -10, 0, v, width, BARS_HEIGHT, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
    }

    // assumes texture is already set
    private static void drawBar(DrawContext context, int y, int v) {
        drawBar(context, y, v, BARS_WIDTH);
    }

    // assumes texture is already set
    private static void drawRedOutlineBar(DrawContext context, int y) {
        long time = MinecraftClient.getInstance().world != null ? MinecraftClient.getInstance().world.getTime() : 0;
        if ((time % BARS_FLICKER_PERIOD) < BARS_FLICKER_PERIOD / 2)
            drawBar(context, y, BARS_RED_OUTLINE_V, BARS_WIDTH);
    }

    // assumes texture is already set
    private static void drawFilledBar(DrawContext context, int y, int vEmpty, float progress) {
        int vFilled = vEmpty + BARS_HEIGHT;       // see texture: filled bar is below empty one

        drawBar(context, y, vEmpty);
        drawBar(context, y, vFilled, (int) (BARS_WIDTH * progress));
    }

    // assumes texture is already set
    private static void drawBarTicks(DrawContext context, int y, BarCapacity cap) {
        if (cap == BarCapacity.CAP_MAX)
            return;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        drawBar(context, y, cap.vTicks);
        RenderSystem.disableBlend();
    }

    private static void drawBarText(DrawContext context, int y, int amount, float progress) {
        if (amount == 0)
            return;
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        int x = (int) (progress * BARS_WIDTH);
        String label = Integer.toString(amount);
        int textWidth = tr.getWidth(label);

        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(BAR_TEXT_SCALE, BAR_TEXT_SCALE, BAR_TEXT_SCALE);
        context.drawText(tr, label, -textWidth - 1, 1, WHITE, true);
        context.getMatrices().pop();
    }

    /**
     * Renders filled bar with ticks and text. Texture does not need to be set beforehand.
     */
    private static void drawFilledBar(DrawContext context, int y, int vEmpty, int amount, BarCapacity cap) {
        drawFilledBar(context, y, vEmpty, amount, cap, true);
    }

    /**
     * Renders filled bar with ticks, text and instability indicator. Texture does not need to be set beforehand.
     */
    private static void drawFilledBar(DrawContext context, int y, int vEmpty, int amount, BarCapacity cap, boolean stable) {
        float progress = (float) amount / cap.capacity;

        RenderSystem.setShaderTexture(0, BARS_TEXTURE);
        drawFilledBar(context, y, vEmpty, progress);
        drawBarTicks(context, y, cap);

        if (!stable) {
            drawRedOutlineBar(context, y);
        }

        // this call changes texture
        drawBarText(context, y, amount, progress);
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
