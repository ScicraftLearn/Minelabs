package be.uantwerpen.scicraft.screen;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandler {


    public static final ScreenHandlerType<AtomPortalScreenHandler> PORTAL_SCREEN_HANDLER = register(AtomPortalScreenHandler::new, "atom_portal");

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(ScreenHandlerRegistry.SimpleClientHandlerFactory<T> screenHandlerType, String identifier) {
        return ScreenHandlerRegistry.registerSimple(new Identifier(Scicraft.MOD_ID, identifier), screenHandlerType);
    }
}
