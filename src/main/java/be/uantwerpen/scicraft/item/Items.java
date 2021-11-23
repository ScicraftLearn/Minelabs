package be.uantwerpen.scicraft.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import be.uantwerpen.scicraft.block.Blocks;

public class Items {
    // BlockItems
    public static final Item PROTON= new BlockItem(Blocks.PROTON_BLOCK, new FabricItemSettings().group(ItemGroup.QUANTUM_FIELDS));
    public static final Item PION_NUL  = new BlockItem(Blocks.PION_NUL_BLOCK, new FabricItemSettings().group(ItemGroup.QUANTUM_FIELDS));
}
