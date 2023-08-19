package jipthechip.diabolism.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import org.jetbrains.annotations.Nullable;

public class MagickaTransferParticleFactory implements ParticleFactory<ColoredSpellParticleTypeData> {
    private final SpriteProvider sprites;


    public MagickaTransferParticleFactory(SpriteProvider sprites) {
        this.sprites = sprites;
    }

    @Nullable
    @Override
    public Particle createParticle(ColoredSpellParticleTypeData particleType, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        return new MagickaTransferParticle(world, x, y, z, this.sprites, velocityX, velocityY, velocityZ, particleType.color);
    }

    public static ParticleEffect createData(int color){
        return new ColoredSpellParticleTypeData(DiabolismParticles.COLORED_SPELL_PARTICLE, color);
    }
}
