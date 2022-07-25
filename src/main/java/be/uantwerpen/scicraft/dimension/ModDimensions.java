package be.uantwerpen.scicraft.dimension;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {
    private static final RegistryKey<DimensionOptions> DIMENSION_KEY=RegistryKey.of(Registry.DIMENSION_KEY,new Identifier(Scicraft.MOD_ID,"subatom"));
    public static  RegistryKey<World> SUBATOM_KEY = RegistryKey.of(Registry.WORLD_KEY,DIMENSION_KEY.getValue());
    public static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY=RegistryKey.of(Registry.DIMENSION_TYPE_KEY,new Identifier(Scicraft.MOD_ID,"subatom_type"));

    public static void register(){
        System.out.println("Dimension done");
    }
}
