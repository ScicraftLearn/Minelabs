package be.minelabs.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)V", at = @At(value= "INVOKE", target = "Lnet/minecraft/block/BlockState;onStacksDropped(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Z)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void dropStacks(BlockState state, LootContext.Builder lootContext, CallbackInfo ci, ServerWorld serverWorld, BlockPos blockPos) {
        if (serverWorld.getBlockState(blockPos) == state) {
            // If the block is replaced by the lasertool
            // Do the last call of the original function without experience drop then cancel to prevent the original call to be executed
            state.onStacksDropped(serverWorld, blockPos, ItemStack.EMPTY, false);
            ci.cancel();
        }
    }

    @Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", at = @At(value= "INVOKE", target = "Lnet/minecraft/block/BlockState;onStacksDropped(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Z)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void dropStacks(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        if (world.getBlockState(pos) == state) {
            // If the block is replaced by the lasertool
            // Do the last call of the original function without experience drop then cancel to prevent the original call to be executed
            state.onStacksDropped((ServerWorld)world, pos, ItemStack.EMPTY, false);
            ci.cancel();
        }
    }

    @Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;)V", at = @At(value= "INVOKE", target = "Lnet/minecraft/block/BlockState;onStacksDropped(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Z)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void dropStacks(BlockState state, WorldAccess world, BlockPos pos, BlockEntity blockEntity, CallbackInfo ci) {
        if (world.getBlockState(pos) == state) {
            // If the block is replaced by the lasertool
            // Do the last call of the original function without experience drop then cancel to prevent the original call to be executed
            state.onStacksDropped((ServerWorld)world, pos, ItemStack.EMPTY, false);
            ci.cancel();
        }
    }

    @Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At(value= "INVOKE", target = "Lnet/minecraft/block/BlockState;onStacksDropped(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Z)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void dropStacks(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfo ci) {
        if (world.getBlockState(pos) == state) {
            // If the block is replaced by the lasertool
            // Do the last call of the original function without experience drop then cancel to prevent the original call to be executed
            state.onStacksDropped((ServerWorld)world, pos, stack, false);
            ci.cancel();
        }
    }
}
