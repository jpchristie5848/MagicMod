package jipthechip.diabolism.entities;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class ProjectileSpellEntityRenderer extends EntityRenderer<ProjectileSpellEntity> {

    public ProjectileSpellEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ProjectileSpellEntity entity) {
        return null;
    }

}
