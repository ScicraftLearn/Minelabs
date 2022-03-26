package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ErlenmeyerItem extends BlockItem
        implements FluidModificationItem {

    public ErlenmeyerItem(Settings settings) {
        super(Blocks.ERLENMEYER_STAND, settings);
    }

    @Override
    public void onEmptied(@Nullable PlayerEntity player, World world, ItemStack stack, BlockPos pos) {
        FluidModificationItem.super.onEmptied(player, world, stack, pos);
    }

    @Override
    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        return false;
    }
}
