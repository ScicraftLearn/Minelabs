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
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger("scicraft");
    // TODO: Move to dedicated classes

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "proton"), Items.PROTON);
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "proton"), Blocks.PROTON_BLOCK);
    }
}