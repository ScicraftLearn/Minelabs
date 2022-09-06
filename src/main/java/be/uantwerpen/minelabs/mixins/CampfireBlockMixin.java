package be.uantwerpen.minelabs.mixins;

import be.uantwerpen.minelabs.block.ICampfireBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.CampfireBlock.SIGNAL_FIRE;
import static net.minecraft.block.CampfireBlock.WATERLOGGED;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin implements ICampfireBlock {

    @Shadow
    @Final
    public static BooleanProperty LIT;
    @Shadow
    @Final
    public static DirectionProperty FACING;

    @Shadow
    protected abstract boolean isSignalFireBaseBlock(BlockState state);

    private static final IntProperty FIRE_COLOR = IntProperty.of("fire_color", 0, 10);

    @Override
    public IntProperty getFireColor() {
        return FIRE_COLOR;
    }

    @Inject(method = "appendProperties", at = @At("HEAD"))
    public void injectProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(FIRE_COLOR);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructor(boolean emitsParticles, int fireDamage, AbstractBlock.Settings settings, CallbackInfo ci) {
        BlockAccessorMixin block = ((BlockAccessorMixin) this);
        block.setDefaultState(block.getStateManager().getDefaultState().with(LIT, true)
                .with(SIGNAL_FIRE, false).with(WATERLOGGED, false)
                .with(FACING, Direction.NORTH).with(FIRE_COLOR, 0));
    }

    @Inject(method = "getPlacementState", at = @At("TAIL"), cancellable = true)
    public void injectPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockAccessorMixin block = ((BlockAccessorMixin) this);
        WorldAccess worldAccess = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        boolean bl = worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER;
        //ICampfireBlockEntity entity = (ICampfireBlockEntity) worldAccess.getBlockEntity(blockPos);

        cir.setReturnValue(block.getStateManager().getDefaultState().with(WATERLOGGED, bl)
                .with(SIGNAL_FIRE, isSignalFireBaseBlock(worldAccess.getBlockState(blockPos.down()))).with(LIT, !bl)
                .with(FACING, ctx.getPlayerFacing()).with(FIRE_COLOR, 0));
    }
}
