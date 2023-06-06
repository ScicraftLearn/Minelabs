package be.minelabs.item.items;

import be.minelabs.inventory.OrderedInventory;
import be.minelabs.item.Items;
import be.minelabs.screen.AtomStorageScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AtomPackItem extends Item {

    private final OrderedInventory inventory = new OrderedInventory(Items.ATOMS.size());

    public AtomPackItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (hand == Hand.MAIN_HAND && !world.isClient){
            user.openHandledScreen(new NamedScreenHandlerFactory() {
                @Override
                public Text getDisplayName() {
                    return Text.translatable(getTranslationKey());
                }

                @Nullable
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new AtomStorageScreenHandler(syncId, playerInventory, inventory);
                }
            });
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }
}
