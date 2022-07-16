package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.entity.BlockEntities;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.gui.Screens;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.lewisrecipes.MoleculeRecipe;
import be.uantwerpen.scicraft.paintings.Paintings;
import be.uantwerpen.scicraft.sound.SoundEvents;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Scicraft implements ModInitializer {

    public static final String MOD_ID = "scicraft";

    // This logger is used to write text to the console and the log file.
    public static final Logger LOGGER = LogManager.getLogger("scicraft");

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Scicraft world!");

        Items.registerItems();
        Blocks.registerBlocks();
        BlockEntities.registerBlockEntities();
        Entities.registerEntities();
        ExtraDispenserBehavior.registerBehaviors();
        SoundEvents.registerSounds();
        Screens.registerScreens();
        Paintings.registerPaintings();

        MoleculeRecipe.register();
    }
}