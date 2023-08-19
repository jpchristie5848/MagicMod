package jipthechip.diabolism.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

public class ChillyStatusEffect extends ClientSyncedStatusEffect {

    protected ChillyStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        float slowDownPercentage = 0.20f + ((amplifier/100.0f) * 0.8f);

        entity.setVelocity(entity.getVelocity().getX() * (1-slowDownPercentage), entity.getVelocity().getY(), entity.getVelocity().getZ()*(1-slowDownPercentage));


        if(!entity.getWorld().isClient && entity.hasStatusEffect(DiabolismPotions.WET_STATUS_EFFECT)){

            double random = Math.random();
            double freezeProbability = amplifier/10000.0f;

            if(random < freezeProbability && ! entity.hasStatusEffect(DiabolismPotions.FROZEN_STATUS_EFFECT)){
                entity.removeStatusEffect(DiabolismPotions.WET_STATUS_EFFECT);
                entity.removeStatusEffect(DiabolismPotions.CHILLY_STATUS_EFFECT);
                entity.addStatusEffect(new StatusEffectInstance(DiabolismPotions.FROZEN_STATUS_EFFECT, 200));
                entity.addStatusEffect(new StatusEffectInstance(DiabolismPotions.MAGIC_HARM_STATUS_EFFECT, 200, amplifier, false, false));
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
