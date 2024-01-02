package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.entities.blockentities.ArcaneAltar;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ArcaneAltarBlockModel extends GeoModel<ArcaneAltar> {
    @Override
    public Identifier getModelResource(ArcaneAltar animatable) {
        return new Identifier("diabolism", "geo/arcane_altar.geo.json");
    }

    @Override
    public Identifier getTextureResource(ArcaneAltar animatable) {
        return new Identifier("diabolism", "textures/block/arcane_altar.png");
    }

    @Override
    public Identifier getAnimationResource(ArcaneAltar animatable) {
        return new Identifier("diabolism", "animations/arcane_altar.animation.json");
    }
}
