package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Yeast;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.packets.DiabolismPackets;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.Random;

public class MagicFermenter extends AbstractFluidContainer implements GeoBlockEntity {

    private long tickCounter = 0;
    private long lastToggled = 0;
    private boolean isOpen = false;

    private long lastFermentUpdate = 0;

    private float fermentationProgress = 0;

    private Yeast yeast;

    private AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation OPEN_ANIM = RawAnimation.begin().thenLoop("lid_open");
    private static final RawAnimation FLOAT_ANIM = RawAnimation.begin().thenLoop("lid_float");



    public MagicFermenter(BlockPos pos, BlockState state) {
        super(DiabolismEntities.MAGIC_FERMENTER_BLOCKENTITY, pos, state, 1000);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

        //controllerRegistrar.add()
        controllerRegistrar.add(new AnimationController<MagicFermenter>(this, "magic_fermenter_controller", this::predicate));
    }

    private PlayState predicate(AnimationState<MagicFermenter> animationState) {
        if(isOpen){
            if (System.currentTimeMillis() - lastToggled < 1000){
                animationState.setAnimation(OPEN_ANIM);
            }else{
                animationState.setAnimation(FLOAT_ANIM);
            }
        }else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.instanceCache;
    }

    @Override
    public double getBoneResetTime() {
        return 20;
    }

    public static void ticker(World world, BlockPos blockPos, BlockState state, MagicFermenter be) {
        be.tick(world, blockPos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state){
        tickCounter++;

        if(!isOpen && fluid != null && fluid.getAmount() >= 1000 && yeast != null && !world.isClient) {
            if(System.currentTimeMillis() - lastFermentUpdate > 10000){
                boolean fermentationOngoing = false;
                for(int i = 0; i < fluid.getElementContents().length; i++){
                    if(fluid.getElementContents()[i] > 0){
                        float elementToRemove = Math.min(1.0f, fluid.getElementContents()[i])*0.25f;
                        float magickaToCreate = (yeast.getYieldMultipliers()[i]*elementToRemove);
                        if(elementToRemove < 0.25f){
                            fluid.setElementContents(i, 0.0f);
                        }else{
                            fluid.removeElementContent(i, elementToRemove);
                        }
                        fluid.addMagickaContent(i, magickaToCreate);
                        fermentationOngoing = true;
                    }
                }
                if(!fermentationOngoing){
                    yeast = null;
                }
                System.out.println("current elements: "+ Arrays.toString(fluid.getElementContents()));
                System.out.println("current magicka: "+ Arrays.toString(fluid.getMagickaContents()));
                syncWithClient();
                markDirty();
                lastFermentUpdate = System.currentTimeMillis();
            }
            if (tickCounter % 10 == 0){
                double randomX = (Math.random()*0.7);
                double randomY = (Math.random()*0.05);
                double randomZ  = (Math.random()*0.7);
                //System.out.println("random vals: "+randomX+", "+randomY+", "+randomZ);
                Vec3d sparklePos = new Vec3d(pos.getX()+0.15 + randomX, pos.getY()+1.2+randomY, pos.getZ()+0.15 + randomZ);
                //System.out.println("sparklePos: "+sparklePos);
                int age = (int) (Math.random()*10+30);
                Random random = new Random();
                int i = random.nextInt(3);
                int color = i == 0 ? getFluidColor() : yeast.getColor(i-1);
                PlayerLookup.tracking(this).forEach(player ->
                        ((ServerWorld)world).spawnParticles(ColoredSpellParticleFactory.createData(color, age), sparklePos.getX(), sparklePos.getY(), sparklePos.getZ(), 1, 0.01,0.25, 0.01, 0));
            }

        }

        if(tickCounter % 5 == 0 && isOpen && fluid != null && fluid.getMagickaContents() != null && !world.isClient){
            double randomX = (Math.random()*0.7);
            double randomY = (Math.random()*0.05);
            double randomZ  = (Math.random()*0.7);
            //System.out.println("random vals: "+randomX+", "+randomY+", "+randomZ);
            Vec3d sparklePos = new Vec3d(pos.getX()+0.15 + randomX, pos.getY()+0.9+randomY, pos.getZ()+0.15 + randomZ);
            //System.out.println("sparklePos: "+sparklePos);
            int age = (int) (Math.random()*20+5.0f);
            Random random = new Random();
            int i = random.nextInt(3);
            int color = fluid.getMagickaParticleColor();
            PlayerLookup.tracking(this).forEach(player ->
                    ((ServerWorld)world).spawnParticles(ColoredSpellParticleFactory.createData(color, age), sparklePos.getX(), sparklePos.getY(), sparklePos.getZ(), 1, 0.01,0.25, 0.01, 0));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("isOpen", isOpen);
        nbt.putString("yeast", DataUtils.SerializeToString(yeast));
        nbt.putFloat("fermentationProgress", fermentationProgress);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.isOpen = nbt.getBoolean("isOpen");
        this.yeast = (Yeast)DataUtils.DeserializeFromString(nbt.getString("yeast"));
        this.fermentationProgress = nbt.getFloat("fermentationProgress");
    }

    public void toggleOpen(){
        if(System.currentTimeMillis() - this.lastToggled > 1000){
            this.lastToggled = System.currentTimeMillis();
            this.isOpen = !this.isOpen;
        }
    }

    public void syncWithClient(){
        super.syncWithClient();
        if(!world.isClient){
            PacketByteBuf buf = PacketByteBufs.create();

            buf.writeBoolean(isOpen);
            buf.writeString(DataUtils.SerializeToString(yeast));
            buf.writeFloat(fermentationProgress);
            buf.writeBlockPos(pos);

            PlayerLookup.tracking(this).forEach(player -> ServerPlayNetworking.send(player, DiabolismPackets.SYNC_FERMENTER_W_CLIENT, buf));
        }
    }

    public void syncWithServer(){
        super.syncWithServer();
        if(MinecraftClient.getInstance().player != null && world != null && world.isClient){
            PacketByteBuf buf = PacketByteBufs.create();

            buf.writeBoolean(isOpen);
            buf.writeString(DataUtils.SerializeToString(yeast));
            buf.writeBlockPos(pos);

            ClientPlayNetworking.send(DiabolismPackets.SYNC_FERMENTER_W_SERVER, buf);
        }
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setYeast(Yeast yeast){
        this.yeast = yeast;
    }

    public void setFermentationProgress(float progress){
        this.fermentationProgress = progress;
    }

    public void removeYeast(){
        this.yeast = null;
    }
}
