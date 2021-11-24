package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import be.uantwerpen.scicraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {
    // Items


    // BlockItems
    public static final Item ELECTRON = new BlockItem(Blocks.ELECTRON, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS));
    public static final Item PROTON = new BlockItem(Blocks.PROTON, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS));
    public static final Item PION_NUL = new BlockItem(Blocks.PION_NUL, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES));
    public static final Item PION_MINUS = new BlockItem(Blocks.PION_MINUS, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES));
    public static final Item PION_PLUS = new BlockItem(Blocks.PION_PLUS, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES));

    /**
     * Register an Item
     *
     * @param item: Item Object to register
     * @param identifier: String name of the Item
     *
     * @return Item
     */
    private static Item registerItem(Item item, String identifier) {
        return Registry.register(Registry.ITEM, new Identifier(Scicraft.MOD_ID, identifier), item);
    }

    /**
     * Main class method
     * Registers all (Block)Items
     */
    public static void registerItems() {
        registerItem(ELECTRON, "electron");
        registerItem(PROTON, "proton");
        registerItem(PION_NUL, "pion_nul");
        registerItem(PION_MINUS, "pion_minus");
        registerItem(PION_PLUS, "pion_plus");
    }

}
