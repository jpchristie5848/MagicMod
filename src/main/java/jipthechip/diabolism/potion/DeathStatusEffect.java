package jipthechip.diabolism.potion;

import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.Spell;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import jipthechip.diabolism.sound.DiabolismSounds;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class DeathStatusEffect extends AbstractElementalStatusEffect{

    protected DeathStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, MagicElement.DEATH);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        int damage = ((amplifier / 10) + 1);
        World world = entity.getWorld();

        if (world instanceof ServerWorld serverWorld) {
            if (entity.isUndead()) {
                if (entity.getHealth() < entity.getMaxHealth()) {
                    entity.heal(damage);
                    serverWorld.playSoundFromEntity(null, entity, DiabolismSounds.DEATH_SPELL, SoundCategory.BLOCKS, 1.0f, (float) (Math.random() * 0.5f) + 0.75f);
                    playParticles(entity, amplifier);
                }
            } else {
                entity.damage(entity.getDamageSources().magic(), damage);
                serverWorld.playSoundFromEntity(null, entity, DiabolismSounds.DEATH_SPELL, SoundCategory.BLOCKS, 1.0f, (float) (Math.random() * 0.5f) + 0.75f);
                playParticles(entity, amplifier);
            }
        }
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        World world = entity.getWorld();
        if(world instanceof ServerWorld serverWorld) {
            serverWorld.playSoundFromEntity(null, entity, DiabolismSounds.DEATH_SPELL, SoundCategory.BLOCKS, 1.0f, (float)(Math.random()*0.5f)+0.75f);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
