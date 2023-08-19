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
        World world = pump.getWorld();
        BlockState state = world.getBlockState(pump.getPos());
        Direction dir;
        if(state.getBlock() != DiabolismBlocks.FLUID_PUMP){
            dir = Direction.NORTH;
        }else{
            dir = state.get(FluidPumpBlock.PUMP_FROM_DIRECTION);
        }

        return new Identifier("diabolism", "geo/pipe_side_pump_"+dir.getName()+".geo.json");
    }

    @Override
    public Identifier getTextureResource(FluidPump animatable) {
        return new Identifier("diabolism", "textures/block/pipe_side_pump.png");
    }

    @Override
    public Identifier getAnimationResource(FluidPump animatable) {
        return new Identifier("diabolism", "animations/fluid_pump.animation.json");
    }
}
