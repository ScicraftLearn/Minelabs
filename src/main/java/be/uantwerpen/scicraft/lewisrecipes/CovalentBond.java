package be.uantwerpen.scicraft.lewisrecipes;

public class CovalentBond extends Bond{

    int bondOrder;

    public CovalentBond(int bondOrder){
        assert bondOrder >= 1 && bondOrder <= 3;
        this.bondOrder = bondOrder;
    }

    @Override
    public String toString() {
        return switch (bondOrder) {
            case 1 -> "single";
            case 2 -> "double";
            case 3 -> "triple";
            default -> throw new RuntimeException("Invalid bondOrder in covalent bond: " + bondOrder);
        };
    }
}
