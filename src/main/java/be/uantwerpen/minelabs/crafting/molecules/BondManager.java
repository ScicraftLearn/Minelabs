package be.uantwerpen.minelabs.crafting.molecules;

import be.uantwerpen.minelabs.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BondManager {

    public static final class Bond {
        private final boolean vertical;
        private final Slot slot1;
        private final Slot slot2;
        private final int bondCount;

        @Contract(pure = true)
        public Bond(@NotNull Slot slot1, @NotNull Slot slot2, int bondCount) {
            if (slot1.x != slot2.x && slot1.y == slot2.y)
                vertical = false;
            else if (slot1.x == slot2.x && slot1.y != slot2.y)
                vertical = true;
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
            nbt.putBoolean("MinelabsBondDirection", vertical);
                nbt.putInt("MinelabsBondAmount", bondCount);
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

    public Bond getBond(Slot slot1, Slot slot2) {
        for (Bond bond : bonds) {
            if (bond.slot1 == slot1 && bond.slot2 == slot2
                    || bond.slot1 == slot2 && bond.slot2 == slot2) {
                return bond;
            }
        }
        return null;
    }

    public Map<String, Boolean> findEmptyBonds(Slot slot) {
        Map<String, Boolean> dirs = new HashMap<>(Map.of(
                "n", true,
                "e", true,
                "s", true,
                "w", true
        ));

        for (Bond bond : bonds) {
            if (bond.slot1 == slot) { // given slot is bond's slot 1
                // CHECK Y
                if (bond.slot1.y > bond.slot2.y) { // ABOVE
                    dirs.put("n", false);
                } else if (bond.slot1.y < bond.slot2.y) { //BELOW
                    dirs.put("s", false);
                } else {
                    // CHECK X
                    if (bond.slot1.x > bond.slot2.x) { // LEFT
                        dirs.put("w", false);
                    } else if (bond.slot1.x < bond.slot2.x) { //RIGHT
                        dirs.put("e", false);
                    }
                }
            } else if (bond.slot2 == slot) { // given slot is bond's slot 2
                // CHECK Y
                if (bond.slot1.y > bond.slot2.y) { // BLOW
                    dirs.put("s", false);
                } else if (bond.slot1.y < bond.slot2.y) { //ABOVE
                    dirs.put("n", false);
                } else {
                    // CHECK X
                    if (bond.slot1.x > bond.slot2.x) { // RIGHT
                        dirs.put("e", false);
                    } else if (bond.slot1.x < bond.slot2.x) { //LEFT
                        dirs.put("w", false);
                    }
                }
            }
        }
        return dirs;
    }

    public Bond getOrCreateBond(Slot slot1, Slot slot2, int bondOrder) {
        if (getBond(slot1, slot2) == null) {
            this.addBond(new Bond(slot1, slot2, bondOrder));
        }
        return getBond(slot1, slot2);
    }
}
