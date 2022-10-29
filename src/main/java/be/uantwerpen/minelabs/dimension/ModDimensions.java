package be.uantwerpen.minelabs.dimension;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.Blocks;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {
    private static final RegistryKey<DimensionOptions> DIMENSION_KEY = RegistryKey.of(Registry.DIMENSION_KEY, new Identifier(Minelabs.MOD_ID, "subatom"));
    public static RegistryKey<World> SUBATOM_KEY = RegistryKey.of(Registry.WORLD_KEY, DIMENSION_KEY.getValue());
    public static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier(Minelabs.MOD_ID, "subatom_type"));

    public static void register() {
        Minelabs.LOGGER.info("Dimensions done");
        registerPortals();
        Minelabs.LOGGER.info("Portals done");
    }

    private static void registerPortals() {
        //TODO CUSTOM TEXTURE ?? -> custom portal block (must extend CustomPortalBlock)
        // Light Item -> ATOM ?
        // SOUNDS ??
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.SALT_BLOCK)
                .destDimID(new Identifier(Minelabs.MOD_ID, "subatom"))
                .onlyLightInOverworld()
                //.customPortalBlock((CustomPortalBlock) Blocks.PORTAL_BLOCK)
                .tintColor(153, 180, 181)
                .registerPortal();
    }
}
