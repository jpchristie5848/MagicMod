package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Fluid;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.MagickaFluid;
import jipthechip.diabolism.packets.DiabolismPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.ArrayList;
import java.util.List;

public class AbstractMagickaConsumer extends AbstractSyncedBlockEntity implements GeoAnimatable {

    protected final int capacity;

    protected List<MagickaFluid> fluids;

    protected int progress = 0;
    protected int maxProgress = 100;
    protected long lastUpdate = 0;

    public AbstractMagickaConsumer(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity) {
        super(type, pos, state);
        fluids = new ArrayList<>();
        this.capacity = capacity;
    }

    public void setFluids(List<MagickaFluid> fluids){
        this.fluids = fluids;
    }

    public List<MagickaFluid> getFluids(){return this.fluids;}

    public int addFluid(Fluid fluid, int amount){

        if(amount == 0){
            System.out.println("amount is 0");
            return 0;
        }

        if(fluid == null){
            System.out.println("fluid is null");
            return 0;
        }

        float spaceLeft = getRemainingSpace(); // amount of space left in the container

        if(spaceLeft <= 0) {
            System.out.println("no space left");
            return 0;
        }

        float[] magickaContents = fluid.getMagickaContents();

        if(magickaContents == null){
            System.out.println("magicka contents null");
            return 0;
        }

        int numMagickaTypes = 0;

        // get the total number of magical elements in the fluid
        for(int i = 0; i < magickaContents.length; i++){
            if(magickaContents[i]>0) numMagickaTypes++;
        }

        if(numMagickaTypes == 0){
            System.out.println("no magicka content");
            return 0;
        }

        // evenly divide the fluid between the present elements
        float amountForEachElement = Math.min(spaceLeft, amount)/numMagickaTypes;

        // convert the magical elements in the fluid to magicka fluid, and add it to the container
        for(int i = 0; i < magickaContents.length; i++){
            if(magickaContents[i] <= 0) continue;
            System.out.println("magic contents of "+MagicElement.values()[i].name()+": "+MagicElement.values()[i]);
            addElement(MagicElement.values()[i], amountForEachElement, magickaContents[i]/100.0f);
        }

        syncWithClient();
        markDirty();

        // amount to remove from the original container
        return (int)Math.ceil(amountForEachElement * numMagickaTypes);
    }

    private void addElement(MagicElement element, float amount, float purity){

        MagickaFluid newFluid = new MagickaFluid(element, amount, purity);

        // check if we already have magicka fluid for the given element
        int alreadyExistsIndex = -1;
        for(int i = 0; i < fluids.size(); i++){
            if(fluids.get(i).getElement() == element){
                alreadyExistsIndex = i;
                break;
            }
        }

        if(alreadyExistsIndex == -1){
            fluids.add(newFluid); // add new magicka fluid if one doesn't exist already
        }else{
            fluids.get(alreadyExistsIndex).addOther(newFluid); // add to existing magicka fluid if one already exists
        }
    }

    public float getRemainingSpace(){
        int totalOccupied = 0;

        for(MagickaFluid magickaFluid : fluids){
            totalOccupied += magickaFluid.getAmount();
        }
        return capacity - totalOccupied;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        fluids = new ArrayList<>();
        int NumFluids = nbt.getInt("NumFluids");
        for(int i = 0; i < NumFluids; i++){
            fluids.add((MagickaFluid) DataUtils.DeserializeFromString(nbt.getString("fluid_"+i)));
        }
        syncWithClient();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("NumFluids", fluids.size());
        for(int i = 0; i < fluids.size(); i++){
            nbt.putString("fluid_"+i, DataUtils.SerializeToString(fluids.get(i)));
        }
        super.writeNbt(nbt);
    }

    protected void syncWithClient(){
        if(world != null && !world.isClient){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(this.fluids.size());
            for(int i = 0; i < this.fluids.size(); i++){
                buf.writeString(DataUtils.SerializeToString(this.fluids.get(i)));
            }
            buf.writeInt(progress);

            buf.writeBlockPos(getPos());
            PlayerLookup.tracking(this).forEach(player -> ServerPlayNetworking.send(player, DiabolismPackets.SYNC_MAGICKA_CONSUMER_W_CLIENT, buf));
        }
    }

    @Override
    protected void syncWithServer() {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }
    public int getMaxProgress(){
        return maxProgress;
    }
}
