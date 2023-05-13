package be.minelabs.block.entity;

import be.minelabs.item.Items;
import be.minelabs.crafting.ionic.IonicInventory;
import be.minelabs.crafting.ionic.IonicRecipe;
import be.minelabs.gui.ionic_gui.IonicBlockScreenHandler;
import be.minelabs.network.IonicDataPacket;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static be.minelabs.gui.ionic_gui.IonicBlockScreenHandler.GRIDSIZE;

/**
 * IonicBlockEntity. We still implement {@link Inventory} for compat.
 */
public class IonicBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, Inventory {

    //The actual inventory
    private IonicInventory inventory = new IonicInventory(9, 9, 11);
    //PropertyDelegate that holds the progress, density and charge of both sides. synced to the client
    private final PropertyDelegate propertyDelegate;
    //private final RecipeManager.MatchGetter<IonicInventory, IonicRecipe> matchGetter;
    //List of needed input left ingredients; Synced by IonicDataPacket
    private DefaultedList<Ingredient> leftIngredients = DefaultedList.of();
    //List of needed input right ingredients; Synced by IonicDataPacket
    private DefaultedList<Ingredient> rightIngredients = DefaultedList.of();
    //Progress of the recipe; -1 means not started; Synced by propertyDelegate
    private int progress = -1;
    //Density (amount) of items needed for the recipe;  Also used to tell the client a recipe is found;Synced by propertyDelegate
    private int leftdensity;
    //Density (amount) of items needed for the recipe;  Also used to tell the client a recipe is found;Synced by propertyDelegate
    private int rightdensity;
    //Charge of items needed for the recipe;Synced by propertyDelegate
    private int leftCharge;
    //Charge of items needed for the recipe;Synced by propertyDelegat
    private int rightCharge;
    //Current recipe selected; NOT synced
    private IonicRecipe currentrecipe;

    public IonicBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.IONIC_BLOCK_ENTITY, pos, state);
        //this.matchGetter = RecipeManager.createCachedMatchGetter(CraftingRecipes.IONIC_CRAFTING);

        this.inventory.addListener(sender -> IonicBlockEntity.this.markDirty());

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0:
                        return IonicBlockEntity.this.progress;
                    case 1:
                        return IonicBlockEntity.this.leftdensity;
                    case 2:
                        return IonicBlockEntity.this.rightdensity;
                    case 3:
                        return IonicBlockEntity.this.leftCharge;
                    case 4:
                        return IonicBlockEntity.this.rightCharge;
                    default:
                        return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        IonicBlockEntity.this.progress = value;
                        break;
                    case 1:
                        IonicBlockEntity.this.leftdensity = value;
                        break;
                    case 2:
                        IonicBlockEntity.this.rightdensity = value;
                        break;
                    case 3:
                        IonicBlockEntity.this.leftCharge = value;
                        break;
                    case 4:
                        IonicBlockEntity.this.rightCharge = value;
                        break;
                    default:

                }
            }

            @Override
            public int size() {
                return 5;
            }
        };
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("Crafting");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new IonicBlockScreenHandler(syncId,inv,inventory, propertyDelegate, pos);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        NbtList nbtList = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
        this.inventory = new IonicInventory(9, 9, 11);
        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;
            if (j >= 0 && j < inventory.size()) {
                inventory.setStack(j, ItemStack.fromNbt(nbtCompound));
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtList nbtList = new NbtList();

        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = inventory.getStack(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                itemStack.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            }
        }
        nbt.put("Items", nbtList);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return super.createNbt();
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public static void tick(World world, BlockPos pos, BlockState state, IonicBlockEntity ionic){
        //No recipe loaded, try to load one.
        if (ionic.currentrecipe == null) {
            Optional<IonicRecipe> recipe = world.getRecipeManager().getFirstMatch(IonicRecipe.IonicRecipeType.INSTANCE, ionic.inventory, world);
            //Optional<IonicRecipe> recipe = ionic.mactchGetter.getFirstMatch(ionic.inventory, world)
            recipe.ifPresent(r -> {
                ionic.currentrecipe = r;
                ionic.leftIngredients = r.getLeftingredients();
                ionic.rightIngredients = r.getRightingredients();
                ionic.leftdensity = r.getLeftdensity();
                ionic.rightdensity = r.getRightdensity();
                ionic.leftCharge = r.getLeftCharge();
                ionic.rightCharge = r.getRightCharge();
                IonicDataPacket packet = new IonicDataPacket(pos, ionic.leftIngredients, ionic.rightIngredients);
                packet.sent(world, pos);
            });
        }
        //recipe loaded, check if enough items
        // TODO : CHECK IS THIS FINE ? DynamicRegistryManager.EMPTY
        else if (ionic.inventory.getStack(28).isEmpty()
                || ionic.inventory.getStack(28).isOf(ionic.currentrecipe.getOutput(DynamicRegistryManager.EMPTY).getItem())){
            if (!ionic.inventory.getStack(27).isOf(Items.ERLENMEYER) || ionic.inventory.getStack(27).getCount() < 1) return; //has erlenmeyer
            boolean correct = false;
            for (int i = 0; i < ionic.leftIngredients.size(); i++) {
                correct = true;
                if (!ionic.leftIngredients.get(i).test(ionic.inventory.getStack(2*GRIDSIZE+i)) || ionic.inventory.getStack(2*GRIDSIZE+i).getCount() < ionic.leftdensity) {
                    correct = false; // not enough items
                    break;
                }
            }
            for (int i = 0; i < ionic.rightIngredients.size(); i++) {
                if (!ionic.rightIngredients.get(i).test(ionic.inventory.getStack(2*GRIDSIZE+i + ionic.leftIngredients.size())) || ionic.inventory.getStack(2*GRIDSIZE+i+ionic.leftIngredients.size()).getCount() < ionic.currentrecipe.getRightdensity()) {
                    correct = false; // not enough items
                    break;
                }
            }
            if (ionic.progress > 0) {
                ionic.progress = correct? ionic.progress : -1; //enough items? continue or reset;
            } else {
                ionic.progress = correct? 0 : -1; //enough items? start or reset;
            }
        }
        //Busy crafting
        if (ionic.progress > -1) {
            ionic.progress += 1;
            if (ionic.progress >= 23) { //Done crafting
                if (ionic.inventory.getStack(28).isEmpty()) { //Set output slot
                    // TODO : CHECK IS THIS FINE ? DynamicRegistryManager.EMPTY
                    ionic.inventory.setStack(28, ionic.currentrecipe.getOutput(DynamicRegistryManager.EMPTY));
                }
                else {
                    ionic.inventory.getStack(28).increment(1);
                }
                ionic.inventory.getStack(27).decrement(1);
                for (int i = 0; i < ionic.leftIngredients.size(); i++) {
                    ionic.inventory.getStack(2*GRIDSIZE+i).decrement(ionic.leftdensity);
                }
                for (int i = 0; i < ionic.rightIngredients.size(); i++) {
                    ionic.inventory.getStack(2*GRIDSIZE+i+ionic.leftIngredients.size()).decrement(ionic.rightdensity);
                }
                ionic.resetRecipe();
            }
        }
        ionic.markDirty();
    }

    public void resetRecipe() {
        this.currentrecipe = null;
        this.progress = -1;
        this.leftdensity = 0;
        this.rightdensity = 0;
        this.leftCharge = 0;
        this.rightCharge = 0;
        this.leftIngredients = DefaultedList.of();
        this.rightIngredients = DefaultedList.of();
        this.markDirty();
    }

    /**
     * Provides a buffer to {@link IonicBlockScreenHandler}, to get the position of the blockentity
     * The blockentity is used to get all needed values.
     *
     * @param player the player that is opening the screen
     * @param buf    the packet buffer
     */
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public void setIngredients(DefaultedList<Ingredient> leftIngredients, DefaultedList<Ingredient> rightIngredients) {
        this.leftIngredients = leftIngredients;
        this.rightIngredients = rightIngredients;
    }

    public DefaultedList<Ingredient> getLeftIngredients() {
        return this.leftIngredients;
    }

    public DefaultedList<Ingredient> getRightIngredients() {
        return rightIngredients;
    }

    //Iventory overrides, for compatibility

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inventory.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return inventory.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setStack(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void clear() {
        inventory.clear();
    }
}
