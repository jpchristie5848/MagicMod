package jipthechip.diabolism.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;

public class WatcherParticle extends SpriteBillboardParticle {

    protected WatcherParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteSet, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        this.scale = 0.15f;
        this.maxAge = 8;
        this.setSpriteForAge(spriteSet);
        this.velocityX *= 0.05;
        this.velocityY *= 0.05;
        this.velocityZ *= 0.05;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
//        if (this.age++ >= this.maxAge) {
//            this.markDead();
//        }
        fadeOut();
    }

    private void fadeOut(){
        this.alpha = (-(1/(float)maxAge) * age + 1);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet){
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld world, double x, double y, double z, double dx, double dy, double dz){
            return new WatcherParticle(world, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
