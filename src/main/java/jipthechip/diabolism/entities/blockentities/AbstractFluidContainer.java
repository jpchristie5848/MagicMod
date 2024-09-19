package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.brewing.BrewIngredient;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.brewing.ExtendedFluid;
import jipthechip.diabolism.data.brewing.Fluid;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.render.FluidRenderData;
import jipthechip.diabolism.render.RenderDataMappings;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
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
import java.util.HashMap;


public abstract class AbstractFluidContainer<E> extends AbstractSyncedBlockEntity<ExtendedFluid<E>> implements GeoAnimatable {

    protected long lastInteractionTime = 0;
    protected int capacity;

    protected final boolean canFillBucket;

    public AbstractFluidContainer(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, boolean canFillBucket, @Nullable E extraData) {
        super(type, pos, state);
        this.capacity = capacity;
        this.canFillBucket = canFillBucket;
        this.data = new ExtendedFluid<>(extraData);
    }

    public FluidRenderData getFluidRenderData(){return this.data == null ? null : this.data.getRenderData();}

    public Fluid getFluidData(){
        return this.data;
    }

    public int addFluid(Fluid fluid){
        if(fluid != null){
            return addFluid(fluid.getAmount(), fluid.getElementContentsMap(), fluid.getMagickaContentsMap(), fluid.getColor(), fluid.getRenderData());
        }
        return 0;
    }

    public int addFluid(int amount, FluidRenderData renderData){
        return addFluid(amount, null, null, -1, renderData);
    }

    public int addFluid(int amount, @Nullable HashMap<MagicElement, Float> elementContents, @Nullable HashMap<MagicElement, Float> magickaContents, int color, FluidRenderData renderData) {
        int fluidAdded;
        fluidAdded = this.data.add(new Fluid(amount, elementContents, magickaContents, color, renderData), this.capacity);

        if(fluidAdded > 0) markDirty();
        return fluidAdded;
    }

    public int addFluid(AbstractFluidContainer fromContainer, int amount){
        if(fromContainer.getFluidData() == null || this.data == null) return 0;
        int fluidAdded;
        fluidAdded = this.data.add(fromContainer.getFluidData(), this.capacity);
        if(fluidAdded > 0) markDirty();
        return fluidAdded;
    }

    public void removeFluid(int amount){
        if(this.data != null){
            this.data.remove(amount);
            if(this.data.getAmount() <= 0){
                emptyFluid();
            }
            markDirty();
        }
    }

    public void emptyFluid(){
        this.data.reset();
        markDirty();
    }

    public boolean isFull(){
        return this.data != null && this.data.getAmount() >= this.capacity;
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
        return this.data == null ? 0 : this.data.getAmount();
    }

    public void setAmount(int amount) {
        if(this.data != null){
            this.data.setAmount(amount, this.capacity);
        }
        markDirty();
    }

    public int getCapacity(){return capacity;}

    public int getFluidColor(){
        return this.data == null ? -1 : this.data.getColor();
    }

    public void setFluidColor(int color){
        if(this.data != null) {
            this.data.setColor(color);
            markDirty();
        }
    }

    public HashMap<MagicElement, Float> getElementContents() {
        return this.data.getElementContents();
    }

    public void addElementContents(BrewIngredient brewIngredient){
        if(this.data != null){
            this.data.addElementContents(brewIngredient);
            markDirty();
        }
    }

    public boolean canFillBucket(){
        return canFillBucket;
    }

    public boolean tryFluidTransfer(AbstractFluidContainer toContainer, int amount){

        if(this.data != null){
            int amountToTransfer = Math.min(amount, this.getFluidAmount());
            int amountTransferred = toContainer.addFluid(this, amountToTransfer);
            if(amountTransferred > 0){
                removeFluid(amountTransferred);

                sync();
                toContainer.sync();
                markDirty();
                toContainer.markDirty();

                return true;
            }
        }
        return false;
    }

    // client interactions

    protected boolean canInteract(){
        return System.currentTimeMillis() - lastInteractionTime >= 500;
    }

    public boolean addWaterBucket(PlayerEntity player, Hand hand){
        if(canFillBucket && world != null && !world.isClient && player.getStackInHand(hand).getItem() == Items.WATER_BUCKET && this.getFluidData().getAmount() == 0 && canInteract()){
            this.addFluid(1000, RenderDataMappings.Fluids.get("churner_fluid"));
            if(!player.isCreative()){
                player.setStackInHand(hand, new ItemStack(Items.BUCKET));
            }

            Vec3d posVec3d = getPosVec3d();
            world.playSound(null, posVec3d.getX(), posVec3d.getY(), posVec3d.getZ(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f);

            lastInteractionTime = System.currentTimeMillis();
            markDirty();
            sync();
            return true;
        }
        return false;
    }

    public boolean addBrewBucket(PlayerEntity player, Hand hand){

        if(canFillBucket && world != null && !world.isClient && player.getStackInHand(hand).getItem() == DiabolismItems.BREW_FLUID_BUCKET && this.getFluidData().getAmount() == 0 && canInteract()){
            Fluid fluid = DataUtils.readObjectFromItemNbt(player.getStackInHand(hand), Fluid.class);
            this.addFluid(fluid);
            Vec3d posVec3d = getPosVec3d();
            world.playSound(null, posVec3d.getX(), posVec3d.getY(), posVec3d.getZ(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f);

            if(!player.isCreative()){
                player.setStackInHand(hand, new ItemStack(Items.BUCKET));
            }

            lastInteractionTime = System.currentTimeMillis();
            markDirty();
            sync();
            return true;
        }
        return false;
    }

    public boolean fillBucket(PlayerEntity player, Hand hand){

        if(canFillBucket && world != null && !world.isClient && player.getStackInHand(hand).getItem() == Items.BUCKET && this.getFluidData().getAmount() > 0 && canInteract()){

            ItemStack brewBucketStack = new ItemStack(DiabolismItems.BREW_FLUID_BUCKET);
            DataUtils.writeObjectToItemNbt(brewBucketStack, this.getFluidData());
            player.setStackInHand(hand, brewBucketStack);

            this.removeFluid(this.getFluidAmount());

            Vec3d posVec3d = getPosVec3d();
            world.playSound(null, posVec3d.getX(), posVec3d.getY(), posVec3d.getZ(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f);

            lastInteractionTime = System.currentTimeMillis();
            markDirty();
            sync();
            return true;
        }
        return false;
    }
}
