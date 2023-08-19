package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.entities.blockentities.MagicChurner;
import jipthechip.diabolism.entities.blockentities.MagicFermenter;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MagicFermenterBlockModel extends GeoModel<MagicFermenter> {
    @Override
    public Identifier getModelResource(MagicFermenter animatable) {
        return new Identifier("diabolism", "geo/magic_fermenter.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagicFermenter animatable) {
        return new Identifier("diabolism", "textures/block/magic_fermenter.png");
    }

    @Override
    public Identifier getAnimationResource(MagicFermenter animatable) {
        return new Identifier("diabolism", "animations/magic_fermenter.animation.json");
    }
}
