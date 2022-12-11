package jipthechip.diabolism.packets;

import jipthechip.diabolism.entities.ProjectileSpellEntity;
import jipthechip.diabolism.Utils.IMagicProperties;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class DiabolismPackets {

    public static final Identifier SET_BLOCK_PACKET = new Identifier("diabolism", "set_block");
    public static final Identifier SET_ENTITY_RADIUS_PACKET = new Identifier("diabolism", "set_entity_radius");
    public static final Identifier KILL_ENTITY_PACKET = new Identifier("diabolism", "kill_entity");

    public static void registerPacketReceivers(){

        // register SET BLOCK packet receiver
        ServerPlayNetworking.registerGlobalReceiver(SET_BLOCK_PACKET, (server, player, handler, buf, sender) -> {
            BlockPos pos = buf.readBlockPos();
            Block blockToSet = Registry.BLOCK.get(buf.readIdentifier());

            server.execute(()->{
                player.getWorld().setBlockState(pos, blockToSet.getDefaultState());
            });
        });

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
    }
}