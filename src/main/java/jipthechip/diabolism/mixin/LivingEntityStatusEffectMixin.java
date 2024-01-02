package jipthechip.diabolism.mixin;


import jipthechip.diabolism.packets.DiabolismPackets;
import jipthechip.diabolism.potion.ClientSyncedStatusEffect;
import jipthechip.diabolism.potion.DiabolismEffects;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityStatusEffectMixin {


    @Inject(method="onStatusEffectRemoved", at=@At("HEAD"))
    public void syncEffectRemovalWithClient(StatusEffectInstance effect, CallbackInfo ci){

        if(effect.getEffectType() instanceof ClientSyncedStatusEffect){
            LivingEntity entity = (LivingEntity) (Object) this;

            if(!entity.getWorld().isClient){
                PlayerLookup.tracking(entity).forEach(player -> {
                    sendRemovedEntityEffectToClient(entity.getId(), effect.getEffectType(), player);
                });
            }
        }
    }


    private static void sendRemovedEntityEffectToClient(int entityId, StatusEffect effect, ServerPlayerEntity player){

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        buf.writeString(effect.getName().getString());

        ServerPlayNetworking.send(player, DiabolismPackets.REMOVE_ENTITY_EFFECT_ON_CLIENT, buf);
    }
}
