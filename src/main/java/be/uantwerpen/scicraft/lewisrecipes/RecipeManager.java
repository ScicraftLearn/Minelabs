package be.uantwerpen.scicraft.lewisrecipes;

import org.apache.commons.lang3.ArrayUtils;

import java.util.BitSet;
import java.util.Map;

public class RecipeManager {

    public static Molecule getMolecule(Map<Atom, Integer> ingredients) {
        for (Molecule molecule : Molecule.values()) {
            if (molecule.getIngredients().equals(ingredients)) return molecule;
        }

        return null;
    }

    public static boolean isCorrectMolecule(Molecule molecule, Atom[] atoms) {
        switch (molecule) {
            case WATER -> {
                return countBoundAtoms(atoms, Atom.OXYGEN, Atom.HYDROGEN) == 2;
            }
            case DIOXYGEN -> {
                return isDoubleAtom(atoms, Atom.OXYGEN);
            }
            case DINITROGEN -> {
                return isDoubleAtom(atoms, Atom.NITROGEN);
            }
            case SALT -> {
                return hasBoundAtom(atoms, Atom.SODIUM, Atom.CHLORINE);
            }
            case METHANE -> {
                return countBoundAtoms(atoms, Atom.CARBON, Atom.HYDROGEN) == 4;
            }

            case DIHYDROGEN -> {
                return isDoubleAtom(atoms, Atom.HYDROGEN);
            }
            case NITRIC_OXIDE -> {
                return hasBoundAtom(atoms, Atom.NITROGEN, Atom.OXYGEN);
            }
            case NITROGEN_DIOXIDE -> {
                return countBoundAtoms(atoms, Atom.NITROGEN, Atom.OXYGEN) == 2;
            }
            case SODIUM -> {
                return true;
            }
            case DICHLORINE -> {
                return isDoubleAtom(atoms, Atom.CHLORINE);
            }
            case SODIUM_HYDROXIDE -> {
                return hasBoundAtom(atoms, Atom.OXYGEN, Atom.SODIUM)
                        && hasBoundAtom(atoms, Atom.OXYGEN, Atom.HYDROGEN);
            }
            case CARBON_DIOXIDE -> {
                return countBoundAtoms(atoms, Atom.CARBON, Atom.OXYGEN) == 2;
            }
            case CARBON_MONOXIDE -> {
                return hasBoundAtom(atoms, Atom.CARBON, Atom.OXYGEN);
            }

            case NITRIC_ACID -> {
                return hasBoundAtom(atoms, Atom.HYDROGEN, Atom.OXYGEN)
                        && countBoundAtoms(atoms, Atom.NITROGEN, Atom.OXYGEN) == 3;
            }
            case AMMONIA -> {
                return countBoundAtoms(atoms, Atom.NITROGEN, Atom.HYDROGEN) == 2;
            }
            case AMMONIUM_NITRATE -> {
                return (countBoundAtoms(atoms, Atom.NITROGEN, Atom.HYDROGEN) == 4
                        && countBoundAtomsToSecond(atoms, Atom.NITROGEN, Atom.OXYGEN) == 3)
                        ||
                        (countBoundAtomsToSecond(atoms, Atom.NITROGEN, Atom.HYDROGEN) == 4
                        && countBoundAtoms(atoms, Atom.NITROGEN, Atom.OXYGEN) == 3);
            }
            case NITROUS_OXIDE -> {
                return countBoundAtoms(atoms, Atom.OXYGEN, Atom.NITROGEN) == 2;
            }
            case HYDROGEN_CLORIDE -> {
                return hasBoundAtom(atoms, Atom.HYDROGEN, Atom.CHLORINE);
            }
            case SODIUM_OXIDE -> {
                return countBoundAtoms(atoms, Atom.OXYGEN, Atom.SODIUM) == 2;
            }
            default -> {
                return false;
            }
        }
    }

    private static int countBoundAtoms(Atom[] atoms, Atom base, Atom bound) {
        int baseAtom = ArrayUtils.indexOf(atoms, base);
        if (baseAtom == -1 || ArrayUtils.indexOf(atoms, bound) == -1) return -1;
        int[] allowed = new int[]{baseAtom - 5, baseAtom - 1, baseAtom + 1, baseAtom + 5};
        BitSet bitSet = ArrayUtils.indexesOf(atoms, bound);
        int boundFound = 0;
        if (bitSet.get(allowed[0])) boundFound++;
        if (bitSet.get(allowed[1])) boundFound++;
        if (bitSet.get(allowed[2])) boundFound++;
        if (bitSet.get(allowed[3])) boundFound++;
        return boundFound;
    }
    private static int countBoundAtomsToSecond(Atom[] atoms, Atom base, Atom bound) {
        int baseAtom = ArrayUtils.indexOf(atoms, base, ArrayUtils.indexOf(atoms, base) + 1);
        if (baseAtom == -1 || ArrayUtils.indexOf(atoms, bound) == -1) return -1;
        int[] allowed = new int[]{baseAtom - 5, baseAtom - 1, baseAtom + 1, baseAtom + 5};
        BitSet bitSet = ArrayUtils.indexesOf(atoms, bound);
        int boundFound = 0;
        if (bitSet.get(allowed[0])) boundFound++;
        if (bitSet.get(allowed[1])) boundFound++;
        if (bitSet.get(allowed[2])) boundFound++;
        if (bitSet.get(allowed[3])) boundFound++;
        return boundFound;
    }
    private static boolean hasBoundAtom(Atom[] atoms, Atom base, Atom bound) {
        if (bound == null) return false;
        int baseAtom = ArrayUtils.indexOf(atoms, base);
        if (baseAtom == -1 || ArrayUtils.indexOf(atoms, bound) == -1) return false;
        try {
            return bound.equals(atoms[baseAtom - 5])
                    || bound.equals(atoms[baseAtom - 1])
                    || bound.equals(atoms[baseAtom + 1])
                    || bound.equals(atoms[baseAtom + 5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
    private static boolean isDoubleAtom(Atom[] atoms, Atom type) {
        return hasBoundAtom(atoms, type, type);
    }
}
