package jipthechip.diabolism.potion;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.packets.DiabolismPackets;
import jipthechip.diabolism.packets.StatusEffectInstanceData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientSyncedStatusEffect extends StatusEffect {

    protected ClientSyncedStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(!entity.getWorld().isClient){
            super.onApplied(entity, attributes, amplifier);
            PlayerLookup.tracking(entity).forEach(player -> {
                int entityId = entity.getId();
                sendEntityEffectsToClient(entityId, player);
            });
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(!entity.getWorld().isClient){
            super.onRemoved(entity, attributes, amplifier);
            PlayerLookup.tracking(entity).forEach(player -> {
                sendRemovedEntityEffectToClient(entity.getId(), this, player);
            });
        }
    }

    public static void sendEntityEffectsToClient(int entityId, ServerPlayerEntity player){
        Entity entity = player.getWorld().getEntityById(entityId);
        if(entity instanceof LivingEntity livingEntity){

            List<StatusEffectInstance> customEffectInstances = new ArrayList<>();

            // get status effect info to be sent to client
            for(StatusEffectInstance instance : livingEntity.getStatusEffects()){
                if(DiabolismPotions.STATUS_EFFECT_MAP.containsValue(instance.getEffectType())){
                    customEffectInstances.add(instance);
                }
            }

            PacketByteBuf buf = PacketByteBufs.create();

            // write status effect info to packet buf
            buf.writeInt(entityId);
            buf.writeInt(customEffectInstances.size());
            for(StatusEffectInstance instance : customEffectInstances){
                String effectKey = "";
                for(String key : DiabolismPotions.STATUS_EFFECT_MAP.keySet()){
                    if(DiabolismPotions.STATUS_EFFECT_MAP.get(key) == instance.getEffectType()){
                        effectKey = key;
                        break;
                    }
                }
                buf.writeString(DataUtils.SerializeToString(new StatusEffectInstanceData(effectKey, instance.getDuration(), instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles(), instance.shouldShowIcon())));
            }

            // send status effect info to client
            ServerPlayNetworking.send(player, DiabolismPackets.ADD_ENTITY_EFFECTS_ON_CLIENT, buf);
        }
    }

    public static void sendRemovedEntityEffectToClient(int entityId, StatusEffect effect, ServerPlayerEntity player){
        String effectKey = "";
        for(String key : DiabolismPotions.STATUS_EFFECT_MAP.keySet()){
            if(DiabolismPotions.STATUS_EFFECT_MAP.get(key) == effect){
                effectKey = key;
                break;
            }
        }

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        buf.writeString(effectKey);

        ServerPlayNetworking.send(player, DiabolismPackets.REMOVE_ENTITY_EFFECT_ON_CLIENT, buf);
    }
}
