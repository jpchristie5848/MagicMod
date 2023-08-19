package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.blocks.AbstractOmniDirectionalBlock;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.entities.blockentities.FluidPump;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import software.bernie.geckolib.model.GeoModel;

public class FluidOutputPipeBlockModel extends GeoModel<FluidPump> {
    @Override
    public Identifier getModelResource(FluidPump pump) {
        World world = pump.getWorld();
        BlockState state = world.getBlockState(pump.getPos());

        Direction dir = null;

        if(state.getBlock() == DiabolismBlocks.FLUID_PUMP){
            for(Direction dir2 : Direction.values()){
                if(state.get(AbstractOmniDirectionalBlock.DIRECTION_PROPERTIES.get(dir2))){
                    dir = dir2;
                    break;
                }
            }
        }
        if(dir == null){
            return null;
        }
        return new Identifier("diabolism", "geo/pipe_side_"+dir.getName()+".geo.json");
    }

    @Override
    public Identifier getTextureResource(FluidPump animatable) {
        return new Identifier("diabolism", "textures/block/pipe_side_geo.png");
    }

    @Override
    public Identifier getAnimationResource(FluidPump animatable) {
        return new Identifier("diabolism", "animations/fluid_pump.animation.json");
    }
}
