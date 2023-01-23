package jipthechip.diabolism.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DiabolismParticles {

    public static final DefaultParticleType WATCHER_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType PROJECTILE_SPELL_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles(){
        Registry.register(Registry.PARTICLE_TYPE, new Identifier("diabolism", "watcher_particle"), WATCHER_PARTICLE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier("diabolism", "projectile_spell_particle"), PROJECTILE_SPELL_PARTICLE);
    }

    public static void registerParticlesClient(){
        ParticleFactoryRegistry.getInstance().register(DiabolismParticles.WATCHER_PARTICLE, WatcherParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(DiabolismParticles.PROJECTILE_SPELL_PARTICLE, ProjectileSpellParticle.Factory::new);
    }
}
