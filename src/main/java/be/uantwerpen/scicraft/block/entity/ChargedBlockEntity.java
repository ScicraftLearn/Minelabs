package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.block.PionPlusBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ChargedBlockEntity {
    private static final int e_radius = 5;
    private static final double kc = 1;
    private static final double e_move = 1;

    public static void placed(World world, BlockPos pos, BlockState state) {
        if ((world == null) || !(world.getBlockState(pos).getBlock() instanceof PionPlusBlock)) {return;}

        NbtCompound my_nbt = world.getBlockEntity(pos).createNbt();
        double my_field_x = my_nbt.getDouble("ex");
        double my_field_y = my_nbt.getDouble("ey");
        double my_field_z = my_nbt.getDouble("ez");
        double my_charge = my_nbt.getDouble("q");
        int my_x = pos.getX();
        int my_y = pos.getY();
        int my_z = pos.getZ();

        Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(
                pos.add(-e_radius, -e_radius, -e_radius),
                pos.add(e_radius, e_radius, e_radius)
        );

        double d_x, d_y, d_z;
        double d_E;
        double d_E_x, d_E_y, d_E_z;
        double d_E_x_other, d_E_y_other, d_E_z_other;

        for (BlockPos pos_block : blocks_in_radius) {
            if ((world.getBlockState(pos_block).getBlock() instanceof PionPlusBlock)
                    && (pos_block.asLong() != pos.asLong())) {
                NbtCompound nbt_other = world.getBlockEntity(pos_block).createNbt();
                d_x = my_x - pos_block.getX();
                d_y = my_y - pos_block.getY();
                d_z = my_z - pos_block.getZ();
                d_E = nbt_other.getDouble("q") * my_charge * kc /
                         Math.pow(Math.pow(d_x, 2) + Math.pow(d_y, 2) + Math.pow(d_z, 2), 1.5);
                d_E_x = d_E * d_x;
                d_E_y = d_E * d_y;
                d_E_z = d_E * d_z;
                d_E_x_other = nbt_other.getDouble("ex") + d_E_x;
                d_E_y_other = nbt_other.getDouble("ey") + d_E_y;
                d_E_z_other = nbt_other.getDouble("ez") + d_E_z;
                nbt_other.putDouble("ex", d_E_x_other);
                nbt_other.putDouble("ey", d_E_y_other);
                nbt_other.putDouble("ez", d_E_z_other);
                nbt_other.putBoolean("ut", Math.pow(d_E_x_other, 2) + Math.pow(d_E_y_other, 2) + Math.pow(d_E_z_other, 2) > e_move);
                world.getBlockEntity(pos_block).readNbt(nbt_other);
                my_field_x -= d_E_x;
                my_field_y -= d_E_y;
                my_field_z -= d_E_z;
            }
        }
        my_nbt.putDouble("ex", my_field_x);
        my_nbt.putDouble("ey", my_field_y);
        my_nbt.putDouble("ez", my_field_z);
        my_nbt.putDouble("q", my_charge);
        my_nbt.putBoolean("ut", Math.pow(my_field_x, 2) + Math.pow(my_field_y, 2) + Math.pow(my_field_z, 2) > e_move);
        world.getBlockEntity(pos).readNbt(my_nbt);
    }

    public static void removed(World world, BlockPos pos, BlockState state) {
        if ((world == null) || !(world.getBlockState(pos).getBlock() instanceof PionPlusBlock)) {return;}

        NbtCompound my_nbt = world.getBlockEntity(pos).createNbt();
        double my_charge = my_nbt.getDouble("q");
        int my_x = pos.getX();
        int my_y = pos.getY();
        int my_z = pos.getZ();

        Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(
                pos.add(-e_radius, -e_radius, -e_radius),
                pos.add(e_radius, e_radius, e_radius)
        );

        double d_x, d_y, d_z;
        double d_E;
        double d_E_x, d_E_y, d_E_z;
        double d_E_x_other, d_E_y_other, d_E_z_other;

        for (BlockPos pos_block : blocks_in_radius) {
            if ((world.getBlockState(pos_block).getBlock() instanceof PionPlusBlock)
                    && (pos_block.asLong() != pos.asLong())) {
                NbtCompound nbt_other = world.getBlockEntity(pos_block).createNbt();
                d_x = my_x - pos_block.getX();
                d_y = my_y - pos_block.getY();
                d_z = my_z - pos_block.getZ();
                d_E = nbt_other.getDouble("q") * my_charge * kc /
                        Math.pow(Math.pow(d_x, 2) + Math.pow(d_y, 2) + Math.pow(d_z, 2), 1.5);
                d_E_x = d_E * d_x;
                d_E_y = d_E * d_y;
                d_E_z = d_E * d_z;
                d_E_x_other = nbt_other.getDouble("ex") - d_E_x;
                d_E_y_other = nbt_other.getDouble("ey") - d_E_y;
                d_E_z_other = nbt_other.getDouble("ez") - d_E_z;
                nbt_other.putDouble("ex", d_E_x_other);
                nbt_other.putDouble("ey", d_E_y_other);
                nbt_other.putDouble("ez", d_E_z_other);
                nbt_other.putBoolean("ut", Math.pow(d_E_x_other, 2) + Math.pow(d_E_y_other, 2) + Math.pow(d_E_z_other, 2) > e_move);
                world.getBlockEntity(pos_block).readNbt(nbt_other);
            }
        }
    }
}
