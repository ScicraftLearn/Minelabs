package be.uantwerpen.minelabs.block.entity;

import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.inventory.ImplementedInventory;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.MinelabsProperties;
import be.uantwerpen.minelabs.util.NucleusState;
import be.uantwerpen.minelabs.util.NuclidesTable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BohrBlockEntity extends BlockEntity implements ImplementedInventory {

    //    Color for rendering the text
    public static final int WHITE_COLOR = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    public static final int GREEN_COLOR = ColorHelper.Argb.getArgb(255, 0, 255, 0);
    public static final int RED_COLOR = ColorHelper.Argb.getArgb(255, 255, 0, 0);
    public static final int MAX_TIMER = 99 * 20;
    private int timer = MAX_TIMER;
    private BlockPos masterPos;

    // status: 0 = normal, 1 = atom collectible, 2 = atom unstable

    //the inventory stack [0,3[ = protons, [3,6[ = neutrons, [6,9[ = electrons
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public BohrBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.BOHR_BLOCK_ENTITY, pos, state);
    }

    public static List<BlockPos> getBohrParts(BlockPos pos, World world) {
        List<BlockPos> poses = new ArrayList<>();
        if (world.getBlockState(pos).getBlock() == Blocks.BOHR_BLOCK) {
            poses.add(pos);
        }
        for (Direction dir : Properties.HORIZONTAL_FACING.getValues()) {
            if (world.getBlockState(pos.offset(dir)).getBlock() == Blocks.BOHR_BLOCK) {
                poses.add(pos.offset(dir));
            }
            if (dir == Direction.NORTH || dir == Direction.SOUTH) {
                if (world.getBlockState(pos.offset(dir).offset(Direction.EAST)).getBlock() == Blocks.BOHR_BLOCK) {
                    poses.add(pos.offset(dir));
                }
                if (world.getBlockState(pos.offset(dir).offset(Direction.WEST)).getBlock() == Blocks.BOHR_BLOCK) {
                    poses.add(pos.offset(dir));
                }
            } else if (dir == Direction.EAST || dir == Direction.WEST) {
                if (world.getBlockState(pos.offset(dir).offset(Direction.NORTH)).getBlock() == Blocks.BOHR_BLOCK) {
                    poses.add(pos.offset(dir));
                }
                if (world.getBlockState(pos.offset(dir).offset(Direction.SOUTH)).getBlock() == Blocks.BOHR_BLOCK) {
                    poses.add(pos.offset(dir));
                }
            }
        }
        return poses;
    }

    /**
     * counts all the protons in the block
     *
     * @return nr of protons in the block
     */
    public int getProtonCount() {
        int count = 0;
//        slot 0 to 2
        for (int slot = 0; slot < 3; slot++)
//            if there is protons
            if (getItems().get(slot).getItem() == Items.PROTON) {
//                add to sum
                count += getItems().get(slot).getCount();
            }
        return count;
    }

    /**
     * counts all the neutrons in the block
     *
     * @return nr of neutrons in the block
     */
    public int getNeutronCount() {
        int count = 0;
//        slot 3 to 5
        for (int slot = 3; slot < 6; slot++)
//            if neutrons in the stack
            if (getItems().get(slot).getItem() == Items.NEUTRON) {
//                add them in the count
                count += getItems().get(slot).getCount();
            }
        return count;
    }

    /**
     * counts all the electrons in the block
     *
     * @return nr of electrons in the block
     */
    public int getElectronCount() {
        int count = 0;
//        slot 6 to 8
        for (int slot = 6; slot < 9; slot++)
//            if electron in slot
            if (getItems().get(slot).getItem() == Items.ELECTRON) {
                count += getItems().get(slot).getCount();
            }
        return count;
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, BohrBlockEntity entity) {
        if (world.isClient()) {
            return;
        }

        int nrOfProtons = entity.getProtonCount();
        int nrOfNeutrons = entity.getNeutronCount();
        int nrOfElectrons = entity.getElectronCount();
        if (NuclidesTable.isStable(nrOfProtons, nrOfNeutrons, nrOfElectrons)) {
            entity.timer = MAX_TIMER; // max timer value
        } else {
            entity.timer = Math.max(0, entity.timer - 1);
        }

        if (entity.timer == 0) {
            entity.scatterParticles(3);
            entity.timer = MAX_TIMER;
        }


        int status = 0;
        if (entity.isCollectable()) {
            status = 1;
        } else if (!NuclidesTable.isStable(entity.getProtonCount(), entity.getNeutronCount(), entity.getElectronCount())) {
            status = 2;
        }
        world.setBlockState(blockPos, state.with(MinelabsProperties.STATUS, status));
    }

    /**
     * @return itemstacks of the plate
     */
    @Override
    public DefaultedList<ItemStack> getItems() {
//        return items if master otherwise return the items of the master
        BohrBlockEntity master = getMaster(world);
        if (master == null) {
            return DefaultedList.of();
        }
        return isMaster() ? items : master.items;
    }

    /**
     * Determines the help messages (for neutrons and electrons) to be shown to the player while building an atom.
     *
     * @param nrOfProtons   : amount of protons
     * @param nrOfNeutrons  : amount of neutrons
     * @param nrOfElectrons : amount of electrons
     * @return : array of two values (size=2, type=String) = the neutron help and the electron help message.
     */
    public ArrayList<String> getBuildHelp(int nrOfProtons, int nrOfNeutrons, int nrOfElectrons) {

        int neutronDiff;
        int electronDiff;
        String neutronHelp = "";
        String electronHelp = "";
        neutronDiff = NuclidesTable.findNextStableAtom(nrOfProtons, false); // here: amount of total neutrons we need
        if (neutronDiff != -1 && (neutronDiff != nrOfNeutrons)) {
            neutronDiff = nrOfNeutrons - neutronDiff; // here: the actual difference between what we need and what we have
            if (neutronDiff < 0) {
                neutronDiff = Math.abs(neutronDiff);
                neutronHelp = "Add ";
            } else {
                neutronHelp = "Remove ";
            }
            neutronHelp += neutronDiff + " neutron";
            if (neutronDiff > 1) {
                neutronHelp += "s";
            }
        }

        if (Math.abs(nrOfProtons - nrOfElectrons) > 5) {
            if (!neutronHelp.isEmpty()) {
                electronHelp += " and ";
            }
            if (nrOfProtons - nrOfElectrons > 0) {
                electronDiff = nrOfProtons - nrOfElectrons - 5;
                electronHelp += "add ";
            } else {
                electronDiff = nrOfElectrons - (nrOfProtons + 5);
                electronHelp += "remove ";
            }
            electronHelp += electronDiff + " electron";
            if (electronDiff > 1) {
                electronHelp += "s";
            }
        }
        return new ArrayList<>(Arrays.asList(neutronHelp, electronHelp));
    }

    public boolean isMaster() {
        return this.getCachedState().get(MinelabsProperties.MASTER);
    }

    /**
     * renders the text of the bohrplate status
     */
    public void renderText() {
//        if not the master, execute in the master
        if (!isMaster()) {
            Objects.requireNonNull(getMaster(world)).renderText();
            return;
        }
        assert world != null;

        int nrOfProtons = getProtonCount();
        int nrOfNeutrons = getNeutronCount();
        int nrOfElectrons = getElectronCount();

        String neutronHelp = "";
        String electronHelp = "";

        MatrixStack matrixStack = new MatrixStack();
        NucleusState nuclideStateInfo = NuclidesTable.getNuclide(nrOfProtons, nrOfNeutrons);
        String protonString = "#Protons: " + nrOfProtons;
        String electronString = "#Electrons: " + nrOfElectrons;
        String neutronString = "#Neutrons: " + nrOfNeutrons;
        String atomName = "None";
        String symbol = "None";
        String mainDecayMode = "Unstable";
        String ionicCharge = NuclidesTable.calculateIonicCharge(nrOfProtons, nrOfElectrons);
        String ion = "ion: " + ionicCharge;
        int color = RED_COLOR;
        boolean doesStableNuclideExist = true;

        if (nuclideStateInfo != null) {
            atomName = nuclideStateInfo.getAtomName();
            symbol = nuclideStateInfo.getSymbol();
//            mainDecayMode = nuclideStateInfo.getMainDecayMode();

            if (NuclidesTable.isStable(nrOfProtons, nrOfNeutrons, nrOfElectrons)) {
                color = GREEN_COLOR;
                mainDecayMode = "Stable";
            }
            else {
                doesStableNuclideExist = false;
            }
        }
        else {
            doesStableNuclideExist = false;
        }
        if (!doesStableNuclideExist) {
            ArrayList<String> buildHelp = getBuildHelp(nrOfProtons, nrOfNeutrons, nrOfElectrons);
            neutronHelp = buildHelp.get(0);
            electronHelp = buildHelp.get(1);
        }
        String atomInfo = mainDecayMode + "    " + atomName + "    " + symbol + "    " + ion + "    Timer: " + this.timer / 20;
        String helpInfo = neutronHelp + electronHelp + " to stabilise.";
        /*
         * Rendering of text:
         */
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, atomInfo, 10, 10, color);
        if (!neutronHelp.isEmpty() || !electronHelp.isEmpty()) {
            MinecraftClient.getInstance().textRenderer.draw(matrixStack, helpInfo, 10, 20, RED_COLOR);
        }
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, protonString, 10, 30, WHITE_COLOR);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, neutronString, 10, 40, WHITE_COLOR);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, electronString, 10, 50, WHITE_COLOR);
    }

    /**
     * needs to be called on inventory change
     */
    @Override
    public void markDirty() {
        super.markDirty();
    }

    /**
     * getter for the master/base block of the plate
     *
     * @param world the world to search the master in
     * @return the base block of the plate
     */
    @Nullable
    public BohrBlockEntity getMaster(World world) {
        for (BlockPos pos : getBohrParts(pos, world)) {
            if (world.getBlockState(pos).isOf(Blocks.BOHR_BLOCK) && world.getBlockState(pos).get(MinelabsProperties.MASTER)) {
                if (world.getBlockEntity(pos) instanceof BohrBlockEntity entity && entity != this) {
                    return entity;
                }
            }
        }
        /*
        if (this.getCachedState().getBlock() instanceof BohrBlock && this.getCachedState().get(MinelabsProperties.MASTER)) {
            BlockPos blockPos = getMasterPos(world, this.getCachedState(), pos);
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof BohrBlockEntity bohrBlockEntity && world.getBlockState(blockPos).get(MinelabsProperties.MASTER)) {
                return bohrBlockEntity;
            } else {
                world.removeBlock(pos, false);

//                System.out.println("No base bohr block found");
//                world.breakBlock(getPos(), false);
                return null;
            }
        }*/
        return this;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    /**
     * @return true if an atom can be collected from the plate
     */
    public boolean isCollectable() {
        int protons = getProtonCount();
        int neutrons = getNeutronCount();
        int electrons = getElectronCount();
        NucleusState nucleusState = NuclidesTable.getNuclide(protons, neutrons);
        return protons == electrons && nucleusState != null && nucleusState.getAtomItem() != null && nucleusState.isStable() && protons != 0;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.items);
        timer = nbt.getInt("Timer");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Timer", timer);
        Inventories.writeNbt(nbt, this.items);
    }

    /**
     * inserts one particle of item
     *
     * @param item the item to insert
     * @return success or failure
     */
    public ActionResult insertParticle(Item item) {
        if (!isMaster()) {
            BohrBlockEntity master = getMaster(world);
            if (master != null) return master.insertParticle(item);
            else {
                return ActionResult.FAIL;
            }
        }

        int slot, times_looped = 0;

        if (item == Items.PROTON) {
            slot = 0;
        } else if (item == Items.NEUTRON) {
            slot = 3;
        } else if (item == Items.ELECTRON) {
            slot = 6;
        } else {
            return ActionResult.FAIL;
        }

        while (items.get(slot).getCount() == 64 && items.get(slot).getItem() == item) {
            slot += 1;
            times_looped += 1;
            if (times_looped == 3) return ActionResult.FAIL;
        }
        assert items.get(slot).getCount() < 64;
//            if the inventory is empty initialize the inventory with 0 items
        if (items.get(slot).isEmpty()) {
//                the item that the player was holding
            items.set(slot, new ItemStack(item));
//                set the counter for the item on 0
            items.get(slot).setCount(0);
        }
//            if the stack isn't full
        if (items.get(slot).getCount() < 64) {
//                add 1 to the inventory
            items.get(slot).setCount(items.get(slot).getCount() + 1);
        }
        markDirty();
        return ActionResult.SUCCESS;
    }


    public void scatterParticles() {
        this.scatterParticles(1);
    }

    /**
     * scatter the items above the bohrplate
     *
     * @param distance, nr of blocks up
     */
    public void scatterParticles(int distance) {
        if (!isMaster()) {
            BohrBlockEntity master = getMaster(world);
            if (master != null) {
                master.scatterParticles();
            } else {
                return;
            }
        }
//        0.5 east and 0.5 south
        ItemScatterer.spawn(Objects.requireNonNull(getWorld()), getPos().up(distance).add(0.5f, 0, 0.5f), items);
        markDirty();
    }

    /**
     * Removes a particle (proton, neutron or electron depending on passed param) from the bohrblock.
     *
     * @param item : item particle to be removed.
     */
    public ActionResult removeParticle(Item item) {
        if (!isMaster()) {
            BohrBlockEntity master = getMaster(world);
            if (master != null) {
                master.removeParticle(item);
            } else {
                return ActionResult.FAIL;
            }
        }
        int index;
        if (List.of(Items.ANTI_PROTON, Items.ANTI_NEUTRON, Items.POSITRON).contains(item)) {
            if (item == Items.ANTI_PROTON) {
                index = 0;
                item = Items.PROTON;
            } else if (item == Items.ANTI_NEUTRON) {
                index = 3;
                item = Items.NEUTRON;
            } else if (item == Items.POSITRON) {
                index = 6;
                item = Items.ELECTRON;
            } else return ActionResult.FAIL;
            for (int offset = 0; offset < 3; offset++) {

                if (items.get(index).getCount() > 0 && item == items.get(index + offset).getItem()) {
                    items.get(index + offset).setCount(items.get(index + offset).getCount() - 1);
                    markDirty();
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.FAIL;
    }

    /**
     * creates and spawns the atom
     *
     * @param world the world of the bohrplate
     * @param pos   the position of the bohrblock
     */
    public void createAtom(World world, BlockPos pos) {
        if (!isMaster()) {
            BohrBlockEntity master = getMaster(world);
            if (master != null) {
                master.createAtom(world, pos);
            } else {
                return;
            }
        }
        int protons = getProtonCount();
        int neutrons = getNeutronCount();
        NucleusState nucleusState = NuclidesTable.getNuclide(protons, neutrons);
        if (isCollectable()) {
            assert nucleusState != null;
            ItemStack itemStack = new ItemStack(nucleusState.getAtomItem());
            itemStack.setCount(1);
            DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(1);
            defaultedList.add(itemStack);
            ItemScatterer.spawn(world, pos.up(1).add(0.5f, 0, 0.5f), defaultedList);
            clear();
            markDirty();
        }
    }
}
