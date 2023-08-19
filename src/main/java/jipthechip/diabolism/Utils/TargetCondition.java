package jipthechip.diabolism.Utils;

import net.minecraft.block.entity.BlockEntity;

public interface TargetCondition {

    boolean evaluate(BlockEntity be);
}
