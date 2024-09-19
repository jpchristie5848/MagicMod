package jipthechip.diabolism.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class HarmStatusEffect extends ClientSyncedStatusEffect {

    protected HarmStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x00000, "harm");
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
