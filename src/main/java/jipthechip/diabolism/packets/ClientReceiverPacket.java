package jipthechip.diabolism.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.io.Serializable;

public interface ClientReceiverPacket {

    void sendToClient(Serializable obj);

    ClientPlayNetworking.PlayChannelHandler clientReceiver();
}
