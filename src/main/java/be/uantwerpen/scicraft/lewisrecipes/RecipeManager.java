//package be.uantwerpen.scicraft.lewisrecipes;
//
//import com.mojang.datafixers.util.Pair;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Map;
//
//public class RecipeManager {
//
//    @Nullable
//    public static Molecule getMolecule(Map<Atom, Integer> ingredients) {
//        for (Molecule molecule : Molecule.values()) {
//            if (molecule.getIngredients().equals(ingredients)) return molecule;
//        }
//        return null;
//    }
//
//    public static boolean isCorrectMolecule(@NotNull Molecule molecule, Atom[][] atoms) {
//        switch (molecule) {
//            case WATER -> {
//                return countBoundAtoms(atoms, Atom.OXYGEN, Atom.HYDROGEN) == 2;
//            }
//            case DIOXYGEN -> {
//                return isDoubleAtom(atoms, Atom.OXYGEN);
//            }
//            case DINITROGEN -> {
//                return isDoubleAtom(atoms, Atom.NITROGEN);
//            }
//            case SALT -> {
//                return hasBoundAtom(atoms, Atom.SODIUM, Atom.CHLORINE);
//            }
//            case METHANE -> {
//                return countBoundAtoms(atoms, Atom.CARBON, Atom.HYDROGEN) == 4;
//            }
//
//            case DIHYDROGEN -> {
//                return isDoubleAtom(atoms, Atom.HYDROGEN);
//            }
//            case NITRIC_OXIDE -> {
//                return hasBoundAtom(atoms, Atom.NITROGEN, Atom.OXYGEN);
//            }
//            case NITROGEN_DIOXIDE -> {
//                return countBoundAtoms(atoms, Atom.NITROGEN, Atom.OXYGEN) == 2;
//            }
//            case SODIUM -> {
//                return true;
//            }
//            case DICHLORINE -> {
//                return isDoubleAtom(atoms, Atom.CHLORINE);
//            }
//            case SODIUM_HYDROXIDE -> {
//                return hasBoundAtom(atoms, Atom.OXYGEN, Atom.SODIUM)
//                        && hasBoundAtom(atoms, Atom.OXYGEN, Atom.HYDROGEN);
//            }
//            case CARBON_DIOXIDE -> {
//                return countBoundAtoms(atoms, Atom.CARBON, Atom.OXYGEN) == 2;
//            }
//            case CARBON_MONOXIDE -> {
//                return hasBoundAtom(atoms, Atom.CARBON, Atom.OXYGEN);
//            }
//
//            case NITRIC_ACID -> {
//                return hasBoundAtom(atoms, Atom.HYDROGEN, Atom.OXYGEN)
//                        && countBoundAtoms(atoms, Atom.NITROGEN, Atom.OXYGEN) == 3;
//            }
//            case AMMONIA -> {
//                return countBoundAtoms(atoms, Atom.NITROGEN, Atom.HYDROGEN) == 3;
//            }
//            case AMMONIUM_NITRATE -> {
//                return (countBoundAtoms(atoms, Atom.NITROGEN, Atom.HYDROGEN) == 4
//                        && countBoundAtoms(atoms, Atom.NITROGEN, Atom.OXYGEN, 1) == 3)
//                        ||
//                        (countBoundAtoms(atoms, Atom.NITROGEN, Atom.HYDROGEN, 1) == 4
//                                && countBoundAtoms(atoms, Atom.NITROGEN, Atom.OXYGEN) == 3);
//            }
//            case NITROUS_OXIDE -> {
//                return countBoundAtoms(atoms, Atom.OXYGEN, Atom.NITROGEN) == 2;
//            }
//            case HYDROGEN_CHLORIDE -> {
//                return hasBoundAtom(atoms, Atom.HYDROGEN, Atom.CHLORINE);
//            }
//            case SODIUM_OXIDE -> {
//                return countBoundAtoms(atoms, Atom.OXYGEN, Atom.SODIUM) == 2;
//            }
//            default -> {
//                return false;
//            }
//        }
//    }
//
//    @Contract(pure = true)
//    private static boolean isAtom(@NotNull Atom[][] atoms, int i, int j, @NotNull Atom atom) {
//        try {
//            return atom.equals(atoms[i][j]);
//        } catch (ArrayIndexOutOfBoundsException e) {
//            return false;
//        }
//    }
//
//    @Contract("_, _ -> new")
//    @NotNull
//    private static Pair<Integer, Integer> findAtom(Atom[][] atoms, Atom toFind) {
//        return findAtom(atoms, toFind, 0);
//    }
//
//    @Contract("_, _, _ -> new")
//    @NotNull
//    private static Pair<Integer, Integer> findAtom(Atom[][] atoms, @NotNull Atom toFind, int atomsToSkip) {
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 5; j++) {
//                if (toFind.equals(atoms[i][j])) {
//                    if (atomsToSkip > 0) atomsToSkip--;
//                    else return new Pair<>(i, j);
//                }
//            }
//        }
//        return new Pair<>(-1, -1);
//    }
//
//    private static int countBoundAtoms(Atom[][] atoms, Atom base, Atom bound) {
//        return countBoundAtoms(atoms, base, bound, 0);
//    }
//
//    private static int countBoundAtoms(Atom[][] atoms, Atom base, Atom bound, int baseAtomsToSkip) {
//        int baseX, baseY;
//        Pair<Integer, Integer> baseFind = findAtom(atoms, base, baseAtomsToSkip);
//        baseX = baseFind.getFirst();
//        baseY = baseFind.getSecond();
//        if (baseX == -1 || baseY == -1) return -1;
//        int boundFound = 0;
//        if (isAtom(atoms, baseX - 1, baseY, bound)) boundFound++;
//        if (isAtom(atoms, baseX + 1, baseY, bound)) boundFound++;
//        if (isAtom(atoms, baseX, baseY - 1, bound)) boundFound++;
//        if (isAtom(atoms, baseX, baseY + 1, bound)) boundFound++;
//        return boundFound;
//    }
//
//    private static boolean hasBoundAtom(Atom[][] atoms, Atom base, Atom bound) {
//        int baseX, baseY;
//        Pair<Integer, Integer> baseFind = findAtom(atoms, base);
//        baseX = baseFind.getFirst();
//        baseY = baseFind.getSecond();
//        if (baseX == -1 || baseY == -1) return false;
//        return isAtom(atoms, baseX - 1, baseY, bound)
//                || isAtom(atoms, baseX + 1, baseY, bound)
//                || isAtom(atoms, baseX, baseY - 1, bound)
//                || isAtom(atoms, baseX, baseY + 1, bound);
//    }
//
//    private static boolean isDoubleAtom(Atom[][] atoms, Atom type) {
//        return hasBoundAtom(atoms, type, type);
//    }
//}
