package be.uantwerpen.minelabs.block.entity;

import be.uantwerpen.minelabs.advancement.criterion.Criteria;
import be.uantwerpen.minelabs.advancement.criterion.LCTMakeBondCriterion;
import be.uantwerpen.minelabs.crafting.lewis.LewisCraftingGrid;
import be.uantwerpen.minelabs.crafting.lewis.MoleculeRecipe;
import be.uantwerpen.minelabs.gui.lewis_gui.LewisBlockScreenHandler;
import be.uantwerpen.minelabs.inventory.OrderedInventory;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.network.LewisDataPacket;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LewisBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {
    // slots 0-24
    private final LewisCraftingGrid craftingGrid = new LewisCraftingGrid(5,5);

    // slots 0-8: atoms, slot 9: erlenmeyer, slot 10: output
    // combined with craftingGrid this gives slots 25-33: atoms, slot 34: erlenmeyer, slot 35: output
    private final OrderedInventory ioInventory = new OrderedInventory(11);

    //PropertyDelegate is an interface which is implemented inline here.
    //It can normally contain multiple integers as data identified by the index.
    private final PropertyDelegate propertyDelegate;
    //List of needed input ingredients; Synced by LewisDataPacket
    private DefaultedList<Ingredient> ingredients = DefaultedList.of();
    //private final RecipeManager.MatchGetter<LewisCraftingGrid, MoleculeRecipe> matchGetter;
    //Progress of the recipe; -1 means not started; Synced by propertyDelegate
    public int progress = -1;
    public int maxProgress = 23;
    //Density (amount) of items needed for the recipe;  Also used to tell the client a recipe is found;Synced by propertyDelegate
    private int density;
    //Current recipe selected; NOT synced
    private MoleculeRecipe currentRecipe;

    public LewisBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LEWIS_BLOCK_ENTITY, pos, state);
        //this.matchGetter = RecipeManager.createCachedMatchGetter(CraftingRecipes.MOLECULE_CRAFTING);

        propertyDelegate = new PropertyDelegate() {

            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> LewisBlockEntity.this.progress; //progress of the recipe
                    case 1 -> LewisBlockEntity.this.maxProgress;
                    case 2 -> LewisBlockEntity.this.density; //Density (amount) of items needed for the recipe
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> LewisBlockEntity.this.progress = value;
                    case 1 -> LewisBlockEntity.this.maxProgress = value;
                    case 2 -> LewisBlockEntity.this.density = value;
                }
            }

            @Override
            public int size() {
                return 3;
            }
        };
    }

    //These Methods are from the NamedScreenHandlerFactory Interface
    //createMenu creates the ScreenHandler itself
    //getDisplayName will Provide its name which is normally shown at the top
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new LewisBlockScreenHandler(syncId, playerInventory, craftingGrid, ioInventory, propertyDelegate, pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.progress = nbt.getInt("pr");
        this.craftingGrid.readNbtList(nbt.getList("grid", NbtElement.COMPOUND_TYPE));
        this.ioInventory.readNbtList(nbt.getList("io_inv", NbtElement.COMPOUND_TYPE));
        this.density = nbt.getInt("dens");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("pr", this.progress);
        nbt.put("grid", craftingGrid.toNbtList());
        nbt.put("io_inv", ioInventory.toNbtList());
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
            Optional<MoleculeRecipe> recipe = world.getRecipeManager().getFirstMatch(MoleculeRecipe.MoleculeRecipeType.INSTANCE, lewis.craftingGrid, world);
            //Optional<MoleculeRecipe> recipe = lewis.matchGetter.getFirstMatch(lewis.craftingGrid, world);
            recipe.ifPresent(r -> {
                lewis.setRecipe(r);
                LewisDataPacket packet = new LewisDataPacket(pos, lewis.ingredients); //custom packet to sync ingredients
                packet.send(world, pos);
            });
        }
        //recipe loaded, check if enough items
        else if (lewis.ioInventory.getStack(10).isEmpty() || lewis.ioInventory.getStack(10).isOf(lewis.currentRecipe.getOutput().getItem())){ //can output
            //System.out.println(lewis.currentRecipe.getIngredients());
            if (!lewis.ioInventory.getStack(9).isOf(Items.ERLENMEYER) || lewis.ioInventory.getStack(9).getCount() < 1) {
                lewis.resetProgress();
                return; //has NO erlenmeyer
            }
            boolean correct = false;
            for (int i = 0; i < lewis.ingredients.size(); i++) {
                correct = true;
                if (!lewis.ingredients.get(i).test(lewis.ioInventory.getStack(i)) || lewis.ioInventory.getStack(i).getCount() < lewis.currentRecipe.getDensity()) {
                    correct = false; // not enough items
                    break;
                }
            }
            if (lewis.progress > 0) {
                lewis.progress = correct ? lewis.progress : -1; //enough items? continue or reset;
            } else {
                lewis.progress = correct? 0 : -1; //enough items? start or reset;
            }
        }
        //Busy crafting
        if (lewis.progress > -1 && lewis.currentRecipe != null) {
            if (lewis.ioInventory.getStack(10).isEmpty() || lewis.ioInventory.getStack(10).getMaxCount() > 1) {
                lewis.progress += 1;
            }
            if (lewis.progress >= lewis.maxProgress) { //Done crafting
                if (lewis.ioInventory.getStack(10).isEmpty()) { //Set output slot
                    lewis.ioInventory.setStack(10, lewis.currentRecipe.getOutput());
                } else {
                    lewis.ioInventory.getStack(10).increment(1);
                }
                lewis.ioInventory.getStack(9).decrement(1);
                for (int i = 0; i < lewis.ingredients.size(); i++) {
                    lewis.ioInventory.getStack(i).decrement(lewis.density);
                }
                lewis.resetProgress();
            }
        }
        lewis.markDirty();
    }

    private void setRecipe(MoleculeRecipe recipe){
        this.currentRecipe = recipe;
        ingredients = DefaultedList.ofSize(0); // why?
        ingredients = recipe.getIngredients();
        density = recipe.getDensity();
    }

    private void resetProgress() {
        progress = -1;
        markDirty();
    }

    public LewisCraftingGrid getLewisCraftingGrid() {
        return craftingGrid;
    }

    public Inventory getIoInventory() {
        return ioInventory;
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

    public MoleculeRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }
}
