package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.data.BrewIngredient;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Fluid;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.packets.DiabolismPackets;
import jipthechip.diabolism.render.FluidRenderData;
import jipthechip.diabolism.render.RenderDataMappings;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import javax.annotation.Nullable;

public abstract class AbstractFluidContainer extends AbstractSyncedBlockEntity implements GeoAnimatable {

    protected long lastInteractionTime = 0;
    protected int capacity;

    protected Fluid fluid;

    protected final boolean canFillBucket;

    public AbstractFluidContainer(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, boolean canFillBucket) {
        super(type, pos, state);
        this.capacity = capacity;
        this.canFillBucket = canFillBucket;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putString("fluid", DataUtils.SerializeToString(fluid));

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.fluid = (Fluid)DataUtils.DeserializeFromString(nbt.getString("fluid"));
    }

    public FluidRenderData getFluidRenderData(){return this.fluid == null ? null : this.fluid.getRenderData();}

    public Fluid getFluid(){
        return this.fluid;
    }

    public void setFluid(Fluid fluid){
        this.fluid = fluid;
    }


    public int addFluid(Fluid fluid){
        if(fluid != null){
            return addFluid(fluid.getAmount(), fluid.getElementContents(), fluid.getMagickaContents(), fluid.getColor(), fluid.getRenderData());
        }
        return 0;
    }

    public int addFluid(int amount, FluidRenderData renderData){
        return addFluid(amount, null, null, -1, renderData);
    }

    public int addFluid(int amount, @Nullable float[] elementContents, @Nullable float[] magickaContents, int color, FluidRenderData renderData){
        int fluidAdded;
        if(this.fluid == null){
            this.fluid = new Fluid(Math.min(amount, this.capacity), elementContents, magickaContents, color, renderData, this.capacity);
            fluidAdded = this.fluid.getAmount();
        }else{
            fluidAdded = this.fluid.add(new Fluid(amount, elementContents, magickaContents, color, renderData, this.capacity));
        }
        if(fluidAdded > 0) markDirty();
        return fluidAdded;
    }

    public int addFluid(AbstractFluidContainer fromContainer, int amount){
        int fluidAdded;
        if(this.fluid == null){
            this.fluid = new Fluid(fromContainer.getFluid());
            this.fluid.setCapacity(this.capacity);
            this.fluid.setAmount(Math.min(amount, this.capacity));
            fluidAdded = this.fluid.getAmount();
        }else{
            fluidAdded = this.fluid.add(fromContainer.getFluid(), amount);
        }
        if(fluidAdded > 0) markDirty();
        return fluidAdded;
    }

    public void removeFluid(int amount){
        if(this.fluid != null){
            this.fluid.remove(amount);
            if(this.fluid.getAmount() <= 0){
                System.out.println("remove fluid set to null");
                this.fluid = null;
            }
            markDirty();
        }
    }

    public void emptyFluid(){
        this.fluid = null;
        markDirty();
    }

    public boolean isFull(){
        return this.fluid != null && this.fluid.isFull();
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

    public int getFluidAmount() {
        return this.fluid == null ? 0 : this.fluid.getAmount();
    }

    public void setAmount(int amount) {
        if(this.fluid != null){
            this.fluid.setAmount(amount);
        }
        markDirty();
    }

    public int getCapacity(){return capacity;}

    public int getFluidColor(){
        return this.fluid == null ? -1 : this.fluid.getColor();
    }

    public void setFluidColor(int color){
        if(this.fluid != null) {
            this.fluid.setColor(color);
            markDirty();
        }
    }

    public float[] getElementContents() {
        return this.fluid.getElementContents();
    }

    public void addElementContents(BrewIngredient brewIngredient){
        if(this.fluid != null){
            this.fluid.addElementContents(brewIngredient);
            markDirty();
        }
    }

    public void setElementContents(int index, float value){
        if(this.fluid != null){
            this.fluid.setElementContents(index, value);
            markDirty();
        }
    }

    public void setFluidRenderData(@Nullable String name){
        if(this.fluid != null){
            this.fluid.setRenderData(name);
        }
    }

    public void setElementContents(float [] elementContents){
        if(this.fluid != null){
            this.fluid.setElementContents(elementContents);
            markDirty();
        }
    }

    @Override
    public void syncWithServer() {
        if(MinecraftClient.getInstance().player != null && world != null && world.isClient){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(DataUtils.SerializeToString(this.fluid));
            buf.writeBlockPos(getPos());
            //System.out.println("buffer: "+fluidColor+" "+DataUtils.floatToByteArr(elementContents).length+" "+getPos());

            ClientPlayNetworking.send(DiabolismPackets.SYNC_FLUID_CONTAINER_W_SERVER, buf);
        }
    }

    protected void syncWithClient(){
        if(!world.isClient){
            PacketByteBuf buf = PacketByteBufs.create();
            //System.out.println("fluid in sync with client in abstract fluid container: "+fluid);
            buf.writeString(DataUtils.SerializeToString(this.fluid));
            buf.writeBlockPos(getPos());
            //System.out.println("buffer: "+fluidColor+" "+DataUtils.floatToByteArr(elementContents).length+" "+getPos());
            PlayerLookup.tracking(this).forEach(player -> ServerPlayNetworking.send(player, DiabolismPackets.SYNC_FLUID_CONTAINER_W_CLIENT, buf));
        }
    }

    public boolean tryFluidTransfer(AbstractFluidContainer toContainer, int amount){

        if(this.fluid != null){
            //System.out.println("before amount: "+this.getFluidAmount());
            //System.out.println("to container before amount: "+toContainer.getFluidAmount());
            int amountToTransfer = Math.min(amount, this.getFluidAmount());
            int amountTransferred = toContainer.addFluid(this, amountToTransfer);
            if(amountTransferred > 0){
                removeFluid(amountTransferred);
                //System.out.println("transferred "+amountTransferred+" fluid to container at "+toContainer.getPos());

                syncWithClient();
                toContainer.syncWithClient();
                markDirty();
                toContainer.markDirty();
                //syncWithServer();
                //toContainer.syncWithServer();

                return true;
            }
        }
        //System.out.println("transferred "+amountTransferred+" fluid to container at "+toContainer.getPos());
        return false;
    }

    protected boolean canInteract(){
        return System.currentTimeMillis() - lastInteractionTime >= 500;
    }

    public boolean addWaterBucket(PlayerEntity player, Hand hand){
        if(canFillBucket && world != null && !world.isClient && player.getStackInHand(hand).getItem() == Items.WATER_BUCKET && this.getFluid() == null && canInteract()){
            this.addFluid(1000, RenderDataMappings.Fluids.get("churner_fluid"));
            if(!player.isCreative()){
                player.setStackInHand(hand, new ItemStack(Items.BUCKET));
            }

            Vec3d posVec3d = getPosVec3d();
            world.playSound(null, posVec3d.getX(), posVec3d.getY(), posVec3d.getZ(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f);

            lastInteractionTime = System.currentTimeMillis();
            markDirty();
            syncWithClient();
            return true;
        }
        return false;
    }

    public boolean addBrewBucket(PlayerEntity player, Hand hand){

        if(canFillBucket && world != null && !world.isClient && player.getStackInHand(hand).getItem() == DiabolismItems.BREW_FLUID_BUCKET && this.getFluid() == null && canInteract()){
            Fluid fluid = DataUtils.readObjectFromItemNbt(player.getStackInHand(hand), Fluid.class);
            this.addFluid(fluid);
            Vec3d posVec3d = getPosVec3d();
            world.playSound(null, posVec3d.getX(), posVec3d.getY(), posVec3d.getZ(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f);

            if(!player.isCreative()){
                player.setStackInHand(hand, new ItemStack(Items.BUCKET));
            }

            lastInteractionTime = System.currentTimeMillis();
            markDirty();
            syncWithClient();
            return true;
        }
        return false;
    }

    public boolean fillBucket(PlayerEntity player, Hand hand){

        if(canFillBucket && world != null && !world.isClient && player.getStackInHand(hand).getItem() == Items.BUCKET && this.getFluid() != null && canInteract()){

            ItemStack brewBucketStack = new ItemStack(DiabolismItems.BREW_FLUID_BUCKET);
            DataUtils.writeObjectToItemNbt(brewBucketStack, this.getFluid());
            player.setStackInHand(hand, brewBucketStack);

            this.removeFluid(this.getFluidAmount());

            Vec3d posVec3d = getPosVec3d();
            world.playSound(null, posVec3d.getX(), posVec3d.getY(), posVec3d.getZ(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f);

            lastInteractionTime = System.currentTimeMillis();
            markDirty();
            syncWithClient();
            return true;
        }
        return false;
    }
}
