package be.minelabs.recipe.laser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class LaserRecipe implements Recipe<LaserInventory> {
    private final Identifier id;

    private final DefaultedList<Ingredient> ingredients;
    private final DefaultedList<ChanceStack> outputs;

    private final int time;

    public LaserRecipe(Identifier id, DefaultedList<Ingredient> ingredients, DefaultedList<ChanceStack> outputs, int time) {
        this.id = id;
        this.ingredients = ingredients;
        this.outputs = outputs;
        this.time = time;
    }

    @Override
    public boolean matches(LaserInventory inventory, World world) {
        if (world.isClient) {
            // needs to be called??
            return false;
        }
        return ingredients.get(0).test(inventory.getInputStack());
    }

    @Override
    public ItemStack craft(LaserInventory inventory, DynamicRegistryManager registryManager) {
        return getOutput(registryManager);
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        // First output should always produce
        return outputs.get(0).getStack().copy();
    }

    public DefaultedList<ChanceStack> getOutputs() {
        return outputs;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return LaserRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return LaserRecipeType.INSTANCE;
    }

    public static class LaserRecipeType implements RecipeType<LaserRecipe> {
        public static final LaserRecipeType INSTANCE = new LaserRecipeType();
        public static final String ID = "laser_crafting";

        public LaserRecipeType() {
        }
    }

    public static class LaserRecipeSerializer implements RecipeSerializer<LaserRecipe> {

        public static final LaserRecipeSerializer INSTANCE = new LaserRecipeSerializer();
        public static final String ID = "laser_crafting";

        @Override
        public LaserRecipe read(Identifier id, JsonObject json) {

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }


            JsonArray out = JsonHelper.getArray(json, "output");
            DefaultedList<ChanceStack> outputs = DefaultedList.ofSize(5, ChanceStack.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                outputs.set(i, ChanceStack.fromJson(out.get(i)));
            }

            int time = JsonHelper.getInt(json, "time", 23);

            return new LaserRecipe(id, inputs, outputs, time);
        }

        @Override
        public LaserRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromPacket(buf));

            DefaultedList<ChanceStack> outputs = DefaultedList.ofSize(buf.readInt(), ChanceStack.EMPTY);
            outputs.replaceAll(stack -> ChanceStack.fromPacket(buf));

            int time = buf.readInt();

            return new LaserRecipe(id, inputs, outputs, time);
        }

        @Override
        public void write(PacketByteBuf buf, LaserRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeInt(recipe.getOutputs().size());
            for (ChanceStack stack : recipe.getOutputs()) {
                stack.write(buf);
            }
            buf.writeInt(recipe.time);
        }
    }

}
