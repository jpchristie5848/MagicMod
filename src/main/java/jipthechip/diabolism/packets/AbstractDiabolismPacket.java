package jipthechip.diabolism.packets;

import jipthechip.diabolism.Diabolism;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public abstract class AbstractDiabolismPacket {
    public Identifier ID;
    public AbstractDiabolismPacket(String string){
        this.ID = new Identifier(Diabolism.MOD_ID, string);
    }

    public void register(){
        if(this instanceof ServerReceiverPacket serverReceiverPacket){
            ServerPlayNetworking.registerGlobalReceiver(ID, serverReceiverPacket.serverReceiver());
        }
        if(this instanceof ClientReceiverPacket clientReceiverPacket){
            ClientPlayNetworking.registerGlobalReceiver(ID, clientReceiverPacket.clientReceiver());
        }
    }

}
