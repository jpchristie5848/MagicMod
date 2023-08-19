package jipthechip.diabolism.items.geo;

import jipthechip.diabolism.items.MagicChurnerBlockItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MagicChurnerItemRenderer extends GeoItemRenderer<MagicChurnerBlockItem> {

    public MagicChurnerItemRenderer() {
        super(new MagicChurnerItemModel());
    }
}
