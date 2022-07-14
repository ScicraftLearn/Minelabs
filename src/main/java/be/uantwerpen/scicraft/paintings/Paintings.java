package be.uantwerpen.scicraft.paintings;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Paintings {

    public static final PaintingVariant PARTICLE_CPY = register(new PaintingVariant(32, 32), "particle_cpy");
    public static final PaintingVariant PARTICLE_RGB = register(new PaintingVariant(32, 32), "particle_rgb");

    /**
     * Register a new painting
     *
     * @param motive : new painting
     * @param id     : string name of painting
     * @return {@link PaintingVariant}
     */
    private static PaintingVariant register(PaintingVariant motive, String id) {
        return Registry.register(Registry.PAINTING_VARIANT, new Identifier(Scicraft.MOD_ID, id), motive);
    }

    public static void registerPaintings() {
        Scicraft.LOGGER.info("Registering Paintings");
    }
}
