package be.uantwerpen.minelabs.paintings;

import be.uantwerpen.minelabs.Minelabs;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class Paintings {
    public static final RegistryKey<PaintingVariant> PARTICLE_CPY = register("particle_cpy", new PaintingVariant(32, 32));
    public static final RegistryKey<PaintingVariant> PARTICLE_RGB = register("particle_rgb", new PaintingVariant(32, 32));

    public static final RegistryKey<PaintingVariant> LAB_FLAMMABLE = register("lab_flammable", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_CREEPER = register("lab_creeper", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_EXPLOSIVE = register("lab_explosive", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_CORROSIVE = register("lab_corrosive", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_HARMFUL = register("lab_harmful", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_OXIDIZING = register("lab_oxidizing", new PaintingVariant(16, 16));

    /**
     * Register a new painting
     *
     * @param motive : new painting
     * @param id     : string name of painting
     * @return {@link PaintingVariant}
     */
    private static RegistryKey<PaintingVariant> register(String id, PaintingVariant motive) {
        RegistryKey<PaintingVariant> key = RegistryKey.of(Registry.PAINTING_VARIANT_KEY, new Identifier(Minelabs.MOD_ID, id));
        Registry.register(Registry.PAINTING_VARIANT, key, motive);
        return key;
    }

    public static void registerPaintings() {
        Minelabs.LOGGER.info("registering paintings");
    }
}
