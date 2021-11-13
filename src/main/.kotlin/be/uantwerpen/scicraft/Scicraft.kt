package be.uantwerpen.scicraft
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings

import net.minecraft.item.Item
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


@Suppress("UNUSED")
object Scicraft: ModInitializer {
    private const val MOD_ID = "scicraft"

    // TODO: Move to dedicated classes
    // Blocks
    val PROTON_BLOCK: Block = Block(FabricBlockSettings.of(Material.WOOL).mapColor(MapColor.WHITE).strength(2f).nonOpaque().collidable(false))

    // BlockItems
    val PROTON: Item = BlockItem(PROTON_BLOCK, FabricItemSettings().group(ItemGroup.MISC))

    // Items


    override fun onInitialize() {
//        println("Example mod has been initialized.")
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "proton"), PROTON)
        Registry.register(Registry.BLOCK, Identifier(MOD_ID, "proton"), PROTON_BLOCK)
    }
}