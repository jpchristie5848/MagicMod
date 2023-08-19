package jipthechip.diabolism.items.geo;

import jipthechip.diabolism.items.MagicFermenterBlockItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MagicFermenterItemRenderer extends GeoItemRenderer<MagicFermenterBlockItem> {

    public MagicFermenterItemRenderer() {
        super(new MagicFermenterItemModel());
    }
}
