package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.crafting.ionic.CraftingRecipes;
import be.uantwerpen.scicraft.crafting.ionic.IonicInventory;
import be.uantwerpen.scicraft.crafting.ionic.IonicRecipe;
import be.uantwerpen.scicraft.gui.ionic_gui.IonicBlockScreenHandler;
import be.uantwerpen.scicraft.lewisrecipes.LewisCraftingGrid;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
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

import java.util.Optional;

import static be.uantwerpen.scicraft.gui.ionic_gui.IonicBlockScreenHandler.GRIDSIZE;

public class IonicBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, Inventory {

    private final IonicInventory inventory = new IonicInventory(9, 9, 11);
    private final PropertyDelegate propertyDelegate;
    private final RecipeManager.MatchGetter<IonicInventory, IonicRecipe> matchGetter;
    private DefaultedList<Ingredient> ingredients = DefaultedList.of();
    private int progress = -1;
    private int leftdensity;
    private int rightdensity;
    private IonicRecipe currentrecipe;

    public IonicBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.IONIC_BLOCK_ENTITY, pos, state);

        this.matchGetter = RecipeManager.createCachedMatchGetter(CraftingRecipes.IONIC_CRAFTING);

        this.inventory.addListener(new InventoryChangedListener() {
            @Override
            public void onInventoryChanged(Inventory sender) {
                IonicBlockEntity.this.markDirty();
            }
        });

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
                    default:

                }
            }

            @Override
            public int size() {
                return 3;
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
        return new IonicBlockScreenHandler(syncId,inv,this, propertyDelegate, pos);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory.readNbtList(nbt.getList("inv", NbtElement.COMPOUND_TYPE));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("inv",inventory.toNbtList());
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return super.createNbt();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public static void tick(World world, BlockPos pos, BlockState state, IonicBlockEntity ionic){
        if (ionic.currentrecipe == null) {
            Optional<IonicRecipe> recipe = ionic.matchGetter.getFirstMatch(ionic.inventory, world);
            recipe.ifPresent( r -> {
                ionic.currentrecipe = r;
                ionic.ingredients = r.getIngredients();
                ionic.leftdensity = r.getLeftdensity();
                ionic.rightdensity = r.getRightdensity();
                ionic.ingredients = r.getIngredients();
            });
        }
        else if (ionic.inventory.getStack(28).isEmpty() || ionic.inventory.getStack(28).isOf(ionic.currentrecipe.getOutput().getItem())){
            boolean correct = false;
            for (int i = 0; i < ionic.ingredients.size(); i++) {
                correct = true;
                if (!ionic.ingredients.get(i).test(ionic.inventory.getStack(GRIDSIZE+i)) || ionic.inventory.getStack(GRIDSIZE*2+i).getCount() < ionic.currentrecipe.getLeftdensity()) {
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
        if (ionic.progress > -1) {
            ionic.progress += 1;
            if (ionic.progress >= 23) { //Done crafting
                if (ionic.inventory.getStack(34).isEmpty()) { //Set output slot
                    ionic.inventory.setStack(34, ionic.currentrecipe.getOutput());
                }
                else {
                    ionic.inventory.getStack(34).increment(1);
                }
                for (int i = 0; i < ionic.ingredients.size(); i++) {
                    ionic.inventory.getStack(LewisCraftingGrid.GRIDSIZE+i).decrement(ionic.leftdensity);
                }
                ionic.resetRecipe();
            }
        }
        ionic.markDirty();
    }

    private void resetRecipe() {
        this.currentrecipe = null;
        this.progress = -1;
        this.leftdensity = 0;
        this.rightdensity = 0;
        this.ingredients = DefaultedList.of();
        this.markDirty();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public DefaultedList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    //Iventory overrides

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
