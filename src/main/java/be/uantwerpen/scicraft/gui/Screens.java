package be.uantwerpen.scicraft.gui;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Screens {

    public static final ScreenHandlerType<LewisBlockScreenHandler> LEWIS_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(Scicraft.MOD_ID, "lewis_block"), LewisBlockScreenHandler::new);

    /**
     * Someone write a register function here
     */


    /**
     * Main class method
     * Registers all Screens
     */
    public static void registerScreens() {
        Scicraft.LOGGER.info("registering screens");
    }
}
