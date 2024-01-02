package jipthechip.diabolism.data;

import jipthechip.diabolism.potion.AbstractElementalStatusEffect;
import jipthechip.diabolism.potion.DiabolismEffects;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.io.Serializable;

public class StatusEffectInstanceContainer implements Serializable {

    String effectKey;

    int amplifier;

    int duration;



    public StatusEffectInstanceContainer(StatusEffectInstance instance){
        this.effectKey = instance.getEffectType().getName().getString();
        this.amplifier = instance.getAmplifier();
        this.duration = instance.getDuration();
    }

    public StatusEffectInstanceContainer(MagicElement element, int amplifier, int duration){

    }

    public String getEffectKey() {
        return effectKey;
    }

    public MagicElement getEffectElement(){
        return (DiabolismEffects.ELEMENTAL.get(effectKey).getElement());
    }

    public int getAmplifier() {
        return amplifier;
    }

    public int getDuration() {
        return duration;
    }

    public StatusEffectInstance createInstance(){
        return new StatusEffectInstance(DiabolismEffects.ELEMENTAL.get(effectKey), duration, amplifier);
    }

}
