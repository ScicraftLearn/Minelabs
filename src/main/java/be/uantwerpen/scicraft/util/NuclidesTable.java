package be.uantwerpen.scicraft.util;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.crafting.molecules.Atom;
import be.uantwerpen.scicraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * The NucleusTable class handles everything related to the use of the real nucleus table.
 * It most importantly keeps the nuclidesTable map which contains all (stable/unstable) atoms.
 */
public class NuclidesTable {

    private static final Map<String, NucleusState> nuclidesTable = readCSV(); // map with key=nrOfProtons:nrOfNeutrons and value=nucleus_state_object
    private static final Map<Integer, Integer> shells = new HashMap<>(); // map with key=amount_of_shells and value=amount_of_electrons (corresponding)

    static { // see https://www-nds.iaea.org/relnsd/vcharthtml/VChartHTML.html for the nuclides table

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

    private static Map<String, NucleusState> readCSV() {

        Map<String, NucleusState> _nuclidesTable = new HashMap<>();

        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("../src/main/resources/data/nuclidesTable/nndc_nudat_data_export.csv"));
            int it = 0;

            while ((line = br.readLine()) != null) {
                if (it == 0) {
                    it++; continue;
                }
                String[] nuclideInfo = line.split(splitBy); // use comma as separator (see .csv file)
                String symbol = nuclideInfo[2].replaceAll("[0-9]", "");
                Atom atom = Atom.getBySymbol(symbol);
                String atomName = "";
                Item atomItem = null;
                if (atom != null) {
                    atomName = atom.name();
                    atomItem = atom.getItem();
                }
                int z = Integer.parseInt(nuclideInfo[0]);
                int n = Integer.parseInt(nuclideInfo[1]);
                String mainDecayMode = "stable";
                String halflife = "";

                if (nuclideInfo.length > 5) {
                    halflife = nuclideInfo[4];
                    mainDecayMode = nuclideInfo[5].toLowerCase();
                }
                else if (nuclideInfo.length > 4 && nuclideInfo[4].equals("STABLE")) {
                    mainDecayMode = "stable";
                }
                float _halflife = 0f;
                if (!halflife.isEmpty()) {
                    _halflife = parseHalflife(halflife);
                }
                // a, b+, b-, sf, n, p (ec = b+)
                // bn it, it, p, ep, sf, ec, b- it, bn, a, b-, it le, it ap, ec, n,
                try {
                    addNuclidesTableEntry(
                            z,
                            n,
                            atomName,
                            symbol,
                            mainDecayMode,
                            atomItem,
                            1,
                            _nuclidesTable,
                            _halflife
                    );
                }
                catch (NumberFormatException e) {
//                    e.printStackTrace();
                    Scicraft.LOGGER.info("NumberFormatException");
                }
                catch (NullPointerException e) {
                    Scicraft.LOGGER.info("NullPointerException");
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return _nuclidesTable;
    }

    /**
     * Add a nuclide to the nuclidesTable (map).
     *
     * @param z : atom number
     * @param n : amount of neutrons
     * @param atomName : String name of the atom
     * @param symbol : String name of the symbol associated with the atom
     * @param mainDecayMode : String name of the decay mode (possible options are: alpha, EC+ beta+, beta-, p, n, EC, SF, Stable)
     * @param atomItem : minecraft atom item
     * @param unstability : integer value for how far from the black line we are on the nuclides table
     */
    private static void addNuclidesTableEntry(int z, int n, String atomName, String symbol, String mainDecayMode, Item atomItem, int unstability, Map<String, NucleusState> nuclidestable, float halflife) {
        String compositeAtomKey = z + ":" + n;
        NucleusState nucleus = new NucleusState(mainDecayMode, symbol, atomName, atomItem, z, n, unstability, halflife);
        nuclidestable.put(compositeAtomKey, nucleus);
    }

    /**
     * Finds the value associated with the key ("nrOfProtons:nrOfNeutrons") in the nuclides map.
     *
     * @param nrOfProtons : amount of protons
     * @param nrOfNeutrons : amount of neutrons
     * @return : array of three values [atomName, symbol, mainDecayMode] (to see what these are, check the addNuclidesTableEntry function parameters)
     */
    public static NucleusState getNuclide(int nrOfProtons, int nrOfNeutrons) {
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
    public static String calculateIonicCharge(int nrOfProtons, int nrOfElectrons)
    {
        int ionicCharge = nrOfProtons - nrOfElectrons;
        String ionChargeString = "";
        if (nrOfProtons > nrOfElectrons) {
            ionChargeString = "+";
        }
        ionChargeString = ionChargeString + ionicCharge;
        return ionChargeString;
    }

    public static boolean isStable(int nrOfProtons, int nrOfNeutrons, int nrOfElectrons) {
        NucleusState nucleusState = getNuclide(nrOfProtons,nrOfNeutrons);
        return nucleusState != null && Math.abs(nrOfProtons - nrOfElectrons) <= 5 && nucleusState.isStable();
    }

    /**
     * Returns integer representing how far from the black line (nuclides table) the atom is.
     *
     * @param nrOfProtons : amount of protons
     * @param nrOfNeutrons : amount of neutrons
     * @param nrOfElectrons : amount of electrons
     * @return : positive integer (0 included (=stable)) representing how far from the black line (nuclides table) the atom is.
     */
    public static int getStabilityDeviation(int nrOfProtons, int nrOfNeutrons, int nrOfElectrons){
        NucleusState nucleusState = getNuclide(nrOfProtons,nrOfNeutrons);
        if (nucleusState != null) {
            return nucleusState.getUnstability();
        }
        return 0;
    }

    public static Map<String, NucleusState> getNuclidesTable() {
        return nuclidesTable;
    }

    /**
     * parses halflife number (we skip the number after the unit, not really sure what its meaning is)
     *
     * @param halflife
     * @return
     */
    public static float parseHalflife(String halflife) {
        String floatNumber = "";
        String unit = "";
        boolean isFirstNumber = false; // number before the unit
        for (int c = 0; c < halflife.length(); c++) {
            char _char = halflife.charAt(c);
            if (Character.isDigit(_char) || (_char == '.')) {
                floatNumber += _char;
                isFirstNumber = true;
            }
            else if ((_char == 'E') && isFirstNumber) {
                floatNumber += _char;
            }
            else if (Character.isLetter(_char) && isFirstNumber) {
                unit += _char;
            }
            else if (Character.isWhitespace(_char)) {
                if (!unit.isEmpty() && !(floatNumber.isEmpty())) {
                    break;
                }
            }
        }
        return convertToSeconds(floatNumber, unit);
    }

    public static float convertToSeconds(String floatNumber, String unit) {

        float multiplier = 1f;
        switch (unit) {
            case "ns":
                multiplier = (float)Math.pow(10, -9);
                break;
            case "us":
                multiplier = (float)Math.pow(10, -6);
                break;
            case "ms":
                multiplier = (float)Math.pow(10, -3);
                break;
            case "s":
                break;
            case "h":
                multiplier = 3600;
                break;
            case "d":
                multiplier = 86400;
                break;
            case "m":
                multiplier = 2629743.83f;
                break;
            case "y":
                multiplier = 31556926;
                break;
            default:

        }
        float seconds = 0;
        float power = 0;

        int indexOfE = floatNumber.indexOf('E'); // you could also use NumberFormat (with the E)
        if (indexOfE != -1) {
            power = Float.parseFloat((floatNumber.substring(indexOfE + 1)));
            seconds = Float.parseFloat((floatNumber.substring(0, indexOfE)));
        }
        else {
            if (!floatNumber.isEmpty()) {
                seconds = Float.parseFloat(floatNumber);
            }
        }
        return seconds*(float)(Math.pow(10, power))*multiplier;
    }

}




