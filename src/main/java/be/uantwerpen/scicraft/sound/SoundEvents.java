package be.uantwerpen.scicraft.sound;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SoundEvents {

    public static final SoundEvent ENTITY_ENTROPY_CREEPER_DEATH = register("entity.entropy_creeper.death");
    public static final SoundEvent ENTITY_ENTROPY_CREEPER_HURT = register("entity.entropy_creeper.hurt");
    public static final SoundEvent ENTITY_ENTROPY_CREEPER_PRIMED = register("entity.entropy_creeper.primed");
    public static final SoundEvent ENTITY_ENTROPY_CREEPER_EXPLODE = register("entity.entropy_creeper.explode");

    private static SoundEvent register(String id) {
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(new Identifier(Scicraft.MOD_ID, id)));
    }

    /**
     * Main class method
     * Registers all sounds
     */
    public static void registerSounds() {
        Scicraft.LOGGER.info("registering sounds");
    }


}
