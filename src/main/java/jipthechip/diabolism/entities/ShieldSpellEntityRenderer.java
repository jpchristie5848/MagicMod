package jipthechip.diabolism.entities;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class ShieldSpellEntityRenderer extends EntityRenderer<ShieldSpellEntity> {
    protected ShieldSpellEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(ShieldSpellEntity entity) {
        return null;
    }
}
