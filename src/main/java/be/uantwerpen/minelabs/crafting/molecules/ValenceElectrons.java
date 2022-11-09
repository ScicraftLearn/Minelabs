package be.uantwerpen.minelabs.crafting.molecules;

import be.uantwerpen.minelabs.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class ValenceElectrons {
    private final Map<String, Boolean> bondDirections;
    private final int totalElectronCount;
    private int electronCount;
    private Map<String, Integer> directionalValence = Map.of(
            "N", 0,
            "E", 0,
            "S", 0,
            "W", 0
    );
    public ValenceElectrons(Map<String, Boolean> bondDirections, int valenceE){
        this.bondDirections = bondDirections;
        this.totalElectronCount = valenceE;
        this.electronCount = valenceE;
        addElectrons();
    }

    private void addElectrons(){
        //inverse bonddir & voorkeur N-E-S-W
        // int: in  elke richting 0,1,2
        while (electronCount>0){
            for(String key: directionalValence.keySet()){
                if(electronCount==0){break;}
                if(!bondDirections.get(key)){
                    addEDir(key);
                    electronCount -=1;
                }
            }
        }
    }

    private void addEDir(String direction){
        directionalValence.put(direction, directionalValence.get(direction) + 1);
    }

    @NotNull
    public ItemStack getStack(String direction) {
        ItemStack stack = new ItemStack(Items.VALENCEE);
        //TODO make item and overrides for model
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(direction, directionalValence.get(direction));
        return stack;
    }
}

