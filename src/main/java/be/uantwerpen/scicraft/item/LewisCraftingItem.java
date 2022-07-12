package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.lewisrecipes.Atom;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LewisCraftingItem extends AtomItem {

    protected static final String CURRENT_VALENCE_ELECTRONS = "ScicraftCurrentValenceElectrons";
    protected static final String CURRENT_BONDS = "ScicraftCurrentBonds";
    protected static final String CURRENT_NEUTRONS = "ScicraftCurrentNeutrons";

    public LewisCraftingItem(Item.Settings settings, Atom atom) {
        super(settings, atom);
    }

    public int getCurrentValenceElectrons(@NotNull ItemStack stack) {
        return stack.getOrCreateNbt().contains(CURRENT_VALENCE_ELECTRONS, 3 /* type INT */)
                ? stack.getOrCreateNbt().getInt(CURRENT_VALENCE_ELECTRONS) : this.getAtom().getInitialValenceElectrons();
    }
    public void setCurrentValenceElectrons(@NotNull ItemStack stack, int currentValenceElectrons) {
        stack.getOrCreateNbt().putInt(CURRENT_VALENCE_ELECTRONS, currentValenceElectrons);
    }

    public int getCurrentBonds(@NotNull ItemStack stack) {
        return stack.getOrCreateNbt().contains(CURRENT_BONDS, 3 /* type INT */)
                ? stack.getOrCreateNbt().getInt(CURRENT_BONDS) : 0;
    }
    public void setCurrentBonds(@NotNull ItemStack stack, int currentBonds) {
        stack.getOrCreateNbt().putInt(CURRENT_BONDS, currentBonds);
    }

    public int getCurrentNeutrons(@NotNull ItemStack stack) {
        return stack.getOrCreateNbt().contains(CURRENT_NEUTRONS, 3 /* type INT */)
                ? stack.getOrCreateNbt().getInt(CURRENT_NEUTRONS) : this.getAtom().getInitialNeutrons();
    }
    public void setCurrentNeutrons(@NotNull ItemStack stack, int currentNeutrons) {
        stack.getOrCreateNbt().putInt(CURRENT_NEUTRONS, currentNeutrons);
    }
}
