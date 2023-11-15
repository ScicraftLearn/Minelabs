package be.minelabs.block.entity;

import be.minelabs.block.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

public class ErlenmeyerBlockEntity extends BlockEntity {

    private Item item;

    public ErlenmeyerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.ERLENMEYER_STAND_BLOCK_ENTITY, pos, state);
    }

    public void setItem(Item item){
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        item = Item.byRawId(nbt.getInt("erlenmeyer.id"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("erlenmeyer.id", Item.getRawId(item));
        super.writeNbt(nbt);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
