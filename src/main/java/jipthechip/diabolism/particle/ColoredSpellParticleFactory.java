package jipthechip.diabolism.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ColoredSpellParticleFactory implements ParticleFactory<ColoredSpellParticleTypeData> {
    private final SpriteProvider sprites;

    public ColoredSpellParticleFactory(SpriteProvider spriteSet){
        this.sprites = spriteSet;
    }

    @Nullable
    @Override
    public Particle createParticle(ColoredSpellParticleTypeData particleType, ClientWorld world, double x, double y, double z, double dx, double dy, double dz){
        return new ColoredSpellParticle(world, x, y, z, this.sprites, dx, dy, dz, particleType.color, particleType.age);
    }

    public static ParticleEffect createData(int color){
        return new ColoredSpellParticleTypeData(DiabolismParticles.COLORED_SPELL_PARTICLE, color);
    }

    public static ParticleEffect createData(int color, int age){
        return new ColoredSpellParticleTypeData(DiabolismParticles.COLORED_SPELL_PARTICLE, color, age);
    }
}
