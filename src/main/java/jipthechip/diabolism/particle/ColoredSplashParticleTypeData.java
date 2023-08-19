package jipthechip.diabolism.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public class ColoredSplashParticleTypeData extends AbstractColoredParticleTypeData{


    public static final Codec<ColoredSplashParticleTypeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("color").forGetter(d -> d.color),
            Codec.BOOL.fieldOf("disableDepthTest").forGetter(d -> d.disableDepthTest),
            Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
            Codec.FLOAT.fieldOf("alpha").forGetter(d -> d.alpha),
            Codec.INT.fieldOf("age").forGetter(d -> d.age)
    ).apply(instance, ColoredSplashParticleTypeData::new));

    public ColoredSplashParticleTypeData(ParticleType<ColoredSplashParticleTypeData> particleType, int color, boolean disableDepthTest, float size, float alpha, int age) {
        this.type = particleType;
        this.color = color;
        this.disableDepthTest = disableDepthTest;
        this.size = size;
        this.alpha = alpha;
        this.age = age;
    }
    public ColoredSplashParticleTypeData(ParticleType<ColoredSplashParticleTypeData> particleType, int color) {
        this(particleType, color, false, 0.25f, 1.0f, 36);
    }

    public ColoredSplashParticleTypeData(ParticleType<ColoredSplashParticleTypeData> particleType, int color, int age) {
        this(particleType, color, false, 0.25f, 1.0f, age);
    }
    public ColoredSplashParticleTypeData(int color, boolean disableDepthTest, float size, float alpha, int age) {
        this(DiabolismParticles.COLORED_SPLASH_PARTICLE, color, disableDepthTest, size, alpha, age);
    }


    static final ParticleEffect.Factory<ColoredSplashParticleTypeData> FACTORY = new Factory<ColoredSplashParticleTypeData>() {
        @Override
        public ColoredSplashParticleTypeData read(ParticleType<ColoredSplashParticleTypeData> type, StringReader reader) throws CommandSyntaxException {
            return new ColoredSplashParticleTypeData(type, reader.readInt());
        }

        @Override
        public ColoredSplashParticleTypeData read(ParticleType<ColoredSplashParticleTypeData> type, PacketByteBuf buf) {
            return new ColoredSplashParticleTypeData(type, buf.readInt());
        }
    };
}
