package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.AtomConfiguration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
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

    private static final float ELECTRON_SCALE = 0.15f;
    private static final float ELECTRON_FIRST_SHELL_RADIUS = 0.5f;
    private static final int ELECTRON_SHELLS_AMOUNT = 7;
    // distance between two shells
    private static final float ELECTRON_SHELL_RADIUS_OFFSET = (MAX_RENDER_RADIUS - ELECTRON_FIRST_SHELL_RADIUS) / (ELECTRON_SHELLS_AMOUNT - 1);
    private static final Quaternion[] ELECTRON_SHELL_ROTATIONS = {
            Vec3f.POSITIVE_X.getDegreesQuaternion(0),
            Vec3f.POSITIVE_Y.getDegreesQuaternion(90),
            Vec3f.POSITIVE_X.getDegreesQuaternion(90),
            Vec3f.POSITIVE_Y.getDegreesQuaternion(45),
            Vec3f.POSITIVE_X.getDegreesQuaternion(45),
            Vec3f.POSITIVE_Y.getDegreesQuaternion(135),
            Vec3f.POSITIVE_X.getDegreesQuaternion(135)
    };
    // rotation period in ticks
    private static final float ELECTRON_ROTATION_PERIOD = 3 * 20;
    // amount of points to use for the electron shell orbit line
    private static final int ELECTRON_LINE_NUMPOINTS = 32;
    private static final int ELECTRON_SHELL_LINE_MAX_ALPHA = 150;
    // until this range the lines are fully visible (no fading)
    private static final double ELECTRON_SHELL_LINE_MIN_RENDER_RANGE = 4f;
    // line fades out until alpha zero at this distance
    private static final double ELECTRON_SHELL_LINE_MAX_RENDER_RANGE = 8f;

    // instability animation
    private static final double ELECTRON_INSTABILITY_MIN_PERIOD = 0.8d * 20;
    private static final double ELECTRON_INSTABILITY_MAX_PERIOD = 0.3d * 20;
    private static final float ELECTRON_INSTABILITY_MAX_OFFSET = ELECTRON_SHELL_RADIUS_OFFSET * 1.2f;

    // size of individual particle items
    private static final float NUCLEUS_SCALE = 0.15f;

    // for scaling nucleus radius depending on amount of particles
    private static final float NUCLEUS_MIN_RADIUS = 0.05f;
    private static final float NUCLEUS_MAX_RADIUS = 0.2f;
    // distance between layers of nucleus
//    private static final float NUCLEUS_RADIUS_OFFSET = 0.15f;

    // how many subdivisions are made for the nucleus coordinates. Amount of points is square this number.
    private static final int NUCLEUS_COORDINATES_STEPS = 8;
    private static final List<Vec3f> NUCLEUS_COORDINATES = uniformSphericalCoordinates(NUCLEUS_COORDINATES_STEPS);

    private static final int NUCLEUS_MAX_ITEMS_RENDERED = 2 * NUCLEUS_COORDINATES.size();
    // radius of nucleus gets bigger until this capacity.
    private static final int NUCLEUS_MAX_CONTENT_FOR_RADIUS = BohrBlueprintEntity.MAX_PROTONS + BohrBlueprintEntity.MAX_NEUTRONS;

    private static final float NUCLEUS_INSTABILITY_MAX_SCALE = 2f;
    private static final float NUCLEUS_INSTABILITY_PULSE_PERIOD = 1f * 20;

    private static final float NUCLEUS_INSTABILITY_MIN_PULSE_PERCENT = 1f / 1.5f;
    private static final float NUCLEUS_INSTABILITY_MAX_PULSE_PERCENT = 1f / 4f;

    private static final int NUCLEUS_INSTABILITY_GROUPS = 50;

    /**
     * Computes uniformly spread out points on a unit sphere by iterating polar coordinates.
     * Steps define in how many pieces each angle is divided so amount of points is `steps ** 2`.
     */
    private static List<Vec3f> uniformSphericalCoordinates(int steps) {
        List<Vec3f> points = new ArrayList<>();

        float pi2 = (float) (2 * Math.PI);
        float angle = pi2 / steps;
        for (float i = 0; i < steps; i++) {
            float alpha = i * angle;
            for (float j = 0; j < steps; j++) {
                float beta = j * angle;

                float x = (float) (Math.sin(alpha) * Math.cos(beta));
                float y = (float) (Math.sin(alpha) * Math.sin(beta));
                float z = (float) Math.cos(alpha);
                points.add(new Vec3f(x, y, z));
            }
        }

        Collections.shuffle(points, new Random(42));
        return points;
    }

    private int[] getElectronShellConfiguration(int nE) {
        return ELECTRON_SHELL_CAPACITIES[nE];
    }


    private final ItemRenderer itemRenderer;

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
        AtomConfiguration atomConfig = entity.getAtomConfig();
        int nP = Math.min(atomConfig.getProtons(), BohrBlueprintEntity.MAX_PROTONS);
        int nN = Math.min(atomConfig.getNeutrons(), BohrBlueprintEntity.MAX_NEUTRONS);
        int nE = Math.min(atomConfig.getElectrons(), BohrBlueprintEntity.MAX_ELECTRONS);

        float electronInstability = atomConfig.getElectronInstability();
        float nucleusInstability = atomConfig.getNucleusInstability();

        float time = entity.age + tickDelta;
        double dToCamera = Math.sqrt(dispatcher.getSquaredDistanceToCamera(entity));

        MinecraftClient.getInstance().getProfiler().push("bohr");

//        renderResultingAtom(tickDelta, matrices, entity, light, vertexConsumers);

        matrices.push();

        MinecraftClient.getInstance().getProfiler().push("nucleus");
        renderNucleus(nP, nN, nucleusInstability, time, matrices, vertexConsumers, light);
        MinecraftClient.getInstance().getProfiler().swap("electrons");
        renderElectrons(nE, electronInstability, dToCamera, time, matrices, vertexConsumers, light);
        MinecraftClient.getInstance().getProfiler().pop();

        matrices.pop();
        MinecraftClient.getInstance().getProfiler().pop();
    }

    private void renderElectrons(int nE, float instability, double dToCamera, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Electrons are rendered in at most 7 different shells with chosen yaw, pitch and roll.
        // Within an orbit the electrons are equally spaced from each other.

        int[] electronShellConfiguration = getElectronShellConfiguration(nE);

        float radius = ELECTRON_FIRST_SHELL_RADIUS;

        for (int i = 0; i < electronShellConfiguration.length; i++) {
            int electronsInShell = electronShellConfiguration[i];
            Quaternion rotation = ELECTRON_SHELL_ROTATIONS[i];

            // don't update normals for rendering electron items (lighting effect)
            matrices.push();
            matrices.multiplyPositionMatrix(new Matrix4f(rotation));
            renderElectronShellElectrons(electronsInShell, radius, instability, time, matrices, vertexConsumers, light);
            matrices.pop();

            // do updated normals for rendering line
            matrices.push();
            matrices.multiply(rotation);
            renderElectronShellLine(radius, dToCamera, matrices, vertexConsumers);
            matrices.pop();

            radius += ELECTRON_SHELL_RADIUS_OFFSET;
        }
    }

    /**
     * Render an electron shell in the XY-plane with specified radius and number of electrons.
     */
    private void renderElectronShellElectrons(int nE, float radius, float instability, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // rotation animation of electrons on shell
        float angle = (time % ELECTRON_ROTATION_PERIOD) / ELECTRON_ROTATION_PERIOD * 360;

        matrices.push();
        // don't update normals. This allows the lighting to be computed as if the outside is always lit
        matrices.multiplyPositionMatrix(new Matrix4f(Vec3f.POSITIVE_Z.getDegreesQuaternion(angle)));

        // render electron at top of orbit then rotate and repeat
        float angleBetweenElectrons = 360f / nE;
        for (int e = 0; e < nE; e++) {
            Vec3f pos = new Vec3f(0, radius, 0);
            float percentOfOrbit = (float) e / nE;
            renderElectron(matrices, pos, instability, percentOfOrbit, time, vertexConsumers, light);

            // don't update normals
            matrices.multiplyPositionMatrix(new Matrix4f(Vec3f.POSITIVE_Z.getDegreesQuaternion(angleBetweenElectrons)));

        }
        matrices.pop();
    }

    private void renderElectronShellLine(float radius, double dToCamera, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        if (dToCamera > ELECTRON_SHELL_LINE_MAX_RENDER_RANGE)
            return;

        double delta = (dToCamera - ELECTRON_SHELL_LINE_MIN_RENDER_RANGE) / (ELECTRON_SHELL_LINE_MAX_RENDER_RANGE - ELECTRON_SHELL_LINE_MIN_RENDER_RANGE);
        int alpha = (int) Math.floor(MathHelper.clampedLerp(ELECTRON_SHELL_LINE_MAX_ALPHA, 0, delta));

        float angleBetweenLinePoints = 360f / ELECTRON_LINE_NUMPOINTS;

        // compute normal of line which should point towards next point
        // other angle of triangle formed by origin and line
        double beta = Math.toRadians(180 - angleBetweenLinePoints) / 2f;
        float normalX = (float) -Math.sin(beta);
        float normalY = (float) -Math.cos(beta);

        VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLines());
        matrices.push();
        MatrixStack.Entry matrixEntry = matrices.peek();
        for (int e = 0; e < ELECTRON_LINE_NUMPOINTS; e++) {
            lineBuffer.vertex(matrixEntry.getPositionMatrix(), 0, radius, 0)
                    .color(0, 0, 0, alpha)
                    .normal(matrixEntry.getNormalMatrix(), normalX, normalY, 0)
                    .next();

            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(angleBetweenLinePoints));

            lineBuffer.vertex(matrixEntry.getPositionMatrix(), 0, radius, 0)
                    .color(0, 0, 0, alpha)
                    .normal(matrixEntry.getNormalMatrix(), -normalX, normalY, 0)
                    .next();
        }

        matrices.pop();
    }

    private void renderNucleus(int nP, int nN, float instability, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // nothing to render (prevent division by zero)
        if (nP + nN == 0)
            return;

        // place nucleus particles in predetermined coordinates
        Iterator<Vec3f> posIterator = NUCLEUS_COORDINATES.iterator();


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
        int amountToRender = Math.min(NUCLEUS_MAX_ITEMS_RENDERED, nP + nN);
        float nucleusRadius = MathHelper.clampedLerp(NUCLEUS_MIN_RADIUS, NUCLEUS_MAX_RADIUS, (float) (nP + nN) / NUCLEUS_MAX_CONTENT_FOR_RADIUS);

        matrices.push();
        while (nPRendered + nNRendered < amountToRender) {
            if (!posIterator.hasNext()) {
                // when we run out of coordinates, start new iteration with decreased radius
                nucleusRadius /= 2;
                posIterator = NUCLEUS_COORDINATES.iterator();
            }
            Vec3f pos = posIterator.next().copy();
            pos.scale(nucleusRadius);

            ItemStack type = pRatio > pRatioRendered ? PROTON : NEUTRON;
            renderNucleusParticle(type, pos, instability, nNRendered + nPRendered, amountToRender, time, matrices, vertexConsumers, light);

            if (type == PROTON)
                nPRendered += 1;
            else
                nNRendered += 1;
            pRatioRendered = (float) nPRendered / (float) (nPRendered + nNRendered);
        }
        matrices.pop();
    }

    private Vec3f getElectronInstabilityOffset(float instability, float percentOfOrbit, float time) {
        // if you don't want all electrons to be in sync
//        time = time + percentOfOrbit * ELECTRON_ROTATION_PERIOD;

        double period = MathHelper.lerp(instability, ELECTRON_INSTABILITY_MIN_PERIOD, ELECTRON_INSTABILITY_MAX_PERIOD);
        float offset = instability * ELECTRON_INSTABILITY_MAX_OFFSET;
        float yOffset = (float) (Math.sin(Math.toRadians(time / period * 360)) + 1) / 2 * offset;
        return new Vec3f(0, yOffset, 0);
    }

    private void renderElectron(MatrixStack matrices, Vec3f pos, float instability, float percentOfOrbit, float time, VertexConsumerProvider vertexConsumers, int light) {
        if (instability > 0)
            pos.add(getElectronInstabilityOffset(instability, percentOfOrbit, time));

        matrices.push();
        matrices.translate(pos.getX(), pos.getY(), pos.getZ());
        // because we use rotation and the offset is always in the y direction, this faces them outwards
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
        matrices.scale(ELECTRON_SCALE, ELECTRON_SCALE, ELECTRON_SCALE);
        renderItem(ELECTRON, matrices, vertexConsumers, light);
        matrices.pop();
    }

    /**
     * Evaluate pulse function with long tail in progress [0, 1].
     * pulsePercent is percent of function that is pulse. Rest is tail.
     */
    private float getPulseWithTail(float progress, float pulsePercent) {
        if (progress <= pulsePercent) {
            // local progress to simulate pulse
            progress = progress / pulsePercent;
            return (1 + (float) Math.cos(progress * 2 * Math.PI + Math.PI)) / 2;
        } else {
            return 0f;
        }
    }

    private float getNuclideInstabilityScale(float instability, int index, int total, float time) {
        float pulsePercent = MathHelper.lerp(instability, NUCLEUS_INSTABILITY_MAX_PULSE_PERCENT, NUCLEUS_INSTABILITY_MIN_PULSE_PERCENT);

        // This keeps width of pulse the same (independent of instability)
        float period = NUCLEUS_INSTABILITY_PULSE_PERIOD / pulsePercent;
        float progress = (time % period) / period;

        // group specific progress
        int groups = Math.min(total, NUCLEUS_INSTABILITY_GROUPS);
        float groupOffset = (float) (index % groups) / groups;
        progress = (progress + groupOffset) % 1;

        float minScale = 1;
        float maxScale = instability * NUCLEUS_INSTABILITY_MAX_SCALE;
        float value = getPulseWithTail(progress, pulsePercent);
        float scale = MathHelper.lerp(value, minScale, maxScale);

        return scale;
    }

    private void renderNucleusParticle(ItemStack type, Vec3f pos, float instability, int index, int total, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (instability > 0)
            pos.scale(getNuclideInstabilityScale(instability, index, total, time));

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
        ItemStack atomStack = entity.getCraftableAtom();
        if (atomStack.isEmpty()) return;

        matrixStack.push();
        matrixStack.translate(0, 5f / 16 - 1 - getPositionOffset(entity, tickDelta).getY(), 0.25);
        matrixStack.scale(2, 0.5f, 2);
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));

        itemRenderer.renderItem(atomStack, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumers, 0);
        matrixStack.pop();
    }


    // Maps the number of electrons to the correct shell configuration by index.
    // Placed at the end of the file for better readability.
    private static final int[][] ELECTRON_SHELL_CAPACITIES = {
            {},
            {1},
            {2},
            {2, 1},
            {2, 2},
            {2, 3},
            {2, 4},
            {2, 5},
            {2, 6},
            {2, 7},
            {2, 8},
            {2, 8, 1},
            {2, 8, 2},
            {2, 8, 3},
            {2, 8, 4},
            {2, 8, 5},
            {2, 8, 6},
            {2, 8, 7},
            {2, 8, 8},
            {2, 8, 8, 1},
            {2, 8, 8, 2},
            {2, 8, 9, 2},
            {2, 8, 10, 2},
            {2, 8, 11, 2},
            {2, 8, 13, 1},
            {2, 8, 13, 2},
            {2, 8, 14, 2},
            {2, 8, 15, 2},
            {2, 8, 16, 2},
            {2, 8, 18, 1},
            {2, 8, 18, 2},
            {2, 8, 18, 3},
            {2, 8, 18, 4},
            {2, 8, 18, 5},
            {2, 8, 18, 6},
            {2, 8, 18, 7},
            {2, 8, 18, 8},
            {2, 8, 18, 8, 1},
            {2, 8, 18, 8, 2},
            {2, 8, 18, 9, 2},
            {2, 8, 18, 10, 2},
            {2, 8, 18, 12, 1},
            {2, 8, 18, 13, 1},
            {2, 8, 18, 13, 2},
            {2, 8, 18, 15, 1},
            {2, 8, 18, 16, 1},
            {2, 8, 18, 18},
            {2, 8, 18, 18, 1},
            {2, 8, 18, 18, 2},
            {2, 8, 18, 18, 3},
            {2, 8, 18, 18, 4},
            {2, 8, 18, 18, 5},
            {2, 8, 18, 18, 6},
            {2, 8, 18, 18, 7},
            {2, 8, 18, 18, 8},
            {2, 8, 18, 18, 8, 1},
            {2, 8, 18, 18, 8, 2},
            {2, 8, 18, 18, 9, 2},
            {2, 8, 18, 19, 9, 2},
            {2, 8, 18, 21, 8, 2},
            {2, 8, 18, 22, 8, 2},
            {2, 8, 18, 23, 8, 2},
            {2, 8, 18, 24, 8, 2},
            {2, 8, 18, 25, 8, 2},
            {2, 8, 18, 25, 9, 2},
            {2, 8, 18, 27, 8, 2},
            {2, 8, 18, 28, 8, 2},
            {2, 8, 18, 29, 8, 2},
            {2, 8, 18, 30, 8, 2},
            {2, 8, 18, 31, 8, 2},
            {2, 8, 18, 32, 8, 2},
            {2, 8, 18, 32, 9, 2},
            {2, 8, 18, 32, 10, 2},
            {2, 8, 18, 32, 11, 2},
            {2, 8, 18, 32, 12, 2},
            {2, 8, 18, 32, 13, 2},
            {2, 8, 18, 32, 14, 2},
            {2, 8, 18, 32, 15, 2},
            {2, 8, 18, 32, 17, 1},
            {2, 8, 18, 32, 18, 1},
            {2, 8, 18, 32, 18, 2},
            {2, 8, 18, 32, 18, 3},
            {2, 8, 18, 32, 18, 4},
            {2, 8, 18, 32, 18, 5},
            {2, 8, 18, 32, 18, 6},
            {2, 8, 18, 32, 18, 7},
            {2, 8, 18, 32, 18, 8},
            {2, 8, 18, 32, 18, 8, 1},
            {2, 8, 18, 32, 18, 8, 2},
            {2, 8, 18, 32, 18, 9, 2},
            {2, 8, 18, 32, 18, 10, 2},
            {2, 8, 18, 32, 20, 9, 2},
            {2, 8, 18, 32, 21, 9, 2},
            {2, 8, 18, 32, 22, 9, 2},
            {2, 8, 18, 32, 24, 8, 2},
            {2, 8, 18, 32, 25, 8, 2},
            {2, 8, 18, 32, 25, 9, 2},
            {2, 8, 18, 32, 27, 8, 2},
            {2, 8, 18, 32, 28, 8, 2},
            {2, 8, 18, 32, 29, 8, 2},
            {2, 8, 18, 32, 30, 8, 2},
            {2, 8, 18, 32, 31, 8, 2},
            {2, 8, 18, 32, 32, 8, 2},
            {2, 8, 18, 32, 32, 8, 3},
            {2, 8, 18, 32, 32, 10, 2},
            {2, 8, 18, 32, 32, 11, 2},
            {2, 8, 18, 32, 32, 12, 2},
            {2, 8, 18, 32, 32, 13, 2},
            {2, 8, 18, 32, 32, 14, 2},
            {2, 8, 18, 32, 32, 15, 2},
            {2, 8, 18, 32, 32, 16, 2},
            {2, 8, 18, 32, 32, 17, 2},
            {2, 8, 18, 32, 32, 18, 2},
            {2, 8, 18, 32, 32, 18, 3},
            {2, 8, 18, 32, 32, 18, 4},
            {2, 8, 18, 32, 32, 18, 5},
            {2, 8, 18, 32, 32, 18, 6},
            {2, 8, 18, 32, 32, 18, 7},
            {2, 8, 18, 32, 32, 18, 8}
    };

}