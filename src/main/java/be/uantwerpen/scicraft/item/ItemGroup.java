package be.uantwerpen.scicraft.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import be.uantwerpen.scicraft.block.Blocks;

public class ItemGroup {
    public static final net.minecraft.item.ItemGroup QUANTUM_FIELDS = FabricItemGroupBuilder.create(
                    new Identifier("scicraft", "quantum_fields"))
            .icon(() -> new ItemStack(Blocks.PROTON_BLOCK))
            .appendItems(stacks -> {
                stacks.add(new ItemStack(Blocks.PROTON_BLOCK));
            })
            .build();

    public static final net.minecraft.item.ItemGroup ELEMENTARY_PARTICLES = FabricItemGroupBuilder.create(
                    new Identifier("scicraft", "elementary_particles"))
            .icon(() -> new ItemStack(Blocks.PION_NUL_BLOCK))
            .appendItems(stacks -> {
                stacks.add(new ItemStack(Blocks.PION_NUL_BLOCK));
                stacks.add(new ItemStack(Blocks.PION_MINUS_BLOCK));
                stacks.add(new ItemStack(Blocks.PION_PLUS_BLOCK));
            })
            .build();
}
