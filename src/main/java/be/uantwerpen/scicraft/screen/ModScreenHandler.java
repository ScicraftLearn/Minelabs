package be.uantwerpen.scicraft.screen;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandler {
    public static ScreenHandlerType<AtomPortalScreenHandler> ATOM_TYPE_HANDLER() {
        return ScreenHandlerRegistry.registerSimple(new Identifier(Scicraft.MOD_ID, "portal_block"),
                AtomPortalScreenHandler::new);
    }
}
