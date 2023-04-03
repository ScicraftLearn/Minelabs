package be.uantwerpen.minelabs.crafting.lewis;

import be.uantwerpen.minelabs.crafting.molecules.Molecule;
import be.uantwerpen.minelabs.crafting.molecules.MoleculeRecipeJsonFormat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class MoleculeRecipe implements Recipe<LewisCraftingGrid> {

    private final Molecule molecule;
    private final DefaultedList<Ingredient> ingredients = DefaultedList.of();
    private final Identifier id;
    private final JsonObject json;
    private final Integer density;


    public MoleculeRecipe(Molecule molecule, Identifier id, JsonObject json,Integer density) {
        this.molecule = molecule;
        ingredients.addAll(molecule.getIngredients().stream().map(atom -> Ingredient.ofItems(atom.getItem())).toList());
        this.id = id;
        this.json = json;
        this.density = density;
//        Minelabs.LOGGER.info("Recipe made: " + id.toString());
//        Minelabs.LOGGER.info(molecule.getStructure().toCanonical());
    }

    /*
     * Disables Recipe Book error
     */
    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    public Integer getDensity() {return density;}

    public Molecule getMolecule(){
        return molecule;
    }

    @Override
    public boolean matches(LewisCraftingGrid inventory, World world) {
        return inventory.getPartialMolecule().getStructure().isIsomorphicTo(molecule.getStructure());
    }

    /**
     * Seems to be used for recipe book functionality (showing which recipes are craftable based on contents of inventory).
     */
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack craft(LewisCraftingGrid inventory) {
        return getOutput();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return new ItemStack(molecule.getItem());
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public JsonObject getJson(){
        return json;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MoleculeRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return MoleculeRecipeType.INSTANCE;
    }

    public static class MoleculeRecipeType implements RecipeType<MoleculeRecipe> {
        public static final MoleculeRecipeType INSTANCE = new MoleculeRecipeType();
        public static final String ID = "molecule_crafting";

        private MoleculeRecipeType() {
        }
    }

    public static class MoleculeRecipeSerializer implements RecipeSerializer<MoleculeRecipe> {

        private MoleculeRecipeSerializer() {
        }

        public static final MoleculeRecipeSerializer INSTANCE = new MoleculeRecipeSerializer();
        public static final String ID = "molecule_crafting";

        @Override
        public MoleculeRecipe read(Identifier id, JsonObject json) {
            MoleculeRecipeJsonFormat recipeJson = new Gson().fromJson(json, MoleculeRecipeJsonFormat.class);
            // Validate all fields are there
            if (recipeJson.result == null)
                throw new JsonSyntaxException("Attribute 'result' is missing");
            if (recipeJson.structure == null)
                throw new JsonSyntaxException("Attribute 'structure' is missing");
            if (recipeJson.density == null)
                throw new JsonSyntaxException("Attribute 'density' is missing");

            Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.result.item))
                    // Validate the entered item actually exists
                    .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.result.item));

            Molecule molecule = new Molecule(recipeJson.structure.get(), outputItem);

            return new MoleculeRecipe(molecule, id, json, recipeJson.density);
        }

        @Override
        public MoleculeRecipe read(Identifier id, PacketByteBuf buf) {
            // TODO: untested code
            // Writing graph optimized to bytes is hard -> just send json over.
            String jsonStr = buf.readString();
            JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
            return read(id, json);
        }

        @Override
        public void write(PacketByteBuf buf, MoleculeRecipe recipe) {
            // Writing graph optimized to bytes is hard -> just send json over.
//            Minelabs.LOGGER.info(recipe.getJson().toString());
            buf.writeString(recipe.getJson().toString());
        }
    }

}
