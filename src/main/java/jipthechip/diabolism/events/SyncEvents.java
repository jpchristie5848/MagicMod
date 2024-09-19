package jipthechip.diabolism.events;

import jipthechip.diabolism.Utils.PacketUtils;
import jipthechip.diabolism.entities.blockentities.ClientSyncedInventory;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;

public class SyncEvents {

    public static void registerEvents(){
        ClientEntityEvents.ENTITY_LOAD.register((entity, world)->{
            // When an entity loads in, make sure to sync all custom effects with the client
            if(entity instanceof LivingEntity){
                PacketUtils.syncEntityEffectsWithClient(entity.getId());
            }
            if(entity instanceof ClientSyncedInventory inventoryEntity){
                //inventoryEntity.syncInventoryWithClient();
            }
        });
    }
}
