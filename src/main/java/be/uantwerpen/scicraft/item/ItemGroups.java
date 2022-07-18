package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemGroup;

public class ItemGroups {
    public static final ItemGroup SCICRAFT = FabricItemGroupBuilder.create(
                    new Identifier(Scicraft.MOD_ID, "scicraft"))
            .icon(() -> new ItemStack(Items.HELIUM))
            .build();

    public static final ItemGroup QUANTUM_FIELDS = FabricItemGroupBuilder.create(
                    new Identifier(Scicraft.MOD_ID, "quantum_fields"))
            .icon(() -> new ItemStack(Items.GLUON_QUANTUMFIELD))
            .build();

    public static final ItemGroup ELEMENTARY_PARTICLES = FabricItemGroupBuilder.create(
                    new Identifier(Scicraft.MOD_ID, "elementary_particles"))
            .icon(() -> new ItemStack(Items.PION_NUL))
            .build();

    public static final ItemGroup ATOMS = FabricItemGroupBuilder.create(
                    new Identifier(Scicraft.MOD_ID, "atoms"))
            .icon(() -> new ItemStack(Items.HYDROGEN_ATOM))
            .build();

}
