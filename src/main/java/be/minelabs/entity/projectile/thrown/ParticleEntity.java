package be.minelabs.entity.projectile.thrown;

import be.minelabs.advancement.criterion.CoulombCriterion;
import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.block.Blocks;
import be.minelabs.block.blocks.TimeFreezeBlock;
import be.minelabs.entity.Entities;
import be.minelabs.science.CoulombGson;
import be.minelabs.sound.SoundEvents;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.World;

import java.io.InputStreamReader;

public class ParticleEntity extends ChargedEntity {

    private CoulombGson data;


    public ParticleEntity(EntityType<? extends ParticleEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Summon/Spawn a new Entity
     *
     * @param world : in what world should we make the entity
     * @param pos   : position in the world to spawn the entity
     * @param stack : Item used for throwing
     */
    public ParticleEntity(World world, BlockPos pos, ItemStack stack) {
        this(Entities.PARTICLE_ENTITY, world);
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
    public ParticleEntity(LivingEntity owner, World world, ItemStack stack) {
        super(Entities.PARTICLE_ENTITY, owner, world);
        setItem(stack);
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

        addVelocity(getField());
        // TODO rework after actual movement :
        Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, getBlockPos(), 5, (condition) -> condition.test(CoulombCriterion.Type.MOVE));

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
        if (entity instanceof ParticleEntity charged) {
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
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        loadData(nbt.getString("data"));
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("data", getName().getString());
    }
}
