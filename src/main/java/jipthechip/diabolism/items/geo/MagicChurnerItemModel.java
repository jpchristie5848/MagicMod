package jipthechip.diabolism.items.geo;

import jipthechip.diabolism.items.MagicChurnerBlockItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MagicChurnerItemModel extends GeoModel<MagicChurnerBlockItem> {
    @Override
    public Identifier getModelResource(MagicChurnerBlockItem object) {
        return new Identifier("diabolism", "geo/magic_churner.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagicChurnerBlockItem object) {
        return new Identifier("diabolism", "textures/block/magic_churner.png");
    }

    @Override
    public Identifier getAnimationResource(MagicChurnerBlockItem animatable) {
        return new Identifier("diabolism", "animations/magic_churner_turn.animation.json");
    }
}
