package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.ElectronBlock;
import be.uantwerpen.scicraft.block.entity.ElectronBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.profiling.jfr.event.PacketSentEvent;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ElectronBlockEntityRenderer<T extends ElectronBlockEntity> implements BlockEntityRenderer<T> {
    public ElectronBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(T blockEntity,
                       float tickDelta,
                       MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light,
                       int overlay) {
        matrices.push();
        double offset;
        if (blockEntity.is_moving) {
            offset = (blockEntity.getWorld().getTime() + tickDelta - blockEntity.time) / 40;
            matrices.translate(0, 0 + offset, 0);
            if (Math.abs(offset) > 1) {
                NbtCompound my_nbt = blockEntity.createNbt();
                my_nbt.putBoolean("is", false);
                my_nbt.putInt("md", 2);
                blockEntity.readNbt(my_nbt);
            }
        } else {
            offset = Math.sin((blockEntity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;
            // Move the item
            matrices.translate(0.5, 1.25 + offset, 0.5);

            // Rotate the item
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((blockEntity.getWorld().getTime() + tickDelta) * 4));

        }
        Random random = new Random();
        MinecraftClient.getInstance().getBlockRenderManager().renderBlock(Blocks.ELECTRON.getDefaultState(), blockEntity.getPos(), blockEntity.getWorld(), matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), true, random);

        // Mandatory call after GL calls
        matrices.pop();


        //int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
        //MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
    }
}
