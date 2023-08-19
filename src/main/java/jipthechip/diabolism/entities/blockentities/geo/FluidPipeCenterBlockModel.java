package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.blocks.FluidPumpBlock;
import jipthechip.diabolism.entities.blockentities.FluidPipe;
import jipthechip.diabolism.entities.blockentities.FluidPump;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class FluidPipeCenterBlockModel extends GeoModel<FluidPump> {
    @Override
    public Identifier getModelResource(FluidPump animatable) {
        return new Identifier("diabolism", "geo/pipe_center.geo.json");
    }

    @Override
    public Identifier getTextureResource(FluidPump animatable) {
        return new Identifier("diabolism", "textures/block/pipe_center_geo.png");
    }

    @Override
    public Identifier getAnimationResource(FluidPump animatable) {
        return new Identifier("diabolism", "animations/fluid_pump.animation.json");
    }
}
