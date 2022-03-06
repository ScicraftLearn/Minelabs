package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.LewisBlock;
import be.uantwerpen.scicraft.block.LewisBlockEntity;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.gui.LewisBlockScreenHandler;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.sound.SoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Scicraft implements ModInitializer {

    public static final String MOD_ID = "scicraft";

    // This logger is used to write text to the console and the log file.
    public static final Logger LOGGER = LogManager.getLogger("scicraft");



    /** BILLY **/
    // a public identifier for multiple parts of our bigger chest
    public static final Identifier BOX = new Identifier(MOD_ID, "lewis_block");

    public static final Block LEWIS_BLOCK = Registry.register(Registry.BLOCK, BOX, new LewisBlock(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.CHEST)));;
    public static final BlockItem LEWIS_BLOCK_ITEM = Registry.register(Registry.ITEM, BOX, new BlockItem(LEWIS_BLOCK, new Item.Settings().group(ItemGroup.MISC)));;
    public static final BlockEntityType<LewisBlockEntity> LEWIS_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, BOX, FabricBlockEntityTypeBuilder.create(LewisBlockEntity::new, LEWIS_BLOCK).build(null));;

    public static final ScreenHandlerType<LewisBlockScreenHandler> LEWIS_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(BOX, LewisBlockScreenHandler::new);;



    @Override
    public void onInitialize() {
        LOGGER.info("Hello Scicraft world!");

        Items.registerItems();
        Blocks.registerBlocks();
        Entities.registerEntities();
        ExtraDispenserBehavior.registerBehaviors();
        SoundEvents.registerSounds();
    }
}