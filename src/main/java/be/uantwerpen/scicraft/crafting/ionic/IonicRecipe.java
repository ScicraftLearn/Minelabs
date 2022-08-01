package be.uantwerpen.scicraft.crafting.ionic;

import be.uantwerpen.scicraft.lewisrecipes.*;
import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import static be.uantwerpen.scicraft.crafting.ionic.CraftingRecipes.IONIC_CRAFTING;


public class IonicRecipe implements Recipe<IonicInventory> {


    private final JsonObject leftjson;
    private final int leftdensity;
    private final JsonObject rightjson;
    private final int rightdensity;
    private final Identifier id;
    private final PartialMolecule leftMolecule;
    private final PartialMolecule rightMolecule;
    private final DefaultedList<Ingredient> ingredients = DefaultedList.of();
    private final DefaultedList<Ingredient> leftingredients = DefaultedList.of();
    private final DefaultedList<Ingredient> rightingredients = DefaultedList.of();
    private final ItemStack output;


    IonicRecipe(JsonObject leftjson, int leftdensity, JsonObject rightjson,  int rightdensity, ItemStack output, Identifier id) {
        MoleculeGraphJsonFormat leftGraph = new Gson().fromJson(leftjson, MoleculeGraphJsonFormat.class);
        this.leftMolecule = new PartialMolecule(leftGraph.get());

        MoleculeGraphJsonFormat rightGraph = new Gson().fromJson(leftjson, MoleculeGraphJsonFormat.class);
        this.rightMolecule = new PartialMolecule(rightGraph.get());

        this.leftjson = leftjson;
        this.leftdensity = leftdensity;
        this.rightjson = rightjson;
        this.rightdensity = rightdensity;
        this.output = output;
        this.id = id;

        this.leftingredients.addAll(leftMolecule.getIngredients().stream().map(atom -> Ingredient.ofItems(atom.getItem())).toList());
        this.rightingredients.addAll(rightMolecule.getIngredients().stream().map(atom -> Ingredient.ofItems(atom.getItem())).toList());
        this.ingredients.addAll(leftingredients);
        this.ingredients.addAll(rightingredients);
    }

    @Override
    public boolean matches(IonicInventory inventory, World world) {
        boolean left = inventory.getLeftGrid().getPartialMolecule().getStructure().isIsomorphicTo(leftMolecule.getStructure());
        boolean right = inventory.getRightGrid().getPartialMolecule().getStructure().isIsomorphicTo(rightMolecule.getStructure());
        System.out.println("left: " + left + " , right: " + right);
        return left && right;
    }

    @Override
    public ItemStack craft(IonicInventory inventory) {
        return this.getOutput();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return IonicRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return IONIC_CRAFTING;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    public DefaultedList<Ingredient> getLeftingredients() {
        return leftingredients;
    }

    public DefaultedList<Ingredient> getRightingredients() {
        return rightingredients;
    }

    public int getLeftdensity() {
        return leftdensity;
    }

    public int getRightdensity() {
        return rightdensity;
    }

    public static class IonicRecipeSerializer implements RecipeSerializer<IonicRecipe> {

        public static final IonicRecipeSerializer INSTANCE = new IonicRecipeSerializer();

        @Override
        public IonicRecipe read(Identifier id, JsonObject json) {
            JsonObject left = json.getAsJsonObject("left");

            int leftDensity = left.get("density").getAsInt();

            JsonObject right = json.getAsJsonObject("right");
            int rightDensity = right.get("density").getAsInt();

            ItemStack output = ShapedRecipe.outputFromJson(json.getAsJsonObject("result"));
            return new IonicRecipe(left.getAsJsonObject("structure"), leftDensity, right.getAsJsonObject("structure"), rightDensity, output, id);
        }

        @Override
        public IonicRecipe read(Identifier id, PacketByteBuf buf) {
            JsonObject leftJson = JsonParser.parseString(buf.readString()).getAsJsonObject();
            int leftDensity = buf.readInt();
            JsonObject rightJson = JsonParser.parseString(buf.readString()).getAsJsonObject();
            int rightDensity = buf.readInt();
            ItemStack output = buf.readItemStack();
            return new IonicRecipe(leftJson, leftDensity, rightJson, rightDensity, output, id);
        }

        @Override
        public void write(PacketByteBuf buf, IonicRecipe recipe) {
            buf.writeString(recipe.leftjson.toString());
            buf.writeInt(recipe.leftdensity);
            buf.writeString(recipe.rightjson.toString());
            buf.writeInt(recipe.rightdensity);
            buf.writeItemStack(recipe.output);
        }
    }
}
