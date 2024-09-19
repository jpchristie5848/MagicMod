package jipthechip.diabolism.entities.blockentities;

import io.wispforest.owo.util.ImplementedInventory;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.packets.BlockEntitySyncPacket;
import jipthechip.diabolism.packets.SyncPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSyncedBlockEntity<T extends Serializable> extends BlockEntity {

    protected T data;

    public static Map<String, BlockEntitySyncPacket> SYNC_PACKETS = new HashMap<>();

    public AbstractSyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected Vec3d getPosVec3d(){
        return new Vec3d(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
    }

    public void setData(T data){
        this.data = data;
        markDirty();
    }

    public T getData(){
        return this.data;
    }

    public void sync(){
        if(data != null)
            getSyncPacket(this.getClass()).sendSyncPacket(this);
    }

    public static <B extends AbstractSyncedBlockEntity> BlockEntitySyncPacket getSyncPacket(Class<B> beClass){
        return SYNC_PACKETS.get(beClass.getSimpleName().toLowerCase());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        try{
            this.data = (T) DataUtils.DeserializeFromString(nbt.getString(getClass().getSimpleName().toLowerCase()));
        }catch(ClassCastException e){
            System.err.println("Error casting read NBT data for block entity type '"+getClass().getSimpleName()+"' at "+getPos());
            System.err.println(e);
        }catch(Exception e){
            System.err.println("Error reading NBT data for block entity type '"+getClass().getSimpleName()+"' at "+getPos());
            System.err.println(e);
        }
        if(this instanceof ImplementedInventory invEntity){
            Inventories.readNbt(nbt, invEntity.getItems());
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putString(getClass().getSimpleName().toLowerCase(), DataUtils.SerializeToString(this.data));
        if(this instanceof ImplementedInventory invEntity){
            Inventories.writeNbt(nbt, invEntity.getItems());
        }
        super.writeNbt(nbt);
    }
}
