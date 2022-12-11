package jipthechip.diabolism.items.potion;

import jipthechip.diabolism.Utils.IMagicProperties;
import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.WatcherEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.Vec3d;

public class AwakeningStatusEffect extends StatusEffect {

    protected AwakeningStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier){
        if(((IMagicProperties)entity).isAwakened() || entity.getHealth() <= 0.0f){
            entity.removeStatusEffect(StatusEffects.NAUSEA);
            entity.removeStatusEffect(this);
        }else{
            entity.damage(DamageSource.MAGIC, 1.0f);
            if(entity instanceof PlayerEntity){
                if(Math.random() > 0.90){
                    Vec3d watcherPos = MathUtils.getPointOnSphere((float)((Math.random()*50)), (float) (Math.random()*360), 3.0f, new Vec3d(0,2,0));
                    WatcherEntity watcherEntity = new WatcherEntity(DiabolismEntities.WATCHER, watcherPos.x, watcherPos.y, watcherPos.z, entity.getWorld(), entity.getId());
                    entity.getWorld().spawnEntity(watcherEntity);
                    entity.getWorld().playSoundFromEntity((PlayerEntity) entity, watcherEntity, SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 1.0f, 1.0f);
                }
            }
        }
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if(entity instanceof PlayerEntity playerEntity){
            playerEntity.sendMessage(MutableText.of(new LiteralTextContent("You feel like something's watching you.")));
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if(!((IMagicProperties)entity).isAwakened() && entity instanceof PlayerEntity playerEntity && entity.getHealth() > 0.0f){
            ((IMagicProperties)playerEntity).setAwakened(true);
            ((IMagicProperties)playerEntity).setMaxMagicka(20);
            ((IMagicProperties)playerEntity).setMagickaRegenRate(1.0f);
            playerEntity.sendMessage(MutableText.of(new LiteralTextContent("The visions have ceased. You're safe now, but you feel different.")));
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 20 >> amplifier + (2 - (duration / 1200));
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}
