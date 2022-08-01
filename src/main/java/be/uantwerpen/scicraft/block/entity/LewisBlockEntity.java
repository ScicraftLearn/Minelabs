package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.gui.lewis_gui.LewisBlockScreenHandler;
import be.uantwerpen.scicraft.inventory.ImplementedInventory;
import be.uantwerpen.scicraft.lewisrecipes.LewisCraftingGrid;
import be.uantwerpen.scicraft.lewisrecipes.MoleculeRecipe;
import be.uantwerpen.scicraft.network.LewisDataPacket;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static be.uantwerpen.scicraft.lewisrecipes.LewisCraftingGrid.GRIDSIZE;

public class LewisBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    //Inventory of items
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(36, ItemStack.EMPTY);
    //PropertyDelegate is an interface which is implemented inline here.
    //It can normally contain multiple integers as data identified by the index.
    private final PropertyDelegate propertyDelegate;
    //List of needed input ingredients; Synced by LewisDataPacket
    private DefaultedList<Ingredient> ingredients = DefaultedList.of();
    private final RecipeManager.MatchGetter<LewisCraftingGrid, MoleculeRecipe> matchGetter;
    //Progress of the recipe; -1 means not started; Synced by propertyDelegate
    public int progress = -1;
    //Density (amount) of items needed for the recipe;  Also used to tell the client a recipe is found;Synced by propertyDelegate
    private int density;
    //Current recipe selected; NOT synced
    private MoleculeRecipe currentRecipe;


    public LewisBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LEWIS_BLOCK_ENTITY, pos, state);
        this.matchGetter = RecipeManager.createCachedMatchGetter(MoleculeRecipe.MOLECULE_CRAFTING);

        propertyDelegate = new PropertyDelegate() {

            @Override
            public int get(int index) {
                switch (index) {
                    case 0:
                        return LewisBlockEntity.this.progress; //progress of the recipe
                    case 1:
                        return LewisBlockEntity.this.density; //Density (amount) of items needed for the recipe
                    default:
                        return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        LewisBlockEntity.this.progress = value;
                        break;
                    case 1:
                        LewisBlockEntity.this.density = value;
                        break;
                    default:
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    //From the ImplementedInventory Interface

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    //These Methods are from the NamedScreenHandlerFactory Interface
    //createMenu creates the ScreenHandler itself
    //getDisplayName will Provide its name which is normally shown at the top

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new LewisBlockScreenHandler(syncId, playerInventory, this, propertyDelegate, pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.progress = nbt.getInt("pr");
        Inventories.readNbt(nbt, this.inventory);
        this.density = nbt.getInt("dens");

    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("pr", this.progress);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("dens", this.density);

    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return super.createNbt();
    }

    public static void tick(World world, BlockPos pos, BlockState state, LewisBlockEntity lewis) {
        //No recipe loaded, try to load one.
        if (lewis.currentRecipe == null) {
            Optional<MoleculeRecipe> recipe = lewis.matchGetter.getFirstMatch(lewis.getLewisCraftingGrid(), world);
            recipe.ifPresent(r -> {
                lewis.ingredients = DefaultedList.ofSize(0);
                lewis.currentRecipe = r;
                lewis.ingredients = r.getIngredients();
                lewis.density = r.getDensity();
                LewisDataPacket packet = new LewisDataPacket(pos, lewis.ingredients); //custom packet to sync ingredients
                packet.sent(world, pos);
            });
        }
        //recipe loaded, check if enough items
        else if (lewis.inventory.get(34).isEmpty() || lewis.inventory.get(34).isOf(lewis.currentRecipe.getOutput().getItem())){ //can output
            boolean correct = false;
            for (int i = 0; i < lewis.ingredients.size(); i++) {
                correct = true;
                if (!lewis.ingredients.get(i).test(lewis.inventory.get(GRIDSIZE+i)) || lewis.inventory.get(GRIDSIZE+i).getCount() < lewis.currentRecipe.getDensity()) {
                    correct = false; // not enough items
                    break;
                }
            }
            if (lewis.progress > 0) {
                lewis.progress = correct? lewis.progress : -1; //enough items? continue or reset;
            } else {
                lewis.progress = correct? 0 : -1; //enough items? start or reset;
            }
        }
        //Busy crafting
        if (lewis.progress > -1) {
            lewis.progress += 1;
            if (lewis.progress >= 23) { //Done crafting
                if (lewis.inventory.get(34).isEmpty()) { //Set output slot
                    lewis.inventory.set(34, lewis.currentRecipe.getOutput());
                }
                else {
                    lewis.inventory.get(34).increment(1);
                }
                for (int i = 0; i < lewis.ingredients.size(); i++) {
                    lewis.inventory.get(GRIDSIZE+i).decrement(lewis.density);
                }
                lewis.resetRecipe();
            }
        }
        lewis.markDirty();
    }

    public LewisCraftingGrid getLewisCraftingGrid() {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < GRIDSIZE; i++) {
            stacks.add(inventory.get(i));
        }
//        Scicraft.LOGGER.info("stacks: " + stacks);

        return new LewisCraftingGrid(stacks.toArray(new ItemStack[0]));
    }

    /**
     * Provides a buffer to {@link LewisBlockScreenHandler}, to get the position of the blockentity
     * The blockentity is used to get all needed values.
     *
     * @param player the player that is opening the screen
     * @param buf    the packet buffer
     */
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    //Reset the recipe
    public void resetRecipe() {
        this.currentRecipe = null;
        this.progress = -1;
        this.ingredients = DefaultedList.ofSize(0);
        this.density = 0;
        markDirty();
    }

    public void setIngredients(DefaultedList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public DefaultedList<Ingredient> getIngredients() {
        return this.ingredients;
    }
}
