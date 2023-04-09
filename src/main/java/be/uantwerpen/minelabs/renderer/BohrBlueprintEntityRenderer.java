package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.item.Items;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BohrBlueprintEntityRenderer extends EntityRenderer<BohrBlueprintEntity> {
    private static final ItemStack PROTON = new ItemStack(Items.PROTON, 1);
    private static final ItemStack NEUTRON = new ItemStack(Items.NEUTRON, 1);
    private static final ItemStack ELECTRON = new ItemStack(Items.ELECTRON, 1);

    private static final float MAX_RENDER_RADIUS = 0.75f + 11f / 16f - 0.1f;
    private static final int[] ELECTRON_SHELL_CAPACITIES = {2, 8, 18, 32, 32, 18, 8};
    private static final float ELECTRON_SCALE = 0.15f;
    private static final float ELECTRON_FIRST_SHELL_RADIUS = 0.5f;
    // distance between two shells
    private static final float ELECTRON_SHELL_RADIUS_OFFSET = (MAX_RENDER_RADIUS - ELECTRON_FIRST_SHELL_RADIUS) / (ELECTRON_SHELL_CAPACITIES.length - 1);
    // rotation period in ticks
    private static final float ELECTRON_ROTATION_PERIOD = 3 * 20;
    // amount of points to use for the electron shell orbit line
    private static final int ELECTRON_LINE_NUMPOINTS = 32;

    private static final int MAX_NUCLEUS_ITEMS_RENDERED = 36;

    // size of individual particle items
    private static final float NUCLEUS_SCALE = 0.15f;

    // how big the core should be
    private static final float NUCLEUS_RADIUS_MULTIPLIER = 0.1f;
    // distance between layers of nucleus
    private static final float NUCLEUS_RADIUS_OFFSET = 0.15f;
    private static final List<Vec3f> NUCLEUS_COORDINATES = createIcosahedron();


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

        icosahedron.forEach(v -> v.scale(NUCLEUS_RADIUS_MULTIPLIER));
        return icosahedron;
    }

    private List<Integer> getElectronShellConfiguration(int nE) {
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

        float radius = ELECTRON_FIRST_SHELL_RADIUS;
        matrices.push();
        for (int electronsInShell : electronShellConfiguration) {
            renderElectronShell(electronsInShell, radius, time, matrices, vertexConsumers, light);

            // don't update normals
            matrices.multiplyPositionMatrix(new Matrix4f(Vec3f.POSITIVE_X.getDegreesQuaternion(45)));
            matrices.multiplyPositionMatrix(new Matrix4f(Vec3f.POSITIVE_Y.getDegreesQuaternion(30)));
            matrices.multiplyPositionMatrix(new Matrix4f(Vec3f.POSITIVE_Z.getDegreesQuaternion(15)));

            radius += ELECTRON_SHELL_RADIUS_OFFSET;
        }
        matrices.pop();
    }

    /**
     * Render an electron shell in the XY-plane with specified radius and number of electrons.
     */
    private void renderElectronShell(int nE, float radius, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        renderElectronShellLine(radius, matrices, vertexConsumers);
        renderElectronShellElectrons(nE, radius, time, matrices, vertexConsumers, light);
    }

    private void renderElectronShellElectrons(int nE, float radius, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        // rotation animation of electrons on shell
        float angle = (time % ELECTRON_ROTATION_PERIOD) / ELECTRON_ROTATION_PERIOD * 360;

        // don't update normals. This allows the lighting to be computed as if the outside is always lit
        matrices.multiplyPositionMatrix(new Matrix4f(Vec3f.POSITIVE_Z.getDegreesQuaternion(angle)));

        // render electron at top of orbit then rotate and repeat
        float angleBetweenElectrons = 360f / nE;
        for (int e = 0; e < nE; e++) {
            matrices.push();
            matrices.translate(0, radius, 0);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
            matrices.scale(ELECTRON_SCALE, ELECTRON_SCALE, ELECTRON_SCALE);
            renderElectron(matrices, vertexConsumers, light);

            matrices.pop();
            // don't update normals
            matrices.multiplyPositionMatrix(new Matrix4f(Vec3f.POSITIVE_Z.getDegreesQuaternion(angleBetweenElectrons)));

        }
        matrices.pop();
    }

    private void renderElectron(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        renderItem(ELECTRON, matrices, vertexConsumers, light);
    }

    private void renderElectronShellLine(float radius, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        matrices.push();

        VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
        // lines
        float angleBetweenLinePoints = 360f / ELECTRON_LINE_NUMPOINTS;
        for (int e = 0; e <= ELECTRON_LINE_NUMPOINTS; e++) {
            matrices.push();
            matrices.translate(0, radius, 0);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));

            MatrixStack.Entry matrixEntry = matrices.peek();
            lineBuffer.vertex(matrixEntry.getPositionMatrix(), 0, 0, 0).color(0, 0, 0, 150).normal(matrixEntry.getNormalMatrix(), 1, 0, 0).next();

            matrices.pop();
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(angleBetweenLinePoints));
        }

        matrices.pop();
    }

    private void renderNucleus(int nP, int nN, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // nothing to render (prevent division by zero)
        if (nP + nN == 0)
            return;

        // place nucleus particles in predetermined coordinates
        Iterator<Vec3f> posIterator = NUCLEUS_COORDINATES.iterator();

        // when we run out of coordinates, start new iteration with increased radius
        float radiusMultiplier = 1f;

        // we keep ratio of protons rendered as close to requested as possible
        float pRatio = (float) nP / (float) (nP + nN);
        float pRatioRendered = nP > nN ? 0 : 1;     // initializer determines which to render first

        // special case to handle zero.
        if (nP == 0)
            pRatio = -1;
        else if (nN == 0)
            pRatio = 2;

        int nPRendered = 0;
        int nNRendered = 0;
        int amountToRender = Math.min(MAX_NUCLEUS_ITEMS_RENDERED, nP + nN);

        matrices.push();
        while (nPRendered + nNRendered < amountToRender) {
            if (!posIterator.hasNext()) {
                radiusMultiplier += NUCLEUS_RADIUS_OFFSET;
                posIterator = NUCLEUS_COORDINATES.iterator();
            }
            Vec3f pos = posIterator.next().copy();
            pos.scale(radiusMultiplier);

            ItemStack type = pRatio > pRatioRendered ? PROTON : NEUTRON;
            if (type == PROTON)
                nPRendered += 1;
            else
                nNRendered += 1;

            renderNucleusParticle(type, pos, time, matrices, vertexConsumers, light);
            pRatioRendered = (float) nPRendered / (float) (nPRendered + nNRendered);
        }
        matrices.pop();
    }

    private void renderNucleusParticle(ItemStack type, Vec3f pos, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(pos.getX(), pos.getY(), pos.getZ());
        matrices.scale(NUCLEUS_SCALE, NUCLEUS_SCALE, NUCLEUS_SCALE);

        // have everything facing the camera orientation
        matrices.multiply(dispatcher.getRotation());

        renderItem(type, matrices, vertexConsumers, light);
        matrices.pop();
    }

    private void renderItem(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        MinecraftClient.getInstance().getProfiler().push("item renderer");
        itemRenderer.renderItem(stack, ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
        MinecraftClient.getInstance().getProfiler().pop();
    }
}
