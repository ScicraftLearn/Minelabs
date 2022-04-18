package be.uantwerpen.scicraft.block.entity;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class ParticleBlockEntity extends BlockEntity{
	private final int e_radius = 5;
    private final double kc = 1;
    private final double e_move = .8;
	
	private double charge;
    private Vec3f field = Vec3f.ZERO;
    private boolean update_next_tick = false;
    public long time = 0;
    public boolean is_moving = false;
    public int mark_delete = 0;

	public ParticleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, double charge) {
		super(type, pos, state);
		this.charge = charge;
	}
	@Override
    public void writeNbt(NbtCompound tag) {
        // Save the current value of the number to the tag
        tag.putFloat("ex", field.getX());
        tag.putFloat("ey", field.getY());
        tag.putFloat("ez", field.getZ());
        tag.putDouble("q", charge);
        tag.putBoolean("ut", update_next_tick);
        tag.putBoolean("is", is_moving);
        tag.putLong("time", time);
        tag.putInt("md", mark_delete);
        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        field = new Vec3f(tag.getFloat("ex"), tag.getFloat("ey"), tag.getFloat("ez"));
        charge = tag.getDouble("q");
        update_next_tick = tag.getBoolean("ut");
        is_moving = tag.getBoolean("is");
        time = tag.getLong("time");
        mark_delete = tag.getInt("md");
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    
    public Vec3f getField() {
		return field;
	}
    
    public double getCharge() {
		return charge;
	}
    
    public void needsUpdate(boolean b) {
    	this.update_next_tick = b;
    }
    
    private void updateField(World world, BlockPos pos) {
    	if ((world.getTime() + pos.asLong()) % 5 == 0) {
        	Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(pos.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos.mutableCopy().add(e_radius, e_radius, e_radius));
        	field.set(Vec3f.ZERO);
        	for (BlockPos pos_block : blocks_in_radius) {
        		if (world.getBlockEntity(pos_block) instanceof ParticleBlockEntity particle2 && !pos.equals(pos_block)) {
        	        Vec3f vec_pos = new Vec3f(pos.getX()-pos_block.getX(), pos.getY()-pos_block.getY(), pos.getZ()-pos_block.getZ());
        	        float d_E = (float) ((getCharge() * particle2.getCharge() * kc)/ Math.sqrt(vec_pos.dot(vec_pos)));
        	        vec_pos.scale(d_E);
        	        field.subtract(vec_pos);
        	        needsUpdate(field.dot(field) > e_move);
        		}
        	}
 	        markDirty();
    	}
    }
    
    private void tick(World world, BlockPos pos, BlockState state) {
    	this.updateField(world, pos);
    	if (update_next_tick && !is_moving) { //TODO check if moving is possible first
            update_next_tick = false;
            is_moving = true;
            time = world.getTime();
            markDirty();
        }
        if (is_moving && world.getTime() - time > 40) {
        	is_moving = false;
        	update_next_tick = false;
        	mark_delete = 2; //TODO determine movement direction
        	int x_new = Math.abs(mark_delete) == 1 ?  Math.abs(mark_delete) / mark_delete : 0; //TODO what is this?
        	int y_new = Math.abs(mark_delete) == 2 ?  Math.abs(mark_delete) / mark_delete : 0;
        	int z_new = Math.abs(mark_delete) == 3 ?  Math.abs(mark_delete) / mark_delete : 0;
        	BlockPos nPos = pos.mutableCopy().add(x_new, y_new, z_new); //TODO to late, moving has already finished and we only now check if it's possible
        	if (world.getBlockState(nPos).isAir()) {
        		world.setBlockState(nPos, state.getBlock().getDefaultState(), Block.NOTIFY_ALL);
        		world.removeBlockEntity(pos);
        		world.removeBlock(pos, false);
        	}
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, ParticleBlockEntity be) {
        be.tick(world, pos, state);
    }

}
