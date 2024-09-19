package jipthechip.diabolism.packets;

public class DiabolismPackets {


    public static void registerPacketReceivers(){
        BlockSyncPackets.register();
        ClientPackets.register();
        EntitySyncPackets.register();
    }

}