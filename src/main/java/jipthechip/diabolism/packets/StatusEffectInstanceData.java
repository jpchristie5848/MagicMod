package jipthechip.diabolism.packets;

import jipthechip.diabolism.potion.DiabolismEffects;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.io.Serializable;

public class StatusEffectInstanceData implements Serializable {

    String effectName;
    int duration;
    int amplifier;
    boolean ambient;
    boolean showParticles;
    boolean showIcon;

    public StatusEffectInstanceData(String effectName, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon){
        this.effectName = effectName;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.showParticles = showParticles;
        this.showIcon = showIcon;
    }

    public StatusEffectInstance createFromData(){
        return new StatusEffectInstance(DiabolismEffects.ELEMENTAL.get(effectName), duration, amplifier, ambient, showParticles, showIcon);
    }
}
