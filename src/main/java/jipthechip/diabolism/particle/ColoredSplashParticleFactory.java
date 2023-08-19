package jipthechip.diabolism.particle;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import org.jetbrains.annotations.Nullable;

public class ColoredSplashParticleFactory implements ParticleFactory<ColoredSplashParticleTypeData> {
    private final SpriteProvider sprites;


    public ColoredSplashParticleFactory(SpriteProvider sprites) {
        this.sprites = sprites;
    }

    @Nullable
    @Override
    public Particle createParticle(ColoredSplashParticleTypeData particleType, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        return new ColoredSplashParticle(world, x, y, z, this.sprites, velocityX, velocityY, velocityZ, particleType.color);
    }

    public static ParticleEffect createData(int color){
        return new ColoredSplashParticleTypeData(DiabolismParticles.COLORED_SPLASH_PARTICLE, color);
    }
}
