package be.uantwerpen.minelabs.util;

import be.uantwerpen.minelabs.block.BohrPart;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;

public class MinelabsProperties {
    public static final EnumProperty<BohrPart> BOHR_PART = EnumProperty.of("part", BohrPart.class);

    public static final IntProperty FIRE_COLOR = IntProperty.of("fire_color", 0, 10);

}
