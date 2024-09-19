package jipthechip.diabolism.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.Serializable;

public interface ServerReceiverPacket {

    void sendToServer(Serializable obj);
    ServerPlayNetworking.PlayChannelHandler serverReceiver();
}
