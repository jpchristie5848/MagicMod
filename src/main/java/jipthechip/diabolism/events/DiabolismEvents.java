package jipthechip.diabolism.events;

import jipthechip.diabolism.Utils.IMagicProperties;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class DiabolismEvents {


    public static void registerEvents(){

        WorldEvents.registerEvents();
        PlayerEvents.registerEvents();
        SyncEvents.registerEvents();

        //
        // PLAYER LOG OUT EVENT
        //

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server)->{
            ((IMagicProperties)handler.player).deinitialize();
        });


        //
        // REGISTER UI EVENT HANDLERS
        //
        UIEvents.registerEvents();
    }

}
