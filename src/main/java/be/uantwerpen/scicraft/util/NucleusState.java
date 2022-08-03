package be.uantwerpen.scicraft.util;

import be.uantwerpen.scicraft.item.AtomItem;
import net.minecraft.item.Item;

/**
 * The NucleusState class is used to keep information about the atom for every atom.
 */
public class NucleusState {

    private final String mainDecayMode; // possible options irl are: (alpha, EC+ beta+, beta-, p, n, EC, SF) , Stable, Unstable

    private final String symbol;
    private final String atomName; // full atom name

    private final Item atomItem; // minecraft atom item

    private final int nrOfProtons;
    private final int nrOfNeutrons;

    /**
     * Constructor of the NucleusState. The amount of electrons (as a data member) is not needed.
     *
     * @param mainDecayMode : String name of the decay mode (possible options are: alpha, EC+ beta+, beta-, p, n, EC, SF, Stable)
     * @param symbol : String name of the symbol associated with the atom
     * @param atomName : String name of the atom
     * @param atomItem : minecraft atom item
     * @param nrOfProtons : number of protons
     * @param nrOfNeutrons : number of neutrons
     */
    public NucleusState(String mainDecayMode, String symbol, String atomName, Item atomItem, int nrOfProtons, int nrOfNeutrons) {
        this.mainDecayMode = mainDecayMode;
        this.symbol = symbol;
        this.atomName = atomName;
        this.atomItem = atomItem;
        this.nrOfProtons = nrOfProtons;
        this.nrOfNeutrons = nrOfNeutrons;
    }

    public String getMainDecayMode() {return mainDecayMode;}
    public String getSymbol() {return symbol;}
    public String getAtomName() {return atomName;}
    public Item getAtomItem() {return atomItem;}
    public int getNrOfProtons() {return nrOfProtons;}
    public int getNrOfNeutrons() {return nrOfNeutrons;}

    public boolean isStable() {
        return this.getMainDecayMode().equals("Stable");
    }
}
