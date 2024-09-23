package jipthechip.diabolism.effect;

import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.MagicElementColors;
import jipthechip.diabolism.data.spell.Spell;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public abstract class AbstractElementalStatusEffect extends ClientSyncedStatusEffect{

    MagicElement element;


    protected AbstractElementalStatusEffect(StatusEffectCategory category, int color, MagicElement element, String registryName) {
        super(category, color, registryName);
        this.element = element;
    }

    protected void playParticles(LivingEntity entity, int amplifier){
        World world = entity.getWorld();
        if(world instanceof ServerWorld serverWorld){

            int numParticles = (int) Math.pow(amplifier, 1.3);
            int particleColor = MagicElementColors.MAP.get(this.getElement());


            for(int i = 0; i < numParticles; i++){
                if(entity instanceof ServerPlayerEntity playerEntity){
                    serverWorld.spawnParticles(playerEntity,
                            ColoredSpellParticleFactory.createData(particleColor, (int) (Math.random() * 25 + 10)), true, entity.getX(), entity.getY()+1, entity.getZ(), 1,
                            0, 0, 0, 0);
                }
                PlayerLookup.tracking(entity).forEach(player -> serverWorld.spawnParticles(player,
                        ColoredSpellParticleFactory.createData(particleColor, (int) (Math.random() * 25 + 10)), true, entity.getX(), entity.getY()+1, entity.getZ(), 1,
                        0, 0, 0, 0));
            }
        }
    }

    public MagicElement getElement() {
        return element;
    }
}
