package jipthechip.diabolism.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

//
// fabric version of
// <a href="https://github.com/baileyholl/Ars-Nouveau/blob/1.19.x/src/main/java/com/hollingsworth/arsnouveau/client/particle/ColorParticleTypeData.java">Ars Nouveau</a>
//

public abstract class AbstractColoredParticleTypeData implements ParticleEffect {

    public int color;
    ParticleType<? extends AbstractColoredParticleTypeData> type;
    public boolean disableDepthTest;
    public float size = .25f;
    public float alpha = 1.0f;
    public int age = 36;

    public int typeIndex = 0;


    @Override
    public ParticleType<? extends AbstractColoredParticleTypeData> getType() {
        return type;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(color);
        buf.writeInt(age);
    }


    @Override
    public String asString() {
        return "ColoredParticleType: "+Integer.toHexString(color);
    }
}
