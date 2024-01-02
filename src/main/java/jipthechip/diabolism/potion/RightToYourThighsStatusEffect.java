package jipthechip.diabolism.potion;

import jipthechip.diabolism.sound.DiabolismSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

import static jipthechip.diabolism.misc.DiabolismDamageSources.createSquidwardDamageSource;

public class RightToYourThighsStatusEffect extends ClientSyncedStatusEffect{

    protected RightToYourThighsStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        entity.getWorld().playSoundFromEntity(null, entity, DiabolismSounds.SQUIDWARD, SoundCategory.AMBIENT, 1.0f, 1.0f);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        if(entity instanceof PlayerEntity player){
            entity.getWorld().createExplosion(null, createSquidwardDamageSource(player), null, player.getX(), player.getY()+0.5f, player.getZ(), 5.0f, false, World.ExplosionSourceType.NONE);
        }

    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration == 5;
    }
}
