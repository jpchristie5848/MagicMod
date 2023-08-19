package jipthechip.diabolism.entities;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class MagickaParticleEntityRenderer extends EntityRenderer<MagickaParticleEntity> {
    protected MagickaParticleEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(MagickaParticleEntity entity) {
        return null;
    }
}
