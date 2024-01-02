package jipthechip.diabolism.items.geo;

import jipthechip.diabolism.items.ArcaneAltarBlockItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ArcaneAltarItemRenderer extends GeoItemRenderer<ArcaneAltarBlockItem> {

    public ArcaneAltarItemRenderer() {
        super(new ArcaneAltarItemModel());
    }
}
