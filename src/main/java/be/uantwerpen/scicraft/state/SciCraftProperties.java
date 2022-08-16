package be.uantwerpen.scicraft.state;

import be.uantwerpen.scicraft.block.BohrPart;
import net.minecraft.state.property.EnumProperty;

public class SciCraftProperties {
    public static final EnumProperty<BohrPart> BOHR_PART = EnumProperty.of("part", BohrPart.class);

}
