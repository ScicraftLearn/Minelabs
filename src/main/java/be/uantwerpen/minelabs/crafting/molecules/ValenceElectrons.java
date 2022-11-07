package be.uantwerpen.minelabs.crafting.molecules;

import be.uantwerpen.minelabs.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ValenceElectrons {
    private final ArrayList<Boolean> bondDirections;
    private final int electronCount;
    public ValenceElectrons(ArrayList<Boolean> bondDirections, int valenceE){
        this.bondDirections = bondDirections;
        this.electronCount = valenceE;
    }
    @NotNull
    public ItemStack getStack() {
        ItemStack stack = new ItemStack(Items.VALENCEE);
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putBoolean("MinelabsBondDirection", vertical);
        //inverse bonddir: voorkeur N-E-S-W
        // MAP: in  elke richting 0,1,2
        // NBT of type MAP :D
        return stack;
    }
}

