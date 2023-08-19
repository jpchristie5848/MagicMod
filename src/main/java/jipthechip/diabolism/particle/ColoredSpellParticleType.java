package jipthechip.diabolism.particle;

import com.mojang.serialization.Codec;
import net.minecraft.particle.ParticleType;

public class ColoredSpellParticleType extends ParticleType<ColoredSpellParticleTypeData> {

    public ColoredSpellParticleType() {
        super(false, ColoredSpellParticleTypeData.FACTORY);
    }

    @Override
    public Codec<ColoredSpellParticleTypeData> getCodec() {
        return ColoredSpellParticleTypeData.CODEC;
    }
}
