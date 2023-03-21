package be.uantwerpen.minelabs.entity;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.item.AtomItem;
import be.uantwerpen.minelabs.item.Items;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BohrBlueprintEntity extends Entity {

    //////////////////////////////////////////////////////////
    //      server side information (only stored in nbt)    //
    //////////////////////////////////////////////////////////
    private int validityCheckCounter = 0;

    // sorted list of items added to the entity. Should only contain protons, electrons neutrons and atoms.
    // anti particles are instead removed and can never be added if corresponding particle is not present.
    DefaultedList<ItemStack> inventory = DefaultedList.of();

    // tracked data is synced to the client automatically (still needs to be written to nbt if it needs to be persisted)
    protected static final TrackedData<Integer> PROTONS = DataTracker.registerData(BohrBlueprintEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> ELECTRONS = DataTracker.registerData(BohrBlueprintEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> NEUTRONS = DataTracker.registerData(BohrBlueprintEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public BohrBlueprintEntity(EntityType<? extends BohrBlueprintEntity> entityType, World world) {
        super(entityType, world);
    }

    public BohrBlueprintEntity(World world, BlockPos pos) {
        super(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, world);
        setPosition(Vec3d.ofCenter(pos));
    }

    public int getProtons() {
        return dataTracker.get(PROTONS);
    }

    public int getElectrons() {
        return dataTracker.get(ELECTRONS);
    }

    public int getNeutrons() {
        return dataTracker.get(NEUTRONS);
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.5f * dimensions.height;
    }

    @Override
    public void tick() {
        if (this.world.isClient)
            return;

        if (this.isRemoved())
            return;

        this.attemptTickInVoid();
        if (this.validityCheckCounter++ == 100) {
            this.validityCheckCounter = 0;
            // cleanup for if the entity unexpectedly got left behind after the block was removed
            if (!this.isAttachedToBlock()) {
                this.discard();
            }
        }
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(PROTONS, 0);
        dataTracker.startTracking(ELECTRONS, 0);
        dataTracker.startTracking(NEUTRONS, 0);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    public boolean isAttachedToBlock() {
        return world.getBlockState(getBlockPos().down()).isOf(Blocks.BOHR_BLOCK);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        updateCountsFromContent();
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    /**
     * Sets the amount of protons, electrons and neutrons based on the inventory.
     */
    private void updateCountsFromContent() {
        int protons = 0, electrons = 0, neutrons = 0;
        for (ItemStack stack : inventory) {
            if (stack.isOf(Items.PROTON)) protons++;
            else if (stack.isOf(Items.ELECTRON)) electrons++;
            else if (stack.isOf(Items.NEUTRON)) neutrons++;
            else if (stack.getItem() instanceof AtomItem atomItem) {
                protons += atomItem.getAtom().getAtomNumber();
                electrons += atomItem.getAtom().getAtomNumber();
                neutrons += atomItem.getAtom().getInitialNeutrons();
            } else {
                Minelabs.LOGGER.warn("Incompatible item found in bohr plate: " + stack);
            }
        }
        setProtons(protons);
        setElectrons(electrons);
        setNeutrons(neutrons);
    }

    private void setProtons(int value) {
        dataTracker.set(PROTONS, value);
    }

    private void setElectrons(int value) {
        dataTracker.set(ELECTRONS, value);
    }

    private void setNeutrons(int value) {
        dataTracker.set(NEUTRONS, value);
    }

}
