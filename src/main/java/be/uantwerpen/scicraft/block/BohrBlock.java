package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.BohrBlockEntity;
import be.uantwerpen.scicraft.event.BohrBlockCallBack;
import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class BohrBlock extends BlockWithEntity implements BlockEntityProvider {
    public BohrBlock() {
        super(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1f).nonOpaque());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BohrBlockEntity(pos, state);
    }

    /**
     * prints the inventory of the bohrblock
     *
     * @param world, the world
     * @param pos,   the supposed location of the bohr block
     */
    private void printInventory(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
//        if there is a bohr block on the location in the world
        if (blockEntity instanceof BohrBlockEntity) {
            DefaultedList<ItemStack> neutronInventory = ((BohrBlockEntity) blockEntity).getNeutronInventory();
            DefaultedList<ItemStack> protonInventory = ((BohrBlockEntity) blockEntity).getProtonInventory();
            DefaultedList<ItemStack> electronInventory = ((BohrBlockEntity) blockEntity).getElectronInventory();

            System.out.println(neutronInventory);
            System.out.println(electronInventory);
            System.out.println(protonInventory);
        }
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BohrBlockEntity) {
            BohrBlockCallBack.EVENT.invoker().interact((player), this);

            DefaultedList<ItemStack> neutronInventory = ((BohrBlockEntity) blockEntity).getNeutronInventory();
            DefaultedList<ItemStack> protonInventory = ((BohrBlockEntity) blockEntity).getProtonInventory();
            DefaultedList<ItemStack> electronInventory = ((BohrBlockEntity) blockEntity).getElectronInventory();

            ItemStack stack = player.getStackInHand(hand);
//            if the player has neutrons in his hand
            if (stack.getItem() == Items.NEUTRON) {
                checkInventory(player, hand, neutronInventory);
                return ActionResult.SUCCESS;
            }
//            else if protons in hand
            else if (stack.getItem() == Items.PROTON) {
                checkInventory(player, hand, protonInventory);
                return ActionResult.SUCCESS;
            }
            //            else if electrons in hand
            else if (stack.getItem() == Items.ELECTRON) {
                checkInventory(player, hand, electronInventory);
                return ActionResult.SUCCESS;
            }
//            else if empty hand
            else if (stack.isEmpty()) {
                DefaultedList<ItemStack> allstacks = DefaultedList.ofSize(9);
                allstacks.addAll(neutronInventory);
                allstacks.addAll(protonInventory);
                allstacks.addAll(electronInventory);
                ItemScatterer.spawn(world, pos.up(1), allstacks);
                return ActionResult.SUCCESS;
            }


        }
        return ActionResult.SUCCESS;
    }


    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient()) {
            return BohrBlock::Tick;
        }
        return null;
    }


    private static <T extends BlockEntity> void Tick(World world, BlockPos blockPos, BlockState blockState, T t) {
//        MinecraftClient.getInstance().execute(() -> {
//            System.out.println("Helloo");
//            MatrixStack m = new MatrixStack();
//            MinecraftClient.getInstance().textRenderer.drawWithShadow(m, "help", 10, 10, 0xffffff);
//        });
    }


    //Made a new method to optimize (sorry Brentje xoxo Eliasje)
    private void checkInventory(PlayerEntity player, Hand hand, DefaultedList<ItemStack> inventory) {
        int i = 0;
        while (inventory.get(i).getCount() == 64 && inventory.get(i).getItem() == player.getStackInHand(hand).getItem() ) {
            i += 1;
        }
        if (i == 4) return;
        assert inventory.get(i).getCount() < 64;
//            if the inventory is empty initialize the inventory with 0 items
        if (inventory.get(i).isEmpty()) {
//                the item that the player was holding
            inventory.set(i, (player.getStackInHand(hand).copy()));
//                set the counter for the item on 0
            inventory.get(i).setCount(0);
        }
//            if the stack isn't full
        if (inventory.get(i).getCount() < 64) {
//                add 1 to the inventory
            inventory.get(i).setCount(inventory.get(i).getCount() + 1);
//                decrement 1 from the player's hand
            player.getStackInHand(hand).decrement(1);
            System.out.println("Slot holds : " + inventory.get(i));
        }
    }
}

