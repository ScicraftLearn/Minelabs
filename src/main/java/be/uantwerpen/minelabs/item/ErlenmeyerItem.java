package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ErlenmeyerItem extends BlockItem implements FluidModificationItem, MoleculeItem {

    private final String molecule;

    public ErlenmeyerItem(Settings settings, String molecule) {
        super(Blocks.ERLENMEYER_STAND, settings);
        this.molecule = molecule;
    }

    @Override
    public void onEmptied(@Nullable PlayerEntity player, World world, ItemStack stack, BlockPos pos) {
        FluidModificationItem.super.onEmptied(player, world, stack, pos);
    }

    @Override
    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        return false;
    }

    @Override
    public String getMolecule() {
        return molecule;
    }
}
