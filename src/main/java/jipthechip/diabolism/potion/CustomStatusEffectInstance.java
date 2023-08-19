package jipthechip.diabolism.potion;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;

public class CustomStatusEffectInstance extends StatusEffectInstance {

    public CustomStatusEffectInstance(StatusEffect type) {
        super(type);
    }

    private static StatusEffectInstance fromNbt(StatusEffect type, NbtCompound nbt) {

        StatusEffectInstance instance = StatusEffectInstance.fromNbt(nbt);
        return instance;
    }
}
