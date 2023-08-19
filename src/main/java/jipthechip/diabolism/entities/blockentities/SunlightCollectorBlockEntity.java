package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.entities.DiabolismEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class SunlightCollectorBlockEntity extends AbstractMagickaCollector {

    public SunlightCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(DiabolismEntities.SUNLIGHT_COLLECTOR_BLOCKENTITY, pos, state, "sunlight");
    }

    @Override
    protected int calculatePotency() {
        long timeOfDay = world.getTimeOfDay();

        // makes it so 23000 is the start of the day, and 13000 is the end of the day
        if((timeOfDay >= 23000 && timeOfDay <= 23999)){
            timeOfDay -= 23000;
        }else{
            timeOfDay += 1000;
        }
        double scaledTimeOfDay = (((double)(timeOfDay)/(double)14000) * (2*Math.PI)); // scale time to value between 0 and 2pi so it can be plugged into cos function
        //System.out.println("scaled time of day: "+scaledTimeOfDay);
        int potency = (int) Math.round((Math.cos(scaledTimeOfDay + Math.PI)+1) * 50);
        System.out.println("calculated potency: "+potency);
        return potency; // get potency value between 0 and 100, value is higher when closer to noon
    }

    @Override
    protected boolean tickConditionsMet() {
        long timeOfDay = world.getTimeOfDay();
        return ((timeOfDay >= 0 && timeOfDay <= 13000) || (timeOfDay >= 23000 && timeOfDay <= 23999)) && !world.isRaining() && !world.isThundering();
    }

}
