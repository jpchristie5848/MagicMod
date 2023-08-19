package jipthechip.diabolism.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;

public class ColoredSpellParticle extends SpriteBillboardParticle {

    float maxScale;

    protected ColoredSpellParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteSet, double g, double h, double i, int color, int age) {
        super(clientWorld, d, e, f, g, h, i);
        this.maxScale = (float) ((0.08f)*(Math.random()+1.0f));
        this.scale = maxScale;
        this.maxAge = age; //(int) (Math.random() * 25 + 10);
        this.setSpriteForAge(spriteSet);
        if(g > 0 || h > 0 || i > 0){
            this.velocityX *= g;
            this.velocityY *= h;
            this.velocityZ *= i;
        }else{
            this.velocityX *= 0.5;
            this.velocityY *= 0.5;
            this.velocityZ *= 0.5;
        }
        this.gravityStrength = 0;
        this.setColor(((color & 0xFF0000) >> 16)/255.0f, ((color & 0xFF00) >> 8)/255.0f, (color & 0xFF)/255.0f);

    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheets.GLOW_RENDER_NO_MASK;
    }


    @Override
    protected int getBrightness(float tint) {
        return 255;
    }

    public void tick(){
        super.tick();
        fadeOut();
    }

    @Override
    public int getMaxAge() {
        return super.getMaxAge();
    }

    protected void fadeOut(){
        this.alpha = (-(1/(float)maxAge) * age + 1);
        this.scale = maxScale-(((float)age/(float)maxAge)*maxScale);
    }

}
