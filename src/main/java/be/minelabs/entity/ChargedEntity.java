package be.minelabs.entity;

import be.minelabs.advancement.criterion.CoulombCriterion;
import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.block.Blocks;
import be.minelabs.block.blocks.TimeFreezeBlock;
import be.minelabs.item.Items;
import be.minelabs.science.CoulombGson;
import be.minelabs.sound.SoundEvents;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.World;

import java.io.InputStreamReader;
import java.util.List;

public class ChargedEntity extends ThrownItemEntity {
    private static final TrackedData<Integer> CHARGE = DataTracker.registerData(ChargedEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public final static int e_radius = 12;
    public static final float DEFAULT_SPEED = 0.3f;

    private CoulombGson data;


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
    public ChargedEntity(World world, BlockPos pos, ItemStack stack) {
        this(Entities.CHARGED_ENTITY, world);
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
    public ChargedEntity(LivingEntity owner, World world, ItemStack stack) {
        super(Entities.CHARGED_ENTITY, owner, world);
        setItem(stack);
    }

    @Override
    public Item getDefaultItem() {
        return Items.ELECTRON;
    }

    private void loadData(String file) {
        setCustomName(Text.translatable(file));
        if (world.isClient)
            return;
        // item.minelabs.subatomic.name
        String[] split = file.split("\\.");
        file = "/data/minelabs/science/coulomb/" + split[split.length - 1] + ".json";
        CoulombGson json = new Gson().fromJson(JsonParser.parseReader(
                new InputStreamReader(getClass().getResourceAsStream(file))), CoulombGson.class);
        json.validate();
        setCharge(json.charge);
        this.data = json;
    }

    @Override
    public void setItem(ItemStack item) {
        super.setItem(item);
        loadData(item.getItem().getTranslationKey());
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
    public void tick() {
        Iterable<BlockPos> positions = BlockPos.iterateOutwards(getBlockPos(), e_radius, e_radius, e_radius);
        for (BlockPos pos : positions) {
            if (world.getBlockState(pos).isOf(Blocks.TIME_FREEZE_BLOCK)) {
                if (world.getBlockState(pos).get(TimeFreezeBlock.LIT)) {
                    //"Force" a stop
                    setVelocity(Vec3d.ZERO);
                    super.tick();
                    return;
                }
            }
        }

        if (world.isClient) {
            super.tick();
            return;
        }

        List<Entity> entities = world.getOtherEntities(this,
                Box.of(this.getPos(), e_radius, e_radius, e_radius), entity -> entity instanceof ChargedEntity);

        for (Entity entity : entities) {
            if (entity instanceof ChargedEntity chargedEntity) {
                double force = 8.987f * getCharge() * chargedEntity.getCharge() / squaredDistanceTo(chargedEntity);
                Vec3d vector = getPos().subtract(chargedEntity.getPos()).normalize(); // Vector between entities
                vector = vector.multiply(force / data.mass); //scale vector with Force and mass of atom
                vector = vector.multiply(0.0001);
                if (getVelocity().length() < 5) {
                    addVelocity(vector);
                }
                Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, getBlockPos(), 5, (condition) -> condition.test(CoulombCriterion.Type.MOVE));
            }
        }
        super.tick();
        tryDecay();
    }

    /**
     * Try to decay the entity
     * ONLY if the entity isn't stable
     * <p>
     * Called 20 times a second !
     */
    private void tryDecay() {
        if (!data.stable) {
            if (world.getRandom().nextFloat() < data.decay_chance) {
                ItemEntity item = new ItemEntity(world, getX(), getY() + .5, getZ(), getDecayStack());
                item.setVelocity(getVelocity().multiply(2));
                world.spawnEntity(item);
                Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, getBlockPos(), 5, (condition) -> condition.test(CoulombCriterion.Type.DECAY));
                playSound(SoundEvents.COULOMB_DECAY, 1f, 1f);
                if (data.shouldReplace()) {
                    setItem(data.getDecayReplacement());
                    return;
                }

                this.discard();
            }
        }
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
        if (entity instanceof ChargedEntity charged) {
            // Could do way more with this!
            if (data.getAntiItem() != null && charged.getItem().isOf(data.getAntiItem())) {
                ItemScatterer.spawn(getWorld(), getX(), getY(), getZ(), getAnnihilationStack());
                Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, getBlockPos(), 5,
                        (condition) -> condition.test(CoulombCriterion.Type.ANNIHILATE));
                playSound(SoundEvents.COULOMB_ANNIHILATE, 1f, 1f);
                this.discard();
                charged.discard();
            } else {
                setVelocity(Vec3d.ZERO);
                charged.setVelocity(Vec3d.ZERO);
            }
        } else if (entity instanceof BohrBlueprintEntity bohr) {
            bohr.onParticleCollision(this);
        } else if (entity instanceof CreeperEntity creeperEntity) {
            creeperEntity.setInvulnerable(true);
            creeperEntity.onStruckByLightning((ServerWorld) world, new LightningEntity(EntityType.LIGHTNING_BOLT, world));
            creeperEntity.extinguish();
            creeperEntity.setInvulnerable(false);
            this.discard();
        }
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

    /**
     * Get the Stack that drops when the entity annihilates
     *
     * @return ItemStack
     */
    private ItemStack getAnnihilationStack() {
        return data.getAnnihilationDrop();
    }

    /**
     * Get the Stack that drops when the entity decay's
     *
     * @return ItemStack
     */
    private ItemStack getDecayStack() {
        return data.getDecayDrop();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.setNoGravity(true);
        dataTracker.startTracking(CHARGE, 0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        dataTracker.set(CHARGE, nbt.getInt("charge"));
        loadData(nbt.getString("data"));
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("charge", dataTracker.get(CHARGE));
        nbt.putString("data", getName().getString());
    }

    public int getCharge() {
        return dataTracker.get(CHARGE);
    }

    public void setCharge(int charge) {
        dataTracker.set(CHARGE, charge);
    }

    /**
     * Get the field of this entity
     * direction vector * Force/mass
     *
     * @return Vec3d
     */
    public Vec3d getField() {
        Vec3d field = Vec3d.ZERO;

        List<Entity> entities = world.getOtherEntities(this,
                Box.of(this.getPos(), e_radius, e_radius, e_radius), entity -> entity instanceof ChargedEntity);

        for (Entity entity : entities) {
            if (entity instanceof ChargedEntity chargedEntity) {
                double force = 8.987f * getCharge() * chargedEntity.getCharge() / squaredDistanceTo(chargedEntity);
                Vec3d vector = getPos().subtract(chargedEntity.getPos()).normalize(); // Vector between entities
                vector = vector.multiply(force / data.mass); //scale vector with Force and mass of atom
                //vector = vector.multiply(0.0001);
                //if (field.length() < 5) {
                field = field.add(vector);
                //}
            }
        }
        return field;
    }

    /**
     * Is there enough "velocity" for the entity to be considered moving.
     *
     * @return Boolean
     */
    public boolean hasAField() {
        return getField().length() > 0.0001;
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
}
