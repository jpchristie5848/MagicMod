package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Yeast;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.packets.DiabolismPackets;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
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
    private long lastInteractionTime = 0;
    private boolean isOpen = false;

    private long lastFermentUpdate = 0;

    private float fermentationProgress = 0;

    private Yeast yeast;

    private boolean fermentationOngoing = false;

    private AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation OPEN_ANIM = RawAnimation.begin().thenLoop("lid_open");
    private static final RawAnimation FLOAT_ANIM = RawAnimation.begin().thenLoop("lid_float");

    private static final RawAnimation[] FERMENT_ANIMS = new RawAnimation[]{RawAnimation.begin().thenPlay("lid_ferment0"),
            RawAnimation.begin().thenPlay("lid_ferment1"),
            RawAnimation.begin().thenPlay("lid_ferment2"),
            RawAnimation.begin().thenPlay("lid_ferment3")
    };

    private AnimationController<MagicFermenter>[] fermentControllers = new AnimationController[]{
            new AnimationController<MagicFermenter>(this, "ferment0", state -> PlayState.STOP).triggerableAnim("lid_ferment0", FERMENT_ANIMS[0]),
            new AnimationController<MagicFermenter>(this, "ferment1", state -> PlayState.STOP).triggerableAnim("lid_ferment1", FERMENT_ANIMS[1]),
            new AnimationController<MagicFermenter>(this, "ferment2", state -> PlayState.STOP).triggerableAnim("lid_ferment2", FERMENT_ANIMS[2]),
            new AnimationController<MagicFermenter>(this, "ferment3", state -> PlayState.STOP).triggerableAnim("lid_ferment3", FERMENT_ANIMS[3])
    };

    public MagicFermenter(BlockPos pos, BlockState state) {
        super(DiabolismEntities.MAGIC_FERMENTER_BLOCKENTITY, pos, state, 1000, true);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

        //controllerRegistrar.add()
        controllerRegistrar.add(new AnimationController<MagicFermenter>(this, "magic_fermenter_controller", this::predicate));

        for(AnimationController<MagicFermenter> controller : fermentControllers){
            controllerRegistrar.add(controller);
        }
    }

    private PlayState predicate(AnimationState<MagicFermenter> animationState) {

        if(isOpen){
            if (System.currentTimeMillis() - lastToggled < 1000){
                animationState.setAnimation(OPEN_ANIM);
            }else{
                animationState.setAnimation(FLOAT_ANIM);
            }
        }
        else{
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

    public boolean isFermenting(){
        return fermentationOngoing || (yeast != null);
    }

    public void tick(World world, BlockPos blockPos, BlockState state){
        tickCounter++;

        // only start fermentation when container is closed, full of fluid, and yeast is added
        if(!isOpen && fluid != null && fluid.getAmount() >= 1000 && yeast != null && !world.isClient) {

            if(System.currentTimeMillis() - lastFermentUpdate > 3000){

                fermentationOngoing = false;
                float[] yieldMultipliers = yeast.getYieldMultipliers();

                for(int i = 0; i < fluid.getElementContents().length; i++){


                    // convert element content into magicka content
                    if(fluid.getElementContents()[i] > 0){
                        float elementToRemove = Math.min(1.0f, fluid.getElementContents()[i])*0.25f;
                        float magickaToCreate = (yieldMultipliers[i]*elementToRemove);
                        if(elementToRemove < 0.25f){
                            fluid.setElementContents(i, 0.0f);
                        }else{
                            fluid.removeElementContent(i, elementToRemove);
                        }
                        fluid.addMagickaContent(i, magickaToCreate);
                        fermentationOngoing = true;
                    }
                }
                // destroy yeast when fermentation is completed
                if(!fermentationOngoing){
                    yeast = null;
                }
                System.out.println("current elements: "+ Arrays.toString(fluid.getElementContents()));
                System.out.println("current magicka: "+ Arrays.toString(fluid.getMagickaContents()));

                syncWithClient();
                markDirty();
                lastFermentUpdate = System.currentTimeMillis();

                // trigger animation
                int animIndex = (new Random()).nextInt(4);

                System.out.println("animation index: "+animIndex);
                this.triggerAnim("ferment"+animIndex, "lid_ferment"+animIndex);
            }
            // spawn fermentation particles
            if (tickCounter % 10 == 0 && yeast != null){
                spawnFermentationParticles();
            }

        }

        // spawn magicka particles when container is open and fluid has magicka
        if(tickCounter % 5 == 0){
            spawnMagickaParticles();
        }
    }

    private void spawnMagickaParticles(){
        if(isOpen && fluid != null && fluid.getMagickaContents() != null && !world.isClient){
            double randomX = (Math.random()*0.7);
            double randomY = (Math.random()*0.05);
            double randomZ  = (Math.random()*0.7);
            Vec3d sparklePos = new Vec3d(pos.getX()+0.15 + randomX, pos.getY()+0.9+randomY, pos.getZ()+0.15 + randomZ);
            int age = (int) (Math.random()*20+5.0f);
            int color = fluid.getMagickaParticleColor();
            PlayerLookup.tracking(this).forEach(player ->
                    ((ServerWorld)world).spawnParticles(ColoredSpellParticleFactory.createData(color, age), sparklePos.getX(), sparklePos.getY(), sparklePos.getZ(), 1, 0.01,0.25, 0.01, 0));
        }
    }

    private void spawnFermentationParticles(){
        double randomX = (Math.random()*0.7);
        double randomY = (Math.random()*0.05);
        double randomZ  = (Math.random()*0.7);
        Vec3d sparklePos = new Vec3d(pos.getX()+0.15 + randomX, pos.getY()+1.2+randomY, pos.getZ()+0.15 + randomZ);
        int age = (int) (Math.random()*10+30);
        Random random = new Random();
        int i = random.nextInt(3);
        int color = i == 0 ? getFluidColor() : yeast.getColor(i-1);
        PlayerLookup.tracking(this).forEach(player ->
                ((ServerWorld)world).spawnParticles(ColoredSpellParticleFactory.createData(color, age), sparklePos.getX(), sparklePos.getY(), sparklePos.getZ(), 1, 0.01,0.25, 0.01, 0));
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
            this.markDirty();
            this.syncWithClient();
        }
    }

    public void syncWithClient(){
        super.syncWithClient();
        if(world != null && !world.isClient){
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

    public boolean fillBucket(PlayerEntity player, Hand hand){

        boolean bucketFilled = super.fillBucket(player, hand);
        if(bucketFilled) {
            this.setYeast(null);
            markDirty();
            syncWithClient();
        }
        return bucketFilled;
    }

    public boolean addYeastItem(ItemStack stack, Hand hand, PlayerEntity player){

        if(world != null && !world.isClient && stack.getItem() == DiabolismItems.MYSTICAL_YEAST && this.getFluid() != null && canInteract()){
            lastInteractionTime = System.currentTimeMillis();
            Yeast yeast = DataUtils.readObjectFromItemNbt(stack, Yeast.class);
            if(yeast != null){
                this.setYeast(yeast);
                player.setStackInHand(hand, ItemStack.EMPTY);

                lastInteractionTime = System.currentTimeMillis();
                markDirty();
                syncWithClient();
                return true;
            }
        }
        return false;
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
