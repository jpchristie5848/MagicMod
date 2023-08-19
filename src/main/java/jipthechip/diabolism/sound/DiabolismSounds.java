package jipthechip.diabolism.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class DiabolismSounds {
    public static SoundEvent CHURNER_SLOSH;
    public static SoundEvent MAGIC_FREEZE;

    private static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier("diabolism", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds(){
        CHURNER_SLOSH = registerSoundEvent("churner_slosh");
        MAGIC_FREEZE = registerSoundEvent("magic_freeze");
    }
}
