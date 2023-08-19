package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.entities.DiabolismEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class StarlightCollectorBlockEntity extends AbstractMagickaCollector {

    public StarlightCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(DiabolismEntities.STARLIGHT_COLLECTOR_BLOCKENTITY, pos, state, "starlight");
    }

    @Override
    protected int calculatePotency() {
        long timeOfDay = world.getTimeOfDay();
        double scaledTimeOfDay = (((double)(timeOfDay - 13000)/(double)10000) * (2*Math.PI)); // scale time to value between 0 and 2pi so it can be plugged into cos function
        //System.out.println("scaled time of day: "+scaledTimeOfDay);
        int potency = (int) Math.round((Math.cos(scaledTimeOfDay + Math.PI)+1) * 50);
        System.out.println("calculated potency: "+potency);
        return potency; // get potency value between 0 and 100, value is higher when closer to midnight
    }

    @Override
    protected boolean tickConditionsMet() {
        long timeOfDay = world.getTimeOfDay();
        return (timeOfDay >= 13000 && timeOfDay <= 23000) && !world.isRaining() && !world.isThundering();
    }
}
