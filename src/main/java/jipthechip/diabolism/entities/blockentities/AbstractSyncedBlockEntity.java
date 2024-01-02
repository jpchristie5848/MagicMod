package jipthechip.diabolism.entities.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class AbstractSyncedBlockEntity extends BlockEntity {
    public AbstractSyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    abstract protected void syncWithServer();

    protected Vec3d getPosVec3d(){
        return new Vec3d(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
    }
}
