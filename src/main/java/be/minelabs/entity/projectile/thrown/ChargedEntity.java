package be.minelabs.entity.projectile.thrown;

import be.minelabs.block.Blocks;
import be.minelabs.block.blocks.TimeFreezeBlock;
import be.minelabs.entity.BohrBlueprintEntity;
import be.minelabs.item.Items;
import be.minelabs.util.Tags;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.World;

import java.util.List;

public abstract class ChargedEntity extends ThrownItemEntity {
    private static final TrackedData<Integer> CHARGE = DataTracker.registerData(ChargedEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> STUCK = DataTracker.registerData(ChargedEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private Vec3d field = Vec3d.ZERO;

    public static final int MAX_FIELD = 5;

    public final static int e_radius = 12;
    public static final float DEFAULT_SPEED = 0.3f;

    private int count = 0;


    public ChargedEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Summon/Spawn a new Entity
     *
     * @param world : in what world should we make the entity
     * @param pos   : position in the world to spawn the entity
     * @param stack : Item used for throwing
     */
    public ChargedEntity(EntityType<? extends ThrownItemEntity> type, World world, BlockPos pos, ItemStack stack) {
        this(type, world);
        setPosition(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
        setItem(stack);
    }

    /**
     * Make a new Thrown Entity
     *
     * @param owner : who threw the Entity/Item
     * @param world : what world did we do this in
     * @param stack : Item used for throwing
     */
    public ChargedEntity(EntityType<? extends ThrownItemEntity> entityType, LivingEntity owner, World world, ItemStack stack) {
        super(entityType, owner, world);
        setItem(stack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ELECTRON;
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
    public boolean canFreeze() {
        // Don't want to freeze the entity in "powdered snow"
        return false;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        this.kill();
        return true;
    }

    @Override
    public void kill() {
        if (getItem() == null || getItem().isOf(net.minecraft.item.Items.AIR)) {
            world.spawnEntity(new ItemEntity(world, getX(), getY() + 0.2, getZ(), new ItemStack(getDefaultItem())));
        } else {
            world.spawnEntity(new ItemEntity(world, getX(), getY() + 0.2, getZ(), getItem()));
        }
        super.kill();
    }

    @Override
    public void tick() {
        if (isStuck()) {
            this.setVelocity(Vec3d.ZERO);
            if (world.isClient() && count % 5 == 0) {
                world.addParticle(ParticleTypes.SNOWFLAKE, getX(), getY() + 0.1, getZ(), 0, -0.01, 0);
                count = 0;
            }
            count++;
        } else {
            // No need to check for freeze if we are already stuck
            Iterable<BlockPos> positions = BlockPos.iterateOutwards(getBlockPos(), e_radius, e_radius, e_radius);
            for (BlockPos pos : positions) {
                if (world.getBlockState(pos).isOf(Blocks.TIME_FREEZE_BLOCK)) {
                    if (world.getBlockState(pos).get(TimeFreezeBlock.LIT)) {
                        //"Force" a stop
                        setVelocity(Vec3d.ZERO);
                    }
                }
            }
        }


        List<Entity> entities = world.getOtherEntities(this,
                Box.of(this.getPos(), e_radius, e_radius, e_radius), entity -> entity instanceof ChargedEntity);

        field = Vec3d.ZERO;

        for (Entity entity : entities) {
            if (entity instanceof ChargedEntity charged) {
                double force = 8.987f * getCharge() * charged.getCharge() / squaredDistanceTo(charged);
                Vec3d vector = getPos().subtract(charged.getPos()).normalize(); // Vector between entities
                vector = vector.multiply(force); //scale vector with Force
                vector = vector.multiply(0.01);

                field = field.add(vector);

                if (field.length() >= MAX_FIELD) {
                    field = field.multiply(MAX_FIELD / field.length()); // SCALE TO MAX_FIELD
                }
            }
        }

        super.tick();
    }

    /**
     * Entity collision
     *
     * @param entityHitResult : info regarding the collision
     */
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (world.isClient)
            return;
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof BohrBlueprintEntity bohr) {
            bohr.onParticleCollision(this);
        } else if (entity instanceof CreeperEntity creeperEntity) {
            creeperEntity.setInvulnerable(true);
            creeperEntity.onStruckByLightning((ServerWorld) world, new LightningEntity(EntityType.LIGHTNING_BOLT, world));
            creeperEntity.extinguish();
            creeperEntity.setInvulnerable(false);
            this.discard();
        } else if (entity instanceof ChargedEntity charged) {
            onChargedEntityHit(charged);
        }

        super.onEntityHit(entityHitResult);
    }

    public void onChargedEntityHit(ChargedEntity entity) {
        setVelocity(Vec3d.ZERO);
        entity.setVelocity(Vec3d.ZERO);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockState state = world.getBlockState(blockHitResult.getBlockPos());
        if (state.getCollisionShape(EmptyBlockView.INSTANCE, blockHitResult.getBlockPos(), ShapeContext.absent()) == VoxelShapes.empty()) {
            // No collision keep moving
            return;
        }
        Vec3d sideHit = Vec3d.of(blockHitResult.getSide().getVector()); // Side that the entity hit
        addVelocity(getVelocity().multiply(sideHit));
        super.onBlockHit(blockHitResult);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isIn(Tags.Items.STICKY_ITEMS) && !isStuck()) {
            useSticky(player, stack);
            return ActionResult.success(player.world.isClient());
        } else if (stack.isOf(net.minecraft.item.Items.WATER_BUCKET) && isStuck()) {
            useCleanse(player, hand);
            return ActionResult.success(player.world.isClient());
        }

        return super.interact(player, hand);
    }

    private void useSticky(PlayerEntity player, ItemStack stack) {
        setStuck(true);
        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }
    }

    private void useCleanse(PlayerEntity player, Hand hand) {
        setStuck(false);
        if (!player.getAbilities().creativeMode) {
            player.setStackInHand(hand, new ItemStack(net.minecraft.item.Items.BUCKET));
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.setNoGravity(true);
        dataTracker.startTracking(CHARGE, 0);
        dataTracker.startTracking(STUCK, false);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        dataTracker.set(CHARGE, nbt.getInt("charge"));
        dataTracker.set(STUCK, nbt.getBoolean("stasis_frozen"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("charge", dataTracker.get(CHARGE));
        nbt.putBoolean("stasis_frozen", dataTracker.get(STUCK));
    }

    @Override
    protected ItemStack getItem() {
        return super.getItem();
    }

    public int getCharge() {
        return dataTracker.get(CHARGE);
    }

    protected void setCharge(int charge) {
        dataTracker.set(CHARGE, charge);
    }

    public boolean isStuck() {
        return dataTracker.get(STUCK);
    }

    public void setStuck(boolean bool) {
        dataTracker.set(STUCK, bool);
    }

    public Vec3d getField() {
        return field;
    }
}
