package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.BlockEntities;
import be.uantwerpen.scicraft.block.entity.ChargedBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ChargedBlock<T extends BlockEntity> extends BlockWithEntity {
    private final double charge;
    private final BlockEntityType<T> entity_name;

    public ChargedBlock(Settings settings, double charge_in, BlockEntityType<T> NAME) {
        super(settings);
        this.charge = charge_in;
        this.entity_name = NAME;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChargedBlockEntity(entity_name, pos, state, this.charge);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        ChargedBlockEntity.placed(world, pos, state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        ChargedBlockEntity.removed(world, pos, state);
    }

    @Override
    public <t extends BlockEntity> BlockEntityTicker<t> getTicker(World world, BlockState state, BlockEntityType<t> type) {
        return checkType(type, entity_name, ChargedBlockEntity::tick);
    }
}
