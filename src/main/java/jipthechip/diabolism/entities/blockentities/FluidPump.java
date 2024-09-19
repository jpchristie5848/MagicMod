package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.Utils.BlockHelpers;
import jipthechip.diabolism.Utils.TargetCondition;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.blocks.FluidPumpBlock;
import jipthechip.diabolism.data.brewing.entity.FluidPumpData;
import jipthechip.diabolism.entities.DiabolismEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FluidPump extends AbstractFluidContainer<FluidPumpData> {

    long lastPumped = -1;

    long tickCounter = -1;
    int num_updates = 0;

    private final TargetCondition targetCondition = (blockEntity) -> {

        return (blockEntity instanceof AbstractFluidContainer &&
                !(blockEntity instanceof FluidPump || blockEntity instanceof FluidPipe)
                /*!(blockEntity instanceof MagicFermenter fermenter && fermenter.isFermenting()))*/
                || blockEntity instanceof AbstractMagickaConsumer consumer);
    };


    private static final RawAnimation PUMP_NS = RawAnimation.begin().thenPlay("pipe_pump_animation_northsouth");
    private AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    public FluidPump(BlockPos pos, BlockState state) {
        super(DiabolismEntities.FLUID_PUMP_BLOCKENTITY, pos, state,50, false, new FluidPumpData());
    }

    public static void ticker(World world, BlockPos pos, BlockState state, FluidPump be) {
        be.tick(world, pos, state);
    }

    @Override
    public double getBoneResetTime() {
        return 10;
    }

    private void tick(World world, BlockPos pos, BlockState state){

        if(System.currentTimeMillis() - lastPumped >= 2000 && data != null && data.getExtendedData().isActive() && !world.isClient){
            long currentTime = System.currentTimeMillis();
            BlockPos pumpFromPos = getPos().add(state.get(FluidPumpBlock.PUMP_FROM_DIRECTION).getVector());
            BlockEntity pumpFromBlockEntity = world.getBlockEntity(pumpFromPos);

            //System.out.println("target condition on pump from: "+targetCondition.evaluate(pumpFromBlockEntity));

            if(targetCondition.evaluate(pumpFromBlockEntity)){ // check if the container being pumped from is a fluid container that is NOT a pipe or pump

                //System.out.println("checking for valid path:");

                boolean successfulPumpAchieved = false;

                int pathCounter = 0;
                List<BlockPos> path;
                List<BlockPos> ignoredTargets = new ArrayList<>();
                do{

                    // search for a path to the closest available fluid container
                    path = BlockHelpers.PipeBFS(world, pumpFromPos, getPos(), targetCondition, ignoredTargets, DiabolismBlocks.FLUID_PIPE, DiabolismBlocks.FLUID_PUMP);

                    for(int i = path.size()-2; i >= 0; i--){ // sequentially transfer fluids from the current container to the next, starting with the second to last in the path and going backwards
                        BlockEntity blockEntity1 = world.getBlockEntity(path.get(i));
                        BlockEntity blockEntity2 = world.getBlockEntity(path.get(i+1));

                        // transferring into other fluid container
                        if(blockEntity1 instanceof AbstractFluidContainer container1 && blockEntity2 instanceof AbstractFluidContainer container2){

                            boolean transferSuccess = container1.tryFluidTransfer(container2, container1.getFluidAmount()); // attempt to transfer, and record whether it was able to move any fluid
                            if(transferSuccess) successfulPumpAchieved = true;

                        // transferring into magicka consumer
                        }else if(blockEntity1 instanceof AbstractFluidContainer container && blockEntity2 instanceof AbstractMagickaConsumer consumer){

                            //System.out.println("fluid in pump tick: "+container.getFluidData().getBaseData());

                            int amountToRemove = consumer.addFluid(container.getFluidData(), container.getFluidAmount());
                            if(amountToRemove > 0) {
                                successfulPumpAchieved = true;
                                container.removeFluid(amountToRemove);
                                container.sync();
                            }
                        }
                    }
                    if(!path.isEmpty()){
                        ignoredTargets.add(path.get(path.size()-1));
                    }
                }
                while(!successfulPumpAchieved && !path.isEmpty());
                // if a successful movement of fluid isn't achieved on a path, it will continue to search for other containers to pump to
            }


            lastPumped = System.currentTimeMillis();
            num_updates++;
        }
        tickCounter++;

//        if(tickCounter % 20 == 0){
//            System.out.println("mixed ingredients: "+mixedIngredients);
//            List<BlockPos> path = BlockHelpers.OmniDirectionalBFS(world, DiabolismBlocks.FLUID_PIPE, pos, targetCondition);
//            System.out.println("path start");
//            System.out.println("----------------");
//            for (BlockPos bpos : path){
//                System.out.println(bpos);
//            }
//        }

//        if(System.currentTimeMillis() - lastTriggered < 2000){ // check if block was triggered by pump
//            if(System.currentTimeMillis() - lastTransferred >= 2000 && getFluidAmount() > 0){ // if triggered by pump, should transfer fluid every 2s and
//                if(next != null){
//                    BlockEntity blockEntity = world.getBlockEntity(next);
//                    if(blockEntity instanceof AbstractFluidContainer container){
//                        container.addFluid(fluidRenderData, getFluidAmount());
//                        container.setElementContents(elementContents);
//                        this.emptyFluid();
//                    }
//                }
//            }
//        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<FluidPump> controller = new AnimationController<FluidPump>(this, "fluid_pump_controller", this::predicate);
        controllerRegistrar.add(controller);
    }

    private PlayState predicate(AnimationState<FluidPump> event) {
        World world = getWorld();
        BlockPos pos = getPos();
        BlockState state = world.getBlockState(pos);
        if(state.getBlock() != DiabolismBlocks.FLUID_PUMP){
           return PlayState.CONTINUE;
        }
        if(data != null && !data.getExtendedData().isActive()){
            return PlayState.STOP;
        }
        event.setAnimation(PUMP_NS);

        return PlayState.CONTINUE;
    }

    public boolean toggleActive(){
        if(!world.isClient) {
            data.getExtendedData().toggleActive();
            sync();
            return true;
        }
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.instanceCache;
    }

    public double getTick(Object o) {
        return RenderUtils.getCurrentTick();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }


    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

//    public void syncWithClient(){
//        super.syncWithClient();
//        if(!world.isClient){
//            PacketByteBuf buf = PacketByteBufs.create();
//            buf.writeBoolean(isActive);
//            buf.writeBlockPos(pos);
//
//            PlayerLookup.tracking(this).forEach(player -> {
//                ServerPlayNetworking.send(player, BlockSyncPackets.SYNC_FLUID_PUMP_W_CLIENT, buf);
//            });
//        }
//    }
//
//    @Override
//    public void syncWithServer() {
//        super.syncWithServer();
//        if(MinecraftClient.getInstance().player != null && world != null && world.isClient){
//            PacketByteBuf buf = PacketByteBufs.create();
//            buf.writeBoolean(isActive);
//            buf.writeBlockPos(pos);
//
//            ClientPlayNetworking.send(BlockSyncPackets.SYNC_FLUID_PUMP_W_SERVER, buf);
//        }
//    }


    public Direction getPumpFromDir(){
        World world = getWorld();
        if(world != null){
            BlockState state = world.getBlockState(getPos());
            if(state.getBlock() != DiabolismBlocks.FLUID_PUMP){
                return Direction.NORTH;
            }else{
                return state.get(FluidPumpBlock.PUMP_FROM_DIRECTION);
            }
        }
        return null;
    }

}
