package be.uantwerpen.scicraft.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SchrodingersBox extends ChestBlock {

    protected SchrodingersBox() {
        super(FabricBlockSettings.of(Material.STONE)
                .hardness(3.0F)
                .resistance(3.0F)
                .sounds(BlockSoundGroup.STONE)
                .requiresTool(), ExtraChestTypes.SCHRODINGERS_BOX::getBlockEntityType);
    }
}
