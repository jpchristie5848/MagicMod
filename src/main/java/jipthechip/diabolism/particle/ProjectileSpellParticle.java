package jipthechip.diabolism.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.SwordItem;
import net.minecraft.particle.DefaultParticleType;

public class ProjectileSpellParticle extends SpriteBillboardParticle {

    float maxScale;
    protected ProjectileSpellParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteSet, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        this.maxScale = (0.08f);
        this.scale = maxScale;
        this.maxAge = (int) (Math.random() * 25 + 10);
        this.setSpriteForAge(spriteSet);
        this.velocityX *= 0.1;
        this.velocityY *= 0.1;
        this.velocityZ *= 0.1;
        this.gravityStrength = 0;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick(){
        super.tick();
        fadeOut();
    }

    private void fadeOut(){
        this.alpha = (-(1/(float)maxAge) * age + 1);
        this.scale = maxScale-(((float)age/(float)maxAge)*maxScale);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet){
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld world, double x, double y, double z, double dx, double dy, double dz){
            return new ProjectileSpellParticle(world, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
