package jipthechip.diabolism.particle;

import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class MagickaTransferParticle extends ColoredSpellParticle {

    protected MagickaTransferParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteSet, double g, double h, double i, int color) {
        super(clientWorld, d, e, f, spriteSet, g, h, i, color, (int) (Math.random() * 8 + 2));
        this.maxAge = (int) (Math.random() * 8 + 2);
        this.setColor(((color & 0xFF0000) >> 16)/255.0f, ((color & 0xFF00) >> 8)/255.0f, (color & 0xFF)/255.0f);
        this.velocityY *= 0.05;
        this.velocityX *= 0.3;
        this.velocityZ *= 0.3;
    }

    @Override
    protected void fadeOut(){
        //this.alpha = (-(1/(float)maxAge) * age + 1);
        //this.scale = maxScale-((((float)age/(float)maxAge)*maxScale)*3)/4;
    }
    
}
