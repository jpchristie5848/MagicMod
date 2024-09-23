package jipthechip.diabolism.packets;

import io.wispforest.owo.util.ImplementedInventory;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.Utils.PacketUtils;
import jipthechip.diabolism.entities.blockentities.AbstractSyncedBlockEntity;
import jipthechip.diabolism.items.AbstractSyncedItemData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class ItemSyncPacket<T extends Serializable> extends SyncPacket<AbstractSyncedItemData<T>> {
    public ItemSyncPacket(Class<? extends AbstractSyncedItemData<T>> itClass) {
        super(itClass.getSimpleName().toLowerCase());
    }

    @Override
    public void sendSyncPacket(AbstractSyncedItemData<T> dataSource) {
        T data = dataSource.getData();
        ItemStack stack = dataSource.getStack();
        Entity entity = stack.getHolder();
        if(entity != null){
            World world = entity.getWorld();
            if(world != null){
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeString(DataUtils.SerializeToString(data));
                buf.writeItemStack(stack);
                if(world.isClient){
                    ClientPlayNetworking.send(this.ID, buf);
                }
                else{
                    PacketUtils.writeInventoryToBuffer(buf, dataSource);
                    PlayerLookup.tracking(entity).forEach((player)-> ServerPlayNetworking.send(player, this.ID, buf));
                }
            }
        }
    }

    @Override
    protected void receiveSyncPacket(ReentrantThreadExecutor executor, PacketByteBuf buf, @Nullable PacketListener handler) {

        T data = (T) DataUtils.DeserializeFromString(buf.readString());
        ItemStack stack = buf.readItemStack();
        DefaultedList<ItemStack> inventory = PacketUtils.readInventoryFromBuffer(buf);
        Entity entity = stack.getHolder();
        if(entity != null) {
            World world = entity.getWorld();
            if (world != null) {
                if(entity instanceof PlayerEntity player){
                    Hand hand = player.getActiveHand();
                    ItemStack stackInHand = player.getStackInHand(hand);

                    DataUtils.writeObjectToItemNbt(stackInHand, data);
                    if(inventory != null){
                        Inventories.writeNbt(stackInHand.getNbt(), inventory);
                    }
                }
            }
        }

    }

    @Override
    public void sendToClient(Serializable obj) {

    }

    @Override
    public void sendToServer(Serializable obj) {

    }

    public static <D extends Serializable, P extends AbstractSyncedItemData<D>> ItemSyncPacket<D> registerSyncPacket(Class<P> itemData){

        ItemSyncPacket<D> packet = new ItemSyncPacket<>(itemData);
        packet.register();

        return packet;
    }

}
