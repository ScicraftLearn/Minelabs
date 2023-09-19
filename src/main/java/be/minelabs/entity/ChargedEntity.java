package be.minelabs.entity;

import be.minelabs.Minelabs;
import be.minelabs.advancement.criterion.CoulombCriterion;
import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.block.Blocks;
import be.minelabs.loot.LootTables;
import be.minelabs.science.CoulombGson;
import be.minelabs.sound.SoundEvents;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import java.io.*;
import java.util.List;

public class ChargedEntity extends ThrownEntity {
    private static final TrackedData<Integer> CHARGE = DataTracker.registerData(ChargedEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private final static int e_radius = 12;
    public static final float DEFAULT_SPEED = 1.5f;

    private CoulombGson data;


    public ChargedEntity(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Summon/Spawn a new Entity
     *
     * @param world : in what world should we make the entity
     * @param pos   : position in the world to spawn the entity
     * @param data  : String, with location to data file (json)
     */
    public ChargedEntity(World world, BlockPos pos, String data) {
        this(Entities.CHARGED_ENTITY, world);
        setPosition(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
        loadData(data);
    }

    /**
     * Make a new Thrown Entity
     *
     * @param owner : who threw the Entity/Item
     * @param world : what world did we do this in
     * @param data  : String, with location to data file (json)
     */
    public ChargedEntity(LivingEntity owner, World world, String data) {
        super(Entities.CHARGED_ENTITY, owner, world);
        loadData(data);
    }

    private void loadData(String file) {
        setCustomName(Text.translatable(file));
        if (world.isClient)
            return;
        // item.minelabs.subatomic.name
        String[] split = file.split("\\.");
        file = "/data/minelabs/science/coulomb/" + split[split.length-1] + ".json";
        CoulombGson json = new Gson().fromJson(JsonParser.parseReader(
                new InputStreamReader(getClass().getResourceAsStream(file))), CoulombGson.class);
        json.validate();
        setCharge(json.charge);
        this.data = json;
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
    public boolean damage(DamageSource source, float amount) {
        this.kill();
        return true;
    }

    @Override
    public void tick() {
        if (world.isClient) {
            super.tick();
            return;
        }

        Iterable<BlockPos> positions = BlockPos.iterateOutwards(getBlockPos(), e_radius, e_radius, e_radius);
        for (BlockPos pos : positions) {
            if (world.getBlockState(pos).isOf(Blocks.TIME_FREEZE_BLOCK)) {
                //"Force" a stop
                setVelocity(Vec3d.ZERO);
                super.tick();
                return;
            }
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
                this.discard();
                Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, getBlockPos(), 5, (condition) -> condition.test(CoulombCriterion.Type.DECAY));
                playSound(SoundEvents.COULOMB_DECAY, 1f, 1f);
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
        if (entityHitResult.getEntity() instanceof ChargedEntity charged) {
            // Could do way more with this!
            if (data.getAntiItem() != null && data.getAntiItem() == charged.getItem()) {
                ItemScatterer.spawn(getWorld(), getX(), getY(), getZ(), getAnnihilationStack());
                Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, getBlockPos(), 5,
                        (condition) -> condition.test(CoulombCriterion.Type.ANNIHILATE));
                playSound(SoundEvents.COULOMB_ANNIHILATE, 1f, 1f);
                this.discard();
                charged.discard();
            }
        } else if (entityHitResult.getEntity() instanceof BohrBlueprintEntity bohr) {
            bohr.onParticleCollision(this);
        }
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        if (!world.isClient && !state.isAir()) {
            setVelocity(Vec3d.ZERO); // invert velocity (should only happen on the block hit)
        }
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
        this.setNoGravity(true);
        dataTracker.startTracking(CHARGE, 0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        dataTracker.set(CHARGE, nbt.getInt("charge"));
        loadData(nbt.getString("data"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
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
     * Get the Item used to "spawn" the entity (saved in custom entity name)
     *
     * @return Item
     */
    public Item getItem() {
        // spliting on '.' : translation key: "item.minelabs.name"
        return Registries.ITEM.get(new Identifier(Minelabs.MOD_ID, getName().getString().split("\\.")[2]));
    }
}
