package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.entity.MologramBlockEntity;
import be.uantwerpen.minelabs.item.IMoleculeItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;
import net.minecraft.world.World;


public class MologramBlockEntityRenderer implements BlockEntityRenderer<MologramBlockEntity> {
    private final static float ROTATION_SPEED = 0.2f;
    public MologramBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(MologramBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        if (world == null) return;

        BlockPos pos = entity.getPos();
        ItemStack stack = entity.getStack(0);
        if (stack.isEmpty()) return;


        // Render item inside
        matrices.push();
        if (Block.getBlockFromItem(stack.getItem()) != Blocks.AIR) matrices.translate(0.5, 0, 0.5); //if BlockItem
        else {
            matrices.translate(0.5, 0.10, 0.64);
            matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90));
        }

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, null, 0);
        matrices.pop();


        // Render molecule above
        BakedModel model;
        if (stack.getItem() instanceof IMoleculeItem molecule) {
            model = MinecraftClient.getInstance().getBakedModelManager().models.get(
                    new Identifier(Minelabs.MOD_ID, "molecules/" + molecule.getMolecule().toLowerCase()));
        } else {
            return;
        }

        if (model == null) {
            return;
        }

        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(getRotation(world)));
        matrices.translate(-0.5, 13/16f, -0.5);
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(world, model, entity.getCachedState(), pos, matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), true, net.minecraft.util.math.random.Random.create(), 0, overlay);
        matrices.pop();
    }

    private int getRotation(World world) {
        long t = world.getTime();
        int x = (int) (1/ROTATION_SPEED*20);
        int l = (int) t % x;
        return  l*360/x;
    }

    @Override
    public boolean rendersOutsideBoundingBox(MologramBlockEntity blockEntity) {
        return true;
    }
}
