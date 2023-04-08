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
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static be.uantwerpen.minelabs.util.NuclidesTable.calculateNrOfElectrons;
import static net.minecraft.client.gui.DrawableHelper.drawCenteredText;
import static net.minecraft.client.gui.DrawableHelper.drawTexture;

@Environment(EnvType.CLIENT)
public class BohrBlueprintEntityRenderer extends EntityRenderer<BohrBlueprintEntity> {
    private static final ItemStack PROTON = new ItemStack(Items.PROTON, 1);
    private static final ItemStack NEUTRON = new ItemStack(Items.NEUTRON, 1);
    private static final ItemStack ELECTRON = new ItemStack(Items.ELECTRON, 1);

    private static final List<Vec3f> NUCLEUS_COORDINATES = createIcosahedron();

    private static final int[] ELECTRON_SHELL_CAPACITIES = {2, 8, 18, 32, 32, 18, 8};
    private static final float ELECTRON_SCALE = 0.2f;
    // radius of first shell
    private static final float ELECTRON_SHELL_RADIUS = 0.5f;
    // distance between two shells
    private static final float ELECTRON_SHELL_RADIUS_OFFSET = (1 - ELECTRON_SHELL_RADIUS) / (ELECTRON_SHELL_CAPACITIES.length - 1);
    // rotation period in ticks
    private static final float ELECTRON_ROTATION_PERIOD = 3 * 20;
    // amount of points to use for the electron shell orbit line
    private static final int ELECTRON_LINE_NUMPOINTS = 32;

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

    private List<Integer> getElectronShellConfiguration(int nE){
        MinecraftClient.getInstance().getProfiler().push("shell config");
        // TODO: placeholder that fills until max. Should use energy based filling of shells.
        // TODO: if too slow, place cache over this function
        List<Integer> result = new ArrayList<>(7);
        for (int c : ELECTRON_SHELL_CAPACITIES) {
            if (nE <= 0) break;
            int amount = Math.min(c, nE);
            result.add(amount);
            nE -= amount;
        }
        MinecraftClient.getInstance().getProfiler().pop();
        return result;
    }

    public BohrBlueprintEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(BohrBlueprintEntity entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    @Override
    public Vec3d getPositionOffset(BohrBlueprintEntity entity, float tickDelta) {
        return new Vec3d(0, entity.getHeight() / 2, 0);
    }

    @Override
    protected int getBlockLight(BohrBlueprintEntity entity, BlockPos pos) {
        return 15;      // render as if always fully lit
    }

    @Override
    protected int getSkyLight(BohrBlueprintEntity entity, BlockPos pos) {
        return 15;      // render as if always fully lit
    }

    @Override
    public void render(BohrBlueprintEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Should someone use data set to go above the limit of the entity, this will ensure the rendering does not crash.
        int nP = Math.min(entity.getProtons(), BohrBlueprintEntity.MAX_PROTONS);
        int nN = Math.min(entity.getNeutrons(), BohrBlueprintEntity.MAX_NEUTRONS);
        int nE = Math.min(entity.getElectrons(), BohrBlueprintEntity.MAX_ELECTRONS);

        float time = entity.age + tickDelta;

        MinecraftClient.getInstance().getProfiler().push("bohr");
        matrices.push();

        MinecraftClient.getInstance().getProfiler().push("protons");
        renderNucleus(nP, nN, time, matrices, vertexConsumers, light);
        MinecraftClient.getInstance().getProfiler().swap("electrons");
        renderElectrons(nE, time, matrices, vertexConsumers, light);
        MinecraftClient.getInstance().getProfiler().pop();

        matrices.pop();
        MinecraftClient.getInstance().getProfiler().pop();
    }

    private void renderElectrons(int nE, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Electrons are rendered in at most 7 different shells with chosen yaw, pitch and roll.
        // Within an orbit the electrons are equally spaced from each other.

        List<Integer> electronShellConfiguration = getElectronShellConfiguration(nE);

        float radius = ELECTRON_SHELL_RADIUS;
        matrices.push();
        for (int electronsInShell : electronShellConfiguration){
            // TODO: rotate
            renderElectronShell(electronsInShell, radius, time, matrices, vertexConsumers, light);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(30));
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(15));
            radius += ELECTRON_SHELL_RADIUS_OFFSET;
        }
        matrices.pop();
    }

    /**
     * Render an electron shell in the XY-plane with specified radius and number of electrons.
     */
    private void renderElectronShell(int nE, float radius, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
        renderElectronShellLine(radius, matrices, vertexConsumers);
        renderElectronShellElectrons(nE, radius, time, matrices, vertexConsumers, light);
    }

    private void renderElectronShellElectrons(int nE, float radius, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
        matrices.push();
        // rotation animation of electrons on shell
        float angle = (time % ELECTRON_ROTATION_PERIOD) / ELECTRON_ROTATION_PERIOD * 360;
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(angle));

        // render electron at top of orbit then rotate and repeat
        float angleBetweenElectrons = 360f / nE;
        for(int e = 0; e < nE; e++){
            matrices.push();
            matrices.translate(0, radius, 0);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
            matrices.scale(ELECTRON_SCALE, ELECTRON_SCALE, ELECTRON_SCALE);
            renderElectron(matrices, vertexConsumers, light);

            matrices.pop();
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(angleBetweenElectrons));
        }
        matrices.pop();
    }

    private void renderElectronShellLine(float radius, MatrixStack matrices, VertexConsumerProvider vertexConsumers){
        matrices.push();

        VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
        // lines
        float angleBetweenLinePoints = 360f / ELECTRON_LINE_NUMPOINTS;
        for(int e = 0; e <= ELECTRON_LINE_NUMPOINTS; e++){
            matrices.push();
            matrices.translate(0, radius, 0);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));

            MatrixStack.Entry matrixEntry = matrices.peek();
            lineBuffer.vertex(matrixEntry.getPositionMatrix(), 0, 0, 0).color(0, 0, 0, 255).normal(matrixEntry.getNormalMatrix(), 1, 0, 0).next();

            matrices.pop();
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(angleBetweenLinePoints));
        }

        matrices.pop();
    }

    private void renderElectron(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
        MinecraftClient.getInstance().getProfiler().push("item renderer");
        itemRenderer.renderItem(ELECTRON, ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
        MinecraftClient.getInstance().getProfiler().pop();
    }

    private void renderNucleus(int nP, int nN, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

    }

//    /**
//     * Set up the matrices to render everything facing the player.
//     */
//    private void transformToFacePlayer(BohrBlueprintEntity entity, MatrixStack matrices) {
//        Vec3d pos = this.dispatcher.camera.getPos();
//        Vec3f entityToPlayer = new Vec3f(entity.getPos().add(0,0.75,0).relativize(pos));
//        entityToPlayer.normalize();
//        double pitch = Math.asin(-entityToPlayer.getY());
//        double yaw = Math.atan2(entityToPlayer.getX(), entityToPlayer.getZ());
//        entityToPlayer.cross(Vec3f.POSITIVE_Y);
//        entityToPlayer.normalize();
//        matrices.multiply(entityToPlayer.getRadialQuaternion((float)-pitch));
//        matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion((float) yaw));
//    }

}
