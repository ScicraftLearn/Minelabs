package be.minelabs.network;

import be.minelabs.block.entity.LewisBlockEntity;
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

public class LewisDataPacket {

    private final BlockPos pos;
    private final DefaultedList<Ingredient> ingredients;

    public LewisDataPacket(BlockPos pos, DefaultedList<Ingredient> ingredients) {
        this.pos = pos;
        this.ingredients = ingredients;
    }

    public PacketByteBuf write() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.ingredients.size());
        for (Ingredient ingredient: this.ingredients) {
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
        int size = buf.readInt();
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                ingredients.add(Ingredient.fromPacket(buf));
            }
        }
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof LewisBlockEntity lewis) {
            lewis.setIngredients(ingredients);
        }
    }

    public void send(World world, BlockPos pos) {
        if (world.isClient) {
            return;
        }
        PacketByteBuf buf = this.write();
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, pos)) {
            ServerPlayNetworking.send(player, NetworkingConstants.LEWISDATASYNC, buf);
        }
    }
}
