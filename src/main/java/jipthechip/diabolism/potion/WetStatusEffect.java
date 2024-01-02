package jipthechip.diabolism.potion;

import jipthechip.diabolism.data.MagicElement;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class WetStatusEffect extends AbstractElementalStatusEffect {

    protected WetStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, MagicElement.WATER);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();

        for(int i = 0; i < 1; i++){
            double x = entity.getX() + (Math.random()-0.5);
            double y = entity.getY() + (Math.random()+0.5);
            double z = entity.getZ() + (Math.random()-0.5);

            //System.out.println("spawning particle at "+x+", "+y+", "+z);

            world.addParticle(ParticleTypes.SPLASH, x, y, z, 0,0,0);

            //PlayerLookup.tracking(entity).forEach(player -> ((ServerWorld)world).spawnParticles(player, ParticleTypes.ELECTRIC_SPARK, true, x, y, z, 1, 0, 0, 0, 0));
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 4 == 0;
    }
}
