package jipthechip.diabolism.Utils;

import io.wispforest.owo.util.ImplementedInventory;
import jipthechip.diabolism.packets.BlockSyncPackets;
import jipthechip.diabolism.packets.EntitySyncPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class PacketUtils {

    public static void syncEntityEffectsWithClient(int entityId){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        ClientPlayNetworking.send(EntitySyncPackets.GET_ENTITY_EFFECTS_FROM_SERVER, buf);
    }

    public static void syncBlockInventoryWithClient(World world, BlockPos pos, Inventory inventory){
        //System.out.println("world is client: "+world.isClient);

        if(world != null && !world.isClient){
            //System.out.println("in sync block inv with client");
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(pos);
            buf.writeInt(inventory.size());
            for(int i = 0; i < inventory.size(); i++){
                buf.writeItemStack(inventory.getStack(i));
            }
            BlockEntity entity = world.getBlockEntity(pos);
            if(entity != null){
                //PlayerLookup.tracking(entity).forEach(player -> ServerPlayNetworking.send(player, BlockSyncPackets.SYNC_BLOCK_INVENTORY_W_CLIENT, buf));
            }
        }
    }

    public static void writeInventoryToBuffer(PacketByteBuf buf, Object dataSource){
        if(dataSource instanceof ImplementedInventory invEntity){
            DefaultedList<ItemStack> inventory = invEntity.getItems();
            buf.writeInt(inventory.size());
            for(int i = 0; i < inventory.size(); i++){
                buf.writeItemStack(inventory.get(i));
            }
        }
    }

    public static DefaultedList<ItemStack> readInventoryFromBuffer(PacketByteBuf buf){
        int inventorySize = 0;
        try{
            inventorySize = buf.readInt();
            DefaultedList<ItemStack> inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
            for(int i = 0; i < inventorySize; i++){
                inventory.set(i, buf.readItemStack());
            }
            return inventory;
        }catch(IndexOutOfBoundsException e){
            if(inventorySize == 0){
                System.out.println("readInventoryFromBuffer: Failed to read inventory because it wasn't found in the buffer");
            }else{
                System.err.println("readInventoryFromBuffer: Failed to read inventory because the actual number of itemstacks in the buffer is less than the inventory size");
            }
        }catch(Exception e){
            System.err.println("readInventoryFromBuffer: an unhandled exception occurred.");
            throw e;
        }
        return null;

    }
}
