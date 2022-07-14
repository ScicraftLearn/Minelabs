package be.uantwerpen.scicraft.paintings;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class Paintings {
    public static final RegistryKey<PaintingVariant> PARTICLE_CPY = register("particle_cpy", new PaintingVariant(32, 32));
    public static final RegistryKey<PaintingVariant> PARTICLE_RGB = register("particle_rgb", new PaintingVariant(32, 32));

    /**
     * Register a new painting
     *
     * @param motive : new painting
     * @param id     : string name of painting
     * @return {@link PaintingVariant}
     */
    private static RegistryKey<PaintingVariant> register(String id, PaintingVariant motive) {
        RegistryKey<PaintingVariant> key = RegistryKey.of(Registry.PAINTING_VARIANT_KEY, new Identifier(Scicraft.MOD_ID, id));
        Registry.register(Registry.PAINTING_VARIANT, key, motive);
        return key;
    }

    public static void registerPaintings() {
        Scicraft.LOGGER.info("Registering Paintings");
    }
}
