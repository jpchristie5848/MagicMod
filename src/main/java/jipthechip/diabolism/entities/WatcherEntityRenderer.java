package jipthechip.diabolism.entities;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class WatcherEntityRenderer extends EntityRenderer<WatcherEntity> {
    protected WatcherEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(WatcherEntity entity) {
        return null;
    }
}
