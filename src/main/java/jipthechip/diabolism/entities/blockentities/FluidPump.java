package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.Utils.BlockHelpers;
import jipthechip.diabolism.Utils.TargetCondition;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.blocks.FluidPumpBlock;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.packets.DiabolismPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
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

public class FluidPump extends AbstractFluidContainer {

    long lastPumped = -1;

    long tickCounter = -1;

    boolean isActive = false;

    private final TargetCondition targetCondition = (blockEntity) -> {
        return blockEntity instanceof AbstractFluidContainer container && !(blockEntity instanceof FluidPump || blockEntity instanceof FluidPipe);
    };

    private static final RawAnimation PUMP_NS = RawAnimation.begin().thenPlay("pipe_pump_animation_northsouth");
    private static final RawAnimation PUMP_EW = RawAnimation.begin().thenLoop("pipe_pump_animation_eastwest");
    private static final RawAnimation PUMP_UD = RawAnimation.begin().thenLoop("pipe_pump_animation_updown");
    private AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    public FluidPump(BlockPos pos, BlockState state) {
        super(DiabolismEntities.FLUID_PUMP_BLOCKENTITY, pos, state,50);
    }

    public static void ticker(World world, BlockPos pos, BlockState state, FluidPump be) {
        be.tick(world, pos, state);
    }

    @Override
    public double getBoneResetTime() {
        return 10;
    }

    private void tick(World world, BlockPos pos, BlockState state){

        if(System.currentTimeMillis() - lastPumped >= 2000 && isActive && !world.isClient){

            BlockPos pumpFromPos = getPos().add(state.get(FluidPumpBlock.PUMP_FROM_DIRECTION).getVector());
            BlockEntity pumpFromBlockEntity = world.getBlockEntity(pumpFromPos);

            System.out.println("target condition: "+targetCondition.evaluate(pumpFromBlockEntity));

            if(targetCondition.evaluate(pumpFromBlockEntity)){ // check if the container being pumped from is a fluid container that is NOT a pipe or pump

                //System.out.println("checking for valid path:");

                boolean successfulPumpAchieved = false;

                int pathCounter = 0;
                List<BlockPos> path;
                List<BlockPos> ignoredTargets = new ArrayList<>();
                do{

                    // search for a path to the closest available fluid container
                    path = BlockHelpers.PipeBFS(world, pumpFromPos, getPos(), targetCondition, ignoredTargets, DiabolismBlocks.FLUID_PIPE, DiabolismBlocks.FLUID_PUMP);

//                    System.out.println("pump pos: "+getPos());
//                    System.out.println("path contains pump pos: "+path.contains(getPos()));
//                    System.out.println("path "+pathCounter+" start");
//                    System.out.println("----------------");
//                    for (BlockPos bpos : path){
//                        System.out.println(bpos);
//                    }

                    System.out.println("path is empty: "+path.isEmpty());
                    System.out.println("start fluid transfer through path ------------------------");
                    for(int i = path.size()-2; i >= 0; i--){ // sequentially transfer fluids from the current container to the next, starting with the second to last in the path and going backwards
                        BlockEntity blockEntity1 = world.getBlockEntity(path.get(i));
                        BlockEntity blockEntity2 = world.getBlockEntity(path.get(i+1));

                        if(blockEntity1 instanceof AbstractFluidContainer container1 && blockEntity2 instanceof AbstractFluidContainer container2){

                            boolean transferSuccess = container1.tryFluidTransfer(container2, container1.getFluidAmount()); // attempt to transfer, and record whether it was able to move any fluid
                            if(transferSuccess) successfulPumpAchieved = true;
                        }
                    }
                    System.out.println();
                    if(!path.isEmpty()){
                        ignoredTargets.add(path.get(path.size()-1));
                    }
                }
                while(!successfulPumpAchieved && !path.isEmpty());
                // if a successful movement of fluid isn't achieved on a path, it will continue to search for other containers to pump to until it can't find any more
            }


            lastPumped = System.currentTimeMillis();
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
    public void readNbt(NbtCompound nbt) {
        this.isActive = nbt.getBoolean("turnedOn");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("turnedOn", this.isActive);
        super.writeNbt(nbt);
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
        Direction dir;
        if(state.getBlock() != DiabolismBlocks.FLUID_PUMP){
           return PlayState.CONTINUE;
        }
        if(!isActive){
            return PlayState.STOP;
        }
        dir = state.get(FluidPumpBlock.PUMP_FROM_DIRECTION);

        if(dir == Direction.NORTH || dir == Direction.SOUTH) {
            event.setAnimation(PUMP_NS);
        }else if(dir == Direction.EAST || dir == Direction.WEST) {
            event.setAnimation(PUMP_EW);
        } else if(dir == Direction.UP || dir == Direction.DOWN) {
            event.setAnimation(PUMP_UD);
        }
        return PlayState.CONTINUE;
    }

    public void toggleActive(){
        this.isActive = !this.isActive;
        syncWithServer();
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

    public void syncWithClient(){
        super.syncWithClient();
        if(!world.isClient){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(isActive);
            buf.writeBlockPos(pos);

            PlayerLookup.tracking(this).forEach(player -> {
                ServerPlayNetworking.send(player, DiabolismPackets.SYNC_FLUID_PUMP_W_CLIENT, buf);
            });
        }
    }

    @Override
    public void syncWithServer() {
        super.syncWithServer();
        if(MinecraftClient.getInstance().player != null && world != null && world.isClient){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(isActive);
            buf.writeBlockPos(pos);

            ClientPlayNetworking.send(DiabolismPackets.SYNC_FLUID_PUMP_W_SERVER, buf);
        }
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }

}
