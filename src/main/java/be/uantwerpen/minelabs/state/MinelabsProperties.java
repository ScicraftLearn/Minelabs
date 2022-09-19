package be.uantwerpen.minelabs.state;

import be.uantwerpen.minelabs.block.BohrPart;
import be.uantwerpen.minelabs.block.CornerShape;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;

public class MinelabsProperties {
    public static final EnumProperty<BohrPart> BOHR_PART = EnumProperty.of("part", BohrPart.class);

    public static final BooleanProperty ZOOMED = BooleanProperty.of("zoom");

    //USE WITH INTENT OF ADDING CUSTOM MODELS FOR COUNTERED BLOCKS
    // 0 : default (no offset)
    // 1 : Center Lab block (1 pixel offset) -> float offset = 0.0625f
    // 2 : Lab Block (2 pixel offset)        -> float offset = 0.1250f
    public static final IntProperty COUNTER = IntProperty.of("counter", 0, 2);
    public static final EnumProperty<CornerShape> CONNECT = EnumProperty.of("connect", CornerShape.class);
}
