package be.uantwerpen.scicraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@SuppressWarnings("UNUSED")
public class Scicraft implements ModInitializer {
    public static final String MOD_ID = "scicraft";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger("modid");


    // TODO: Move to dedicated classes
    // Blocks
    public static final Block PROTON_BLOCK = new Block(FabricBlockSettings.of(Material.WOOL).mapColor(MapColor.WHITE).strength(2f).nonOpaque().collidable(false));

    // BlockItems
    public static final Item PROTON = new BlockItem(PROTON_BLOCK, new FabricItemSettings().group(ItemGroup.MISC));

    // Items


    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "proton"), PROTON);
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "proton"), PROTON_BLOCK);
    }
}