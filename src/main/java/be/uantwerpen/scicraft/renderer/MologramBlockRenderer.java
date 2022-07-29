package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.entity.MologramBlockEntity;
import be.uantwerpen.scicraft.particle.Particles;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.Random;

public class MologramBlockRenderer implements BlockEntityRenderer<MologramBlockEntity> {

    private static ItemStack stack = ItemStack.EMPTY;
    public static final Identifier BEAM_TEXTURE2 = new Identifier("scicraft:textures/block/helium.png");

    public MologramBlockRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(MologramBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Random random = new Random();
        World world = entity.getWorld();

        if (world == null) return;

        BlockPos pos = entity.getPos();
        stack = entity.getInventory().getStack(0);

        if (stack.isEmpty()) return;

        matrices.push();
//        if(random.nextDouble()<0.2f){
//            world.addParticle(Particles.HOLOGRAM_PARTICLE, pos.getX()+ 0.5d, pos.getY() + 1.75d, pos.getZ() + 0.5d,
//                    0,0,0);
//        }
        // Move the item
        if (Block.getBlockFromItem(stack.getItem()) != Blocks.AIR) matrices.translate(0.5, 0, 0.5);
        else {
            matrices.translate(0.5, 0.10, 0.64);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));
        }

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
        // Mandatory call after GL calls
        matrices.pop();

        // Render molecule above
        matrices.push();
        matrices.translate(0, 1, 0);
        BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier(Scicraft.MOD_ID, "block/sphere"));
        MinecraftClient.getInstance().getBlockRenderManager().renderBlock(
                be.uantwerpen.scicraft.block.Blocks.SPHERE.getDefaultState(), pos, world, matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), true, net.minecraft.util.math.random.Random.create());
        matrices.pop();

        renderBeam(0.4f, 0.7f, pos, matrices, vertexConsumers);


    }
    private void renderBeam(float width, float height, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers){
        matrices.push();
        // Important: may not collide with model, otherwise we get rendering glitches
        matrices.translate(0.5,12f/16f,0.5);
        renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE2, true)), 1f, 1f, 1f, .6F, height, width, 1.0F, 0.0F, 0F, 1f);
        matrices.pop();
    }
    private static void renderBeamLayer(MatrixStack matrices, VertexConsumer vertices, float red, float green, float blue, float alpha, float height, float width, float u1, float u2, float v1, float v2) {
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, height, width, -width, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, height, -width, -width, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, height, width, width, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, height, -width, width, u1, u2, v1, v2);
    }
    private static void renderBeamFace(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, float height, float x, float z, float u1, float u2, float v1, float v2) {
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, 0, 0, 0, u2, v1);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, 0, 0, 0, u2, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x, 0, u1, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, 0, z, u1, v1);

        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, 0, z, u1, v1);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x, 0, u1, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, 0, 0, 0, u2, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, 0, 0, 0, u2, v1);
    }
    private static void renderBeamVertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, float y, float x, float z, float u, float v) {
        vertices.vertex(positionMatrix, x, y, z).color(red, green, blue, alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
    }

    @Override
    public boolean rendersOutsideBoundingBox(MologramBlockEntity blockEntity) {
        return true;
    }
}