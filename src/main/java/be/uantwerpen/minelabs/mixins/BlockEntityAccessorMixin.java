package be.uantwerpen.minelabs.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessorMixin {

    @Accessor
    BlockState getCachedState();

    @Accessor
    void setCachedState(BlockState state);
}

