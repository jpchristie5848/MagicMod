package jipthechip.diabolism.mixin;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

// Minecraft ParticleManager Iterates over an immutable list of ParticleTextureSheets instead of using the key set
// of the particle queue, and this disallows the use of custom ParticleTextureSheets. This fixes that issue

@Mixin(ParticleManager.class)
public class ParticleRenderFix {

    @Final
    @Shadow
    private Map<ParticleTextureSheet, Queue<Particle>> particles;

    @ModifyVariable(method="renderParticles", at=@At("STORE"), ordinal = 0)
    private Iterator<ParticleTextureSheet> fixParticleTextureSheets(Iterator<ParticleTextureSheet> i){
        return this.particles.keySet().stream().toList().iterator();
    }
}
