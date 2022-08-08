package be.uantwerpen.scicraft.util;

import be.uantwerpen.scicraft.item.Items;
import net.minecraft.item.Item;

import java.util.*;

/**
 * The NucleusTable class handles everything related to the use of the real nucleus table.
 * It most importantly keeps the nuclidesTable map which contains all (stable/unstable) atoms.
 */
public class NuclidesTable {

    private static final Map<String, NucleusState> nuclidesTable = new HashMap<>(); // map with key=nrOfProtons:nrOfNeutrons and value=nucleus_state_object
    private static final Map<Integer, Integer> shells = new HashMap<>(); // map with key=amount_of_shells and value=amount_of_electrons (corresponding)

    static { // see https://www-nds.iaea.org/relnsd/vcharthtml/VChartHTML.html for the nuclides table

        addNuclidesTableEntry(0, 1, "Neutron", "n", "Unstable", null);
        addNuclidesTableEntry(0, 4, "Neutron", "n", "Unstable", null);
        addNuclidesTableEntry(0, 6, "Neutron", "n", "Unstable", null);

        addNuclidesTableEntry(1, 0, "Hydrogen", "H", "Stable", Items.HYDROGEN_ATOM );
        addNuclidesTableEntry(1, 1, "Hydrogen", "H", "Stable",Items.HYDROGEN_ATOM);
        addNuclidesTableEntry(1, 2, "Hydrogen", "H", "Unstable", Items.HYDROGEN_ATOM);
        addNuclidesTableEntry(1, 3, "Hydrogen", "H", "Unstable",Items.HYDROGEN_ATOM);
        addNuclidesTableEntry(1, 4, "Hydrogen", "H", "Unstable",Items.HYDROGEN_ATOM);
        addNuclidesTableEntry(1, 5, "Hydrogen", "H", "Unstable",Items.HYDROGEN_ATOM);
        addNuclidesTableEntry(1, 6, "Hydrogen", "H", "Unstable",Items.HYDROGEN_ATOM);

        addNuclidesTableEntry(2, 1, "Helium", "He", "Stable", Items.HELIUM);
        addNuclidesTableEntry(2, 2, "Helium", "He", "Stable", Items.HELIUM);
        addNuclidesTableEntry(2, 3, "Helium", "He", "Unstable", Items.HELIUM);
        addNuclidesTableEntry(2, 4, "Helium", "He", "Unstable", Items.HELIUM);
        addNuclidesTableEntry(2, 5, "Helium", "He", "Unstable", Items.HELIUM);
        addNuclidesTableEntry(2, 6, "Helium", "He", "Unstable", Items.HELIUM);
        addNuclidesTableEntry(2, 7, "Helium", "He", "Unstable", Items.HELIUM);
        addNuclidesTableEntry(2, 8, "Helium", "He", "Unstable", Items.HELIUM);

        addNuclidesTableEntry(3, 1, "Lithium", "Li", "Unstable", Items.LITHIUM_ATOM);
        addNuclidesTableEntry(3, 2, "Lithium", "Li", "Unstable", Items.LITHIUM_ATOM);
        addNuclidesTableEntry(3, 3, "Lithium", "Li", "Stable", Items.LITHIUM_ATOM);
        addNuclidesTableEntry(3, 4, "Lithium", "Li", "Stable", Items.LITHIUM_ATOM);
        addNuclidesTableEntry(3, 5, "Lithium", "Li", "Unstable", Items.LITHIUM_ATOM);
        addNuclidesTableEntry(3, 6, "Lithium", "Li", "Unstable", Items.LITHIUM_ATOM);
        addNuclidesTableEntry(3, 7, "Lithium", "Li", "Unstable", Items.LITHIUM_ATOM);
        addNuclidesTableEntry(3, 8, "Lithium", "Li", "Unstable", Items.LITHIUM_ATOM);
        addNuclidesTableEntry(3, 9, "Lithium", "Li", "Unstable", Items.LITHIUM_ATOM);

        addNuclidesTableEntry(4, 2, "Beryllium", "Be", "Unstable", Items.BERYLLIUM_ATOM);
        addNuclidesTableEntry(4, 3, "Beryllium", "Be", "Unstable", Items.BERYLLIUM_ATOM);
        addNuclidesTableEntry(4, 4, "Beryllium", "Be", "Unstable", Items.BERYLLIUM_ATOM);
        addNuclidesTableEntry(4, 5, "Beryllium", "Be", "Stable", Items.BERYLLIUM_ATOM);
        addNuclidesTableEntry(4, 6, "Beryllium", "Be", "Unstable", Items.BERYLLIUM_ATOM);
        addNuclidesTableEntry(4, 7, "Beryllium", "Be", "Unstable", Items.BERYLLIUM_ATOM);
        addNuclidesTableEntry(4, 8, "Beryllium", "Be", "Unstable", Items.BERYLLIUM_ATOM);
        addNuclidesTableEntry(4, 9, "Beryllium", "Be", "Unstable", Items.BERYLLIUM_ATOM);
        addNuclidesTableEntry(4, 10, "Beryllium", "Be", "Unstable", Items.BERYLLIUM_ATOM);
        addNuclidesTableEntry(4, 11, "Beryllium", "Be", "Unstable", Items.BERYLLIUM_ATOM);
        addNuclidesTableEntry(4, 12, "Beryllium", "Be", "Unstable", Items.BERYLLIUM_ATOM);

        addNuclidesTableEntry(5, 2, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 3, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 4, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 5, "Boron", "B", "Stable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 6, "Boron", "B", "Stable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 7, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 8, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 9, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 10, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 11, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 12, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 13, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 14, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 15, "Boron", "B", "Unstable", Items.BORON_ATOM);
        addNuclidesTableEntry(5, 16, "Boron", "B", "Unstable", Items.BORON_ATOM);

        addNuclidesTableEntry(6, 2, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 3, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 4, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 5, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 6, "Carbon", "C", "Stable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 7, "Carbon", "C", "Stable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 8, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 9, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 10, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 11, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 12, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 13, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 14, "Carbon", "C", "Unstable", Items.CARBON_ATOM);
        addNuclidesTableEntry(6, 16, "Carbon", "C", "Unstable", Items.CARBON_ATOM);

        addNuclidesTableEntry(7, 3, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 4, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 5, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 6, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 7, "Nitrogen", "N", "Stable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 8, "Nitrogen", "N", "Stable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 9, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 10, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 11, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 12, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 13, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 14, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 15, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 16, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);
        addNuclidesTableEntry(7, 17, "Nitrogen", "N", "Unstable", Items.NITROGEN_ATOM);

        addNuclidesTableEntry(8, 3, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 4, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 5, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 6, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 7, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 8, "Oxygen", "O", "Stable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 9, "Oxygen", "O", "Stable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 10, "Oxygen", "O", "Stable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 11, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 12, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 13, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 14, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 15, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 16, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);
        addNuclidesTableEntry(8, 18, "Oxygen", "O", "Unstable", Items.OXYGEN_ATOM);

        addNuclidesTableEntry(9, 5, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 6, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 7, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 8, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 9, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 10, "Fluorine", "F", "Stable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 11, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 12, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 13, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 14, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 15, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 16, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 17, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 18, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 19, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 20, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 21, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);
        addNuclidesTableEntry(9, 22, "Fluorine", "F", "Unstable", Items.FLUORINE_ATOM);

        addNuclidesTableEntry(10, 5, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 6, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 7, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 8, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 9, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 10, "Neon", "Ne", "Stable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 11, "Neon", "Ne", "Stable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 12, "Neon", "Ne", "Stable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 13, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 14, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 15, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 16, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 17, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 18, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 19, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 20, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 21, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 22, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 23, "Neon", "Ne", "Unstable",Items.NEON_ATOM);
        addNuclidesTableEntry(10, 24, "Neon", "Ne", "Unstable",Items.NEON_ATOM);

        addNuclidesTableEntry(11, 6, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 7, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 8, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 9, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 10, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 11, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 12, "Sodium", "Na", "Stable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 13, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 14, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 15, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 16, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 17, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 18, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 19, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 20, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 21, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 22, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 23, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 24, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 25, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);
        addNuclidesTableEntry(11, 26, "Sodium", "Na", "Unstable",Items.SODIUM_ATOM);

        addNuclidesTableEntry(12, 7, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 8, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 9, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 10, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 11, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 12, "Magnesium", "Mg", "Stable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 13, "Magnesium", "Mg", "Stable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 14, "Magnesium", "Mg", "Stable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 15, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 16, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 17, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 18, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 19, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 20, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 21, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 22, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 23, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 24, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 25, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 26, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 27, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);
        addNuclidesTableEntry(12, 28, "Magnesium", "Mg", "Unstable",Items.MAGNESIUM_ATOM);

        addNuclidesTableEntry(13, 8, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 9, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 10, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 11, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 12, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 13, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 14, "Aluminium", "Al", "Stable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 15, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 16, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 17, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 18, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 19, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 20, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 21, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 22, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 23, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 24, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 25, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 26, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 27, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 28, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 29, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);
        addNuclidesTableEntry(13, 30, "Aluminium", "Al", "Unstable",Items.ALUMINIUM_ATOM);

        addNuclidesTableEntry(14, 8, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 9, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 10, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 11, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 12, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 13, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 14, "Silicon", "Si", "Stable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 15, "Silicon", "Si", "Stable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 16, "Silicon", "Si", "Stable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 17, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 18, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 19, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 20, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 21, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 22, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 23, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 24, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 25, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 26, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 27, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 28, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 29, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);
        addNuclidesTableEntry(14, 30, "Silicon", "Si", "Unstable",Items.SILICON_ATOM);

        addNuclidesTableEntry(15, 10, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 11, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 12, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 13, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 14, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 15, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 16, "Phosphorus", "P", "Stable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 17, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 18, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 19, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 20, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 21, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 22, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 23, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 24, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 25, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 26, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 27, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 28, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 29, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 30, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);
        addNuclidesTableEntry(15, 31, "Phosphorus", "P", "Unstable", Items.PHOSPHORUS_ATOM);

        addNuclidesTableEntry(16, 10, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 11, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 12, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 13, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 14, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 15, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 16, "Sulfur", "S", "Stable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 17, "Sulfur", "S", "Stable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 18, "Sulfur", "S", "Stable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 19, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 20, "Sulfur", "S", "Stable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 21, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 22, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 23, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 24, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 25, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 26, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 27, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 28, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 29, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 30, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);
        addNuclidesTableEntry(16, 32, "Sulfur", "S", "Unstable",Items.SULFUR_ATOM);

        addNuclidesTableEntry(17, 12, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 13, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 14, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 15, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 16, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 17, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 18, "Chlorine", "Cl", "Stable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 19, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 20, "Chlorine", "Cl", "Stable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 21, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 22, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 23, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 24, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 25, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 26, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 27, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 28, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 29, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 30, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 31, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 32, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 33, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);
        addNuclidesTableEntry(17, 34, "Chlorine", "Cl", "Unstable", Items.CHLORINE_ATOM);

        addNuclidesTableEntry(18, 12, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 13, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 14, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 15, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 16, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 17, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 18, "Argon", "Ar", "Stable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 19, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 20, "Argon", "Ar", "Stable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 21, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 22, "Argon", "Ar", "Stable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 23, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 24, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 25, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 26, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 27, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 28, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 29, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 30, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 31, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 32, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 33, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 34, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);
        addNuclidesTableEntry(18, 35, "Argon", "Ar", "Unstable", Items.ARGON_ATOM);

        addNuclidesTableEntry(19, 14, "Potassium", "K", "Unstable", null); // TODO: change atomItem when added
        addNuclidesTableEntry(19, 15, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 16, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 17, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 18, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 19, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 20, "Potassium", "K", "Stable", null);
        addNuclidesTableEntry(19, 21, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 22, "Potassium", "K", "Stable", null);
        addNuclidesTableEntry(19, 23, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 24, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 25, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 26, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 27, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 28, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 29, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 30, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 31, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 32, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 33, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 34, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 35, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 36, "Potassium", "K", "Unstable", null);
        addNuclidesTableEntry(19, 37, "Potassium", "K", "Unstable", null);

        // define electron shells map
        shells.put(1, 2);
        shells.put(2, 8);
        shells.put(3, 18);
        shells.put(4, 32);
        shells.put(5, 50);
        shells.put(6, 72);
        shells.put(7, 98);
        shells.put(8, 128);
        shells.put(9, 162);

    }

    /**
     * Default Constructor of the NuclidesTable
     */
    public NuclidesTable() {}

    /**
     * Add a nuclide to the nuclidesTable (map).
     *
     * @param z : atom number
     * @param n : amount of neutrons
     * @param atomName : String name of the atom
     * @param symbol : String name of the symbol associated with the atom
     * @param mainDecayMode : String name of the decay mode (possible options are: alpha, EC+ beta+, beta-, p, n, EC, SF, Stable)
     * @param atomItem : minecraft atom item
     */
    private static void addNuclidesTableEntry(int z, int n, String atomName, String symbol, String mainDecayMode, Item atomItem) {
        String compositeAtomKey = z + ":" + n;
        NucleusState nucleus = new NucleusState(mainDecayMode, symbol, atomName, atomItem, z, n);
        nuclidesTable.put(compositeAtomKey, nucleus);
    }

    /**
     * Finds the value associated with the key ("nrOfProtons:nrOfNeutrons") in the nuclides map.
     *
     * @param nrOfProtons : amount of protons
     * @param nrOfNeutrons : amount of neutrons
     * @return : array of three values [atomName, symbol, mainDecayMode] (to see what these are, check the addNuclidesTableEntry function parameters)
     */
    public NucleusState getNuclide(int nrOfProtons, int nrOfNeutrons) {
        String compositeAtomKey = nrOfProtons + ":" + nrOfNeutrons;
        NucleusState value = nuclidesTable.get(compositeAtomKey);
        if (value != null) {
            return nuclidesTable.get(compositeAtomKey);
        }
        else {
            return null;
        }

    }

    /**
     * Calculates the number of electrons given the shell number.
     *
     * @param shell : shell number
     * @return : integer amount of electrons
     */
    public static int calculateNrOfElectrons(int shell) {
        for (Map.Entry<Integer, Integer> entry : shells.entrySet()) {
            if (shell == entry.getKey()) {
                return entry.getValue();
            }
        }
        return 0;
    }

    /**
     * Calculates the ionic charge of an item given the amount of protons and electrons
     *
     * @param nrOfProtons : amount of protons
     * @param nrOfElectrons : amount of electrons
     * @return : String representation of the charge. The initial form is +/- x (where x is any number).
     */
    public String calculateIonicCharge(int nrOfProtons, int nrOfElectrons)
    {
        int ionicCharge = nrOfProtons - nrOfElectrons;
        String ionChargeString = "";
        if (nrOfProtons > nrOfElectrons) {
            ionChargeString = "+";
        }
        ionChargeString = ionChargeString + ionicCharge;
        return ionChargeString;
    }

}




