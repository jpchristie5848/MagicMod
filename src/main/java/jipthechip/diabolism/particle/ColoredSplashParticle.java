package jipthechip.diabolism.particle;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.WaterSplashParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class ColoredSplashParticle extends SpriteBillboardParticle {

    ColoredSplashParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteSet, double g, double h, double i, int color) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);

        // copied from RainSplashParticle (constructor is package private >:\)
        this.velocityX *= 0.30000001192092896;
        this.velocityY = Math.random() * 0.20000000298023224 + 0.10000000149011612;
        this.velocityZ *= 0.30000001192092896;
        this.setBoundingBoxSpacing(0.01F, 0.01F);
        this.gravityStrength = 0.06F;
        this.maxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));

        // copied from WaterSplashParticle (constructor is package private >:\)
        this.gravityStrength = 0.04F;
        if (h == 0.0 && (g != 0.0 || i != 0.0)) {
            this.velocityX = g;
            this.velocityY = 0.1;
            this.velocityZ = i;
        }

        this.setColor(((color & 0xFF0000) >> 16)/255.0f, ((color & 0xFF00) >> 8)/255.0f, (color & 0xFF)/255.0f);
        setSprite(spriteSet.getSprite(Random.create()));
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
        } else {
            this.velocityY -= (double)this.gravityStrength;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= 0.9800000190734863;
            this.velocityY *= 0.9800000190734863;
            this.velocityZ *= 0.9800000190734863;
            if (this.onGround) {
                if (Math.random() < 0.5) {
                    this.markDead();
                }

                this.velocityX *= 0.699999988079071;
                this.velocityZ *= 0.699999988079071;
            }

            BlockPos blockPos = BlockPos.ofFloored(this.x, this.y, this.z);
            double d = Math.max(this.world.getBlockState(blockPos).getCollisionShape(this.world, blockPos).getEndingCoord(Direction.Axis.Y, this.x - (double)blockPos.getX(), this.z - (double)blockPos.getZ()), (double)this.world.getFluidState(blockPos).getHeight(this.world, blockPos));
            if (d > 0.0 && this.y < (double)blockPos.getY() + d) {
                this.markDead();
            }

        }
    }


    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
}