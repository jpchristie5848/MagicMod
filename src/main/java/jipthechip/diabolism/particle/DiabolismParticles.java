package jipthechip.diabolism.particle;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class DiabolismParticles {


    public static final DefaultParticleType WATCHER_PARTICLE = FabricParticleTypes.simple();
    public static final ParticleType<ColoredSpellParticleTypeData> COLORED_SPELL_PARTICLE = FabricParticleTypes.complex(ColoredSpellParticleTypeData.FACTORY);
    public static final ParticleType<ColoredSplashParticleTypeData> COLORED_SPLASH_PARTICLE = FabricParticleTypes.complex(ColoredSplashParticleTypeData.FACTORY);

    public static void registerParticles(){
        Registry.register(Registries.PARTICLE_TYPE, new Identifier("diabolism", "watcher_particle"), WATCHER_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier("diabolism", "colored_spell_particle"), COLORED_SPELL_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier("diabolism", "colored_splash_particle"), COLORED_SPLASH_PARTICLE);
    }

    public static void registerParticlesClient(){
        ParticleFactoryRegistry.getInstance().register(DiabolismParticles.WATCHER_PARTICLE, WatcherParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(DiabolismParticles.COLORED_SPELL_PARTICLE, ColoredSpellParticleFactory::new);
        //ParticleFactoryRegistry.getInstance().register(DiabolismParticles.COLORED_SPELL_PARTICLE, MagickaTransferParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(DiabolismParticles.COLORED_SPLASH_PARTICLE, ColoredSplashParticleFactory::new);
    }

    // copied from ParticleManager.java (class is private)

    @Environment(EnvType.CLIENT)
    private static class SimpleSpriteProvider implements SpriteProvider {
        private List<Sprite> sprites;

        SimpleSpriteProvider() {
        }

        public Sprite getSprite(int age, int maxAge) {
            return (Sprite)this.sprites.get(age * (this.sprites.size() - 1) / maxAge);
        }

        public Sprite getSprite(Random random) {
            return (Sprite)this.sprites.get(random.nextInt(this.sprites.size()));
        }

        public void setSprites(List<Sprite> sprites) {
            this.sprites = ImmutableList.copyOf(sprites);
        }
    }
}
