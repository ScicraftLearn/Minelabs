package be.uantwerpen.minelabs.gui;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.gui.ionic_gui.IonicBlockScreenHandler;
import be.uantwerpen.minelabs.gui.lab_chest_gui.LabChestScreenHandler;
import be.uantwerpen.minelabs.gui.lewis_gui.LewisBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ScreenHandlers {

    public static final ExtendedScreenHandlerType<LewisBlockScreenHandler> LEWIS_SCREEN_HANDLER = register(LewisBlockScreenHandler::new, "lewis_block");
    public static final ExtendedScreenHandlerType<IonicBlockScreenHandler> IONIC_SCREEN_HANDLER = register(IonicBlockScreenHandler::new, "inonic_block");
    //public static final ScreenHandlerType<LabChestScreenHandler> LAB_CHEST_SCREEN_HANDLER = register(LabChestScreenHandler::new, "");
    public static ScreenHandlerType<LabChestScreenHandler> LAB_CHEST_SCREEN_HANDLER;

    /**
     * Register a Screen
     * <p>
     *
     * @param screenHandlerType : ScreenHandler Object to register
     * @param identifier        : String name of the Item
     * @return {@link ScreenHandler}
     */
    private static <T extends ScreenHandler> ScreenHandlerType<T> register(ScreenHandlerRegistry.SimpleClientHandlerFactory<T> screenHandlerType, String identifier) {
        return ScreenHandlerRegistry.registerSimple(new Identifier(Minelabs.MOD_ID, identifier), screenHandlerType);
    }

    /**
     * Register an {@link ExtendedScreenHandlerType}.
     * This handler has an addition attribute to pass in a buffer with data,
     * it uses to initially open.
     *
     * @param factory
     * @param identifier
     * @return
     * @param <T>
     */
    private static <T extends ScreenHandler> ExtendedScreenHandlerType<T> register(ExtendedScreenHandlerType.ExtendedFactory factory, String identifier) {
        ExtendedScreenHandlerType type = new ExtendedScreenHandlerType<>(factory);
        return Registry.register(Registry.SCREEN_HANDLER, new Identifier(Minelabs.MOD_ID, identifier), type);
    }


    /**
     * Main class method
     * Registers all Screens
     */
    public static void registerScreens() {
        Minelabs.LOGGER.info("registering screens");
        LAB_CHEST_SCREEN_HANDLER = new ScreenHandlerType<>(LabChestScreenHandler::new);
    }
}
