package jipthechip.diabolism.items.geo;

import jipthechip.diabolism.items.FluidPumpBlockItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FluidPumpItemRenderer extends GeoItemRenderer<FluidPumpBlockItem> {

    public FluidPumpItemRenderer() {
        super(new FluidPumpItemModel());
        addRenderLayer(new FluidPumpItemRenderLayer(this));
    }
}
