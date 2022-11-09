package be.uantwerpen.minelabs.crafting.molecules;

import be.uantwerpen.minelabs.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ValenceElectrons {
    private final int totalElectronCount;
    private Map<String, Integer> directionalValence;

    public ValenceElectrons(Map<String, Boolean> bondDirections, int valenceE) {
        this.totalElectronCount = valenceE;
        directionalValence = new HashMap<>(Map.of(
                "N", 0,
                "E", 0,
                "S", 0,
                "W", 0
        ));
        addElectrons(bondDirections, valenceE);
    }

    private void addElectrons(Map<String, Boolean> bondDirections, int count) {
        //todo infinite loop!
        //inverse bonddir & voorkeur N-E-S-W
        // int: in  elke richting 0,1,2
        // while (electronCount>0){
        for (String key : directionalValence.keySet()) {
            if (count == 0) {
                break;
            }
            if (bondDirections.get(key)) {
                addEDir(key);
                count -= 1;
            }
        }
        //}
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

