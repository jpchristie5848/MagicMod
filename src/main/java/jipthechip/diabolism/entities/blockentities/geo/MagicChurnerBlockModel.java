package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.entities.blockentities.MagicChurner;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MagicChurnerBlockModel extends GeoModel<MagicChurner> {
    @Override
    public Identifier getModelResource(MagicChurner object) {
        return new Identifier("diabolism", "geo/magic_churner.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagicChurner object) {
        return new Identifier("diabolism", "textures/block/magic_churner.png");
    }

    @Override
    public Identifier getAnimationResource(MagicChurner animatable) {
        return new Identifier("diabolism", "animations/magic_churner_turn.animation.json");
    }
}
