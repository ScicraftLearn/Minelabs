package be.uantwerpen.minelabs.block.entity;

import be.uantwerpen.minelabs.inventory.ImplementedInventory;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.MinelabsProperties;
import be.uantwerpen.minelabs.util.NucleusState;
import be.uantwerpen.minelabs.util.NuclidesTable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BohrBlockEntity extends BlockEntity implements ImplementedInventory {

    private static final int MAX_TIMER = 99 * 20;

    private int timer = MAX_TIMER;

    //the inventory stack [0,3[ = protons, [3,6[ = neutrons, [6,9[ = electrons
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public BohrBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.BOHR_BLOCK_ENTITY, pos, state);
    }

    /**
     * counts all the protons in the block
     *
     * @return nr of protons in the block
     */
    public int getProtonCount() {
        int count = 0;
        // slot 0 to 2
        for (int slot = 0; slot < 3; slot++)
            // if there is protons
            if (getItems().get(slot).getItem() == Items.PROTON) {
                // add to sum
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
        // slot 3 to 5
        for (int slot = 3; slot < 6; slot++)
            // if neutrons in the stack
            if (getItems().get(slot).getItem() == Items.NEUTRON) {
                // add them in the count
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
        // slot 6 to 8
        for (int slot = 6; slot < 9; slot++)
            // if electron in slot
            if (getItems().get(slot).getItem() == Items.ELECTRON) {
                count += getItems().get(slot).getCount();
            }
        return count;
    }

    public static void tick(World world, BlockPos pos, BlockState state, BohrBlockEntity entity) {
        if (world.isClient) {
            return;
        }

        // normal texture
        int status = 0;

        int nrOfProtons = entity.getProtonCount();
        int nrOfNeutrons = entity.getNeutronCount();
        int nrOfElectrons = entity.getElectronCount();

        if (NuclidesTable.isStable(nrOfProtons, nrOfNeutrons, nrOfElectrons)) {
            entity.timer = MAX_TIMER; // max timer value
            if (entity.isCollectable()) {// collectible -> green texture
                status = 1;
            }
        } else { // unstable => red texture
            status = 2;
            entity.timer = Math.max(0, entity.timer - 1);
        }
        if (entity.timer == 0) {
            NbtCompound nbtCompound = entity.createNbt();
            entity.writeNbt(nbtCompound);
            entity.scatterParticles(3);
            entity.timer = MAX_TIMER;
            markDirty(world, pos, state);
        }

        world.setBlockState(pos, state.with(MinelabsProperties.STATUS, status));
        world.updateListeners(pos, state, state.with(MinelabsProperties.STATUS, status), Block.NOTIFY_LISTENERS);
    }

    /**
     * @return itemstacks of the plate
     */
    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
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

    /**
     * needs to be called on inventory change
     */
    @Override
    public void markDirty() {
        super.markDirty();
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

    /**
     * inserts one particle of item
     *
     * @param item the item to insert
     * @return success or failure
     */
    public ActionResult insertParticle(Item item) {

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
        // if the inventory is empty initialize the inventory with 0 items
        if (items.get(slot).isEmpty()) {
            // the item that the player was holding
            items.set(slot, new ItemStack(item));
            // set the counter for the item on 0
            items.get(slot).setCount(0);
        }
        // if the stack isn't full
        if (items.get(slot).getCount() < 64) {
            // add 1 to the inventory
            items.get(slot).setCount(items.get(slot).getCount() + 1);
        }
        markDirty();
        return ActionResult.SUCCESS;
    }

    public void scatterParticles() {
        this.scatterParticles(1);
    }

    /**
     * Removes a particle (proton, neutron or electron depending on passed param) from the bohrblock.
     *
     * @param item : item particle to be removed.
     */
    public ActionResult removeParticle(Item item) {

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
     * scatter the items above the bohrplate
     *
     * @param distance, nr of blocks up
     */
    public void scatterParticles(int distance) {

//        0.5 east and 0.5 south
        ItemScatterer.spawn(Objects.requireNonNull(getWorld()), getPos().up(distance).add(0.5f, 0, 0.5f), items);
        markDirty();
    }


    /**
     * creates and spawns the atom
     *
     * @param world the world of the bohrplate
     * @param pos   the position of the bohrblock
     */
    public void createAtom(World world, BlockPos pos) {

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

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        nbt.putInt("Timer", timer);
        super.writeNbt(nbt);
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        //TODO
        // INSERT INTO CORRECT SLOTS
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        //TODO
        // EXTACTABLE WHEN COLLECTABLE
        return false;
    }
}