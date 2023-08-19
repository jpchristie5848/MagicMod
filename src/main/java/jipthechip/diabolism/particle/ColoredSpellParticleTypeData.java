package jipthechip.diabolism.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public class ColoredSpellParticleTypeData extends AbstractColoredParticleTypeData{

    public static final Codec<ColoredSpellParticleTypeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("color").forGetter(d -> d.color),
            Codec.INT.fieldOf("age").forGetter(d -> d.age),
            Codec.BOOL.fieldOf("disableDepthTest").forGetter(d -> d.disableDepthTest),
            Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
            Codec.FLOAT.fieldOf("alpha").forGetter(d -> d.alpha)
    ).apply(instance, ColoredSpellParticleTypeData::new));

    public ColoredSpellParticleTypeData(ParticleType<ColoredSpellParticleTypeData> particleType, int color,  int age, boolean disableDepthTest, float size, float alpha) {
        this.type = particleType;
        this.color = color;
        this.disableDepthTest = disableDepthTest;
        this.size = size;
        this.alpha = alpha;
        this.age = age;
    }
    public ColoredSpellParticleTypeData(ParticleType<ColoredSpellParticleTypeData> particleType, int color) {
        this(particleType, color,36, false, 0.25f, 1.0f);
    }

    public ColoredSpellParticleTypeData(ParticleType<ColoredSpellParticleTypeData> particleType, int color, int age) {
        this(particleType, color,  age,false, 0.25f, 1.0f);
    }

    public ColoredSpellParticleTypeData(int color, int age, boolean disableDepthTest, float size, float alpha) {
        this(DiabolismParticles.COLORED_SPELL_PARTICLE, color, age, disableDepthTest, size, alpha);
    }


    static final ParticleEffect.Factory<ColoredSpellParticleTypeData> FACTORY = new Factory<ColoredSpellParticleTypeData>() {
        @Override
        public ColoredSpellParticleTypeData read(ParticleType<ColoredSpellParticleTypeData> type, StringReader reader) throws CommandSyntaxException {
            int color = reader.readInt();
            int age = reader.readInt();
            return new ColoredSpellParticleTypeData(type, color, age);
        }

        @Override
        public ColoredSpellParticleTypeData read(ParticleType<ColoredSpellParticleTypeData> type, PacketByteBuf buf) {
            int color = buf.readInt();
            int age = buf.readInt();
            return new ColoredSpellParticleTypeData(type, color, age);
        }
    };
}
