package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.AnimatedChargedBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class AnimatedChargedBlock extends BlockWithEntity {
    private Supplier<BlockEntityType<? extends  AnimatedChargedBlockEntity>> lazy;

    public AnimatedChargedBlock(Settings settings, Supplier<BlockEntityType<? extends  AnimatedChargedBlockEntity>> lazy) {
        super(settings);
        this.lazy = lazy;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos arg0, BlockState arg1) {
        return lazy.get().instantiate(arg0, arg1);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, this.lazy.get(), AnimatedChargedBlockEntity::tick);
    }
}
