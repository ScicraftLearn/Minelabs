package be.uantwerpen.scicraft.gui;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Screens {

    //public static final ScreenHandlerType<LewisBlockScreenHandler> LEWIS_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(Scicraft.MOD_ID, "lewis_block"), LewisBlockScreenHandler::new);

    public static final ExtendedScreenHandlerType<LewisBlockScreenHandler> LEWIS_SCREEN_HANDLER = register((syncId, inventory, buf) -> new LewisBlockScreenHandler(syncId, inventory, buf), "lewis_block");

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

    /**
     * Register a ExtendedScreen
     *
     * @param factory
     * @param identifier
     * @return
     * @param <T>
     */
    private static <T extends ScreenHandler> ExtendedScreenHandlerType<T> register(ExtendedScreenHandlerType.ExtendedFactory factory, String identifier) {
        ExtendedScreenHandlerType type = new ExtendedScreenHandlerType<>(factory);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(Scicraft.MOD_ID, identifier), type);
        return type;
    }


    /**
     * Main class method
     * Registers all Screens
     */
    public static void registerScreens() {
        Scicraft.LOGGER.info("registering screens");
    }
}
