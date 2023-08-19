package jipthechip.diabolism.entities.blockentities;

import net.minecraft.util.math.BlockPos;

public interface IBlockEntityWithConnections {

    public void addConnectedEntity(BlockPos pos);

    public void removeConnectedEntity(BlockPos pos);
}
