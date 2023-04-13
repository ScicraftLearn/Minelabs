package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.Minelabs;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemGroups {
    // Hard-coded ItemGroup, update from 19.3 allows to use ItemGroupEvents to extend ItemGroups. Currently, only
    //  addition by had is allowed or automatic addition (in Items.Settings). You cant combine both because of the
    //  implementation within the Fabric-API.
    public static final ItemGroup MINELABS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "minelabs"))
            .icon(() -> new ItemStack(Items.MOLOGRAM))
            .appendItems(stack -> {
                stack.add(new ItemStack(Items.ATOM_FLOOR));
                stack.add(new ItemStack(Items.SAFETY_GLASSES));
                stack.add(new ItemStack(Items.LAB_COAT));
                stack.add(new ItemStack(Items.LAB_COUNTER));
                stack.add(new ItemStack(Items.LAB_DRAWER));
                stack.add(new ItemStack(Items.LAB_SINK));
                stack.add(new ItemStack(Items.LAB_CENTER));
                stack.add(new ItemStack(Items.LAB_CORNER));
                stack.add(new ItemStack(Items.MICROSCOPE));
                stack.add(new ItemStack(Items.BURNER));
                stack.add(new ItemStack(Items.TUBERACK));
                stack.add(new ItemStack(Items.LENS));
                stack.add(new ItemStack(Items.BIG_LENS));
                stack.add(new ItemStack(Items.TIME_FREEZE_BLOCK));
                stack.add(new ItemStack(Items.ELECTRIC_FIELD_SENSOR));
                stack.add(new ItemStack(Items.MOLOGRAM));
                stack.add(new ItemStack(Items.BALLOON));
                stack.add(new ItemStack(Items.BOHR_BLUEPRINT));
                stack.add(new ItemStack(Items.LEWIS_BLOCK_ITEM));
                stack.add(new ItemStack(Items.IONIC_BLOCK_ITEM));
                stack.add(new ItemStack(Items.LASERTOOL_IRON));
                stack.add(new ItemStack(Items.LASERTOOL_GOLD));
                stack.add(new ItemStack(Items.LASERTOOL_DIAMOND));
            })
            .build();

    public static final ItemGroup CHEMICALS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "chemicals"))
            .icon(() -> new ItemStack(Items.ERLENMEYER))
            .build();

    public static final ItemGroup ATOMS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "atoms"))
            .icon(() -> new ItemStack(Items.HYDROGEN_ATOM))
            .build();

    public static final ItemGroup ELEMENTARY_PARTICLES = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "elementary_particles"))
            .icon(() -> new ItemStack(Items.PION_NUL))
            .build();

    public static final ItemGroup QUANTUM_FIELDS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "quantum_fields"))
            .icon(() -> new ItemStack(Items.GLUON_QUANTUMFIELD))
            .build();

}
