package be.uantwerpen.scicraft.mixins;

import be.uantwerpen.scicraft.block.ICampfireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin implements ICampfireBlock {

    private static IntProperty FIRE_COLOR = IntProperty.of("fire_color", 0, 10);

    @Override
    public IntProperty getFireColor() {
        return FIRE_COLOR;
    }

    @Inject(method = "appendProperties", at = @At("HEAD"))
    public void injectProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(FIRE_COLOR);
    }
}
