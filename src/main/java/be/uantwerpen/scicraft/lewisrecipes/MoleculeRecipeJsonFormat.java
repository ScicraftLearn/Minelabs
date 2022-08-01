package be.uantwerpen.scicraft.lewisrecipes;

public class MoleculeRecipeJsonFormat {

    public MoleculeGraphJsonFormat structure;

    public Integer density;

    public Result result;

    public static class Result {
        public String item;
    }

}