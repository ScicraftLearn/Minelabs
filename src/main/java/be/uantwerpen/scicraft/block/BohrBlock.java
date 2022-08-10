package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.BohrBlockEntity;
import be.uantwerpen.scicraft.entity.SubatomicParticle;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.network.NetworkingConstants;
import be.uantwerpen.scicraft.util.NucleusState;
import be.uantwerpen.scicraft.util.NuclidesTable;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static be.uantwerpen.scicraft.block.entity.BohrBlockEntity.TIMER;


public class BohrBlock extends BlockWithEntity implements BlockEntityProvider {


    public BohrBlock() {
        super(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1f).nonOpaque());
        this.setDefaultState(this.stateManager.getDefaultState().with(TIMER, 30));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.createAndScheduleBlockTick(pos, this, 1);

    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TIMER);
    }
    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        super.onProjectileHit(world,state,hit,projectile);
        BlockEntity blockEntity = world.getBlockEntity(hit.getBlockPos());
        Item item;
        Entity owner = projectile.getOwner();
        if (!world.isClient()) {
//            BlockEntity playerBlockEntity = playerEntity.getWorld().getBlockEntity(hit.getBlockPos());

            if (projectile instanceof SubatomicParticle subatomicParticle && blockEntity instanceof BohrBlockEntity bohrBlockEntity) {
                item = subatomicParticle.getStack().getItem();
                if (item == Items.ELECTRON || item == Items.NEUTRON || item == Items.PROTON) {
                    bohrBlockEntity.insertParticle(item);
//                    playerBohrBlockEntity.insertParticle(item);

                } else if (item == Items.ANTI_NEUTRON || item == Items.ANTI_PROTON || item == Items.POSITRON) {
                    bohrBlockEntity.removeParticle(item);
//                    playerBohrBlockEntity.insertParticle(item);
                }
            }
        }
        return;
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BohrBlockEntity(pos, state);
    }



    @Override
    public void scheduledTick(BlockState state,ServerWorld world, BlockPos pos, Random random) {

        super.scheduledTick(state, world, pos, random);

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BohrBlockEntity bohrBlockEntity && !world.isClient()) {
            int nrOfProtons = bohrBlockEntity.getProtonCount();
            int nrOfNeutrons = bohrBlockEntity.getNeutronCount();
            int nrOfElectrons = bohrBlockEntity.getElectronCount();
            int remaining = state.get(TIMER);

            if (NuclidesTable.isStable(nrOfProtons,nrOfNeutrons,nrOfElectrons)){
                remaining = 30;
            }
            else{
                remaining = Math.max(0,remaining-1);
            }
            if (remaining == 0){
                NbtCompound nbtCompound = bohrBlockEntity.createNbt();
                bohrBlockEntity.writeNbt(nbtCompound);
                bohrBlockEntity.scatterParticles();
                remaining = 30;

//                Packet<ClientPlayPacketListener> updatePacket = bohrBlockEntity.toUpdatePacket();
//                if (updatePacket != null) {
//                    for (ServerPlayerEntity playerEntity: world.getPlayers()){
//                        playerEntity.networkHandler.sendPacket(updatePacket);
//                    }
//                    world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
//                }

//                bohrBlockEntity.markDirty();
//                world.updateListeners(pos,state,state,Block.NOTIFY_LISTENERS);
            }
            state = state.with(TIMER,remaining);
            world.setBlockState(pos, state);
            world.createAndScheduleBlockTick(pos, this, 20, TickPriority.VERY_HIGH);
        }




//        this.getBlock().scheduledTick(this.asBlockState(), world, pos, random);
//    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
//
//        int tickrate = Fluids.WATER.getTickRate(world)
//
//


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
        if (blockEntity instanceof BohrBlockEntity bohrBlockEntity) {
            System.out.println(bohrBlockEntity.getItems());
        }
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {


        BlockEntity blockEntity = world.getBlockEntity(pos);
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();

        if (blockEntity instanceof BohrBlockEntity bohrBlockEntity) {
            if (item == Items.NEUTRON || item == Items.PROTON || item == Items.ELECTRON) {
                if (bohrBlockEntity.insertParticle(item) == ActionResult.SUCCESS) {
                    player.getStackInHand(hand).decrement(1);
                }
            } else if (item == Items.ANTI_NEUTRON || item == Items.ANTI_PROTON || item == Items.POSITRON) {
                if (bohrBlockEntity.removeParticle(item) == ActionResult.SUCCESS) {
                    player.getStackInHand(hand).decrement(1);
                }
            } else if (stack.isEmpty()) {
//                creating the atom
                if (player.isSneaking()) {
                    createAtom(bohrBlockEntity, world, pos);
                }
//                empty the bohrblock
                else {
                    bohrBlockEntity.scatterParticles();
                }
            }

        }
        return ActionResult.SUCCESS;
    }

    void createAtom(BohrBlockEntity bohrBlockEntity, World world, BlockPos pos) {
        int protons = bohrBlockEntity.getProtonCount();
        int neutrons = bohrBlockEntity.getNeutronCount();
        int electrons = bohrBlockEntity.getElectronCount();
        NucleusState nucleusState = NuclidesTable.getNuclide(protons, neutrons);
        if (protons == electrons && nucleusState != null && nucleusState.getAtomItem() != null && nucleusState.isStable()) {
            ItemStack itemStack = new ItemStack(nucleusState.getAtomItem());
            itemStack.setCount(1);
            DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(1);
            defaultedList.add(itemStack);
            ItemScatterer.spawn(world, pos.up(1), defaultedList);
            bohrBlockEntity.clear();
        }
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
}

