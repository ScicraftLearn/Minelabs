package be.minelabs.block.entity;

import be.minelabs.advancement.criterion.CoulombCriterion;
import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.block.Blocks;
import be.minelabs.item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AnimatedChargedBlockEntity extends BlockEntity {
    public long time = 0;
    public Direction movement_direction = Direction.NORTH;
    public final static int time_move_ticks = 4;
    public BlockState render_state = net.minecraft.block.Blocks.AIR.getDefaultState();
    public boolean annihilation = false;
    // should really just copy all nbt over, not make variables like this
    private final SimpleInventory inventory = new SimpleInventory(1);

    public AnimatedChargedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putBoolean("an", annihilation);
        tag.putLong("time", time);
        tag.putInt("md", movement_direction.getId());
        tag.put("rs", NbtHelper.fromBlockState(render_state));
        tag.put("Content", inventory.toNbtList());
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        annihilation = tag.getBoolean("an");
        time = tag.getLong("time");
        movement_direction = Direction.byId(tag.getInt("md"));
        // TODO CHECK IF CORRECT
        render_state = NbtHelper.toBlockState(Registries.BLOCK.getReadOnlyWrapper(), tag.getCompound("rs"));
        if (tag.contains("Content"))
            inventory.readNbtList(tag.getList("Content", NbtElement.COMPOUND_TYPE));
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbtWithIdentifyingData();
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (time == 0) {
            time = world.getTime();
            world.updateListeners(pos,state,state,3);
        }
        if (!world.isClient) {
            if (world.getTime() - time > time_move_ticks) {
                world.removeBlockEntity(pos);
                world.removeBlock(pos, false);
                BlockPos blockPos = pos.mutableCopy().offset(movement_direction);
                if (world.getBlockState(blockPos).getBlock().equals(Blocks.CHARGED_PLACEHOLDER)) { //also change other particle for client
                    world.setBlockState(blockPos, render_state, Block.NOTIFY_ALL);
                    if(world.getBlockEntity(blockPos) instanceof ChargedPointBlockEntity cpbe) {
                        cpbe.setContents(inventory.getStack(0));
                    }
                }
                Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, pos, 5, (condition) -> condition.test(CoulombCriterion.Type.MOVE));
                if (annihilation) {
                    ItemStack itemStack = new ItemStack(Items.PHOTON, 1);
                    double a = pos.getX() + movement_direction.getVector().getX() / 2d;
                    double b = pos.getY() + movement_direction.getVector().getY() / 2d;
                    double c = pos.getZ() + movement_direction.getVector().getZ() / 2d;
                    ItemEntity itemEntity = new ItemEntity(world, a, b, c, itemStack);
                    world.spawnEntity(itemEntity);
                    // Trigger advancement
                    Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, pos, 5, (condition) -> condition.test(CoulombCriterion.Type.ANNIHILATE));
                }
                markDirty();
            }
        }
    }

    public void setInventory(ItemStack inventoryIn) {
        inventory.setStack(0, inventoryIn);
    }

    public static void tick(World world, BlockPos pos, BlockState state, AnimatedChargedBlockEntity be) {
        be.tick(world, pos, state);
    }

}
