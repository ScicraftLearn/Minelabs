package be.uantwerpen.scicraft.lewisrecipes;

public enum Bond {


    COVALENT_SINGLE(1){
        @Override
        public String toString() {
            return "single";
        }
    },

    COVALENT_DOUBLE(2){
        @Override
        public String toString() {
            return "double";
        }
    },

    COVALENT_TRIPLE(3){
        @Override
        public String toString() {
            return "triple";
        }
    };

    public final int bondOrder;

    Bond(int bondOrder){
        this.bondOrder = bondOrder;
    }

    public static Bond get(int bondOrder){
        return switch (bondOrder) {
            case 1 -> COVALENT_SINGLE;
            case 2 -> COVALENT_DOUBLE;
            case 3 -> COVALENT_TRIPLE;
            default -> throw new IllegalArgumentException("Invalid bondOrder: " + bondOrder);
        };
    }
}
