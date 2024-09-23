package jipthechip.diabolism.items;

import io.wispforest.owo.util.ImplementedInventory;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.spell.Wand;
import jipthechip.diabolism.data.spell.WandData;
import jipthechip.diabolism.entities.blockentities.AbstractSyncedBlockEntity;
import jipthechip.diabolism.packets.BlockEntitySyncPacket;
import jipthechip.diabolism.packets.ItemSyncPacket;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AbstractSyncedItemData<T extends Serializable>{

    protected T data;
    protected ItemStack stack;

    public static Map<String, ItemSyncPacket> SYNC_PACKETS = new HashMap<>();

    public AbstractSyncedItemData(ItemStack stack) {
        this.stack = stack;
    }

    public void setData(T data){
        this.data = data;
    }

    public T getData(){
        return this.data;
    }

    public void setStack(ItemStack stack){
        this.stack = stack;
    }

    public ItemStack getStack(){
        return this.stack;
    }

    public void sync(){
        if(data != null)
            writeToItemNbt();
            getSyncPacket(this.getClass()).sendSyncPacket(this);
    }

    public static <B extends AbstractSyncedItemData> ItemSyncPacket getSyncPacket(Class<B> beClass){
        return SYNC_PACKETS.get(beClass.getSimpleName().toLowerCase());
    }

    public void writeToItemNbt(){
        NbtCompound nbt = DataUtils.writeObjectToItemNbt(stack, this.data);
        if(this instanceof ImplementedInventory inventory)
            Inventories.writeNbt(nbt, inventory.getItems());
    }

    public void readFromItemNbt(){
        this.data = (T) DataUtils.readObjectFromItemNbt(stack, this.data.getClass());
        NbtCompound nbt = stack.getNbt();
        if(nbt != null && this instanceof ImplementedInventory inventory){
            Inventories.readNbt(nbt, inventory.getItems());
        }
    }
}
