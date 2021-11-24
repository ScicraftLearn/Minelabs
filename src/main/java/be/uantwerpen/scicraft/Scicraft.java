package be.uantwerpen.scicraft;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.item.Items;


@SuppressWarnings("UNUSED")
public class Scicraft implements ModInitializer {
    public static final String MOD_ID = "scicraft";

    // This logger is used to write text to the console and the log file.
    public static final Logger LOGGER = LogManager.getLogger("scicraft");

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");

        Items.registerItems();
        Blocks.registerBlocks();
    }
}