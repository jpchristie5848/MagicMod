package jipthechip.diabolism.potion;

import jipthechip.diabolism.data.MagicElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

public class ChillyStatusEffect extends AbstractElementalStatusEffect {

    protected ChillyStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, MagicElement.ICE);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        float slowDownPercentage = 0.20f + ((amplifier/100.0f) * 0.8f);

        entity.setVelocity(entity.getVelocity().getX() * (1-slowDownPercentage), entity.getVelocity().getY(), entity.getVelocity().getZ()*(1-slowDownPercentage));


        if(!entity.getWorld().isClient && entity.hasStatusEffect(DiabolismEffects.ELEMENTAL.get("wet"))){

            double random = Math.random();
            double freezeProbability = amplifier/10000.0f;

            if(random < freezeProbability && ! entity.hasStatusEffect(DiabolismEffects.ELEMENTAL.get("frozen"))){
                entity.removeStatusEffect(DiabolismEffects.ELEMENTAL.get("wet"));
                entity.removeStatusEffect(DiabolismEffects.ELEMENTAL.get("chilly"));
                entity.addStatusEffect(new StatusEffectInstance(DiabolismEffects.ELEMENTAL.get("frozen"), 200));
                entity.addStatusEffect(new StatusEffectInstance(DiabolismEffects.ELEMENTAL.get("harm"), 200, amplifier, false, false));
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
