package jipthechip.diabolism.effect;

import jipthechip.diabolism.data.MagicElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

public class ChillyStatusEffect extends AbstractElementalStatusEffect {

    protected ChillyStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xa3daf0, MagicElement.ICE, "chilly");
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        float slowDownPercentage = 0.20f + ((amplifier/100.0f) * 0.8f);

        entity.setVelocity(entity.getVelocity().getX() * (1-slowDownPercentage), entity.getVelocity().getY(), entity.getVelocity().getZ()*(1-slowDownPercentage));


        if(!entity.getWorld().isClient && entity.hasStatusEffect(DiabolismEffects.MAP.get("wet"))){

            double random = Math.random();
            double freezeProbability = amplifier/10000.0f;

            if(random < freezeProbability && ! entity.hasStatusEffect(DiabolismEffects.MAP.get( "frozen"))){
                entity.removeStatusEffect(DiabolismEffects.MAP.get("wet"));
                entity.removeStatusEffect(DiabolismEffects.MAP.get("chilly"));
                entity.addStatusEffect(new StatusEffectInstance(DiabolismEffects.MAP.get("frozen"), 200));
                entity.addStatusEffect(new StatusEffectInstance(DiabolismEffects.MAP.get( "harm"), 200, amplifier, false, false));
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
