package jipthechip.diabolism.packets;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.MagickaFluid;
import jipthechip.diabolism.data.Yeast;
import jipthechip.diabolism.entities.ProjectileSpellEntity;
import jipthechip.diabolism.Utils.IMagicProperties;
import jipthechip.diabolism.entities.blockentities.*;
import jipthechip.diabolism.data.Fluid;
import jipthechip.diabolism.potion.ClientSyncedStatusEffect;
import jipthechip.diabolism.potion.DiabolismEffects;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiabolismPackets {

    public static final Identifier SET_BLOCK_PACKET = new Identifier("diabolism", "set_block");
    public static final Identifier SET_ENTITY_RADIUS_PACKET = new Identifier("diabolism", "set_entity_radius");
    public static final Identifier KILL_ENTITY_PACKET = new Identifier("diabolism", "kill_entity");

    public static final Identifier SYNC_CHURNER_W_SERVER = new Identifier("diabolism", "sync_churner_w_server");
    public static final Identifier SYNC_FLUID_CONTAINER_W_SERVER = new Identifier("diabolism", "sync_fluid_container_w_server");

    public static final Identifier SYNC_CHURNER_W_CLIENT = new Identifier("diabolism", "sync_churner_w_client");
    public static final Identifier SYNC_FLUID_CONTAINER_W_CLIENT = new Identifier("diabolism", "sync_fluid_container_w_client");

    public static final Identifier SYNC_FERMENTER_W_SERVER = new Identifier("diabolism", "sync_fermenter_w_server");

    public static final Identifier SYNC_FERMENTER_W_CLIENT = new Identifier("diabolism", "sync_fermenter_w_client");

    public static final Identifier BEGIN_CHURNER_MIXING = new Identifier("diabolism", "begin_churner_mixing");

    public static final Identifier SPLASH_PARTICLE_PACKET = new Identifier("diabolism", "splash_particle");

    public static final Identifier SYNC_FLUID_PUMP_W_CLIENT = new Identifier("diabolism", "sync_fluid_pump_w_client");

    public static final Identifier SYNC_FLUID_PUMP_W_SERVER = new Identifier("diabolism", "sync_fluid_pump_w_server");

    public static final Identifier GET_ENTITY_EFFECTS_FROM_SERVER = new Identifier("diabolism", "get_entity_effects_from_server");

    public static final Identifier ADD_ENTITY_EFFECTS_ON_CLIENT = new Identifier("diabolism", "add_entity_effects_on_client");

    public static final Identifier REMOVE_ENTITY_EFFECT_ON_CLIENT = new Identifier("diabolism", "remove_entity_effect_on_client");
    public static final Identifier SYNC_MAGICKA_CONSUMER_W_CLIENT = new Identifier("diabolism", "sync_magicka_consumer_w_client");

    public static void registerPacketReceivers(){

        // register SET BLOCK packet receiver
        ServerPlayNetworking.registerGlobalReceiver(SET_BLOCK_PACKET, (server, player, handler, buf, sender) -> {
            BlockPos pos = buf.readBlockPos();
            Block blockToSet = Registries.BLOCK.get(buf.readIdentifier());

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

        ServerPlayNetworking.registerGlobalReceiver(SYNC_CHURNER_W_SERVER, (server, player, handler, buf, sender) -> {
            float mixingProgress = buf.readFloat();
            int mixedIngredients = buf.readInt();
            BlockPos pos = buf.readBlockPos();

            syncChurnerWServer(mixingProgress, mixedIngredients, pos, server, player);
        });

        ClientPlayNetworking.registerGlobalReceiver(SYNC_FLUID_CONTAINER_W_CLIENT, (client, handler, buf, responseSender) -> {
            Fluid fluid = (Fluid)DataUtils.DeserializeFromString(buf.readString());
            BlockPos pos = buf.readBlockPos();

            syncFluidContainerWClient(fluid, pos, client);
        });

        ServerPlayNetworking.registerGlobalReceiver(SYNC_FLUID_CONTAINER_W_SERVER, (server, player, handler, buf, sender) -> {
            Fluid fluid = (Fluid)DataUtils.DeserializeFromString(buf.readString());
            BlockPos pos = buf.readBlockPos();
            //System.out.println("packet: "+fluidColor+" "+ Arrays.toString(elementContents) +" "+pos);

            syncFluidContainerWServer(fluid, pos, server, player);
        });

        ServerPlayNetworking.registerGlobalReceiver(SPLASH_PARTICLE_PACKET, (server, player, handler, buf, sender) -> {
           BlockPos pos = buf.readBlockPos();
           server.execute(()->{
               player.getWorld().addParticle(ParticleTypes.SPLASH, pos.getX()+0.15 + Math.random()*0.7, pos.getY()+1.5, pos.getX()+0.15 + Math.random()*0.7, 0, 0,0);
           });
        });

        ClientPlayNetworking.registerGlobalReceiver(SYNC_CHURNER_W_CLIENT, (client, handler, buf, responseSender) -> {
            float mixingProgress = buf.readFloat();
            int mixedIngredients = buf.readInt();
            BlockPos pos = buf.readBlockPos();

            syncChurnerWClient(mixingProgress, mixedIngredients, pos, client);
        });

        ServerPlayNetworking.registerGlobalReceiver(BEGIN_CHURNER_MIXING, (server, player, handler, buf, sender) -> {
            BlockPos pos = buf.readBlockPos();
            server.execute(()->{
                BlockEntity entity = player.getWorld().getBlockEntity(pos);
                if(entity instanceof MagicChurner churner){
                    churner.startMixing();
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(SYNC_FERMENTER_W_CLIENT, (client, handler, buf, responseSender) -> {
            boolean isOpen = buf.readBoolean();
            Yeast yeast = (Yeast)DataUtils.DeserializeFromString(buf.readString());
            float fermentationProgress = buf.readFloat();
            BlockPos pos = buf.readBlockPos();

            syncFermenterWClient(isOpen, yeast, fermentationProgress, pos, client);
        });

        ServerPlayNetworking.registerGlobalReceiver(SYNC_FERMENTER_W_SERVER, (server, player, handler, buf, sender)->{
            boolean isOpen = buf.readBoolean();
            Yeast yeast = (Yeast)DataUtils.DeserializeFromString(buf.readString());
            BlockPos pos = buf.readBlockPos();

            syncFermenterWServer(isOpen, yeast, pos, server, player);
        });

        ClientPlayNetworking.registerGlobalReceiver(SYNC_FLUID_PUMP_W_CLIENT, (client, handler, buf, responseSender) -> {
           boolean isActive = buf.readBoolean();
           BlockPos pos = buf.readBlockPos();

           syncFluidPumpWClient(isActive, pos, client);
        });

        ServerPlayNetworking.registerGlobalReceiver(SYNC_FLUID_PUMP_W_SERVER, (server, player, handler, buf, sender)->{
            boolean isActive = buf.readBoolean();
            BlockPos pos = buf.readBlockPos();

            syncFluidPumpWServer(isActive, pos, server, player);
        });

        ServerPlayNetworking.registerGlobalReceiver(GET_ENTITY_EFFECTS_FROM_SERVER, (server, player, handler, buf, sender)->{
            int entityId = buf.readInt();

            getEntityEffectsFromServer(entityId, server, player);
        });

        ClientPlayNetworking.registerGlobalReceiver(ADD_ENTITY_EFFECTS_ON_CLIENT, (client, handler, buf, responseSender) ->{
            int entityId = buf.readInt();
            int effectCount = buf.readInt();
            List<StatusEffectInstance> instances = new ArrayList<>();
            for(int i = 0; i < effectCount; i++){
                StatusEffectInstanceData data = (StatusEffectInstanceData) DataUtils.DeserializeFromString(buf.readString());
                if(data != null){
                    instances.add(data.createFromData());
                }
            }
            addEntityEffectsOnClient(entityId, instances, client);
        });

        ClientPlayNetworking.registerGlobalReceiver(REMOVE_ENTITY_EFFECT_ON_CLIENT, (client, handler, buf, responseSender) ->{
           int entityId = buf.readInt();
           String effectKey = buf.readString();

           removeEntityEffectOnClient(entityId, DiabolismEffects.CLIENT_SYNCED.get(effectKey), client);
        });

        ClientPlayNetworking.registerGlobalReceiver(SYNC_MAGICKA_CONSUMER_W_CLIENT, (client, handler, buf, responseSender) ->{
            int numFluids = buf.readInt();
            List<MagickaFluid> fluids = new ArrayList<>();

            for(int i = 0; i < numFluids; i++){
                fluids.add((MagickaFluid) DataUtils.DeserializeFromString(buf.readString()));
            }
            int progress = buf.readInt();
            BlockPos pos = buf.readBlockPos();

            syncMagickaConsumerWClient(fluids, progress, pos, client);
        });
    }

    private static void syncFluidContainerWClient(Fluid fluid, BlockPos pos, MinecraftClient client){
        client.execute(()->{
            BlockEntity blockEntity = client.player.getWorld().getBlockEntity(pos);
            if (blockEntity instanceof AbstractFluidContainer fluidContainer) {
                fluidContainer.setFluid(fluid);
                //System.out.println("set fluid in sync client for pos "+pos+": "+fluid);
            }
        });
    }

    private static void syncFluidContainerWServer(Fluid fluid, BlockPos pos, MinecraftServer server, ServerPlayerEntity player){
        server.execute(()->{
            BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
            if (blockEntity instanceof AbstractFluidContainer fluidContainer && checkFluidContainerUpdate(fluidContainer, player, fluid)) {
                fluidContainer.setFluid(fluid);
                fluidContainer.markDirty();
                System.out.println("set fluid in sync server for pos "+pos+": "+fluid);
            }
        });
    }

    private static void syncChurnerWServer(float mixingProgress, int mixedIngredients, BlockPos pos, MinecraftServer server, ServerPlayerEntity player){
        server.execute(()->{
            BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
            if (blockEntity instanceof MagicChurner magicChurner) {
                magicChurner.setMixingProgress(mixingProgress);
                magicChurner.setMixedIngredients(mixedIngredients);
                magicChurner.markDirty();
            }
        });
    }

    private static void syncChurnerWClient(float mixingProgress, int mixedIngredients, BlockPos pos, MinecraftClient client){
        client.execute(()->{
            BlockEntity blockEntity = client.player.getWorld().getBlockEntity(pos);
            if (blockEntity instanceof MagicChurner magicChurner) {
                magicChurner.setMixingProgress(mixingProgress);
                magicChurner.setMixedIngredients(mixedIngredients);
                magicChurner.markDirty();
            }
        });
    }


    private static void syncFermenterWClient(boolean isOpen, Yeast yeast, float fermentationProgress, BlockPos pos, MinecraftClient client){
        client.execute(()->{
            BlockEntity blockEntity = client.player.getWorld().getBlockEntity(pos);
            if(blockEntity instanceof MagicFermenter magicFermenter){
                magicFermenter.setOpen(isOpen);
                magicFermenter.setYeast(yeast);
                magicFermenter.setFermentationProgress(fermentationProgress);
            }
        });
    }

    private static void syncFermenterWServer(boolean isOpen, Yeast yeast, BlockPos pos, MinecraftServer server, ServerPlayerEntity player) {
        server.execute(()->{
            BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
            if(blockEntity instanceof MagicFermenter magicFermenter){
                magicFermenter.setOpen(isOpen);
                magicFermenter.setYeast(yeast);
                magicFermenter.syncWithClient();
            }
        });
    }

    private static void syncFluidPumpWClient(boolean isActive, BlockPos pos, MinecraftClient client){
        client.execute(()->{
            BlockEntity blockEntity = client.player.getWorld().getBlockEntity(pos);
            if(blockEntity instanceof FluidPump fluidPump){
                fluidPump.setActive(isActive);
            }
        });
    }

    private static void syncFluidPumpWServer(boolean isActive, BlockPos pos, MinecraftServer server, ServerPlayerEntity player) {
        server.execute(()->{
            BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
            if(blockEntity instanceof FluidPump fluidPump){
                fluidPump.setActive(isActive);
                fluidPump.syncWithClient();
            }
        });
    }

    private static void getEntityEffectsFromServer(int entityId, MinecraftServer server, ServerPlayerEntity player){
        server.execute(()->{
            ClientSyncedStatusEffect.sendEntityEffectsToClient(entityId, player);
        });
    }

    private static void addEntityEffectsOnClient(int entityId, List<StatusEffectInstance> instances, MinecraftClient client){
         client.execute(()->{
             Entity entity = client.world.getEntityById(entityId);
             if(entity instanceof LivingEntity livingEntity){
                 for(StatusEffectInstance instance : instances){
                     //System.out.println("set status effect "+instance.getEffectType().getName()+" on entity "+entityId);
                     livingEntity.setStatusEffect(instance, null);
                 }
             }
         });
    }

    private static void removeEntityEffectOnClient(int entityId, ClientSyncedStatusEffect effect, MinecraftClient client){
        client.execute(()->{
            Entity entity = client.world.getEntityById(entityId);
            if(entity instanceof LivingEntity livingEntity){
                System.out.println("removed status effect "+effect.getName()+" on entity "+entityId);
                livingEntity.removeStatusEffect(effect);
            }
        });
    }

    private static void syncMagickaConsumerWClient(List<MagickaFluid> fluids, int progress, BlockPos pos, MinecraftClient client){
        //System.out.println("syncing magicka consumer w client");
        client.execute(()->{
            BlockEntity blockEntity = client.world.getBlockEntity(pos);
            if(blockEntity instanceof AbstractMagickaConsumer consumer){
                consumer.setFluids(fluids);
                consumer.setProgress(progress);
                System.out.println("on client progress is now: "+consumer.getProgress());
            }
        });
    }

    // checks if the player updating the BE is tracking it and is within 6 blocks, helps prevent players from using hacks to
    // update block entities maliciously
    private static boolean checkDistanceFromBE(BlockEntity be, ServerPlayerEntity player){
        return PlayerLookup.tracking(be).contains(player) && be.getPos().isWithinDistance(player.getPos(), 6);
    }

    // runs checks to make sure that incoming client packets for updating fluid containers are not malicious
    private static boolean checkFluidContainerUpdate(AbstractFluidContainer container, ServerPlayerEntity player, Fluid packetFluid){
                                                    /* client will never request to update fluid in pipes, so any requests to do this are likely malicious */
        //System.out.println("check distance: "+checkDistanceFromBE(container, player));
        if (!checkDistanceFromBE(container, player) || container instanceof FluidPipe || container instanceof FluidPump) return false;

        Item itemInHand = player.getStackInHand(Hand.MAIN_HAND).getItem();

        //System.out.println("item in player's hand: "+itemInHand);

        boolean packetFluidMatchesBucketFluid = false;

        // TODO check if bucket is still full by this point
        NbtCompound nbt = player.getStackInHand(Hand.MAIN_HAND).getNbt();
        if(nbt != null){
            Fluid bucketFluid = (Fluid)DataUtils.DeserializeFromString(nbt.getString("fluid"));
            //System.out.println("fluid in player's hand: "+(bucketFluid == null ? "null" : bucketFluid));
            packetFluidMatchesBucketFluid = (packetFluid == null && bucketFluid == null) || (packetFluid != null && packetFluid.equals(bucketFluid));
            //System.out.println("packet fluid matches bucket fluid: "+packetFluidMatchesBucketFluid);
        }
        return (container instanceof MagicChurner || container instanceof MagicFermenter) && (itemInHand == Items.BUCKET || packetFluidMatchesBucketFluid);
    }
}