package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.Minelabs;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemGroups {
    public static final ItemGroup MINELABS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "minelabs"))
            .icon(() -> new ItemStack(Items.HELIUM))
            .build();

    public static final ItemGroup CHEMICALS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "chemicals"))
            .icon(() -> new ItemStack(Items.ERLENMEYER))
            .build();

    public static final ItemGroup QUANTUM_FIELDS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "quantum_fields"))
            .icon(() -> new ItemStack(Items.GLUON_QUANTUMFIELD))
            .build();

    public static final ItemGroup ELEMENTARY_PARTICLES = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "elementary_particles"))
            .icon(() -> new ItemStack(Items.PION_NUL))
            .build();

    public static final ItemGroup ATOMS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "atoms"))
            .icon(() -> new ItemStack(Items.HYDROGEN_ATOM))
            .build();

}
