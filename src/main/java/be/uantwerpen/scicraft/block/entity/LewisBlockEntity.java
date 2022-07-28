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
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LewisBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(45, ItemStack.EMPTY);
    private DefaultedList<Ingredient> ingredients = DefaultedList.of();
    private final RecipeManager.MatchGetter<LewisCraftingGrid, MoleculeRecipe> matchGetter;
    public int progress = -1;

    //PropertyDelegate is an interface which is implemented inline here.
    //It can normally contain multiple integers as data identified by the index.
    private final PropertyDelegate propertyDelegate;
    private MoleculeRecipe currentRecipe;
    private int density;

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
        return new LewisBlockScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos), this, propertyDelegate, pos);
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

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient) {
            return;
        }
        if (currentRecipe == null) {
            clearIngredients();
            this.ingredients = DefaultedList.ofSize(0);
            Optional<MoleculeRecipe> recipe = matchGetter.getFirstMatch(getLewisCraftingGrid(), world);
            recipe.ifPresent(r -> {
                this.currentRecipe = r;
                this.ingredients = r.getIngredients();
                this.density = r.getDensity();
            });
        }
        else if (this.inventory.get(34).isEmpty() || this.inventory.get(34).isOf(currentRecipe.getOutput().getItem())){
            clearIngredients();
            boolean correct = false;
            for (int i = 0; i < ingredients.size(); i++) {
                correct = true;
                if (!ingredients.get(i).test(this.inventory.get(25+i)) || this.inventory.get(25+i).getCount() < currentRecipe.getDensity()) {
                    correct = false;
                    ItemStack itemStack = ingredients.get(i).getMatchingStacks()[0];
                    this.inventory.set(36+i, itemStack);
                }
            }
            if (this.progress > 0) {
                this.progress = correct? progress : -1;
            } else {
                this.progress = correct? 0 : -1;
            }
        }
        if (this.progress > -1) {
            this.progress += 1;
            if (this.progress >= 23) {
                if (this.inventory.get(34).isEmpty()) {
                    this.inventory.set(34,currentRecipe.getOutput());
                }
                else {
                    this.inventory.get(34).increment(1);
                }
                for (int i = 0; i < ingredients.size(); i++) {
                    this.inventory.get(25+i).decrement(this.density);
                }
                clearIngredients();
                this.progress = -1;
                this.currentRecipe = null;
                this.ingredients = DefaultedList.ofSize(0);
                this.density = 0;
            }
        }
        markDirty();
        world.updateListeners(pos, state, state, 3);
    }

    public LewisCraftingGrid getLewisCraftingGrid() {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            stacks.add(inventory.get(i));
        }
//        Scicraft.LOGGER.info("stacks: " + stacks);

        return new LewisCraftingGrid(stacks.toArray(new ItemStack[0]));
    }

    public int getDensity() {
        return density;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void resetRecipe() {
        this.currentRecipe = null;
        this.progress = -1;
        this.ingredients = DefaultedList.ofSize(0);
        this.density = 0;
        clearIngredients();
        System.out.println(getIngredients());
        markDirty();
        world.updateListeners(pos, getCachedState(), getCachedState(), 3);
    }

    public List<ItemStack> getIngredients() {
        return this.inventory.subList(36,45);
    }

    public void clearIngredients() {
        for (int i = 36; i < 45; i++) {
            this.inventory.set(i, ItemStack.EMPTY);
        }
    }
}
