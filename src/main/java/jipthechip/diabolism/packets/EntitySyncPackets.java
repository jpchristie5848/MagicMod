package jipthechip.diabolism.packets;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.Utils.IMagicProperties;
import jipthechip.diabolism.effect.ClientSyncedStatusEffect;
import jipthechip.diabolism.effect.DiabolismEffects;
import jipthechip.diabolism.entities.ProjectileSpellEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntitySyncPackets {

    public static final Identifier SET_ENTITY_RADIUS_PACKET = new Identifier("diabolism", "set_entity_radius");
    public static final Identifier KILL_ENTITY_PACKET = new Identifier("diabolism", "kill_entity");

    public static final Identifier GET_ENTITY_EFFECTS_FROM_SERVER = new Identifier("diabolism", "get_entity_effects_from_server");

    public static final Identifier ADD_ENTITY_EFFECTS_ON_CLIENT = new Identifier("diabolism", "add_entity_effects_on_client");

    public static final Identifier REMOVE_ENTITY_EFFECT_ON_CLIENT = new Identifier("diabolism", "remove_entity_effect_on_client");

    public static void register(){
        ClientPlayNetworking.registerGlobalReceiver(SET_ENTITY_RADIUS_PACKET, (client, handler, buf, responseSender) -> {
            int entityId = buf.readInt();
            float radius = buf.readFloat();

            client.execute(()->{
                ((ProjectileSpellEntity) Objects.requireNonNull(handler.getWorld().getEntityById(entityId))).setRadius(radius);
                System.out.println("set radius to "+radius);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(KILL_ENTITY_PACKET, (server, player, handler, buf, sender) -> {
            int entityId = buf.readInt();

            System.out.println("killing entity "+entityId);

            server.execute(()->{
                Objects.requireNonNull(player.getWorld().getEntityById(entityId)).kill();
                if(((IMagicProperties)player).getMagicShield() == entityId){
                    ((IMagicProperties)player).setMagicShield(-1);
                }
            });
        });


        ServerPlayNetworking.registerGlobalReceiver(GET_ENTITY_EFFECTS_FROM_SERVER, (server, player, handler, buf, sender)->{
            int entityId = buf.readInt();

            server.execute(()->{
                ClientSyncedStatusEffect.sendEntityEffectsToClient(entityId, player);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ADD_ENTITY_EFFECTS_ON_CLIENT, (client, handler, buf, responseSender) ->{
            int entityId = buf.readInt();
            int effectCount = buf.readInt();
            List<StatusEffectInstance> instances = new ArrayList<>();
            for(int i = 0; i < effectCount; i++){
                StatusEffectInstanceData data = (StatusEffectInstanceData) DataUtils.DeserializeFromString(buf.readString());
                if(data != null){
                    instances.add(data.createInstance());
                }
            }
            client.execute(()->{
                Entity entity = client.world.getEntityById(entityId);
                if(entity instanceof LivingEntity livingEntity){
                    for(StatusEffectInstance instance : instances){
                        //System.out.println("set status effect "+instance.getEffectType().getName()+" on entity "+entityId);
                        livingEntity.setStatusEffect(instance, null);
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(REMOVE_ENTITY_EFFECT_ON_CLIENT, (client, handler, buf, responseSender) ->{
            int entityId = buf.readInt();
            String effectKey = buf.readString();

            ClientSyncedStatusEffect effect = (ClientSyncedStatusEffect) DiabolismEffects.MAP.get(effectKey);

            client.execute(()->{
                Entity entity = client.world.getEntityById(entityId);
                if(entity instanceof LivingEntity livingEntity){
                    System.out.println("removed status effect "+effect.getName()+" on entity "+entityId);
                    livingEntity.removeStatusEffect(effect);
                }
            });
        });
    }
}
