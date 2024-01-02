package jipthechip.diabolism.data;

import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.ProjectileSpellEntity;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import jipthechip.diabolism.potion.AbstractElementalStatusEffect;
import jipthechip.diabolism.potion.DiabolismEffects;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
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
    List<StatusEffectInstanceContainer> effects;

    public Spell(SpellType type, StatusEffectInstance ... instances) {
        this.type = type;
        effects = new ArrayList<>();
        for(StatusEffectInstance instance : instances){
            effects.add(new StatusEffectInstanceContainer(instance));
        }
    }

    public Spell(SpellType type, StatusEffectInstanceContainer... instances) {
        this.type = type;
        effects = List.of(instances);
    }

    public void cast(LivingEntity caster){

        World world = caster.getWorld();

        List<StatusEffectInstance> statusEffects = new ArrayList<>();
        for(StatusEffectInstanceContainer container : effects){
            statusEffects.add(container.createInstance());
        }

        switch(type){
            case PROJECTILE -> {
                Vec3d direction = Vec3d.fromPolar(caster.getPitch(), caster.getYaw());
                ProjectileSpellEntity spellEntity = new ProjectileSpellEntity(DiabolismEntities.PROJECTILE_SPELL,
                        caster.getX() + (direction.getX() * 3),
                        caster.getY() + (direction.getY() * 3)+1.5,
                        caster.getZ() + (direction.getZ() * 3),
                        world, direction.multiply(0.2), statusEffects.toArray(new StatusEffectInstance[0]));


                System.out.println("casting spell with effects:");
//            System.out.println(caster.getX() + (direction.getX() * 3)+", "+
//                    caster.getY() + (direction.getY() * 3)+1.5+", "+
//                    caster.getZ() + (direction.getZ() * 3));
                for(StatusEffectInstance instance : statusEffects){
                    System.out.println(instance.getEffectType());
                }

                world.spawnEntity(spellEntity);
            }
            case SELF -> {
                for(StatusEffectInstance instance : statusEffects){
                    caster.addStatusEffect(instance);
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
            }
        }
    }

    public SpellType getType() {
        return type;
    }

    public List<StatusEffectInstanceContainer> getEffects(){
        return effects;
    }

    //    private StatusEffect getEffect(MagicElement element){
//        switch(element){
//            case AIR:
//                return potency > duration ? DiabolismEffects.GUST_STATUS_EFFECT : DiabolismEffects.UPDRAFT_STATUS_EFFECT;
//            case FIRE:
//                return DiabolismEffects.BURNING_STATUS_EFFECT;
//            case WATER:
//                return DiabolismEffects.WET_STATUS_EFFECT;
//            case EARTH:
//                break;
//            case CHAOS:
//                break;
//            case ORDER:
//                break;
//            case LIGHTNING:
//                return DiabolismEffects.SHOCK_STATUS_EFFECT;
//            case ICE:
//                return DiabolismEffects.CHILLY_STATUS_EFFECT;
//            case LIFE:
//                break;
//            case DEATH:
//                break;
//            case SPIRIT:
//                break;
//        }
//        return null;
//    }

}