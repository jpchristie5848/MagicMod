package jipthechip.diabolism.items.geo;

import jipthechip.diabolism.items.MagicFermenterBlockItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MagicFermenterItemModel extends GeoModel<MagicFermenterBlockItem> {
    @Override
    public Identifier getModelResource(MagicFermenterBlockItem animatable) {
        return new Identifier("diabolism", "geo/magic_fermenter.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagicFermenterBlockItem animatable) {
        return new Identifier("diabolism", "textures/block/magic_fermenter.png");
    }

    @Override
    public Identifier getAnimationResource(MagicFermenterBlockItem animatable) {
        return new Identifier("diabolism", "animations/magic_fermenter.animation.json");}
}
