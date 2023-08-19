package jipthechip.diabolism.particle;

import com.mojang.serialization.Codec;
import net.minecraft.particle.ParticleType;

public class ColoredSplashParticleType extends ParticleType<ColoredSplashParticleTypeData> {

    public ColoredSplashParticleType() {
        super(false, ColoredSplashParticleTypeData.FACTORY);
    }

    @Override
    public Codec<ColoredSplashParticleTypeData> getCodec() {
        return ColoredSplashParticleTypeData.CODEC;
    }
}
