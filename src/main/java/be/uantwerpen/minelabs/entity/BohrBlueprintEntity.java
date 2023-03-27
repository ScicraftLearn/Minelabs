package be.uantwerpen.minelabs.entity;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.item.AtomItem;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.NucleusState;
import be.uantwerpen.minelabs.util.NuclidesTable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

public class BohrBlueprintEntity extends Entity {

    // Transient data (not persisted or tracked)
    private int validityCheckCounter = 0;

    // server side information (only stored in nbt)

    // Ordered list of items added to the entity. Should only contain protons, electrons neutrons and atoms.
    // Anti particles are instead removed and can never be added if corresponding particle is not present.
    // Atom can only be added as first entry and cannot remove parts of it.
    Stack<ItemStack> inventory = new Stack<>();

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

        validityTick();
    }

    private void validityTick() {
        this.attemptTickInVoid();
        if (this.validityCheckCounter++ == 100) {
            this.validityCheckCounter = 0;
            // cleanup for if the entity unexpectedly got left behind after the block was removed
            if (!this.isAttachedToBlock()) {
                this.discard();
            }
        }
    }

    // Called by subatomic particle when it collides with this entity.
    public void onParticleCollision(SubatomicParticle particle) {
        ItemStack stack = particle.getStack();
        Item item = stack.getItem();

        if (addItem(item)) {
            particle.discard();
        }
    }

    public boolean isAttachedToBlock() {
        return world.getBlockState(getBlockPos().down()).isOf(Blocks.BOHR_BLOCK);
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
        dropContents();
    }

    public boolean isEmpty(){
        return inventory.isEmpty();
    }

    public void dropContents() {
        for (ItemStack stack : inventory) {
            dropStack(stack);
        }
        clear();
    }

    public void dropLastItem(){
        if (inventory.isEmpty())
            return;

        ItemStack stack = inventory.pop();
        onItemRemoved(stack);
        dropStack(stack);
    }

    private boolean canAcceptItem(Item item) {
        return (item instanceof AtomItem && inventory.isEmpty()) || List.of(Items.PROTON, Items.NEUTRON, Items.ELECTRON).contains(item);
    }

    private static boolean isRemovalItem(Item item) {
        return List.of(Items.ANTI_PROTON, Items.ANTI_NEUTRON, Items.POSITRON).contains(item);
    }

    @Nullable
    private static Item getAntiItem(Item item) {
        return Map.of(
                Items.ANTI_PROTON, Items.PROTON,
                Items.ANTI_NEUTRON, Items.NEUTRON,
                Items.POSITRON, Items.ELECTRON
        ).get(item);
    }

    public boolean addItem(Item item) {
        if (isRemovalItem(item))
            return removeItem(getAntiItem(item));

        if (!canAcceptItem(item))
            return false;

        // Optional can merge with last stack on list if same type
        ItemStack stack = new ItemStack(item, 1);
        inventory.add(stack);
        onItemAdded(stack);
        return true;
    }

    private boolean removeItem(Item item) {
        // iterate in reverse order
        for (ListIterator<ItemStack> iterator = inventory.listIterator(inventory.size()); iterator.hasPrevious(); ) {
            ItemStack stack = iterator.previous();
            if (stack.isOf(item)) {
                iterator.remove();
                onItemRemoved(stack);
                return true;
            }
        }
        return false;
    }

    private void onItemAdded(ItemStack stack) {
        if (stack.isOf(Items.PROTON)) incrementProtons(1);
        else if (stack.isOf(Items.ELECTRON)) incrementElectrons(1);
        else if (stack.isOf(Items.NEUTRON)) incrementNeutrons(1);
        else if (stack.getItem() instanceof AtomItem) updateCountsFromContent();
    }

    private void onItemRemoved(ItemStack stack) {
        if (stack.isOf(Items.PROTON)) incrementProtons(-1);
        else if (stack.isOf(Items.ELECTRON)) incrementElectrons(-1);
        else if (stack.isOf(Items.NEUTRON)) incrementNeutrons(-1);
        else if (stack.getItem() instanceof AtomItem) updateCountsFromContent();
    }

    private void clear(){
        inventory.clear();
        // sets everything to zero
        updateCountsFromContent();
    }

    /**
     * Tries to craft the atom and clear inventory on success. Otherwise nothing changes and return empty stack.
     */
    public ItemStack craftAtom() {
        Item item = getAtomItem();
        if (item != null) {
            ItemStack stack = new ItemStack(item, 1);
            clear();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    private Item getAtomItem() {
        int protons = getProtons();
        int neutrons = getNeutrons();
        int electrons = getElectrons();

        if (protons == 0 || protons != electrons)
            return null;

        NucleusState nucleusState = NuclidesTable.getNuclide(protons, neutrons);
        if (nucleusState == null || !nucleusState.isStable())
            return null;

        return nucleusState.getAtomItem();
    }

    @Override
    public boolean canHit() {
        return true;
    }

    /**
     * Returning true prevents the attack from happening.
     */
    @Override
    public boolean handleAttack(Entity attacker) {
        return isEmpty();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!this.isRemoved() && !this.world.isClient) {
            if (source.getAttacker() instanceof PlayerEntity)
                onHitByPlayer();
        }
        return true;
    }

    private void onHitByPlayer(){
        dropLastItem();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        // save inventory
        NbtList nbtList = new NbtList();
        for (ItemStack stack : inventory) {
            if (stack.isEmpty()) continue;
            nbtList.add(stack.writeNbt(new NbtCompound()));
        }
        nbt.put("Items", nbtList);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        // load inventory
        NbtList nbtList = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            inventory.add(ItemStack.fromNbt(nbtCompound));
        }
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

    private void incrementProtons(int value) {
        setProtons(getProtons() + value);
    }

    private void incrementNeutrons(int value) {
        setNeutrons(getNeutrons() + value);
    }

    private void incrementElectrons(int value) {
        setElectrons(getElectrons() + value);
    }
}
