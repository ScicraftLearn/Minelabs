package be.minelabs.paintings;

import be.minelabs.Minelabs;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;


public class Paintings {
    public static final RegistryKey<PaintingVariant> PARTICLE_CPY = register("particle_cpy", new PaintingVariant(32, 32));
    public static final RegistryKey<PaintingVariant> PARTICLE_RGB = register("particle_rgb", new PaintingVariant(32, 32));

    public static final RegistryKey<PaintingVariant> LAB_COMPRESSED_GAS = register("lab_compressedgas", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_CORROSIVE = register("lab_corrosive", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_CREEPER = register("lab_creeper", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_EXPLOSIVE = register("lab_explosive", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_FLAMMABLE = register("lab_flammable", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_HEALTH_HAZARD = register("lab_healthhazard", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_IONIZING_RADIATION = register("lab_ionizingradiation", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_IRRITANT = register("lab_irritant", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_LASER = register("lab_laser", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_OXIDIZING = register("lab_oxidizing", new PaintingVariant(16, 16));
    public static final RegistryKey<PaintingVariant> LAB_TOXIC = register("lab_toxic", new PaintingVariant(16, 16));

    /**
     * Register a new painting
     *
     * @param motive : new painting
     * @param id     : string name of painting
     * @return {@link PaintingVariant}
     */
    private static RegistryKey<PaintingVariant> register(String id, PaintingVariant motive) {
        RegistryKey<PaintingVariant> key = RegistryKey.of(Registries.PAINTING_VARIANT.getKey(), new Identifier(Minelabs.MOD_ID, id));
        Registry.register(Registries.PAINTING_VARIANT, key, motive);
        return key;
    }

    public static void registerPaintings() {
        Minelabs.LOGGER.info("registering paintings");
    }
}
