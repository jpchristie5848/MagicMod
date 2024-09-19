package jipthechip.diabolism.packets;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.brewing.Fluid;
import jipthechip.diabolism.entities.blockentities.*;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class BlockSyncPackets {
    public static final Identifier SYNC_FLUID_CONTAINER_W_SERVER = new Identifier("diabolism", "sync_fluid_container_w_server");

    public static final Identifier SYNC_CHURNER_W_CLIENT = new Identifier("diabolism", "sync_churner_w_client");
    public static final Identifier SYNC_CHURNER_W_SERVER = new Identifier("diabolism", "sync_churner_w_server");
    public static final Identifier BEGIN_CHURNER_MIXING = new Identifier("diabolism", "begin_churner_mixing");
    public static final Identifier SYNC_FLUID_CONTAINER_W_CLIENT = new Identifier("diabolism", "sync_fluid_container_w_client");

    public static final Identifier SYNC_FERMENTER_W_SERVER = new Identifier("diabolism", "sync_fermenter_w_server");

    public static final Identifier SYNC_FERMENTER_W_CLIENT = new Identifier("diabolism", "sync_fermenter_w_client");

    public static final Identifier SYNC_FLUID_PUMP_W_CLIENT = new Identifier("diabolism", "sync_fluid_pump_w_client");

    public static final Identifier SYNC_FLUID_PUMP_W_SERVER = new Identifier("diabolism", "sync_fluid_pump_w_server");
    public static final Identifier SYNC_MAGICKA_CONSUMER_W_CLIENT = new Identifier("diabolism", "sync_magicka_consumer_w_client");
    public static final Identifier SYNC_BLOCK_INVENTORY_W_CLIENT = new Identifier("diabolism", "sync_block_inventory_w_client");

    public static void register(){

//        ClientPlayNetworking.registerGlobalReceiver(SYNC_FLUID_CONTAINER_W_CLIENT, (client, handler, buf, responseSender) -> {
//            Fluid fluid = (Fluid)DataUtils.DeserializeFromString(buf.readString());
//            BlockPos pos = buf.readBlockPos();
//
//            client.execute(()->{
//                BlockEntity blockEntity = client.player.getWorld().getBlockEntity(pos);
//                if (blockEntity instanceof AbstractFluidContainer fluidContainer) {
//                    fluidContainer.setFluidData(fluid);
//                    //System.out.println("set fluid in sync client for pos "+pos+": "+fluid);
//                }
//            });
//        });
//
//        ServerPlayNetworking.registerGlobalReceiver(SYNC_FLUID_CONTAINER_W_SERVER, (server, player, handler, buf, sender) -> {
//            Fluid fluid = (Fluid)DataUtils.DeserializeFromString(buf.readString());
//            BlockPos pos = buf.readBlockPos();
//            //System.out.println("packet: "+fluidColor+" "+ Arrays.toString(elementContents) +" "+pos);
//
//            server.execute(()->{
//                BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
//                if (blockEntity instanceof AbstractFluidContainer fluidContainer && checkFluidContainerUpdate(fluidContainer, player, fluid)) {
//                    fluidContainer.setFluidData(fluid);
//                    fluidContainer.markDirty();
//                    System.out.println("set fluid in sync server for pos "+pos+": "+fluid);
//                }
//            });
//        });
//
//        ClientPlayNetworking.registerGlobalReceiver(SYNC_CHURNER_W_CLIENT, (client, handler, buf, responseSender) -> {
//            float mixingProgress = buf.readFloat();
//            int mixedIngredients = buf.readInt();
//            BlockPos pos = buf.readBlockPos();
//
//            client.execute(()->{
//                BlockEntity blockEntity = client.player.getWorld().getBlockEntity(pos);
//                if (blockEntity instanceof MagicChurner magicChurner) {
//                    magicChurner.setMixingProgress(mixingProgress);
//                    magicChurner.setMixedIngredients(mixedIngredients);
//                    magicChurner.markDirty();
//                }
//            });
//        });
//
//
//        ServerPlayNetworking.registerGlobalReceiver(SYNC_CHURNER_W_SERVER, (server, player, handler, buf, sender) -> {
//            float mixingProgress = buf.readFloat();
//            int mixedIngredients = buf.readInt();
//            BlockPos pos = buf.readBlockPos();
//
//            server.execute(()->{
//                BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
//                if (blockEntity instanceof MagicChurner magicChurner) {
//                    magicChurner.setMixingProgress(mixingProgress);
//                    magicChurner.setMixedIngredients(mixedIngredients);
//                    magicChurner.markDirty();
//                }
//            });
//        });
//
//        ServerPlayNetworking.registerGlobalReceiver(BEGIN_CHURNER_MIXING, (server, player, handler, buf, sender) -> {
//            BlockPos pos = buf.readBlockPos();
//            server.execute(()->{
//                BlockEntity entity = player.getWorld().getBlockEntity(pos);
//                if(entity instanceof MagicChurner churner){
//                    churner.startMixing();
//                }
//            });
//        });
//
//        ClientPlayNetworking.registerGlobalReceiver(SYNC_FERMENTER_W_CLIENT, (client, handler, buf, responseSender) -> {
//            boolean isOpen = buf.readBoolean();
//            Yeast yeast = (Yeast)DataUtils.DeserializeFromString(buf.readString());
//            float fermentationProgress = buf.readFloat();
//            BlockPos pos = buf.readBlockPos();
//
//            client.execute(()->{
//                BlockEntity blockEntity = client.player.getWorld().getBlockEntity(pos);
//                if(blockEntity instanceof MagicFermenter magicFermenter){
//                    magicFermenter.setOpen(isOpen);
//                    magicFermenter.setYeast(yeast);
//                    magicFermenter.setFermentationProgress(fermentationProgress);
//                }
//            });
//        });
//
//        ServerPlayNetworking.registerGlobalReceiver(SYNC_FERMENTER_W_SERVER, (server, player, handler, buf, sender)->{
//            boolean isOpen = buf.readBoolean();
//            Yeast yeast = (Yeast)DataUtils.DeserializeFromString(buf.readString());
//            BlockPos pos = buf.readBlockPos();
//
//            server.execute(()->{
//                BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
//                if(blockEntity instanceof MagicFermenter magicFermenter){
//                    magicFermenter.setOpen(isOpen);
//                    magicFermenter.setYeast(yeast);
//                    magicFermenter.syncWithClient();
//                }
//            });
//        });
//
//        ClientPlayNetworking.registerGlobalReceiver(SYNC_FLUID_PUMP_W_CLIENT, (client, handler, buf, responseSender) -> {
//            boolean isActive = buf.readBoolean();
//            BlockPos pos = buf.readBlockPos();
//
//            client.execute(()->{
//                BlockEntity blockEntity = client.player.getWorld().getBlockEntity(pos);
//                if(blockEntity instanceof FluidPump fluidPump){
//                    fluidPump.setActive(isActive);
//                }
//            });
//        });
//
//        ServerPlayNetworking.registerGlobalReceiver(SYNC_FLUID_PUMP_W_SERVER, (server, player, handler, buf, sender)->{
//            boolean isActive = buf.readBoolean();
//            BlockPos pos = buf.readBlockPos();
//
//            server.execute(()->{
//                BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
//                if(blockEntity instanceof FluidPump fluidPump){
//                    fluidPump.setActive(isActive);
//                    fluidPump.syncWithClient();
//                }
//            });
//        });
//
//        ClientPlayNetworking.registerGlobalReceiver(SYNC_MAGICKA_CONSUMER_W_CLIENT, (client, handler, buf, responseSender) ->{
//            int numFluids = buf.readInt();
//            List<MagickaFluid> fluids = new ArrayList<>();
//
//            for(int i = 0; i < numFluids; i++){
//                fluids.add((MagickaFluid) DataUtils.DeserializeFromString(buf.readString()));
//            }
//            int progress = buf.readInt();
//            BlockPos pos = buf.readBlockPos();
//
//            //System.out.println("syncing magicka consumer w client");
//            client.execute(()->{
//                BlockEntity blockEntity = client.world.getBlockEntity(pos);
//                if(blockEntity instanceof AbstractMagickaConsumer consumer){
//                    consumer.setFluids(fluids);
//                    consumer.setProgress(progress);
//                    //System.out.println("on client progress is now: "+consumer.getProgress());
//                }
//            });
//        });
//
//        ClientPlayNetworking.registerGlobalReceiver(SYNC_BLOCK_INVENTORY_W_CLIENT, (client, handler, buf, responseSender) -> {
//            BlockPos pos = buf.readBlockPos();
//            int numStacks = buf.readInt();
//            List<ItemStack> stacks = new ArrayList<>();
//
//            for(int i = 0; i < numStacks; i++){
//                stacks.add(buf.readItemStack());
//            }
//
//            client.execute(()->{
//                BlockEntity blockEntity = client.world.getBlockEntity(pos);
//                if(blockEntity instanceof ImplementedInventory inventoryBlock) {
//                    for(int i = 0; i < numStacks; i++){
//                        inventoryBlock.setStack(i, stacks.get(i));
//                    }
//                }
//            });
//        });
//
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
            Fluid bucketFluid = (Fluid) DataUtils.DeserializeFromString(nbt.getString("fluid"));
            //System.out.println("fluid in player's hand: "+(bucketFluid == null ? "null" : bucketFluid));
            packetFluidMatchesBucketFluid = (packetFluid == null && bucketFluid == null) || (packetFluid != null && packetFluid.equals(bucketFluid));
            //System.out.println("packet fluid matches bucket fluid: "+packetFluidMatchesBucketFluid);
        }
        return (container instanceof MagicChurner || container instanceof MagicFermenter) && (itemInHand == Items.BUCKET || packetFluidMatchesBucketFluid);
    }
}
