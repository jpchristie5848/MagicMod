package jipthechip.diabolism.entities.blockentities;

import io.wispforest.owo.util.ImplementedInventory;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.Utils.InventoryHelpers;
import jipthechip.diabolism.data.BrewIngredient;
import jipthechip.diabolism.Utils.ImageUtils;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.packets.DiabolismPackets;
import jipthechip.diabolism.particle.ColoredSplashParticleFactory;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import jipthechip.diabolism.sound.DiabolismSounds;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MagicChurner extends AbstractFluidContainer implements GeoBlockEntity, ImplementedInventory {

    private final int MAX_DAYTIME = 24000;

    private AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private int tickCounter=0;
    private long mixingStartTime = -1;
    private long mixingLastUpdateTime =-1;

    private long lastTurnedTime=0;


    private static final RawAnimation SPIN_ANIM = RawAnimation.begin().thenPlay("magic_churner_turn");
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("magic_churner_idle");
    private AnimationController<MagicChurner> controller = new AnimationController<MagicChurner>(this, "magic_churner_controller", state -> PlayState.STOP).triggerableAnim("magic_churner_turn", SPIN_ANIM);

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);

    private float mixingProgress = 0;

    private int mixedIngredients = 0;


    public MagicChurner(BlockPos pos, BlockState state) {
        super(DiabolismEntities.MAGIC_CHURNER_BLOCKENTITY, pos, state, 1000, true);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {


        AnimationController<MagicChurner> controller2 = new AnimationController<MagicChurner>(this, "magic_churner_controller2", this::predicate);

        controllerRegistrar.add(controller);
        controllerRegistrar.add(controller2);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.instanceCache;
    }

    @Override
    public double getBoneResetTime() {
        return 20;
    }

    @Override
    public double getTick(Object o) {
        return RenderUtils.getCurrentTick();
    }
//
    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event){
        event.setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    public static void ticker(World world, BlockPos pos, BlockState state, MagicChurner be) {
        be.tick(world, pos, state);
    }

    private void tick(World world, BlockPos pos, BlockState state) {
        tickCounter++;


        //System.out.println("mixing progress: "+mixingProgress);
        if(System.currentTimeMillis() < mixingStartTime + 2500 && System.currentTimeMillis() > mixingLastUpdateTime + 250 && !world.isClient){
            mixIngredients();
        }

        if(tickCounter % 20 == 0 && fluid == null){
            mixingProgress = 0;
            syncWithClient();
        }

    }

    public AnimationController<MagicChurner> getController(){
        return controller;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        mixingProgress = nbt.getFloat("mixingProgress");
        mixedIngredients = nbt.getInt("mixedIngredients");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        nbt.putFloat("mixingProgress", mixingProgress);
        nbt.putInt("mixedIngredients", mixedIngredients);
        super.writeNbt(nbt);
    }

    public int getFirstEmptySlot(){
        int i = 0;
        while (i < inventory.size() && inventory.get(i) != ItemStack.EMPTY ){
            i++;
        }
        return i >= inventory.size() ? -1 : i;
    }

    public void startMixing(){
        mixingStartTime = System.currentTimeMillis();
        mixingLastUpdateTime = mixingStartTime;
    }

    private void mixIngredients(){

        int firstEmptySlot = getFirstEmptySlot(); // check if churner contains items
        if(world != null && !world.isClient && mixingProgress < 100.0f && firstEmptySlot != 0){

            Vec3d splashPos = new Vec3d(pos.getX()+0.15 + Math.random()*0.7, pos.getY()+0.9, pos.getZ()+0.15 + Math.random()*0.7);
            for(int i = 0; i < 3; i++) {
                PlayerLookup.tracking(this).forEach(player ->
                        ((ServerWorld)world).spawnParticles(ColoredSplashParticleFactory.createData(getFluidColor()), splashPos.getX(), splashPos.getY(), splashPos.getZ(), 1, 0,0, 0, 0));
            }

            long tick = (System.currentTimeMillis() - mixingLastUpdateTime)/50; // 1 tick = 1/20 second (50 ms)
            tick *= 0.85; // adjust the progress so 3 turns will get progress to 100
            setMixingProgress(mixingProgress + tick);

            float weight = (tick + mixingProgress) / 200;

            Color currentColor = new Color(getFluidColor(), true);
            Color targetColor = ImageUtils.getAvgColor(getIngredientColors());

            setFluidColor(ImageUtils.getAvgColor(currentColor, targetColor, 1.0f + (1 / weight)).getRGB());
            mixingLastUpdateTime = System.currentTimeMillis();

            if(mixingProgress >= 100){
                for (int i = 0; i < 10; i++){
                    //Vec3d sparklePos = new Vec3d(pos.getX()+0.15, pos.getY()+1.2, pos.getZ()+0.15);
                    double randomX = (Math.random()*0.7);
                    double randomY = (Math.random()*0.3);
                    double randomZ  = (Math.random()*0.7);
                    //System.out.println("random vals: "+randomX+", "+randomY+", "+randomZ);
                    Vec3d sparklePos = new Vec3d(pos.getX()+0.15 + randomX, pos.getY()+1.2+randomY, pos.getZ()+0.15 + randomZ);
                    //System.out.println("sparklePos: "+sparklePos);
                    int age = (int) (Math.random()*10+30);
                    double deltaY = 0;//Math.random()*5.0+5.0;
                    PlayerLookup.tracking(this).forEach(player ->
                            ((ServerWorld)world).spawnParticles(ColoredSpellParticleFactory.createData(getFluidColor(), age), sparklePos.getX(), sparklePos.getY(), sparklePos.getZ(), 1, 0,deltaY, 0, 1));
                }

                world.playSound(null, getPos(), SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, (float)(Math.random()+1.0), (float)(Math.random()+1.0));

                int initialMixedIngredients = mixedIngredients;

                mixedIngredients = getFirstEmptySlot() == -1 ? inventory.size() : getFirstEmptySlot();

//                System.out.println("ingredients start index: "+initialMixedIngredients);
//                System.out.println("ingredients end index: "+mixedIngredients);

                for (int i = initialMixedIngredients; i < mixedIngredients; i++){
                    Item ingredientItem = inventory.get(i).getItem();

                    if(!BrewIngredient.mappings.containsKey(ingredientItem)){
                        System.out.println("WARNING: Item "+ingredientItem+" is not set in the map!");
                    }

                    addElementContents(BrewIngredient.mappings.get(ingredientItem));
                }
                clearInventory();
            }
            syncWithClient();
        }
    }

    private List<Color> getIngredientColors(){ 
        List<Color> colors = new ArrayList<>();
        int firstEmptySlot = getFirstEmptySlot();

        int endIndex = firstEmptySlot > -1 ? firstEmptySlot : inventory.size();

        for (int i = 0; i < endIndex; i++){
            colors.add(ImageUtils.getAvgColor(inventory.get(i).getItem()));
        }
        return colors;
    }

    public void setStack(int slot, ItemStack stack) {
        System.out.println("set churner slot "+slot+" to item "+stack.getItem());
        ImplementedInventory.super.setStack(slot, stack);
        markDirty();
    }

    public void clearInventory(){
        inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
        mixedIngredients = 0;
        markDirty();
    }

    public float getMixingProgress() {
        return mixingProgress;
    }

    public void syncWithServer(){
        if(MinecraftClient.getInstance().player != null){
            super.syncWithServer();
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeFloat(mixingProgress);
            buf.writeInt(mixedIngredients);
            buf.writeBlockPos(getPos());

            ClientPlayNetworking.send(DiabolismPackets.SYNC_CHURNER_W_SERVER, buf);
        }
    }

    protected void syncWithClient(){
        if(!world.isClient){
            super.syncWithClient();
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeFloat(mixingProgress);
            buf.writeInt(mixedIngredients);
            buf.writeBlockPos(getPos());

            PlayerLookup.tracking(this).forEach(player ->
                    ServerPlayNetworking.send(player, DiabolismPackets.SYNC_CHURNER_W_CLIENT, buf));
        }
    }

    public void setMixedIngredients(int mixedIngredients) {
        this.mixedIngredients = mixedIngredients;
    }

    public void setMixingProgress(float mixingProgress) {
        this.mixingProgress = mixingProgress;
        markDirty();
    }

    @Override
    public void removeFluid(int amount) {
        super.removeFluid(amount);
        if(amount == 0){
            this.setMixingProgress(0);
            this.mixedIngredients = 0;
        }
    }

    public boolean addIngredient(PlayerEntity player, Hand hand){
        if(world != null && !world.isClient && canInteract() && BrewIngredient.isValidIngredient(player.getStackInHand(hand).getItem())){
            int slot = this.getFirstEmptySlot();

            if(slot > -1 && this.getMixingProgress() < 100 && this.getFluid() != null){
                //System.out.println("adding ingredient");
                this.setStack(slot, InventoryHelpers.getItemsFromStackInHand(player, hand, 1));
                //world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f, true);
                Vec3d posVec3d = getPosVec3d();
                world.playSound(null, posVec3d.getX(), posVec3d.getY(), posVec3d.getZ(), SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f);
                this.setMixingProgress(0);

                Vec3d splashPos = new Vec3d(pos.getX()+0.15 + Math.random()*0.7, pos.getY()+0.9, pos.getZ()+0.15 + Math.random()*0.7);

                for (int i = 0; i < 15; i++){
                    PlayerLookup.tracking(this).forEach(player1 ->
                            ((ServerWorld)world).spawnParticles(ColoredSplashParticleFactory.createData(getFluidColor()), splashPos.getX(), splashPos.getY(), splashPos.getZ(), 1, 0,0, 0, 0));
                }
                lastInteractionTime = System.currentTimeMillis();
                markDirty();
                syncWithClient();
                return true;
            }
        }
        return false;
    }

    public boolean fillBucket(PlayerEntity player, Hand hand){

        boolean bucketFilled = super.fillBucket(player, hand);
        if(bucketFilled){
            if(this.getMixingProgress() < 100){
                for(ItemStack stack : this.getItems()){
                    world.spawnEntity(new ItemEntity(world, pos.getX()+0.5f, pos.getY()+1.2f, pos.getZ()+0.5f, stack));
                }
                player.setStackInHand(hand, new ItemStack(Items.WATER_BUCKET));
            }else{
                ItemStack brewBucketStack = new ItemStack(DiabolismItems.BREW_FLUID_BUCKET);
                DataUtils.writeObjectToItemNbt(brewBucketStack, this.getFluid());
                player.setStackInHand(hand, brewBucketStack);
            }
            this.clearInventory();
            this.setMixingProgress(0);
            markDirty();
            syncWithClient();
        }
        return false;
    }

    public void turn(){

        if(world != null && !world.isClient && System.currentTimeMillis() - lastTurnedTime > 3500){
            System.out.println("world is client: "+world.isClient);
            lastTurnedTime = System.currentTimeMillis();
            this.triggerAnim("magic_churner_controller", "magic_churner_turn");
            if(this.getFluid() != null){

                //world.playSoundAtBlockCenter(pos, DiabolismSounds.CHURNER_SLOSH, SoundCategory.BLOCKS, 1.5f, (float)1.0f, true);

                Vec3d posVec3d = getPosVec3d();
                world.playSound(null, posVec3d.getX(), posVec3d.getY(), posVec3d.getZ(), DiabolismSounds.CHURNER_SLOSH, SoundCategory.BLOCKS, 1.5f, (float)1.0f);


//                PacketByteBuf buf = PacketByteBufs.create();
//                buf.writeBlockPos(pos);
//                ClientPlayNetworking.send(DiabolismPackets.BEGIN_CHURNER_MIXING, buf);
                startMixing();
            }

            if(this.getFluid() != null){
                System.out.println("magic churner amount: "+this.getFluidAmount());
                System.out.println("magic churner mixing progress: "+this.getMixingProgress());
                System.out.println("magic churner color: "+this.getFluidColor());
                System.out.println("magic churner element contents: "+ Arrays.toString(this.getElementContents()));
            }
        }
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

    //    public void turn(){
//        this.hasBeenTurned = true;
//    }

//    @Override
//    public void removeAnimationFlag(String name) {
//        System.out.println("removing animation flag");
//        switch(name){
//            case "turn": this.hasBeenTurned = false;
//            default:
//        }
//    }
}
