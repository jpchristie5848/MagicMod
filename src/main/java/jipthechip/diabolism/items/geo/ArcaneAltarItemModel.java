package jipthechip.diabolism.items.geo;

import jipthechip.diabolism.entities.blockentities.ArcaneAltar;
import jipthechip.diabolism.items.ArcaneAltarBlockItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ArcaneAltarItemModel extends GeoModel<ArcaneAltarBlockItem> {
    @Override
    public Identifier getModelResource(ArcaneAltarBlockItem animatable) {
        return new Identifier("diabolism", "geo/arcane_altar.geo.json");
    }

    @Override
    public Identifier getTextureResource(ArcaneAltarBlockItem animatable) {
        return new Identifier("diabolism", "textures/block/arcane_altar.png");
    }

    @Override
    public Identifier getAnimationResource(ArcaneAltarBlockItem animatable) {
        return new Identifier("diabolism", "animations/arcane_altar.animation.json");
    }
}
