package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.item.Items;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class BohrBlueprintEntityRenderer extends EntityRenderer<BohrBlueprintEntity> {
    private static final ItemStack PROTON = new ItemStack(Items.PROTON, 1);
    private static final ItemStack NEUTRON = new ItemStack(Items.NEUTRON, 1);
    private static final ItemStack ELECTRON = new ItemStack(Items.ELECTRON, 1);

    // Multiplier for distance to reduce radius at which entity is rendered. In default settings 64 allows rendering it from 16 blocks.
    // Higher multiplier means smaller render radius.
    private static final float RENDER_DISTANCE_MULTIPLIER = 64;

    // radius usable for atom rendering
    private static final float MAX_RENDER_RADIUS = 0.75f + 11f / 16f - 0.1f;

    // maps the number of electrons to the correct shell configuration
    private static final Map<Integer, int[]> ELECTRON_SHELL_MAPPINGS = new HashMap<Integer, int[]>() {{
        put(0, new int[] {0});
        put(1, new int[] {1});
        put(2, new int[] {2});
        put(3, new int[] {2,1});
        put(4, new int[] {2,2});
        put(5, new int[] {2,3});
        put(6, new int[] {2,4});
        put(7, new int[] {2,5});
        put(8, new int[] {2,6});
        put(9, new int[] {2,7});
        put(10, new int[] {2,8});
        put(11, new int[] {2,8,1});
        put(12, new int[] {2,8,2});
        put(13, new int[] {2,8,3});
        put(14, new int[] {2,8,4});
        put(15, new int[] {2,8,5});
        put(16, new int[] {2,8,6});
        put(17, new int[] {2,8,7});
        put(18, new int[] {2,8,8});
        put(19, new int[] {2,8,8,1});
        put(20, new int[] {2,8,8,2});
        put(21, new int[] {2,8,9,2});
        put(22, new int[] {2,8,10,2});
        put(23, new int[] {2,8,11,2});
        put(24, new int[] {2,8,13,1});
        put(25, new int[] {2,8,13,2});
        put(26, new int[] {2,8,14,2});
        put(27, new int[] {2,8,15,2});
        put(28, new int[] {2,8,16,2});
        put(29, new int[] {2,8,18,1});
        put(30, new int[] {2,8,18,2});
        put(31, new int[] {2,8,18,3});
        put(32, new int[] {2,8,18,4});
        put(33, new int[] {2,8,18,5});
        put(34, new int[] {2,8,18,6});
        put(35, new int[] {2,8,18,7});
        put(36, new int[] {2,8,18,8});
        put(37, new int[] {2,8,18,8,1});
        put(38, new int[] {2,8,18,8,2});
        put(39, new int[] {2,8,18,9,2});
        put(40, new int[] {2,8,18,10,2});
        put(41, new int[] {2,8,18,12,1});
        put(42, new int[] {2,8,18,13,1});
        put(43, new int[] {2,8,18,13,2});
        put(44, new int[] {2,8,18,15,1});
        put(45, new int[] {2,8,18,16,1});
        put(46, new int[] {2,8,18,18});
        put(47, new int[] {2,8,18,18,1});
        put(48, new int[] {2,8,18,18,2});
        put(49, new int[] {2,8,18,18,3});
        put(50, new int[] {2,8,18,18,4});
        put(51, new int[] {2,8,18,18,5});
        put(52, new int[] {2,8,18,18,6});
        put(53, new int[] {2,8,18,18,7});
        put(54, new int[] {2,8,18,18,8});
        put(55, new int[] {2,8,18,18,8,1});
        put(56, new int[] {2,8,18,18,8,2});
        put(57, new int[] {2,8,18,18,9,2});
        put(58, new int[] {2,8,18,19,9,2});
        put(59, new int[] {2,8,18,21,8,2});
        put(60, new int[] {2,8,18,22,8,2});
        put(61, new int[] {2,8,18,23,8,2});
        put(62, new int[] {2,8,18,24,8,2});
        put(63, new int[] {2,8,18,25,8,2});
        put(64, new int[] {2,8,18,25,9,2});
        put(65, new int[] {2,8,18,27,8,2});
        put(66, new int[] {2,8,18,28,8,2});
        put(67, new int[] {2,8,18,29,8,2});
        put(68, new int[] {2,8,18,30,8,2});
        put(69, new int[] {2,8,18,31,8,2});
        put(70, new int[] {2,8,18,32,8,2});
        put(71, new int[] {2,8,18,32,9,2});
        put(72, new int[] {2,8,18,32,10,2});
        put(73, new int[] {2,8,18,32,11,2});
        put(74, new int[] {2,8,18,32,12,2});
        put(75, new int[] {2,8,18,32,13,2});
        put(76, new int[] {2,8,18,32,14,2});
        put(77, new int[] {2,8,18,32,15,2});
        put(78, new int[] {2,8,18,32,17,1});
        put(79, new int[] {2,8,18,32,18,1});
        put(80, new int[] {2,8,18,32,18,2});
        put(81, new int[] {2,8,18,32,18,3});
        put(82, new int[] {2,8,18,32,18,4});
        put(83, new int[] {2,8,18,32,18,5});
        put(84, new int[] {2,8,18,32,18,6});
        put(85, new int[] {2,8,18,32,18,7});
        put(86, new int[] {2,8,18,32,18,8});
        put(87, new int[] {2,8,18,32,18,8,1});
        put(88, new int[] {2,8,18,32,18,8,2});
        put(89, new int[] {2,8,18,32,18,9,2});
        put(90, new int[] {2,8,18,32,18,10,2});
        put(91, new int[] {2,8,18,32,20,9,2});
        put(92, new int[] {2,8,18,32,21,9,2});
        put(93, new int[] {2,8,18,32,22,9,2});
        put(94, new int[] {2,8,18,32,24,8,2});
        put(95, new int[] {2,8,18,32,25,8,2});
        put(96, new int[] {2,8,18,32,25,9,2});
        put(97, new int[] {2,8,18,32,27,8,2});
        put(98, new int[] {2,8,18,32,28,8,2});
        put(99, new int[] {2,8,18,32,29,8,2});
        put(100, new int[] {2,8,18,32,30,8,2});
        put(101, new int[] {2,8,18,32,31,8,2});
        put(102, new int[] {2,8,18,32,32,8,2});
        put(103, new int[] {2,8,18,32,32,8,3});
        put(104, new int[] {2,8,18,32,32,10,2});
        put(105, new int[] {2,8,18,32,32,11,2});
        put(106, new int[] {2,8,18,32,32,12,2});
        put(107, new int[] {2,8,18,32,32,13,2});
        put(108, new int[] {2,8,18,32,32,14,2});
        put(109, new int[] {2,8,18,32,32,15,2});
        put(110, new int[] {2,8,18,32,32,16,2});
        put(111, new int[] {2,8,18,32,32,17,2});
        put(112, new int[] {2,8,18,32,32,18,2});
        put(113, new int[] {2,8,18,32,32,18,3});
        put(114, new int[] {2,8,18,32,32,18,4});
        put(115, new int[] {2,8,18,32,32,18,5});
        put(116, new int[] {2,8,18,32,32,18,6});
        put(117, new int[] {2,8,18,32,32,18,7});
        put(118, new int[] {2,8,18,32,32,18,8});
    }};
    private static final int[] ELECTRON_SHELL_CAPACITIES = {2, 8, 18, 32, 32, 18, 8};
    private static final float ELECTRON_SCALE = 0.15f;
    private static final float ELECTRON_FIRST_SHELL_RADIUS = 0.5f;
    // distance between two shells
    private static final float ELECTRON_SHELL_RADIUS_OFFSET = (MAX_RENDER_RADIUS - ELECTRON_FIRST_SHELL_RADIUS) / (ELECTRON_SHELL_CAPACITIES.length - 1);
    // rotation period in ticks
    private static final float ELECTRON_ROTATION_PERIOD = 3 * 20;
    // amount of points to use for the electron shell orbit line
    private static final int ELECTRON_LINE_NUMPOINTS = 32;
    private static final int ELECTRON_SHELL_LINE_MAX_ALPHA = 150;
    // until this range the lines are fully visible (no fading)
    private static final double ELECTRON_SHELL_LINE_MIN_RENDER_RANGE = 4f;
    // line fades out until alpha zero at this distance
    private static final double ELECTRON_SHELL_LINE_MAX_RENDER_RANGE = 8f;


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
        for (int c : ELECTRON_SHELL_MAPPINGS.get(nE)) {
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
    public boolean shouldRender(BohrBlueprintEntity entity, Frustum frustum, double x, double y, double z) {
        // act like it is further away to reduce rendering range and still take renderDistanceMultiplier setting into account.
        return super.shouldRender(entity, frustum, x, y, z) && entity.shouldRender(entity.getPos().squaredDistanceTo(x, y, z) * RENDER_DISTANCE_MULTIPLIER);
    }

    @Override
    public void render(BohrBlueprintEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Should someone use data set to go above the limit of the entity, this will ensure the rendering does not crash.
        int nP = Math.min(entity.getProtons(), BohrBlueprintEntity.MAX_PROTONS);
        int nN = Math.min(entity.getNeutrons(), BohrBlueprintEntity.MAX_NEUTRONS);
        int nE = Math.min(entity.getElectrons(), BohrBlueprintEntity.MAX_ELECTRONS);

        float time = entity.age + tickDelta;
        double dToCamera = Math.sqrt(dispatcher.getSquaredDistanceToCamera(entity));

        MinecraftClient.getInstance().getProfiler().push("bohr");

//        renderResultingAtom(tickDelta, matrices, entity, light, vertexConsumers);

        matrices.push();

        MinecraftClient.getInstance().getProfiler().push("protons");
        renderNucleus(nP, nN, time, matrices, vertexConsumers, light);
        MinecraftClient.getInstance().getProfiler().swap("electrons");
        renderElectrons(nE, dToCamera, time, matrices, vertexConsumers, light);
        MinecraftClient.getInstance().getProfiler().pop();

        matrices.pop();
        MinecraftClient.getInstance().getProfiler().pop();
    }

    private void renderElectrons(int nE, double dToCamera, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Electrons are rendered in at most 7 different shells with chosen yaw, pitch and roll.
        // Within an orbit the electrons are equally spaced from each other.

        List<Integer> electronShellConfiguration = getElectronShellConfiguration(nE);

        float radius = ELECTRON_FIRST_SHELL_RADIUS;
        matrices.push();
        for (int electronsInShell : electronShellConfiguration) {
            renderElectronShell(electronsInShell, radius, dToCamera, time, matrices, vertexConsumers, light);

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
    private void renderElectronShell(int nE, float radius, double dToCamera, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        renderElectronShellLine(radius, dToCamera, matrices, vertexConsumers);
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

    private void renderElectronShellLine(float radius, double dToCamera, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        if (dToCamera > ELECTRON_SHELL_LINE_MAX_RENDER_RANGE)
            return;

        double delta = (dToCamera - ELECTRON_SHELL_LINE_MIN_RENDER_RANGE) / (ELECTRON_SHELL_LINE_MAX_RENDER_RANGE - ELECTRON_SHELL_LINE_MIN_RENDER_RANGE);
        int alpha = (int) Math.floor(MathHelper.clampedLerp( ELECTRON_SHELL_LINE_MAX_ALPHA, 0, delta));

        // lines
        VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
        matrices.push();
        float angleBetweenLinePoints = 360f / ELECTRON_LINE_NUMPOINTS;
        for (int e = 0; e <= ELECTRON_LINE_NUMPOINTS; e++) {
            matrices.push();
            matrices.translate(0, radius, 0);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));

            MatrixStack.Entry matrixEntry = matrices.peek();
            lineBuffer.vertex(matrixEntry.getPositionMatrix(), 0, 0, 0).color(0, 0, 0, alpha).normal(matrixEntry.getNormalMatrix(), 1, 0, 0).next();

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

    private void renderResultingAtom(float tickDelta, MatrixStack matrixStack, BohrBlueprintEntity entity, int light, VertexConsumerProvider vertexConsumers) {
        Item atom = entity.getAtomItem();
        if (atom == null) return;

        matrixStack.push();
        matrixStack.translate(0, 5f / 16 - 1 - getPositionOffset(entity, tickDelta).getY(), 0.25);
        matrixStack.scale(2, 0.5f, 2);
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));

        itemRenderer.renderItem(atom.getDefaultStack(), ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumers, 0);
        matrixStack.pop();
    }

}