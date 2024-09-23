package jipthechip.diabolism.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.util.thread.ReentrantThreadExecutor;

import javax.annotation.Nullable;

public abstract class SyncPacket<D> extends AbstractDiabolismPacket implements ClientReceiverPacket, ServerReceiverPacket{


    public SyncPacket(String string) {
        super(string);
        register();
    }

    @Override
    public ClientPlayNetworking.PlayChannelHandler clientReceiver() {
        return (client, handler, buf, responseSender) -> {
            receiveSyncPacket(client, buf, handler);};
    }

    @Override
    public ServerPlayNetworking.PlayChannelHandler serverReceiver() {
        return (server, player, handler, buf, sender) ->{
            receiveSyncPacket(server, buf, handler);};
    }

    public abstract void sendSyncPacket(D dataSource);

    protected abstract void receiveSyncPacket(ReentrantThreadExecutor executor, PacketByteBuf buf, @Nullable PacketListener handler);


}
