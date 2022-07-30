package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.BohrBlockEntity;
import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BohrBlockEntity) {
            DefaultedList<ItemStack> neutronInventory = ((BohrBlockEntity) blockEntity).getNeutronInventory();
            DefaultedList<ItemStack> protonInventory = ((BohrBlockEntity) blockEntity).getProtonInventory();
            DefaultedList<ItemStack> electronInventory = ((BohrBlockEntity) blockEntity).getElectronInventory();

            ItemStack stack = player.getStackInHand(hand);

            if (stack.getItem() == Items.NEUTRON) {
                checkInventory(player, hand, neutronInventory);
                return ActionResult.SUCCESS;
            } else if (stack.getItem() == Items.PROTON) {
                checkInventory(player, hand, protonInventory);
                return ActionResult.SUCCESS;
            } else if (stack.getItem() == Items.ELECTRON) {
                checkInventory(player, hand, electronInventory);
                return ActionResult.SUCCESS;
            } else if (player.getStackInHand(hand).isEmpty()) {
                //System.out.println("we zijn hier");
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

    //Made a new method to optimize (sorry Brentje xoxo Eliasje)
    private void checkInventory(PlayerEntity player, Hand hand, DefaultedList<ItemStack> inventory) {
        for (int i = 0; i < 3; i++) {
            if (inventory.get(i).isEmpty()) {
                inventory.set(i, (player.getStackInHand(hand).copy()));
                inventory.get(i).setCount(1);
            }
            if (inventory.get(i).getCount() < 64) {
                inventory.get(i).setCount(inventory.get(i).getCount() + 1);
                player.getStackInHand(hand).decrement(1);
                System.out.println("Slot holds : " + inventory.get(i));
            }
        }
    }
}
