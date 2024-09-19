package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.blocks.FluidPumpBlock;
import jipthechip.diabolism.entities.blockentities.FluidPump;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import software.bernie.geckolib.model.GeoModel;

public class FluidPumpBlockModel extends GeoModel<FluidPump> {
    @Override
    public Identifier getModelResource(FluidPump pump) {
        return new Identifier("diabolism", "geo/fluid_pump_side.geo.json");
    }

    @Override
    public Identifier getTextureResource(FluidPump animatable) {
        return new Identifier("diabolism", "textures/block/fluid_pump_side.png");
    }

    @Override
    public Identifier getAnimationResource(FluidPump animatable) {
        return new Identifier("diabolism", "animations/fluid_pump.animation.json");
    }
}
