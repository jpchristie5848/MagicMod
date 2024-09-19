package jipthechip.diabolism.items.geo;

import jipthechip.diabolism.items.FluidPumpBlockItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class FluidPumpItemModel extends GeoModel<FluidPumpBlockItem> {
    @Override
    public Identifier getModelResource(FluidPumpBlockItem animatable) {
        return new Identifier("diabolism", "geo/fluid_pump_side.geo.json");
    }

    @Override
    public Identifier getTextureResource(FluidPumpBlockItem animatable) {
        return new Identifier("diabolism", "textures/block/fluid_pump_side.png");
    }

    @Override
    public Identifier getAnimationResource(FluidPumpBlockItem animatable) {
        return new Identifier("diabolism", "animations/fluid_pump.animation.json");

    }
}
