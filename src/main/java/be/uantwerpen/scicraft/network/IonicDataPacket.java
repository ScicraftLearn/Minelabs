package be.uantwerpen.scicraft.network;

import be.uantwerpen.scicraft.block.entity.IonicBlockEntity;
import be.uantwerpen.scicraft.block.entity.LewisBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IonicDataPacket {

    private final BlockPos pos;
    private final DefaultedList<Ingredient> leftIngredients;
    private final DefaultedList<Ingredient> rightIngredients;

    public IonicDataPacket(BlockPos pos, DefaultedList<Ingredient> leftIngredients, DefaultedList<Ingredient> rightIngredients) {
        this.pos = pos;
        this.leftIngredients = leftIngredients;
        this.rightIngredients = rightIngredients;
    }

    public PacketByteBuf write() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.leftIngredients.size());
        for (Ingredient ingredient: this.leftIngredients) {
            ingredient.write(buf);
        }
        buf.writeInt(this.rightIngredients.size());
        for (Ingredient ingredient: this.rightIngredients) {
            ingredient.write(buf);
        }
        return buf;
    }

    /**
     * Handles receiving the packet.
     * Remember, this class is used on both sides, so it can't contain any client side only methods,
     * such as MinecraftClient and ClientPlayNetworkHandler.
     *
     * @param world
     * @param buf
     * @param responseSender
     */
    public static void receive(World world, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        int leftSize = buf.readInt();
        DefaultedList<Ingredient> leftIngredients = DefaultedList.of();
        if (leftSize > 0) {
            for (int i = 0; i < leftSize; i++) {
                leftIngredients.add(Ingredient.fromPacket(buf));
            }
        }
        int rightSize = buf.readInt();
        DefaultedList<Ingredient> rightIngredients = DefaultedList.of();
        if (rightSize > 0) {
            for (int i = 0; i < rightSize; i++) {
                rightIngredients.add(Ingredient.fromPacket(buf));
            }
        }
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof IonicBlockEntity ionic) {
            ionic.setIngredients(leftIngredients, rightIngredients);
        }
    }

    public void sent(World world, BlockPos pos) {
        if (world.isClient) {
            return;
        }
        PacketByteBuf buf = this.write();
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, pos)) {
            ServerPlayNetworking.send(player, NetworkingConstants.IONICDATASYNC, buf);
        }
    }
}
