package jipthechip.diabolism.potion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class UpdraftStatusEffect extends StatusEffect {

    protected UpdraftStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);

    }

    // will lift you up 2-22 blocks
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        entity.setVelocity(entity.getVelocity().getX(), entity.getVelocity().getY()+0.24, entity.getVelocity().getZ());

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
