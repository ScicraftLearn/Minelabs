package be.uantwerpen.minelabs.entity;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.advancement.criterion.BohrCriterion;
import be.uantwerpen.minelabs.advancement.criterion.Criteria;
import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.item.AtomItem;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.NucleusState;
import be.uantwerpen.minelabs.util.NuclidesTable;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

public class BohrBlueprintEntity extends Entity {
    // Constants
    private static final int MAX_PROTONS = 118;
    private static final int MAX_ELECTRONS = MAX_PROTONS;
    private static final int MAX_NEUTRONS = 176;
    private static final float MAX_INTEGRITY = 100;

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

    // ItemStack is used to track the currently built atom.
    protected static final TrackedData<ItemStack> RESULT_ATOM = DataTracker.registerData(BohrBlueprintEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    // Stability index of the currently formed configuration (whether it exists or not).
    // Instability of 0 means stable, higher values indicate how unstable and how fast integrity decays.
    protected static final TrackedData<Float> INSTABILITY = DataTracker.registerData(BohrBlueprintEntity.class, TrackedDataHandlerRegistry.FLOAT);


    // Integrity ranges from MAX_INTEGRITY to 0 where at 0 the configuration decomposes because it is too unstable.
    // Slowly decreases based on instability index.
    protected static final TrackedData<Float> INTEGRITY = DataTracker.registerData(BohrBlueprintEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private NucleusState nucleusState = null;

    public BohrBlueprintEntity(EntityType<? extends BohrBlueprintEntity> entityType, World world) {
        super(entityType, world);
    }

    public BohrBlueprintEntity(World world, BlockPos pos) {
        this(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, world);
        // entity position is center of bottom, the -0.5 offset aligns it back to the block grid
        setPosition(Vec3d.ofCenter(pos).add(0, -0.5, 0));
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.5f * dimensions.height;
    }

    @Override
    public void tick() {
        if (this.isRemoved())
            return;

        if (this.world.isClient){
            clientTick();
            return;
        }

        logicalTick();
    }

    /**
     * Only runs on version of entity used for rendering.
     */
    private void clientTick(){
    }

    /**
     * Runs on logical server (can either be in physical client or in physical server).
     */
    private void logicalTick() {
        this.attemptTickInVoid();

        // check if stil attached to bohr plate, otherwise clean up
        if (this.validityCheckCounter++ == 100) {
            this.validityCheckCounter = 0;
            // cleanup for if the entity unexpectedly got left behind after the block was removed
            if (!this.isAttachedToBlock()) {
                this.discard();
            }
        }

        // stability and integrity update
        if (!isStable()){
            decrementIntegrity(getInstability() / 20f);
        }
    }

    // Called by subatomic particle when it collides with this entity.
    public void onParticleCollision(SubatomicParticleEntity particle, ServerPlayerEntity thrower) {
        if (world.isClient)
            return;
        ItemStack stack = particle.getStack();
        Item item = stack.getItem();

        if (addItem(item)) {
            particle.discard();
            Criteria.BOHR_CRITERION.trigger(thrower, BohrCriterion.Type.ADD_PARTICLE);
        }
    }

    public BlockPos getBohrBlueprintPos() {
        return getBlockPos().down();
    }

    public boolean isAttachedToBlock() {
        return world.getBlockState(getBohrBlueprintPos()).isOf(Blocks.BOHR_BLUEPRINT);
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(PROTONS, 0);
        dataTracker.startTracking(ELECTRONS, 0);
        dataTracker.startTracking(NEUTRONS, 0);
        dataTracker.startTracking(RESULT_ATOM, ItemStack.EMPTY);
        dataTracker.startTracking(INSTABILITY, 0f);
        dataTracker.startTracking(INTEGRITY, MAX_INTEGRITY);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        // cleanup after entity is removed
        dropContents();
        BlockState state = world.getBlockState(getBohrBlueprintPos());
        if (state.isOf(Blocks.BOHR_BLUEPRINT))
            world.removeBlock(getBohrBlueprintPos(), false);
    }

    public boolean isEmpty() {
        // inventory is not synced to client, so need to check counts.
        return getProtons() == 0 && getNeutrons() == 0 && getElectrons() == 0;
    }

    public void dropContents() {
        for (ItemStack stack : inventory) {
            dropStack(stack);
        }
        clear();
    }

    public void dropLastItem(ServerPlayerEntity player) {
        if (inventory.isEmpty())
            return;

        ItemStack stack = inventory.pop();
        onItemRemoved(stack);
        dropStack(stack);
        if (stack.getItem() instanceof AtomItem)
            Criteria.BOHR_CRITERION.trigger(player, BohrCriterion.Type.REMOVE_ATOM);
        else Criteria.BOHR_CRITERION.trigger(player, BohrCriterion.Type.REMOVE_PARTICLE);
    }

    private boolean canAcceptItem(Item item) {
        // Atom can only be first item inserted
        if (item instanceof AtomItem && inventory.isEmpty())
            return true;

        // Subatomic particles have max capacity
        if (item.equals(Items.PROTON))
            return getProtons() < MAX_PROTONS;
        if (item.equals(Items.NEUTRON))
            return getNeutrons() < MAX_NEUTRONS;
        if (item.equals(Items.ELECTRON))
            return getElectrons() < MAX_ELECTRONS;

        return false;
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
        // we don't want the client to modify inventory. Always return false because we can't check inventory for remove.
        if (world.isClient)
            return false;

        if (isRemovalItem(item))
            return removeItem(getAntiItem(item));

        if (!canAcceptItem(item))
            return false;

        ItemStack stack = new ItemStack(item, 1);
        inventory.add(stack);
        onItemAdded(stack);
        return true;
    }

    private boolean removeItem(Item item) {
        // we don't want the client to modify inventory
        if (world.isClient)
            return false;

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

    private void clear() {
        inventory.clear();
        // sets everything to zero
        updateCountsFromContent();
    }

    /**
     * Tries to craft the atom and clear inventory on success. Otherwise, nothing changes and return empty stack.
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

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    @Override
    public boolean canAvoidTraps() {
        // so it ignores tripwires and pressure plates.
        return true;
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
            if (source.getAttacker() instanceof ServerPlayerEntity player) {
                onHitByPlayer(player);
                return true;
            }
        }
        return false;
    }

    private void onHitByPlayer(ServerPlayerEntity player) {
        dropLastItem(player);
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
        inventory.clear();
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

    @Nullable
    private Item computeAtomItem() {
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

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);

        if(PROTONS.equals(data) || NEUTRONS.equals(data) || ELECTRONS.equals(data)){
            compositionChanged();
        }
    }

    /**
     * Update atom and stability info only once.
     */
    private void compositionChanged() {
        // nucleusState is not synced from server to client. We compute it in the client ourselves.
        nucleusState = NuclidesTable.getNuclide(getProtons(), getNeutrons());

        // server only from here on
        if (world.isClient) return;

        Item item = computeAtomItem();  // it's ok if this is null. The ItemStack will be the empty stack.
        ItemStack stack = new ItemStack(item, 1);
        dataTracker.set(RESULT_ATOM, stack);

        // TODO: compute instability with nuclides
        float instability = 0f;
        if (nucleusState != null && !nucleusState.isStable()) {
            instability = 1f;
        }
        if (getProtons() != getElectrons()){
            instability = 1f;
        }
        setInstability(instability);
        if (instability == 0){
            setIntegrity(MAX_INTEGRITY);
        }
    }

    public NucleusState getNucleusState(){
        return nucleusState;
    }

    @Nullable
    public Item getAtomItem() {
        ItemStack stack = dataTracker.get(RESULT_ATOM);

        if (!stack.isEmpty())
            return stack.getItem();

        return null;
    }

    public float getIntegrity(){
        return dataTracker.get(INTEGRITY);
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

    private void setIntegrity(float value){
        dataTracker.set(INTEGRITY, value);
    }

    private void decrementIntegrity(float value){
        setIntegrity(getIntegrity() - value);
    }

    private float getInstability(){
        return dataTracker.get(INSTABILITY);
    }

    public boolean isStable(){
        return getInstability() == 0;
    }

    private void setInstability(float value){
        dataTracker.set(INSTABILITY, value);
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
