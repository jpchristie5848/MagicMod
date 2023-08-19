package jipthechip.diabolism.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class MagicHarmStatusEffect extends StatusEffect {

    protected MagicHarmStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.damage(entity.getDamageSources().magic(), 1.0f);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % (100 - amplifier) == 0;
    }
}
