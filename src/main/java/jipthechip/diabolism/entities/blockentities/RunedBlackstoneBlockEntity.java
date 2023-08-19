package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.blocks.AbstractActivatedBlock;
import jipthechip.diabolism.entities.DiabolismEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RunedBlackstoneBlockEntity extends BlockEntity {

    private int tickCounter = 0;
    private int lastActivated = 0;
    public RunedBlackstoneBlockEntity(BlockPos pos, BlockState state) {
        super(DiabolismEntities.RUNED_BLACKSTONE_BLOCKENTITY, pos, state);
    }

    public static void ticker(World world, BlockPos pos, BlockState state, RunedBlackstoneBlockEntity be) {
        be.tick(world, pos, state);
    }

    private void tick(World world, BlockPos pos, BlockState state){
        tickCounter++;
        if(tickCounter % 20 == 0){
            if(state.get(AbstractActivatedBlock.ACTIVATED)){
                if(tickCounter - lastActivated > 40){
                    world.setBlockState(pos, state.with(AbstractActivatedBlock.ACTIVATED, false));
                }
            }else if(tickCounter - lastActivated <= 40 && lastActivated > 0){
                world.setBlockState(pos, state.with(AbstractActivatedBlock.ACTIVATED, true));
            }
        }
    }
    public void markActivated(){
        lastActivated = tickCounter;
    }
}
