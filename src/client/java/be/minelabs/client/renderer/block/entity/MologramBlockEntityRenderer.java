package be.minelabs.client.renderer.block.entity;

import be.minelabs.Minelabs;
import be.minelabs.block.entity.MologramBlockEntity;
import be.minelabs.item.IMoleculeItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class MologramBlockEntityRenderer implements BlockEntityRenderer<MologramBlockEntity> {
    private final static float ROTATION_SPEED = 0.2f;
    public MologramBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(MologramBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        if (world == null) return;

        BlockPos pos = entity.getPos();
        ItemStack stack = entity.getContents();
        if (stack.isEmpty()) return;


        // Render item inside
        matrices.push();
        matrices.translate(0.5, 0.1, 0.6);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, null, 0);
        matrices.pop();


        // Render molecule above
        if (!(stack.getItem() instanceof IMoleculeItem molecule))
            return;

        BakedModel model = MinecraftClient.getInstance().getBakedModelManager().models.get(new Identifier(Minelabs.MOD_ID, "molecules/" + molecule.getMolecule().toLowerCase()));
        if (model == null)
            return;

        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(getRotation(world)));
        matrices.translate(-0.5, 13/16f, -0.5);
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(world, model, entity.getCachedState(), pos, matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), false, net.minecraft.util.math.random.Random.create(), 0, overlay);
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
