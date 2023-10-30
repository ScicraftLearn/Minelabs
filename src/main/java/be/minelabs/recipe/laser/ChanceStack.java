package be.minelabs.recipe.laser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class ChanceStack {

    public static final ChanceStack EMPTY = new ChanceStack(ItemStack.EMPTY, 1);

    private final ItemStack stack;

    private final int chance;

    public ChanceStack(ItemStack stack, int chance) {
        this.chance = chance;
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getChance() {
        return chance;
    }

    public static ChanceStack fromJson(JsonElement json) {
        if (json == null || json.isJsonNull()) {
            throw new JsonSyntaxException("Item cannot be null");
        }
        if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            ItemStack stack = ShapedRecipe.outputFromJson(object);
            int chance = 1;
            if (object.has("chance")) {
                chance = object.get("chance").getAsInt();
            }

            return new ChanceStack(stack, chance);
        }

        throw new JsonSyntaxException("Expected item to be object or array of objects");
    }

    public void write(@NotNull PacketByteBuf buf) {
        buf.writeItemStack(stack);
        buf.writeInt(chance);
    }

    public static ChanceStack fromPacket(@NotNull PacketByteBuf buf) {
        ItemStack stack = buf.readItemStack();
        if (stack.isEmpty()) {
            return ChanceStack.EMPTY;
        }
        int chance = buf.readInt();
        return new ChanceStack(stack, chance);
    }


}
