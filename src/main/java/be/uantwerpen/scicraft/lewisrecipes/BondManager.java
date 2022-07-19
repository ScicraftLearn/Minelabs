package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BondManager {

    public static final class Bond {
        private final boolean vertical;
        private final Slot slot1;
        private final Slot slot2;
        private final int bondCount;

        @Contract(pure = true)
        public Bond(@NotNull Slot slot1, @NotNull Slot slot2, int bondCount) {
            if (slot1.x != slot2.x && slot1.y == slot2.y)
                vertical = true;
            else if (slot1.x == slot2.x && slot1.y != slot2.y)
                vertical = false;
            else throw new IllegalArgumentException("Invalid combination of slots provided!");

            this.slot1 = slot1;
            this.slot2 = slot2;
            this.bondCount = bondCount;
        }

        public int getX() {
            return (slot1.x + slot2.x) / 2;
        }

        public int getY() {
            return (slot1.y + slot2.y) / 2;
        }

        @NotNull
        public ItemStack getStack() {
            ItemStack stack = new ItemStack(Items.BOND);
            NbtCompound nbt = stack.getOrCreateNbt();
            nbt.putBoolean("ScicraftBondDirection", vertical);
            nbt.putInt("ScicraftBondAmount", bondCount);
            return stack;
        }

        @Override
        public String toString() {
            return "Bond{" +
                    "vertical=" + vertical +
                    ", X=" + getX() +
                    ", Y=" + getY() +
                    ", bondCount=" + bondCount +
                    '}';
        }
    }

    private final List<Bond> bonds;

    public BondManager() {
        this.bonds = new ArrayList<>();
    }

    public List<Bond> getBonds() {
        return bonds;
    }

    public void addBond(Bond bond) {
        bonds.add(bond);
    }

    public void increaseBond(Bond bond, int amount) {
        if (bond != null && this.removeBond(bond)) {
            if (bond.bondCount + amount <= 0) this.removeBond(bond);
            else this.addBond(new Bond(bond.slot1, bond.slot2, bond.bondCount + amount));
        } else throw new IllegalArgumentException("Specified bond is either null or not currently applied!");
    }

    public void decreaseBond(Bond bond, int amount) {
        this.increaseBond(bond, -amount);
    }

    public boolean removeBond(Bond bond) {
        return bonds.remove(bond);
    }

    public void clear() {
        bonds.clear();
    }
}
