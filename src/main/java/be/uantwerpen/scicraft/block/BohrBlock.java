package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.BohrBlockEntity;
import be.uantwerpen.scicraft.entity.SubatomicParticle;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.util.NucleusState;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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
        NucleusState nucleusState = bohrBlockEntity.getNuclidesTable().getNuclide(protons, neutrons);
        if (protons == electrons && nucleusState != null && nucleusState.getAtomItem() != null && nucleusState.isStable()) {
            ItemStack itemStack = new ItemStack(nucleusState.getAtomItem());
            itemStack.setCount(1);
            DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(1);
            defaultedList.add(itemStack);
            ItemScatterer.spawn(world, pos.up(1), defaultedList);
            bohrBlockEntity.empty();
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

