package jipthechip.diabolism.mixin;

import jipthechip.diabolism.events.LightningStrikeCallback;
import jipthechip.diabolism.particle.ColoredSplashParticleFactory;
import jipthechip.diabolism.particle.DiabolismParticles;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.LightningEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public abstract class SpriteAwareParticlesMixin {

    @Shadow
    protected abstract <T extends ParticleEffect> void registerFactory(ParticleType<T> type, ParticleManager.SpriteAwareFactory<T> spriteAwareFactory);

//    @Inject(at=@At(value = "TAIL"), method="registerDefaultFactories")
//    private void registerDefaultFactories(CallbackInfo ci){
//        this.registerFactory(DiabolismParticles.COLORED_SPLASH_PARTICLE, (ParticleManager.SpriteAwareFactory)(ColoredSplashParticleFactory::new));
//    }
}
