package be.uantwerpen.scicraft.gas;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.util.registry.Registry;

public class Gasses {
    public static final Gas O2;
    public static final Gas N2;
    public static final Gas CH4;
    public static final Gas H2;
    public static final Gas NO;
    public static final Gas NO2;
    public static final Gas Cl2;
    public static final Gas CO2;
    public static final Gas CO;
    public static final Gas NH3;
    public static final Gas N2O;
    public static final Gas HCl;
    public static final Gas He;
    public static final Gas Ne;
    public static final Gas Ar;

    public Gasses() {

    }

    public static void registerPotions() {
        Scicraft.LOGGER.info("registering potions");
    }

    private static Gas register(String name, Gas potion) {
        return (Gas) Registry.register(Registry.POTION, name, potion);
    }

    static {
        O2 = register("o2", new Gas("o2", 16777215));
        N2 = register("n2", new Gas("n2", 16777215));
        CH4 = register("ch4", new Gas("ch4", 16777215));
        H2 = register("h2", new Gas("h2", 16777215));
        NO = register("no", new Gas("no", 16777215));
        NO2 = register("no2", new Gas("no2", 10034176));
        Cl2 = register("cl2", new Gas("cl2", 15266956));
        CO2 = register("co2", new Gas("co2", 16777215));
        CO = register("co", new Gas("co", 16777215));
        NH3 = register("nh3", new Gas("nh3", 16777215));
        N2O = register("n2o", new Gas("n2o", 16777215));
        HCl = register("hcl", new Gas("hcl", 16777215));
        He = register("he", new Gas("he", 16777215));
        Ne = register("ne", new Gas("ne", 16777215));
        Ar = register("ar", new Gas("ar", 16777215));
    }
}
