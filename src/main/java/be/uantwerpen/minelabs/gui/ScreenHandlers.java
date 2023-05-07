package be.uantwerpen.minelabs.gui;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.gui.charged_point_gui.ChargedPointBlockScreenHandler;
import be.uantwerpen.minelabs.gui.ionic_gui.IonicBlockScreenHandler;
import be.uantwerpen.minelabs.gui.lab_chest_gui.LabChestScreenHandler;
import be.uantwerpen.minelabs.gui.lewis_gui.LewisBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlers {

    public static final ExtendedScreenHandlerType<LewisBlockScreenHandler> LEWIS_SCREEN_HANDLER = register(LewisBlockScreenHandler::new, "lewis_block");
    public static final ExtendedScreenHandlerType<IonicBlockScreenHandler> IONIC_SCREEN_HANDLER = register(IonicBlockScreenHandler::new, "ionic_block");
    public static final ScreenHandlerType<LabChestScreenHandler> LAB_CHEST_SCREEN_HANDLER = registerSimple(LabChestScreenHandler::new, "lab_chest");
    public static final ScreenHandlerType<ChargedPointBlockScreenHandler> CHARGED_POINT_SCREEN_HANDLER = registerSimple(ChargedPointBlockScreenHandler::new, "charged_point");

    /**
     * Register a Screen
     * <p>
     *
     * @param factory : ScreenHandlerType.Factory<T> constructor of screenhandler to register
     * @param identifier        : String name of the Item
     * @return {@link ScreenHandler}
     */
    private static <T extends ScreenHandler> ScreenHandlerType<T> registerSimple(ScreenHandlerType.Factory<T> factory, String identifier) {
        return (ScreenHandlerType) Registry.register(Registries.SCREEN_HANDLER, new Identifier(Minelabs.MOD_ID, identifier), new ScreenHandlerType(factory));
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
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(Minelabs.MOD_ID, identifier), type);
    }


    /**
     * Main class method
     * Registers all Screens
     */
    public static void registerScreens() {
        Minelabs.LOGGER.info("registering screens");
    }
}
