package be.uantwerpen.minelabs.state;

import be.uantwerpen.minelabs.block.BohrPart;
import net.minecraft.state.property.EnumProperty;

public class SciCraftProperties {
    public static final EnumProperty<BohrPart> BOHR_PART = EnumProperty.of("part", BohrPart.class);

}
