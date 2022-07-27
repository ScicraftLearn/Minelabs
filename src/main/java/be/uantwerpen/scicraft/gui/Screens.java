package be.uantwerpen.scicraft.gui;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class Screens {

    //public static final ScreenHandlerType<LewisBlockScreenHandler> LEWIS_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(Scicraft.MOD_ID, "lewis_block"), LewisBlockScreenHandler::new);

    public static final ExtendedScreenHandlerType<LewisBlockScreenHandler> LEWIS_SCREEN_HANDLER = register(LewisBlockScreenHandler::new, "lewis_block");

    /**
     * Register a Screen
     * <p>
     *
     * @param screenHandlerType : ScreenHandler Object to register
     * @param identifier        : String name of the Item
     * @return {@link ScreenHandler}
     */
    private static <T extends ScreenHandler> ScreenHandlerType<T> register(ScreenHandlerRegistry.SimpleClientHandlerFactory<T> screenHandlerType, String identifier) {
        return ScreenHandlerRegistry.registerSimple(new Identifier(Scicraft.MOD_ID, identifier), screenHandlerType);
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(ScreenHandlerRegistry.SimpleClientHandlerFactory<T> screenHandlerType, String identifier) {
        return ScreenHandlerRegistry.registerSimple(new Identifier(Scicraft.MOD_ID, identifier), screenHandlerType);
    }


    /**
     * Main class method
     * Registers all Screens
     */
    public static void registerScreens() {
        Scicraft.LOGGER.info("registering screens");
    }
}
