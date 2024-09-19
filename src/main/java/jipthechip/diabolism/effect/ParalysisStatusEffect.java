package jipthechip.diabolism.effect;

import jipthechip.diabolism.sound.DiabolismSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class ParalysisStatusEffect extends IncapacitatedStatusEffect{

    protected ParalysisStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xeeff00, "paralysis");
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        World world = entity.getWorld();
        if(!world.isClient){
            ((ServerWorld)world).playSoundFromEntity(null, entity, DiabolismSounds.PARALYSIS_ZAP, SoundCategory.BLOCKS, 2.0f, 1.0f);
        }
    }
}
