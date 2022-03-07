package be.uantwerpen.scicraft.paintings;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Paintings {

    public static final PaintingMotive PARTICLE_CPY = register(new PaintingMotive(32, 32), "particle_cpy");
    public static final PaintingMotive PARTICLE_RGB = register(new PaintingMotive(32, 32), "particle_rgb");

    /**
     * Register a new painting
     *
     * @param motive : new painting
     * @param id     : string name of painting
     * @return {@link PaintingMotive}
     */
    private static PaintingMotive register(PaintingMotive motive, String id) {
        return Registry.register(Registry.PAINTING_MOTIVE, new Identifier(Scicraft.MOD_ID, id), motive);
    }

    public static void registerPaintings() {
        Scicraft.LOGGER.info("Registering Paintings");
    }
}
