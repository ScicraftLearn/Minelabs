package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.entity.BohrBlockEntity;
import be.uantwerpen.minelabs.crafting.molecules.Atom;
import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.NucleusState;
import be.uantwerpen.minelabs.util.NuclidesTable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import static net.minecraft.client.gui.DrawableHelper.drawTexture;

public class BohrBlueprintEntityRenderer<T extends BohrBlueprintEntity> extends EntityRenderer<T> {

    // range in blocks from where the HUD is rendered
    public static final int HUD_RENDER_RADIUS = 9;

    private static final Identifier BARS_TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/bohr_bars.png");
    private static final int BARS_TEXTURE_SIZE = 256;


    //    Color for rendering the text
    public static final int WHITE = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    public static final int YELLOW = ColorHelper.Argb.getArgb(255, 240, 225, 45);
    public static final int RED = ColorHelper.Argb.getArgb(255,227,23,98);

    private final ItemRenderer itemRenderer;
    private final BlockRenderManager blockRenderManager;

    public BohrBlueprintEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        int seed = entity.getId();

        ItemStack itemStack = new ItemStack(Items.PROTON, 1);
        this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, seed);
    }

    @Override
    public Identifier getTexture(T entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }


    /**
     * renders the text of the bohrplate status. Gets called from HUD render event callback.
     */
    public static void renderHud(MatrixStack matrixStack, BohrBlueprintEntity entity) {
//        int nP = entity.getProtonCount();
//        int nN = entity.getNeutronCount();
//        int nE = entity.getElectronCount();

        int nP = 4;
        int nE = 4;
        int nN = 6;

        renderHud(matrixStack, nP, nE, nN);
    }

    private static void renderHud(MatrixStack matrixStack, int nP, int nE, int nN) {
        int y = 12;
        int x = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - 91;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BARS_TEXTURE);


        renderBars(matrixStack, nP, nE, nN, x, y);

        if(nP>0) {
            NucleusState nuclideStateInfo = NuclidesTable.getNuclide(nP, nN);
            String atomName = "";
            String symbol = "_";
            Atom a = Atom.getByNumber(nP);
            atomName = Text.translatable(a.getItem().getTranslationKey()).getString();
            symbol = a.getSymbol();
            String ionicCharge = NuclidesTable.calculateIonicCharge(nP, nE);

            int Ecolor = WHITE;
            int Zcolor = WHITE;
            boolean doesStableNuclideExist = true;

            if (nuclideStateInfo != null) {

                if (nP != nE) {
                    Ecolor = YELLOW;
                    atomName += " ION";
                }
                if (Math.abs(nP - nE) > 5) {
                    Ecolor = RED;
                    drawTexture(matrixStack, x, y+8, 0, 33, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
                }
                if (!NuclidesTable.getNuclide(nP, nN).isStable()) {
                    Zcolor = RED;
                    drawTexture(matrixStack, x, y+16, 0, 33, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
                }

            }
            else{
                Zcolor = RED;
                drawTexture(matrixStack, x, y+16, 0, 33, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
            }

            /*
             * Rendering of text:
             */
            TextRenderer TR = MinecraftClient.getInstance().textRenderer;
            matrixStack.push();
            matrixStack.scale(2, 2, 2);
            int width = TR.getWidth(symbol);
            TR.draw(matrixStack, symbol, (x - 24 - width / 2) / 2, (y + 2) / 2, WHITE);
            matrixStack.pop();
            //if (!neutronHelp.isEmpty() || !electronHelp.isEmpty()) {
            //  MinecraftClient.getInstance().textRenderer.draw(matrixStack, helpInfo, 10, 20, RED_COLOR);
            //}
            TR.draw(matrixStack, Integer.toString(nP), x - 40, y + 18, WHITE);
            TR.draw(matrixStack, Integer.toString(nP + nN), x - 40, y - 6, Zcolor);
            TR.draw(matrixStack, ionicCharge, x - 12, y - 6, Ecolor);
            if (NuclidesTable.isStable(nP, nN, nE)) {
                TR.draw(matrixStack, atomName, x + 192, y + 7, Ecolor);
            }
        }
    }

    private static void renderBars(MatrixStack matrixStack, int nP, int nE, int nN, int x, int y){
        int n = Math.max(nP, Math.max(nE, nN));
        int scale = n > 40 ? 150 : n > 10 ? 40 : 10;

        drawTexture(matrixStack, x, y, 0, 0, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        drawTexture(matrixStack, x, y+8, 0, 10, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        drawTexture(matrixStack, x, y+16, 0, 20, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);

        int ratio_p = nP*182/scale;
        int ratio_e = nE*182/scale;
        int ratio_n = nN*182/scale;

        drawTexture(matrixStack, x, y, 0, 5, ratio_p, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        drawTexture(matrixStack, x, y+8, 0, 15, ratio_e, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
        drawTexture(matrixStack, x, y+16, 0, 25, ratio_n, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);

        if(scale!=150){
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            int s;
            if(scale==10){s=45;}else{s=55;}
            drawTexture(matrixStack, x, y, 0, s, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
            drawTexture(matrixStack, x, y+8, 0, s, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);
            drawTexture(matrixStack, x, y+16, 0, s, 182, 5, BARS_TEXTURE_SIZE, BARS_TEXTURE_SIZE);

            RenderSystem.disableBlend();
        }
    }

}
