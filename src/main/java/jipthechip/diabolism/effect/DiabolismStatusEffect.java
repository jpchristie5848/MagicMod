package jipthechip.diabolism.effect;

import jipthechip.diabolism.Diabolism;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/*
All of my custom StatusEffects MUST inherit this class

 */
public abstract class DiabolismStatusEffect extends StatusEffect {

    protected String registryName;

    protected DiabolismStatusEffect(StatusEffectCategory category, int color, String registryName) {
        super(category, color);
        this.registryName = registryName;
    }

    protected void register(){
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Diabolism.MOD_ID, registryName+"_status_effect"), this);
    }

    public String getRegistryName() {
        return registryName;
    }
}
