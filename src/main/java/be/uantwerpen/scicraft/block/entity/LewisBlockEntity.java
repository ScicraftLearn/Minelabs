package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.gui.LewisBlockScreenHandler;
import be.uantwerpen.scicraft.inventory.ImplementedInventory;
import be.uantwerpen.scicraft.lewisrecipes.DelegateSettings;
import be.uantwerpen.scicraft.lewisrecipes.LewisCraftingGrid;
import be.uantwerpen.scicraft.lewisrecipes.MoleculeRecipe;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LewisBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(36, ItemStack.EMPTY);
    private final RecipeManager.MatchGetter<LewisCraftingGrid, ? extends Recipe<LewisCraftingGrid>> matchGetter;
    public int progress = -1;

    //PropertyDelegate is an interface which is implemented inline here.
    //It can normally contain multiple integers as data identified by the index.
    private final PropertyDelegate propertyDelegate;
    private MoleculeRecipe currentRecipe;

    public LewisBlockEntity(BlockPos pos, BlockState state) {
        super(Entities.LEWIS_BLOCK_ENTITY, pos, state);
        this.matchGetter = RecipeManager.createCachedMatchGetter(MoleculeRecipe.MOLECULE_CRAFTING);


        propertyDelegate = new PropertyDelegate() {

            @Override
            public int get(int index) {
                switch (index) {
                    case 0:
                        return LewisBlockEntity.this.progress;
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
                    default:
                }
            }

            @Override
            public int size() {
                return 0;
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
        return new LewisBlockScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos), this, propertyDelegate);
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
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("pr", this.progress);
        Inventories.writeNbt(nbt, this.inventory);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        System.out.println(this.progress);
        LewisCraftingGrid lewisCraftingGrid = getLewisCraftingGrid();
        if (currentRecipe == null) {
            Optional<MoleculeRecipe> recipe = (Optional<MoleculeRecipe>) matchGetter.getFirstMatch(lewisCraftingGrid, world);
            recipe.ifPresent(r -> {
                this.currentRecipe = r;
            });
        }
        else {

        }
        if (this.progress > -1) {
            this.progress += 1;
            if (this.progress >= 23) {
                this.inventory.set(34,currentRecipe.getOutput());
                this.progress = -1;
                this.currentRecipe = null;
            }
        }
    }

    public LewisCraftingGrid getLewisCraftingGrid() {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            stacks.add(inventory.get(i));
        }
//        Scicraft.LOGGER.info("stacks: " + stacks);

        return new LewisCraftingGrid(stacks.toArray(new ItemStack[0]));
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }
}
