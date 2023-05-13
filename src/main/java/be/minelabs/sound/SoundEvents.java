package be.minelabs.sound;

import be.minelabs.Minelabs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEvents {

    //public static final SoundEvent ENTITY_ENTROPY_CREEPER_DEATH = register("entity.entropy_creeper.death");
    public static final SoundEvent ENTITY_ENTROPY_CREEPER_HURT = register("entity.entropy_creeper.hurt");
    public static final SoundEvent ENTITY_ENTROPY_CREEPER_PRIMED = register("entity.entropy_creeper.primed");
    public static final SoundEvent ENTITY_ENTROPY_CREEPER_EXPLODE = register("entity.entropy_creeper.explode");

    private static SoundEvent register(String id) {

        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(new Identifier(Minelabs.MOD_ID, id)));
    }

    /**
     * Main class method
     * Registers all sounds
     */
    public static void registerSounds() {
        Minelabs.LOGGER.info("registering sounds");
    }


}
