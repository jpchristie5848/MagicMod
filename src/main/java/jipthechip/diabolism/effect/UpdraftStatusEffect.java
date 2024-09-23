package jipthechip.diabolism.effect;

import jipthechip.diabolism.data.MagicElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class UpdraftStatusEffect extends AbstractElementalStatusEffect {

    protected UpdraftStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xf5e990, MagicElement.AIR, "updraft");

    }

    // will lift you up 2-22 blocks
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        float velocityOffset = 0.2f * (amplifier/100.0f);

        entity.setVelocity(entity.getVelocity().getX(), entity.getVelocity().getY()+0.2+velocityOffset, entity.getVelocity().getZ());

    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        if(duration < 90){
            return duration % 3 == 0;
        }else {
            return duration % 2 == 0;
        }
    }

}
