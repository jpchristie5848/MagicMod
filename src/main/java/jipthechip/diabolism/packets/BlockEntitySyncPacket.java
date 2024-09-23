package jipthechip.diabolism.packets;

import io.wispforest.owo.util.ImplementedInventory;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.Utils.PacketUtils;
import jipthechip.diabolism.entities.blockentities.AbstractSyncedBlockEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class BlockEntitySyncPacket<T extends Serializable> extends SyncPacket<AbstractSyncedBlockEntity<T>> {

    public BlockEntitySyncPacket(Class<? extends AbstractSyncedBlockEntity<T>> beClass) {
        super(beClass.getSimpleName().toLowerCase());
    }

    @Override
    protected void receiveSyncPacket(ReentrantThreadExecutor executor, PacketByteBuf buf, @Nullable PacketListener handler) {
        BlockPos pos = buf.readBlockPos();
        String dataKey = buf.readString();
        try {
            T data = (T)DataUtils.DeserializeFromString(dataKey);

            AbstractSyncedBlockEntity<T> blockEntity;

            World world = null;

            if(executor instanceof MinecraftClient client){
                world = client.world;
            } else if (executor instanceof MinecraftServer && handler instanceof ServerPlayNetworkHandler serverHandler) {
                world = serverHandler.getPlayer().getWorld();
            }

            if(world != null){
                blockEntity = (AbstractSyncedBlockEntity<T>)world.getBlockEntity(pos);
                if(blockEntity != null){
                    // if block entity has inventory, sync it with the client
                    // necessary for gui to properly update
                    if(world.isClient && blockEntity instanceof ImplementedInventory invEntity){
                        int invSize = buf.readInt();
                        for(int i = 0; i < invSize; i++){
                            invEntity.setStack(i, buf.readItemStack());
                        }
                    }
                    blockEntity.setData(data);
                }
            }
        } catch(ClassCastException e){
            System.err.println("Error casting NBT data '"+dataKey+"' for block entity at "+pos+": "+e);
        }
    }

    public void sendSyncPacket(AbstractSyncedBlockEntity<T> blockEntity){
        T data = blockEntity.getData();
        World world = blockEntity.getWorld();
        if(world != null){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(blockEntity.getPos());
            buf.writeString(DataUtils.SerializeToString(data));
            if(world.isClient){
                ClientPlayNetworking.send(this.ID, buf);
                //System.out.println("sending sync packet from client for block entity '"+blockEntity.getClass().getSimpleName()+"' at "+blockEntity.getPos());
            }else{
                PacketUtils.writeInventoryToBuffer(buf, blockEntity);
                //System.out.println("sending sync packet from server for block entity '"+blockEntity.getClass().getSimpleName()+"' at "+blockEntity.getPos());
                PlayerLookup.tracking(blockEntity).forEach((player)-> ServerPlayNetworking.send(player, this.ID, buf));
            }
        }
    }

    @Override
    public void sendToClient(Serializable obj) {
    }

    @Override
    public void sendToServer(Serializable obj) {
    }

    public static <D extends Serializable, B extends AbstractSyncedBlockEntity<D>> BlockEntitySyncPacket<D> registerSyncPacket(Class<B> beClass){

        BlockEntitySyncPacket<D> packet = new BlockEntitySyncPacket<>(beClass);
        packet.register();

        return packet;
    }


}
