package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.block.Blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {
    // Items
    public static final Item ELECTRON = new ElectronItem(new Item.Settings().group(ItemGroup.MISC).maxCount(64));
    public static final Item PROTON = new ProtonItem(new Item.Settings().group(ItemGroup.MISC).maxCount(64));
    public static final Item NEUTRON = new NeutronItem(new Item.Settings().group(ItemGroup.MISC).maxCount(64));

    public static final Item ENTROPY_CREEPER_SPAWN_EGG = new SpawnEggItem(Entities.ENTROPY_CREEPER,
            0xbb64e1, 0x5d0486, new FabricItemSettings().group(ItemGroup.MISC));

    // BlockItems
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
        registerItem(NEUTRON, "neutron");
        registerItem(PION_NUL, "pion_nul");
        registerItem(PION_MINUS, "pion_minus");
        registerItem(PION_PLUS, "pion_plus");
        registerItem(ENTROPY_CREEPER_SPAWN_EGG, "entropy_creeper_spawn_egg");
    }
}
