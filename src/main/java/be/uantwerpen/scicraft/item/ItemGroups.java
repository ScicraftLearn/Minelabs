package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemGroup;

public class ItemGroups {
    public static final ItemGroup SCICRAFT = FabricItemGroupBuilder.create(
                    new Identifier(Scicraft.MOD_ID, "scicraft"))
            .icon(() -> new ItemStack(Items.HYDROGEN_ATOM))
            .build();

    public static final ItemGroup CHEMICALS = FabricItemGroupBuilder.create(
                    new Identifier(Scicraft.MOD_ID, "chemicals"))
            .icon(() -> new ItemStack(Items.ERLENMEYER))
            .build();

    public static final ItemGroup QUANTUM_FIELDS = FabricItemGroupBuilder.create(
                    new Identifier(Scicraft.MOD_ID, "quantum_fields"))
            .icon(() -> new ItemStack(Items.ELECTRON))
            .build();

    public static final ItemGroup ELEMENTARY_PARTICLES = FabricItemGroupBuilder.create(
                    new Identifier(Scicraft.MOD_ID, "elementary_particles"))
            .icon(() -> new ItemStack(Items.PION_NUL))
            .build();
}
