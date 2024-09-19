package jipthechip.diabolism.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ClientPackets {

    public static final Identifier SPLASH_PARTICLE_PACKET = new Identifier("diabolism", "splash_particle");
    public static void register(){
        ServerPlayNetworking.registerGlobalReceiver(SPLASH_PARTICLE_PACKET, (server, player, handler, buf, sender) -> {
            BlockPos pos = buf.readBlockPos();
            server.execute(()->{
                player.getWorld().addParticle(ParticleTypes.SPLASH, pos.getX()+0.15 + Math.random()*0.7, pos.getY()+1.5, pos.getX()+0.15 + Math.random()*0.7, 0, 0,0);
            });
        });
    }
}
