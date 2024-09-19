package jipthechip.diabolism.packets;

import jipthechip.diabolism.effect.DiabolismEffects;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.io.Serializable;

public class StatusEffectInstanceData implements Serializable {

    String effectKey;
    int duration;
    int amplifier;
    boolean ambient;
    boolean showParticles;
    boolean showIcon;

    public StatusEffectInstanceData(String effectKey, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon){
        this.effectKey = effectKey;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.showParticles = showParticles;
        this.showIcon = showIcon;
    }

    public StatusEffectInstanceData(String effectKey, int duration, int amplifier){
        this.effectKey = effectKey;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = true;
        this.showParticles = false;
        this.showIcon = true;
    }

    public StatusEffectInstance createInstance(){
        return new StatusEffectInstance(DiabolismEffects.MAP.get(effectKey), duration, amplifier, ambient, showParticles, showIcon);
    }

    public String getEffectKey() {
        return effectKey;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}
