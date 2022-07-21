package be.uantwerpen.scicraft.lewisrecipes;

public enum Bond {

    // Special case indicating no connection
    COVALENT_ZERO(0){
        @Override
        public String toString() {
            return "none";
        }

        @Override
        public Bond higher() {
            return COVALENT_SINGLE;
        }
    },
    COVALENT_SINGLE(1){
        @Override
        public String toString() {
            return "single";
        }

        @Override
        public Bond higher() {
            return COVALENT_DOUBLE;
        }
    },

    COVALENT_DOUBLE(2){
        @Override
        public String toString() {
            return "double";
        }

        @Override
        public Bond higher() {
            return COVALENT_TRIPLE;
        }
    },

    COVALENT_TRIPLE(3){
        @Override
        public String toString() {
            return "triple";
        }

        @Override
        public Bond higher() {
            throw new UnsupportedOperationException("Triple bond is highest");
        }
    };

    public final int bondOrder;

    Bond(int bondOrder){
        this.bondOrder = bondOrder;
    }

    public abstract Bond higher() throws UnsupportedOperationException;

    public static Bond get(int bondOrder){
        return switch (bondOrder) {
            case 0 -> COVALENT_ZERO;
            case 1 -> COVALENT_SINGLE;
            case 2 -> COVALENT_DOUBLE;
            case 3 -> COVALENT_TRIPLE;
            default -> throw new IllegalArgumentException("Invalid bondOrder: " + bondOrder);
        };
    }
}
