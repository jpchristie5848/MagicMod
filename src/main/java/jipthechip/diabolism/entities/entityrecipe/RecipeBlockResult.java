package jipthechip.diabolism.entities.entityrecipe;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class RecipeBlockResult {

    BlockPos pos;
    BlockState state;


    public RecipeBlockResult(BlockPos pos, BlockState state){
        this.pos = pos;
        this.state = state;
    }


    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

}
