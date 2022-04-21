package be.uantwerpen.scicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;

public class ChargedPlaceholder extends Block {
    public ChargedPlaceholder(Settings settings) {
        super(settings.of(Material.AIR));
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.INVISIBLE;
    }
}
