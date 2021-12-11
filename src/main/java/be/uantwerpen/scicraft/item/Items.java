package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.entity.Entities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {
    // Items
    public static final Item ELECTRON = registerItem(new ElectronItem(new Item.Settings().group(ItemGroup.MISC).maxCount(64)), "electron");
    public static final Item PROTON = registerItem(new ProtonItem(new Item.Settings().group(ItemGroup.MISC).maxCount(64)), "proton");
    public static final Item NEUTRON = registerItem(new NeutronItem(new Item.Settings().group(ItemGroup.MISC).maxCount(64)), "neutron");

    public static final Item ENTROPY_CREEPER_SPAWN_EGG = registerItem(new SpawnEggItem(Entities.ENTROPY_CREEPER,
            0xbb64e1, 0x5d0486, new FabricItemSettings().group(ItemGroup.MISC)), "entropy_creeper_spawn_egg");

    // BlockItems
    public static final Item PION_NUL = registerItem(new BlockItem(Blocks.PION_NUL, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_nul");
    public static final Item PION_MINUS = registerItem(new BlockItem(Blocks.PION_MINUS, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_minus");
    public static final Item PION_PLUS = registerItem(new BlockItem(Blocks.PION_PLUS, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_plus");

    /**
     * Register an Item
     *
     * @param item:       Item Object to register
     * @param identifier: String name of the Item
     * @return {@link Item}
     */
    private static Item registerItem(Item item, String identifier) {
        return Registry.register(Registry.ITEM, new Identifier(Scicraft.MOD_ID, identifier), item);
    }

    /**
     * Main class method
     * Registers all (Block)Items
     */
    public static void registerItems() {
        Scicraft.LOGGER.info("registering items");
    }
}
