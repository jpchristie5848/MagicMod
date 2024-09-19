package jipthechip.diabolism.events;

import jipthechip.diabolism.blocks.AbstractActivatedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.util.ActionResult;

public class WorldEvents {

    public static void registerEvents(){
        //
        // LIGHTNING STRIKE EVENT
        //

        LightningStrikeCallback.EVENT.register(((lightningEntity, blockPos) -> {

            System.out.println("lightning strike event fired");
            BlockState belowState = lightningEntity.getWorld().getBlockState(blockPos.down());

            //
            // Event for activating conductive pillars under lightning rods
            //

            BlockState state = lightningEntity.getWorld().getBlockState(blockPos);
            if(state.getBlock() instanceof LightningRodBlock) {
                if(belowState.getBlock() instanceof AbstractActivatedBlock){
                    if (!belowState.get(AbstractActivatedBlock.ACTIVATED))
                        lightningEntity.getWorld().setBlockState(blockPos.down(),
                                belowState.with(AbstractActivatedBlock.ACTIVATED, true));
                }
            }


            return ActionResult.SUCCESS;
        }));
    }
}
