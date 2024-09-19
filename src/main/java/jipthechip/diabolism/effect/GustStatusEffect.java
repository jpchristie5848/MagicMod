package jipthechip.diabolism.effect;

import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.entities.ProjectileSpellEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class GustStatusEffect extends AbstractElementalStatusEffect {

    private Vec3d effectVelocity;

    protected GustStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xf5e990, MagicElement.AIR, "gust");
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {

        System.out.println("apply instant effect called");

        if(source instanceof ProjectileSpellEntity projectile){
            Vec3d projVelocity = projectile.getVelocity();
            projVelocity.add(0,projVelocity.getY()*-1,0);
            projVelocity = projVelocity.normalize();

            projVelocity.multiply(0.2f + (amplifier * (4.8/100)));

            target.addVelocity(projVelocity);

            effectVelocity = projVelocity;
        }else{
            double xVelocity = (Math.random() * 2.0) - 1.0;
            double zVelocity = (Math.random() * 2.0) - 1.0;
            Vec3d projVelocity = new Vec3d(xVelocity, 0, zVelocity);
            projVelocity.normalize();

            projVelocity.multiply(0.2f + (amplifier * (4.8/100)));

            target.addVelocity(projVelocity);

            effectVelocity = projVelocity;
        }
    }

//    @Override
//    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
//
//        if(effectVelocity == null){
//            System.out.println("effect velocity is null");
//        }
//        if(effectVelocity != null){
//
//            double entityVelX = entity.getVelocity().getX();
//            double effectVelX = effectVelocity.getX();
//
//            if((entityVelX < effectVelX && effectVelX > 0) || (entityVelX > effectVelX && effectVelX < 0)){
//                entity.addVelocity(effectVelocity.getX() * 0.1, 0,0);
//            }
//
//            double entityVelZ = entity.getVelocity().getZ();
//            double effectVelZ = effectVelocity.getZ();
//
//            if((entityVelZ < effectVelZ && effectVelZ > 0) || (entityVelZ > effectVelZ && effectVelZ < 0)){
//                entity.addVelocity(0, 0,effectVelocity.getZ() * 0.1);
//            }
//        }
//    }

    @Override
    public boolean isInstant() {
        return true;
    }

//    @Override
//    public boolean canApplyUpdateEffect(int duration, int amplifier) {
//        return true;
//    }
}
