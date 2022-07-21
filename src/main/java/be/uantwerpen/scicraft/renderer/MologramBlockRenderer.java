package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.MologramBlock;
import be.uantwerpen.scicraft.block.entity.MologramBlockEntity;
import be.uantwerpen.scicraft.particle.Particles;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Random;

public class MologramBlockRenderer implements BlockEntityRenderer<MologramBlockEntity> {

    private static ItemStack stack = ItemStack.EMPTY;

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
        // Move the item
        if (Block.getBlockFromItem(stack.getItem()) != Blocks.AIR) matrices.translate(0.5, 0, 0.5);
        else {
            matrices.translate(0.5, 0.10, 0.64);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));
        }

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
        // Mandatory call after GL calls
        matrices.pop();
    }
}
