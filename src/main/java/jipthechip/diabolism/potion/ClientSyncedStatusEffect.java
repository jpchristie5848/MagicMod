package jipthechip.diabolism.potion;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Spell;
import jipthechip.diabolism.packets.DiabolismPackets;
import jipthechip.diabolism.packets.StatusEffectInstanceData;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientSyncedStatusEffect extends StatusEffect {

    protected ClientSyncedStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if(!entity.getWorld().isClient){
            super.onApplied(entity, amplifier);
            PlayerLookup.tracking(entity).forEach(player -> {
                int entityId = entity.getId();
                sendEntityEffectsToClient(entityId, player);
            });
        }
    }

    @Override
    public void onRemoved(AttributeContainer attributes) {

        // used to sync with client here, but had to move to mixin cause
        // mojang removed the LivingEntity from this method >:(


    }

    public static void sendEntityEffectsToClient(int entityId, ServerPlayerEntity player){
        Entity entity = player.getWorld().getEntityById(entityId);
        if(entity instanceof LivingEntity livingEntity){

            // get status effect info to be sent to client
            List<StatusEffectInstanceData> statusEffectList = new ArrayList<>();
            for(StatusEffectInstance instance : livingEntity.getStatusEffects()){
                if(instance.getEffectType() instanceof ClientSyncedStatusEffect clientEffect){
                    statusEffectList.add(new StatusEffectInstanceData(clientEffect.getRegistryName(), instance.getDuration(),
                            instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles(), instance.shouldShowIcon()));
                }
            }
            if(statusEffectList.size() > 0){
                PacketByteBuf buf = PacketByteBufs.create();

                // write status effect info to packet buf
                buf.writeInt(entityId);
                buf.writeInt(statusEffectList.size());
                for(StatusEffectInstanceData data : statusEffectList){
                    buf.writeString(DataUtils.SerializeToString(data));
                }

                // send status effect info to client
                ServerPlayNetworking.send(player, DiabolismPackets.ADD_ENTITY_EFFECTS_ON_CLIENT, buf);
            }
        }
    }

    public String getRegistryName(){
        return this.getClass().getSimpleName().replace("StatusEffect", "").toLowerCase();
    }

    /*    moved to {@link LivingEntityStatusEffectMixin} */

//    public static void sendRemovedEntityEffectToClient(int entityId, StatusEffect effect, ServerPlayerEntity player){
//        String effectKey = "";
//        for(String key : DiabolismEffects.STATUS_EFFECT_MAP.keySet()){
//            if(DiabolismEffects.STATUS_EFFECT_MAP.get(key) == effect){
//                effectKey = key;
//                break;
//            }
//        }
//
//        PacketByteBuf buf = PacketByteBufs.create();
//        buf.writeInt(entityId);
//        buf.writeString(effectKey);
//
//        ServerPlayNetworking.send(player, DiabolismPackets.REMOVE_ENTITY_EFFECT_ON_CLIENT, buf);
//    }
}
