package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.crafting.molecules.Atom;
import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.NucleusState;
import be.uantwerpen.minelabs.util.NuclidesTable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static be.uantwerpen.minelabs.util.NuclidesTable.calculateNrOfElectrons;
import static net.minecraft.client.gui.DrawableHelper.drawCenteredText;
import static net.minecraft.client.gui.DrawableHelper.drawTexture;

@Environment(EnvType.CLIENT)
public class BohrBlueprintEntityRenderer<E extends BohrBlueprintEntity> extends EntityRenderer<E> {

    // Atom rendering
    private static final ItemStack PROTON = new ItemStack(Items.PROTON, 1);
    private static final ItemStack NEUTRON = new ItemStack(Items.NEUTRON, 1);
    private static final ItemStack ELECTRON = new ItemStack(Items.ELECTRON, 1);

    private static final List<Vec3f> NUCLEUS_COORDINATES = createIcosahedron();

    // the scaling offset we start with, for our icosahedron figure.
    private float startingOffsetScale = 15f;

    // shaking of atom
    private boolean shakeSwitch = true;
    // (shaking of atom) used to know when to 'shake'
    private int switchCounter = 0;

    private final ItemRenderer itemRenderer;

    private static List<Vec3f> createIcosahedron() {
        float c = 2f * (float) Math.PI / 5f;
        List<Vec3f> icosahedron = new ArrayList<>();
        icosahedron.add(new Vec3f(0, 0, (float) Math.sqrt(5) / 2));
        for (int i = 0; i < 5; i++)
            icosahedron.add(new Vec3f((float) Math.cos((i) * c), (float) Math.sin((i) * c), 0.5f));
        for (int i = 0; i < 5; i++)
            icosahedron.add(new Vec3f((float) Math.cos((Math.PI / 5) + i * c), (float) Math.sin((Math.PI / 5) + i * c), -0.5f));
        icosahedron.add(new Vec3f(0, 0, (float) -Math.sqrt(5) / 2));

        return icosahedron;
    }

    // HUD rendering
    // range in blocks from where the HUD is rendered
    public static final int HUD_RENDER_RADIUS = 9;

    private static final Identifier BARS_TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/bohr_bars.png");
    private static final int BARS_TEXTURE_SIZE = 256;

    // Color for rendering the text
    private static final int WHITE = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    private static final int YELLOW = ColorHelper.Argb.getArgb(255, 240, 225, 45);
    private static final int RED = ColorHelper.Argb.getArgb(255, 227, 23, 98);

    public BohrBlueprintEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(E entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    @Override
    public void render(E entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        int nP = entity.getProtons();
        int nN = entity.getNeutrons();
        int nE = entity.getElectrons();

        float time = entity.age + tickDelta;

        boolean stable = entity.isStable();

        // TODO: move to entity
        float shakeFactor = stable ? 0f : 0.02f;

        renderResultingAtom(matrices, entity, light, vertexConsumers);
        matrices.push();

        // center and scale
        matrices.translate(0, 0.75f, 0f);
        matrices.scale(1.5f, 1.5f, 1.5f);

        transformToFacePlayer(entity, matrices);
        makeNucleus(nP, nN, stable, shakeFactor, matrices, light, vertexConsumers);
        makeElectrons(nE, matrices, light, vertexConsumers, time);

        matrices.pop();
    }

    // ################################
    // #    Nucleus and Electrons     #
    // ################################

    /**
     * Set up the matrices to render everything facing the player.
     */
    private void transformToFacePlayer(E entity, MatrixStack matrices) {
        // TODO: fix weird rotation bug when walking full circle around the bohr plate.

        // for facing the player
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        Vec3f entityToPlayer = new Vec3f(entity.getPos().add(0, -1.0, 0).relativize(player.getPos()));

        if (entityToPlayer.equals(Vec3f.ZERO)) {
            // default direction should be north iso east.
            // positive x is east, so we want to rotate -90 degrees along the y-axis.
            matrices.multiply(Direction.UP.getUnitVector().getDegreesQuaternion(90));
        } else {
            /*
             * This algorithm determines the normal vector of the plane described by the original orientation of the arrow (v) and the target direction (entityToPlayer).
             * It then rotates around this vector with the angle theta between the two vectors to point the arrow in the direction of the entityToPlayer.
             */
            // By default, the arrow points in positive x (EAST)
            Vec3f v = new Vec3f(1, 0, 0);

            // Compute theta with cosine formula.
            double theta = Math.acos(v.dot(entityToPlayer) / Math.sqrt(Math.pow(entityToPlayer.getX(), 2) + Math.pow(entityToPlayer.getY(), 2) + Math.pow(entityToPlayer.getZ(), 2)));

            if (theta == 0 || theta == Math.PI) {
                // When the two vectors are parallel, their cross product does not produce the normal vector of the plane.
                // Instead, we set in to one of the infinite valid normal vectors: positive Y.
                v = Direction.UP.getUnitVector();
            } else {
                v.cross(entityToPlayer);
                v.normalize();
            }
            matrices.multiply(v.getRadialQuaternion((float) theta));
        }
        Vec3f y_rotation = new Vec3f(0, 1, 0);
        matrices.multiply(y_rotation.getDegreesQuaternion(-90));
    }

    /**
     * Handles the scaling and placement for the nucleus (protons and neutrons).
     * Data members used: startingOffsetScale, shakeSwitch, icosahedron, neutron_stack, proton_stack, switchCounter, switchCounterModulo
     */
    public void makeNucleus(int protonCount, int neutronCount, boolean stable, float shake, MatrixStack matrices, int lightAbove, VertexConsumerProvider vertexConsumerProvider) {
        int mass = protonCount + neutronCount;

        if (mass >= 12) {
            startingOffsetScale = 11f;
        }
        if (mass >= 120) {
            startingOffsetScale = 12f;
        }
        if (mass >= 180) {
            startingOffsetScale = 13f;
        }
        if (mass >= 240) {
            startingOffsetScale = 15f;
        }

        // variables for placing the particles (they get decreased)
        int nrOfprotonsLeft = protonCount;
        int nrOfneutronsLeft = neutronCount;

        boolean isProtonNext = true; // true if a proton entity needs to be placed in the core next, false = neutron next.
        boolean isProtonAndNeutronLeft = true; // true if both protons and neutrons still need to be placed
        int particlesCounter = 0; // used to count to 12 to restart (increase) the icosahedron scaleOffset.

        // each time we reach a multiple of 12, this value gets increased and used
        // in the function to calculate the total scale factor for our current icosahedron figure.
        float scaleOffset = 0f;
        int dec_index = 0; // variable to stay inside the list indexes of the icosahedron points.

        if (!stable) {
            if (shakeSwitch) {
                shakeSwitch = false;
                shake = -shake;
            } else {
                shakeSwitch = true;
            }
            switchCounter++;
        }

        for (int i = 0; i < mass; i++) {

            float scaleFactor = 2.5f; // lower value => closer to core origin

            if (mass > 50) {
                scaleFactor = 1.75f;
            }
            if (particlesCounter == 12) {
                particlesCounter = 0; // gets increased with one at end of for loop.
                if (mass < 36) {
                    scaleOffset += 2.5f;
                } else {
                    scaleOffset += 0.75f;
                }
                dec_index += 12;
            }

            // calculating the x,y,z offsets to place the protons/neutrons on the icosahedron outer points.
            float totalScale = startingOffsetScale - scaleOffset + scaleOffset / scaleFactor;
            float offset_x = NUCLEUS_COORDINATES.get(i - dec_index).getX() / totalScale;
            float offset_y = NUCLEUS_COORDINATES.get(i - dec_index).getY() / totalScale;
            float offset_z = NUCLEUS_COORDINATES.get(i - dec_index).getZ() / totalScale;

            if (dec_index > 12) {
                float rotateXAngle = (float) Math.PI * (0.125f * ((dec_index / 12) % 4));
                ArrayList<Float> new_y_z = rotateAroundXAxis(offset_y, offset_z, rotateXAngle);
                offset_y = new_y_z.get(0);
                offset_z = new_y_z.get(1);
            }

            matrices.translate(offset_x, offset_y + shake, offset_z);
            matrices.scale(0.2f, 0.2f, 0.2f);

            if (nrOfprotonsLeft == 0) {
                itemRenderer.renderItem(NEUTRON, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
                nrOfneutronsLeft -= 1;
                isProtonAndNeutronLeft = false;
            } else if (nrOfneutronsLeft == 0) {
                itemRenderer.renderItem(PROTON, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
                nrOfprotonsLeft -= 1;
                isProtonAndNeutronLeft = false;
            }
            if (isProtonAndNeutronLeft) {
                if (isProtonNext) {
                    itemRenderer.renderItem(PROTON, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
                    isProtonNext = false;
                    nrOfprotonsLeft -= 1;
                } else {
                    itemRenderer.renderItem(NEUTRON, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
                    isProtonNext = true;
                    nrOfneutronsLeft -= 1;
                }
            }

            matrices.scale(5, 5, 5);
            matrices.translate(-offset_x, -offset_y - shake, -offset_z);

            particlesCounter++;
        }
        if (switchCounter >= 100) {
            switchCounter = 0;
        }
        switchCounter++;


//		if (isImploding) {
//			implodeCounter++;
//		}
    }

    /**
     * Handles the "rendering" of the electrons shells themselves.
     */
    public void makeElectronshells() {
        // may be implemented and used, but it looks fine with how it is now.
    }

    /**
     * Handles the scaling and spinning of the electrons.
     *
     * @param electronCount          : amount of electrons in the borhblock
     * @param matrices               : matrices
     * @param lightAbove             : used in renderItem function to avoid all blackness in the rendering above the block.
     * @param vertexConsumerProvider : vertexConsumerProvider
     * @param time                   :
     */
    public void makeElectrons(int electronCount, MatrixStack matrices, int lightAbove, VertexConsumerProvider vertexConsumerProvider, float time) {

        // for the electron-shell distribution, check the NuclidesTable class static declaration/definition.

        int currentShell = 1;
        int electronCounter = 0;
        for (int el = 0; el < electronCount; el++) {

            int currentNrOfElectrons = calculateNrOfElectrons(currentShell);
            if (electronCounter == currentNrOfElectrons) {
                currentShell++;
                electronCounter = 0;
            }

            // evenly distribution of electrons around core. Used for the electron point calculation.
            int electronsOnCurShell = calcPlaceableElectronsOnShell(electronCount, currentShell, currentNrOfElectrons);

            ArrayList<Float> point = calculateElectronPoint(currentShell, time, electronsOnCurShell, electronCounter);
            float x = point.get(0);
            float y = point.get(1);
            float z = point.get(2);

            matrices.translate(x, y, z);
            matrices.scale(0.1f, 0.1f, 0.1f);

            itemRenderer.renderItem(ELECTRON, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);

            matrices.scale(10, 10, 10);
            matrices.translate(-x, -y, -z);

            electronCounter++;
        }
    }

    /**
     * rotates y and z around x-axis
     *
     * @param y     : y-coordinate
     * @param z     : z-coordinate
     * @param angle : rotate angle
     * @return : array of two elements: new y and z
     */
    public ArrayList<Float> rotateAroundXAxis(float y, float z, float angle) {
        y = y * (float) Math.cos(angle) - z * (float) Math.sin(angle);
        z = z * (float) Math.cos(angle) + y * (float) Math.sin(angle);
        return new ArrayList<>(Arrays.asList(y, z));
    }

    /**
     * rotates x and z around x-axis
     *
     * @param x     : x-coordinate
     * @param z     : z-coordinate
     * @param angle : rotate angle
     * @return : array of two elements: new x and z
     */
    public ArrayList<Float> rotateAroundYAxis(float x, float z, float angle) {
        x = x * (float) Math.cos(angle) - z * (float) Math.sin(angle);
        z = z * (float) Math.cos(angle) + x * (float) Math.sin(angle);
        return new ArrayList<>(Arrays.asList(x, z));
    }

    /**
     * Calculates current total amount of electrons to be placed (outermost shell)
     *
     * @param electronCount        : electron counter
     * @param currentShell         : integer value for which shell we are on (starts with 1)
     * @param currentNrOfElectrons : amount of electrons on the current shell
     * @return :
     */
    public int calcPlaceableElectronsOnShell(int electronCount, int currentShell, int currentNrOfElectrons) {
        int cur_e = electronCount;
        for (int i = 1; i < currentShell; i++) {
            cur_e -= calculateNrOfElectrons(i);
        }
        return Math.min(cur_e, currentNrOfElectrons);
    }

    /**
     * Calculates the x,y and z coordinate for the electron
     *
     * @param currentShell        : integer value for which shell we are on (starts with 1)
     * @param time                :
     * @param electronsOnCurShell : amount of electrons on the current shell
     * @param electronCounter     : electron counter
     * @return : array of three elements [x, y, z] for our point
     */
    public ArrayList<Float> calculateElectronPoint(int currentShell, float time, int electronsOnCurShell, int electronCounter) {

        // multiplier for how fast the electrons will spin around, the greater this value, the slower it will be.
        float speedMultiplier = 40 + 20 * (currentShell - 1);
        float radiusMultiplier = 0.1f * (currentShell - 1); // multiplier for how much further each new shell is from the nucleus

        float speed = (float) (2 * Math.PI) / speedMultiplier; // how fast the electrons rotate
        float radius = 0.4f + radiusMultiplier; // distance from core, used to calculate the points
        float angle = speed * time + (float) ((2 * Math.PI / (electronsOnCurShell))) * (electronCounter);

        float x = (float) Math.cos(angle) * radius;
        float y = (float) Math.sin(angle) * radius;
        float z = (float) Math.sin(angle) * radius * Math.min(currentShell - 1, 1); // 0 on first shell, z on every other shell.

        if (currentShell != 1) {
            float rotateAngle = (float) Math.PI / (2f * (currentShell - 1));
            if (currentShell > 5) {
                rotateAngle = (float) Math.PI / (8f * (currentShell - 1));
            }
            ArrayList<Float> new_y_z = rotateAroundXAxis(y, z, rotateAngle);
            y = new_y_z.get(0);
            z = new_y_z.get(1);
        }

        return new ArrayList<>(Arrays.asList(x, y, z));
    }


    // ##############
    // #    HUD     #
    // ##############

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
            if (nucleusState==null) {
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

    private void renderResultingAtom(MatrixStack matrixStack, BohrBlueprintEntity entity, int light, VertexConsumerProvider vertexConsumers){
        matrixStack.push();
        Item atom = entity.whichAtom();
        if(atom==null) return;
        matrixStack.translate(0, 5f/16- 1.5, 0.25);
        matrixStack.scale(2,0.5f,2);
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));

        MinecraftClient.getInstance().getItemRenderer().renderItem(atom.getDefaultStack(), ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumers, 0);
        matrixStack.pop();
    }
}