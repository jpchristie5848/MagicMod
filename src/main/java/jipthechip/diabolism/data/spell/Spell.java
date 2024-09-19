package jipthechip.diabolism.data.spell;

import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.ProjectileSpellEntity;
import jipthechip.diabolism.packets.StatusEffectInstanceData;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import jipthechip.diabolism.effect.AbstractElementalStatusEffect;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.io.Serializable;
import java.util.*;

public class Spell implements Serializable {

    public static final int[] ELEMENT_COLORS = new int[]{0xffebe3a2, 0xffed3f1c, 0xff1c7eed, 0xff75430d, 0xff000000, 0xffffffff, 0xffeeff00, 0xffa3daf0, 0xff22e305, 0xff000000, 0xfff0dcf5};

    SpellType type;

    // stores data for StatusEffectInstances in Serializable objects
    List<StatusEffectInstanceData> effects;

    public Spell(SpellType type, StatusEffectInstanceData... instances) {
        this.type = type;
        effects = List.of(instances);
    }

    public void cast(LivingEntity caster){

        World world = caster.getWorld();

        switch(type){
            case PROJECTILE -> {
                Vec3d direction = Vec3d.fromPolar(caster.getPitch(), caster.getYaw());
                ProjectileSpellEntity spellEntity = new ProjectileSpellEntity(DiabolismEntities.PROJECTILE_SPELL,
                        caster.getX() + (direction.getX() * 3),
                        caster.getY() + (direction.getY() * 3)+1.5,
                        caster.getZ() + (direction.getZ() * 3),
                        world, direction.multiply(0.2), effects.toArray(new StatusEffectInstanceData[0]));


                System.out.println("casting spell with effects:");
                for(StatusEffectInstanceData data : effects){
                    System.out.println(data.getEffectKey());
                }

                world.spawnEntity(spellEntity);
            }
            case SELF -> {
                for(StatusEffectInstanceData data : effects){
                    StatusEffectInstance instance = data.createInstance();
                    caster.addStatusEffect(instance);
                    playSelfParticles(caster, instance, world);
                }
            }
        }
    }

    private void playSelfParticles(LivingEntity caster, StatusEffectInstance instance, World world){
        int numParticles = (int) Math.pow((instance.getDuration()/6 + instance.getAmplifier())/2, 1.3);

        MagicElement effectElement = ((AbstractElementalStatusEffect)instance.getEffectType()).getElement();

        int particleColor = Spell.ELEMENT_COLORS[effectElement.ordinal()];
        for(int i = 0; i < numParticles; i++){
            if(caster instanceof ServerPlayerEntity playerEntity){
                ((ServerWorld) world).spawnParticles(playerEntity,
                        ColoredSpellParticleFactory.createData(particleColor, (int) (Math.random() * 25 + 10)), true, caster.getX(), caster.getY()+1, caster.getZ(), 1,
                        0, 0, 0, 0);
            }
            PlayerLookup.tracking(caster).forEach(player -> ((ServerWorld) world).spawnParticles(player,
                    ColoredSpellParticleFactory.createData(particleColor, (int) (Math.random() * 25 + 10)), true, caster.getX(), caster.getY()+1, caster.getZ(), 1,
                    0, 0, 0, 0));
        }
    }

    public SpellType getType() {
        return type;
    }

    public List<StatusEffectInstanceData> getEffects(){
        return effects;
    }

}