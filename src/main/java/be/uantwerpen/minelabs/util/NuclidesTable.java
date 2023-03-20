package be.uantwerpen.minelabs.util;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.crafting.molecules.Atom;
import net.minecraft.item.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * The NucleusTable class handles everything related to the use of the real nucleus table.
 * It most importantly keeps the nuclidesTable map which contains all (stable/unstable) atoms.
 *
 * You can change the working of this by writing something new, however, if you wish to change certain things, here's a little guide:
 *
 * The CSV that is read for the nuclides table is currently located at : src/main/resources/data/nuclidesTable/nndc_nudat_data_export.csv
 * Should you change the CSV, don't forget to add the line "0,0,Void,0.0,STABLE," under the header.
 * The parseNuclideInfo is used inside the readCSV() function and handles the parsing (of each line in the file).
 * The halflifeRanges variable has specific halflife ranges that are associated with certain values, in this case a shake multiplier and timer, both used in the bohrplate rendering function (BohrBlockEntityRenderer.java).
 * The parseHalflife and convertToSeconds functions are both for converting the CSV halflife column value to a useful value for us.
 *
 * This class works together well with the NucleusState class (located in the same folder (see NucleusState.java)).
 * When we make an atom, we create a NucleusState object provided with the atom's information.
 *
 * If you want to parse the decay modes, which isn't being done right now, you can do so by going to the parseNuclideInfo function,
 * getting the mainDecayMode variable (it is already declared inside the function and assigned (a unique decay mode if it is present in the CSV))
 * and pass it to a parse method of your making, before you add it to the nucleus in question.
 */
public class NuclidesTable {

    private static final LinkedHashMap<Float, ArrayList<Float>> halflifeRanges = makeHalflifeRanges(); // map with key=halflife and value=[multiplier,timer]
    private static final Map<String, NucleusState> nuclidesTable = readCSV(); // map with key=nrOfProtons:nrOfNeutrons and value=nucleus_state_object
    private static final Map<Integer, Integer> shells = new HashMap<>(); // map with key=amount_of_shells and value=amount_of_electrons (corresponding)

    static { // see https://www.nndc.bnl.gov/nudat3/ for the nuclides table

        // define electron shells map (source: wikipedia)
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
     * Reads CSV file containing nuclides table data
     *
     * @return : own nuclides table map
     */
    private static Map<String, NucleusState> readCSV() {

        Map<String, NucleusState> _nuclidesTable = new HashMap<>();

        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new InputStreamReader(Minelabs.csvFile));
            int it = 0;
            while ((line = br.readLine()) != null) {
                if (it == 0) {it++; continue;}
                String[] nuclideInfo = line.split(splitBy); // use comma as separator (see .csv file)
                parseNuclideInfo(nuclideInfo, _nuclidesTable);
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return _nuclidesTable;
    }

    /**
     * Adds a nuclide (see NucleusState class) to the nuclidesTable (map).
     *
     * @param z : atom number
     * @param n : amount of neutrons
     * @param atomName : String name of the atom
     * @param symbol : String name of the symbol associated with the atom
     * @param mainDecayModes : Array of decay modes
     * @param atomItem : minecraft atom item
     * @param unstability : float value for how far from the black line we are on the nuclides table
     * @param nuclidestable : nuclides table map (by reference)
     * @param halflife : halflife value of the atom
     */
    private static void addNuclidesTableEntry(int z, int n, String atomName, String symbol, ArrayList<String> mainDecayModes, Item atomItem, float unstability, Map<String, NucleusState> nuclidestable, float halflife) {
        String compositeAtomKey = z + ":" + n;
        NucleusState nucleus = new NucleusState(mainDecayModes, symbol, atomName, atomItem, z, n, unstability, halflife);
        nuclidestable.put(compositeAtomKey, nucleus);
    }

    /**
     * Finds the value associated with the composite key ("nrOfProtons:nrOfNeutrons") in the nuclides map.
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
     * Calculates the number of electrons given the shell number (shell number range = [1,9]).
     *
     * @param shell : shell number
     * @return : (integer) amount of electrons
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
        } else if (nrOfProtons < nrOfElectrons){
            ionChargeString = "-";
        }
        ionChargeString = Math.abs(ionicCharge) + ionChargeString;
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
    public static float getStabilityDeviation(int nrOfProtons, int nrOfNeutrons, int nrOfElectrons){
        NucleusState nucleusState = getNuclide(nrOfProtons,nrOfNeutrons);
        if (nucleusState != null) {
            return nucleusState.getUnstability();
        }
        return -1; // nucleusState = null => non-existing atom
    }

    public static Map<String, NucleusState> getNuclidesTable() {
        return nuclidesTable;
    }

    public static LinkedHashMap<Float, ArrayList<Float>> getHalflifeRanges() {
        return halflifeRanges;
    }

    /**
     * parses halflife number (we skip the number after the unit, not really sure what its meaning is)
     *
     * @param halflife : string representation of the halflife (number)
     * @return : halflife expressed in amount of seconds (float)
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

    /**
     * Used to convert a (string) float number with unit (see CSV) to (float) seconds
     *
     * @param floatNumber : string representation of float number
     * @param unit : string representation of the unit corresponding to the float number
     * @return : float number with unit converted to the corresponding amount of seconds.
     */
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

    /**
     * returns the array value associated with the given halflife ("seconds" parameter)
     *
     * @param seconds : float representation of the halflife
     * @return : array (with shaking multiplier and timer value) value corresponding to the "seconds" key, this value can be used as a multiplier (see shaking)
     */
    public static ArrayList<Float> getHalflifeValues(float seconds) {
        ArrayList<Float> halflifeValue = new ArrayList<>();
        for (Map.Entry<Float, ArrayList<Float>> entry : halflifeRanges.entrySet()) {
            if (seconds < entry.getKey()) { // halflifeRanges is a sorted map, so we can iterate starting from the lowest leftmost rangeborder
                halflifeValue.add(entry.getValue().get(0));
                halflifeValue.add(entry.getValue().get(1));
                break;
            }
        }
        if (halflifeValue.isEmpty()) {
            halflifeValue.add(0.005f);
            halflifeValue.add(99f);
        }
        return halflifeValue;
    }

    /**
     * Makes the map with the ranges for the halflife values. Each next entry in the map is the rightmost range.
     *
     * @return : map with ranges
     */
    public static LinkedHashMap<Float, ArrayList<Float>> makeHalflifeRanges() {

        // ranges:
        // [0sec - 1sec], ]1sec - 1uur], ]1uur - 1dag], ]1dag-1maand],
        // ]1maand-1jaar],]1jaar - 10jaar], ]10jaar - 10000jaar], ]10000jaar-oneindig[

        LinkedHashMap<Float, ArrayList<Float>> _halflifeRanges = new LinkedHashMap<>();
        _halflifeRanges.put(1f, new ArrayList<>(Arrays.asList(0.04f, 10f))); // 1 second
        _halflifeRanges.put(3600f, new ArrayList<>(Arrays.asList(0.035f, 20f))); // 1 hour
        _halflifeRanges.put(86400f, new ArrayList<>(Arrays.asList(0.03f, 30f))); // 1 day
        _halflifeRanges.put(2629743.83f, new ArrayList<>(Arrays.asList(0.025f, 40f))); // 1 month
        _halflifeRanges.put(31556926f, new ArrayList<>(Arrays.asList(0.02f, 55f))); // 1 year
        _halflifeRanges.put(315569260f, new ArrayList<>(Arrays.asList(0.015f, 75f))); // 10 years
        _halflifeRanges.put(315569260000f, new ArrayList<>(Arrays.asList(0.01f, 90f))); // 10000 years
        return  _halflifeRanges;
    }

    /**
     * parses the CSV line to make the nuclide.
     *
     * @param nuclideInfo : string array representation of the read line (already split) from the CSV file
     * @param _nuclidesTable : map of the nuclides table, to which the nuclide is added.
     */
    public static void parseNuclideInfo(String[] nuclideInfo, Map<String, NucleusState> _nuclidesTable) {

        String symbol = nuclideInfo[2].replaceAll("[0-9]", "");
        Atom atom = Atom.getBySymbol(symbol);
        String atomName = "";
        Item atomItem = null;
        int z = Integer.parseInt(nuclideInfo[0]);
        int n = Integer.parseInt(nuclideInfo[1]);
        String mainDecayMode = "unstable";
        String halflife = "";
        float _halflife = 0f;
        float unstability = 0f;

        if (atom != null) {
            atomName = atom.name();
            atomItem = atom.getItem();
        }
        if (nuclideInfo.length > 5) {
            halflife = nuclideInfo[4];
            mainDecayMode = nuclideInfo[5].toLowerCase();
        }
        else if (nuclideInfo.length > 4 && nuclideInfo[4].equals("STABLE")) {
            mainDecayMode = "stable";
        }
        if (!halflife.isEmpty()) {
            _halflife = parseHalflife(halflife);
            unstability = getHalflifeValues(_halflife).get(0);
        }
        // a, b+, b-, sf, n, p (ec = b+)
        // possible decays: bn it, it, p, ep, sf, ec, b- it, bn, a, b-, it le, it ap, ec, n,
        NucleusState nucleus = null;
        if (getNuclidesTable() != null) { // for first atom
            nucleus = getNuclide(z, n);
        }
        try {
            if (nucleus != null) { // nucleus already in table (need this since CSV contains duplicate [proton,neutron] entries)
                nucleus.addMainDecayMode(mainDecayMode);
            }
            else {
                addNuclidesTableEntry(
                        z,
                        n,
                        atomName,
                        symbol,
                        new ArrayList<>(List.of(mainDecayMode)),
                        atomItem,
                        unstability,
                        _nuclidesTable,
                        _halflife
                );
            }
        }
        catch (NumberFormatException e) {
//                    e.printStackTrace();
            Minelabs.LOGGER.info("NumberFormatException");
        }
        catch (NullPointerException e) {
            Minelabs.LOGGER.info("NullPointerException");
        }
    }

    /**
     * finds the next stable atom, given the amount of protons.
     *
     * @param protonAmount : amount of protons
     * @param isUnstableAllowed : set to true if the return of an unstable atom is allowed (if there is no stable version). Set to false otherwise.
     * @return : (int) amount of neutrons or -1 if there is no stable atom with the amount of protons and isUnstableAllowed parameter is set to false
     */
    public static int findNextStableAtom(int protonAmount, boolean isUnstableAllowed) {
        int _neutronAmount = 0;
        int tempNeutronAmount = 0;
        float maxHalflife = 0;
        for (Map.Entry<String, NucleusState> entry : NuclidesTable.getNuclidesTable().entrySet()) {
            NucleusState nucleusValue = entry.getValue();
            int protons = nucleusValue.getNrOfProtons();
            if (protons == protonAmount) { // we have found the right amount of protons
                if (nucleusValue.isStable()) {
                    _neutronAmount = nucleusValue.getNrOfNeutrons();
                    break;
                }
                else if (nucleusValue.getHalflife() > maxHalflife) {
                    maxHalflife = nucleusValue.getHalflife();
                    tempNeutronAmount = nucleusValue.getNrOfNeutrons();
                }
            }
        }
        if (_neutronAmount == 0) { // no stable (black) square found
            _neutronAmount = tempNeutronAmount;
            if (!isUnstableAllowed) {
                _neutronAmount = -1;
            }
        }
        return _neutronAmount;
    }

}




