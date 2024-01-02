package jipthechip.diabolism.potion;

import jipthechip.diabolism.sound.DiabolismSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class FrozenStatusEffect extends IncapacitatedStatusEffect {

    protected FrozenStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        World world = entity.getWorld();
        if(!world.isClient){
            ((ServerWorld)world).playSoundFromEntity(null, entity, DiabolismSounds.MAGIC_FREEZE, SoundCategory.BLOCKS, 2.0f, 1.0f);
        }
    }
}
