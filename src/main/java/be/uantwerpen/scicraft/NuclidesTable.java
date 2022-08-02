package be.uantwerpen.scicraft;

import java.util.*;

public class NuclidesTable { // TODO: decaymode overal deftig aanpassen, maar daar moet ik info over vragen

    private static final Map<String, ArrayList<String>> nuclidesTable = new HashMap<>();
    private static final Map<Integer, Integer> shells = new HashMap<>();

    static { // https://www-nds.iaea.org/relnsd/vcharthtml/VChartHTML.html

        addNuclidesTableEntry(0, 1, "Neutron", "n", "beta-");
        addNuclidesTableEntry(0, 4, "Neutron", "n", "");
        addNuclidesTableEntry(0, 6, "Neutron", "n", "");

        addNuclidesTableEntry(1, 0, "Hydrogen", "H", "Stable");
        addNuclidesTableEntry(1, 1, "Hydrogen", "H", "Stable");
        addNuclidesTableEntry(1, 2, "Hydrogen", "H", "beta-");
        addNuclidesTableEntry(1, 3, "Hydrogen", "H", "n");
        addNuclidesTableEntry(1, 4, "Hydrogen", "H", "n");
        addNuclidesTableEntry(1, 5, "Hydrogen", "H", "");
        addNuclidesTableEntry(1, 6, "Hydrogen", "H", "");

        addNuclidesTableEntry(2, 1, "Helium", "He", "Stable");
        addNuclidesTableEntry(2, 2, "Helium", "He", "Stable");
        addNuclidesTableEntry(2, 3, "Helium", "He", "n");
        addNuclidesTableEntry(2, 4, "Helium", "He", "beta-");
        addNuclidesTableEntry(2, 5, "Helium", "He", "n");
        addNuclidesTableEntry(2, 6, "Helium", "He", "beta-");
        addNuclidesTableEntry(2, 7, "Helium", "He", "n");
        addNuclidesTableEntry(2, 8, "Helium", "He", "n");

        addNuclidesTableEntry(3, 1, "Lithium", "Li", "mainDecayMode");
        addNuclidesTableEntry(3, 2, "Lithium", "Li", "mainDecayMode");
        addNuclidesTableEntry(3, 3, "Lithium", "Li", "Stable");
        addNuclidesTableEntry(3, 4, "Lithium", "Li", "Stable");
        addNuclidesTableEntry(3, 5, "Lithium", "Li", "mainDecayMode");
        addNuclidesTableEntry(3, 6, "Lithium", "Li", "mainDecayMode");
        addNuclidesTableEntry(3, 7, "Lithium", "Li", "mainDecayMode");
        addNuclidesTableEntry(3, 8, "Lithium", "Li", "mainDecayMode");
        addNuclidesTableEntry(3, 9, "Lithium", "Li", "mainDecayMode");

        addNuclidesTableEntry(4, 2, "Beryllium", "Be", "mainDecayMode");
        addNuclidesTableEntry(4, 3, "Beryllium", "Be", "mainDecayMode");
        addNuclidesTableEntry(4, 4, "Beryllium", "Be", "mainDecayMode");
        addNuclidesTableEntry(4, 5, "Beryllium", "Be", "Stable");
        addNuclidesTableEntry(4, 6, "Beryllium", "Be", "mainDecayMode");
        addNuclidesTableEntry(4, 7, "Beryllium", "Be", "mainDecayMode");
        addNuclidesTableEntry(4, 8, "Beryllium", "Be", "mainDecayMode");
        addNuclidesTableEntry(4, 9, "Beryllium", "Be", "mainDecayMode");
        addNuclidesTableEntry(4, 10, "Beryllium", "Be", "mainDecayMode");
        addNuclidesTableEntry(4, 11, "Beryllium", "Be", "mainDecayMode");
        addNuclidesTableEntry(4, 12, "Beryllium", "Be", "mainDecayMode");

        addNuclidesTableEntry(5, 2, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 3, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 4, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 5, "Boron", "B", "Stable");
        addNuclidesTableEntry(5, 6, "Boron", "B", "Stable");
        addNuclidesTableEntry(5, 7, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 8, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 9, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 10, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 11, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 12, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 13, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 14, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 15, "Boron", "B", "mainDecayMode");
        addNuclidesTableEntry(5, 16, "Boron", "B", "mainDecayMode");

        addNuclidesTableEntry(6, 2, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 3, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 4, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 5, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 6, "Carbon", "C", "Stable");
        addNuclidesTableEntry(6, 7, "Carbon", "C", "Stable");
        addNuclidesTableEntry(6, 8, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 9, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 10, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 11, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 12, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 13, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 14, "Carbon", "C", "mainDecayMode");
        addNuclidesTableEntry(6, 16, "Carbon", "C", "mainDecayMode");

        addNuclidesTableEntry(7, 3, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 4, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 5, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 6, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 7, "Nitrogen", "N", "Stable");
        addNuclidesTableEntry(7, 8, "Nitrogen", "N", "Stable");
        addNuclidesTableEntry(7, 9, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 10, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 11, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 12, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 13, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 14, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 15, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 16, "Nitrogen", "N", "mainDecayMode");
        addNuclidesTableEntry(7, 17, "Nitrogen", "N", "mainDecayMode");

        addNuclidesTableEntry(8, 3, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 4, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 5, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 6, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 7, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 8, "Oxygen", "O", "Stable");
        addNuclidesTableEntry(8, 9, "Oxygen", "O", "Stable");
        addNuclidesTableEntry(8, 10, "Oxygen", "O", "Stable");
        addNuclidesTableEntry(8, 11, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 12, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 13, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 14, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 15, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 16, "Oxygen", "O", "mainDecayMode");
        addNuclidesTableEntry(8, 18, "Oxygen", "O", "mainDecayMode");

        addNuclidesTableEntry(9, 5, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 6, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 7, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 8, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 9, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 10, "Fluorine", "F", "Stable");
        addNuclidesTableEntry(9, 11, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 12, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 13, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 14, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 15, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 16, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 17, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 18, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 19, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 20, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 21, "Fluorine", "F", "mainDecayMode");
        addNuclidesTableEntry(9, 22, "Fluorine", "F", "mainDecayMode");

        addNuclidesTableEntry(10, 5, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 6, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 7, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 8, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 9, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 10, "Neon", "Ne", "Stable");
        addNuclidesTableEntry(10, 11, "Neon", "Ne", "Stable");
        addNuclidesTableEntry(10, 12, "Neon", "Ne", "Stable");
        addNuclidesTableEntry(10, 13, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 14, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 15, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 16, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 17, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 18, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 19, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 20, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 21, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 22, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 23, "Neon", "Ne", "mainDecayMode");
        addNuclidesTableEntry(10, 24, "Neon", "Ne", "mainDecayMode");

        addNuclidesTableEntry(11, 6, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 7, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 8, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 9, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 10, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 11, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 12, "Sodium", "Na", "Stable");
        addNuclidesTableEntry(11, 13, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 14, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 15, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 16, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 17, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 18, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 19, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 20, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 21, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 22, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 23, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 24, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 25, "Sodium", "Na", "mainDecayMode");
        addNuclidesTableEntry(11, 26, "Sodium", "Na", "mainDecayMode");

        addNuclidesTableEntry(12, 7, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 8, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 9, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 10, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 11, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 12, "Magnesium", "Mg", "Stable");
        addNuclidesTableEntry(12, 13, "Magnesium", "Mg", "Stable");
        addNuclidesTableEntry(12, 14, "Magnesium", "Mg", "Stable");
        addNuclidesTableEntry(12, 15, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 16, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 17, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 18, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 19, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 20, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 21, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 22, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 23, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 24, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 25, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 26, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 27, "Magnesium", "Mg", "mainDecayMode");
        addNuclidesTableEntry(12, 28, "Magnesium", "Mg", "mainDecayMode");

        addNuclidesTableEntry(13, 8, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 9, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 10, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 11, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 12, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 13, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 14, "Aluminium", "Al", "Stable");
        addNuclidesTableEntry(13, 15, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 16, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 17, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 18, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 19, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 20, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 21, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 22, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 23, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 24, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 25, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 26, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 27, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 28, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 29, "Aluminium", "Al", "mainDecayMode");
        addNuclidesTableEntry(13, 30, "Aluminium", "Al", "mainDecayMode");

        addNuclidesTableEntry(14, 8, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 9, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 10, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 11, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 12, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 13, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 14, "Silicon", "Si", "Stable");
        addNuclidesTableEntry(14, 15, "Silicon", "Si", "Stable");
        addNuclidesTableEntry(14, 16, "Silicon", "Si", "Stable");
        addNuclidesTableEntry(14, 17, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 18, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 19, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 20, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 21, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 22, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 23, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 24, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 25, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 26, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 27, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 28, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 29, "Silicon", "Si", "mainDecayMode");
        addNuclidesTableEntry(14, 30, "Silicon", "Si", "mainDecayMode");

        addNuclidesTableEntry(15, 10, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 11, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 12, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 13, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 14, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 15, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 16, "Phosphorus", "P", "Stable");
        addNuclidesTableEntry(15, 17, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 18, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 19, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 20, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 21, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 22, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 23, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 24, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 25, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 26, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 27, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 28, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 29, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 30, "Phosphorus", "P", "mainDecayMode");
        addNuclidesTableEntry(15, 31, "Phosphorus", "P", "mainDecayMode");

        addNuclidesTableEntry(16, 10, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 11, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 12, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 13, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 14, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 15, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 16, "Sulfur", "S", "Stable");
        addNuclidesTableEntry(16, 17, "Sulfur", "S", "Stable");
        addNuclidesTableEntry(16, 18, "Sulfur", "S", "Stable");
        addNuclidesTableEntry(16, 19, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 20, "Sulfur", "S", "Stable");
        addNuclidesTableEntry(16, 21, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 22, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 23, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 24, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 25, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 26, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 27, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 28, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 29, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 30, "Sulfur", "S", "mainDecayMode");
        addNuclidesTableEntry(16, 32, "Sulfur", "S", "mainDecayMode");

        addNuclidesTableEntry(17, 12, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 13, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 14, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 15, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 16, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 17, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 18, "Chlorine", "Cl", "Stable");
        addNuclidesTableEntry(17, 19, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 20, "Chlorine", "Cl", "Stable");
        addNuclidesTableEntry(17, 21, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 22, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 23, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 24, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 25, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 26, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 27, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 28, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 29, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 30, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 31, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 32, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 33, "Chlorine", "Cl", "mainDecayMode");
        addNuclidesTableEntry(17, 34, "Chlorine", "Cl", "mainDecayMode");

        addNuclidesTableEntry(18, 12, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 13, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 14, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 15, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 16, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 17, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 18, "Argon", "Ar", "Stable");
        addNuclidesTableEntry(18, 19, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 20, "Argon", "Ar", "Stable");
        addNuclidesTableEntry(18, 21, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 22, "Argon", "Ar", "Stable");
        addNuclidesTableEntry(18, 23, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 24, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 25, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 26, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 27, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 28, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 29, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 30, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 31, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 32, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 33, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 34, "Argon", "Ar", "mainDecayMode");
        addNuclidesTableEntry(18, 35, "Argon", "Ar", "mainDecayMode");

        addNuclidesTableEntry(19, 14, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 15, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 16, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 17, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 18, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 19, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 20, "Potassium", "K", "Stable");
        addNuclidesTableEntry(19, 21, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 22, "Potassium", "K", "Stable");
        addNuclidesTableEntry(19, 23, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 24, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 25, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 26, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 27, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 28, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 29, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 30, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 31, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 32, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 33, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 34, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 35, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 36, "Potassium", "K", "mainDecayMode");
        addNuclidesTableEntry(19, 37, "Potassium", "K", "mainDecayMode");

        shells.put(1, 2);
        shells.put(2, 8);
        shells.put(3, 18);
        shells.put(4, 32);

    }


    public NuclidesTable() {}

    /**
     * Add a nuclide to the nuclidesTable (map).
     *
     * @param z : atom number
     * @param n : amount of neutrons
     * @param atomName : String name of the atom
     * @param symbol : String name of the symbol associated with the atom
     * @param mainDecayMode : String name of the decay mode (possible options are: alpha, EC+ beta+, beta-, p, n, EC, SF, Stable)
     * @return
     */
    private static void addNuclidesTableEntry(int z, int n, String atomName, String symbol, String mainDecayMode) {
        String compositeAtomKey = z + ":" + n;
        nuclidesTable.put(compositeAtomKey, new ArrayList<String>(Arrays.asList(atomName, symbol, mainDecayMode)));
    }

    /**
     * Finds the value associated with the key ("nrOfProtons:nrOfNeutrons") in the nuclides map.
     *
     * @param nrOfProtons : amount of protons
     * @param nrOfNeutrons : amount of neutrons
     * @return : array of three values [atomName, symbol, mainDecayMode] (to see what these are, check the addNuclidesTableEntry function parameters)
     */
    public ArrayList<String> getNuclide(int nrOfProtons, int nrOfNeutrons) {
        String compositeAtomKey = nrOfProtons + ":" + nrOfNeutrons;
        return nuclidesTable.get(compositeAtomKey);
    }

    /**
     * Calculates the number of shells given the amount of electrons.
     *
     * @param nrOfElectrons : amount of electrons
     * @return : amount of shells (key of dictionary) according to the amount of electrons
     */
    public int calculateNrOfShells(int nrOfElectrons) {
        for (Map.Entry<Integer, Integer> entry : shells.entrySet()) {
            if (nrOfElectrons <= entry.getValue()) {
                return entry.getKey();
            }
        }
        return 0;
    }

    public int calculateIonicCharge(int nrOfProtons, int nrOfElectrons) {
        return nrOfProtons - nrOfElectrons;
    }

}




