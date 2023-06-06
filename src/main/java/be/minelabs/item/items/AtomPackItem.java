package be.minelabs.item.items;

import be.minelabs.inventory.OrderedInventory;
import be.minelabs.item.Items;
import be.minelabs.screen.AtomStorageScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AtomPackItem extends Item {


    public AtomPackItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (hand == Hand.MAIN_HAND && !world.isClient){
            OrderedInventory inventory = new OrderedInventory(Items.ATOMS.size()){
                @Override
                public void onClose(PlayerEntity player) {
                    super.onClose(player);
                    NbtCompound nbt = player.getStackInHand(Hand.MAIN_HAND).getOrCreateNbt();
                    Inventories.writeNbt(nbt, this.stacks);
                }

                @Override
                public void onOpen(PlayerEntity player) {
                    Inventories.readNbt(player.getStackInHand(Hand.MAIN_HAND).getOrCreateNbt(), this.stacks);
                    super.onOpen(player);
                }
            };

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
