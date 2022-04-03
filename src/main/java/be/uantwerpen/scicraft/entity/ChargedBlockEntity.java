package be.uantwerpen.scicraft.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class ChargedBlockEntity extends BlockEntity {
    private int charge = 7;
    private double field_x = 2.0;
    private double field_y = 3.55;
    private double field_z = -5.0;

    public ChargedBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.CHARGED_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        // Save the current value of the number to the tag
        tag.putInt("charge", charge);
        tag.putDouble("field_x", field_x);
        tag.putDouble("field_y", field_y);
        tag.putDouble("field_z", field_z);
        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        charge = tag.getInt("charge");
        field_x = tag.getDouble("field_x");
        field_y = tag.getDouble("field_y");
        field_z = tag.getDouble("field_z");
    }
}
