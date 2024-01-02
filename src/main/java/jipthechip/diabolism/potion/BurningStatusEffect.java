package jipthechip.diabolism.potion;

import jipthechip.diabolism.data.MagicElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class BurningStatusEffect extends AbstractElementalStatusEffect {

    protected BurningStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, MagicElement.FIRE);
    }


    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();

        for(int i = 0; i < 3; i++){
            double x = entity.getX() + (Math.random()-0.5);
            double y = entity.getY() + (Math.random()+0.5);
            double z = entity.getZ() + (Math.random()-0.5);

            //System.out.println("spawning particle at "+x+", "+y+", "+z);

            world.addParticle(ParticleTypes.FLAME, x, y, z, 0,0,0);

            //PlayerLookup.tracking(entity).forEach(player -> ((ServerWorld)world).spawnParticles(player, ParticleTypes.ELECTRIC_SPARK, true, x, y, z, 1, 0, 0, 0, 0));
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 2 == 0;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        entity.addStatusEffect(new StatusEffectInstance(DiabolismEffects.ELEMENTAL.get("harm"), 200, amplifier, false, false));
    }
}
