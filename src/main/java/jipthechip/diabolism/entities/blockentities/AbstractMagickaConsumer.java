package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.data.brewing.ExtendedMagickaConsumerData;
import jipthechip.diabolism.data.brewing.Fluid;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.MagickaFluid;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractMagickaConsumer<T extends Serializable> extends AbstractSyncedBlockEntity<ExtendedMagickaConsumerData<T>> implements GeoBlockEntity {

    protected final int capacity;

    protected List<MagickaFluid> fluids;

    protected int progress = 0;
    protected int maxProgress = 100;
    protected long lastUpdate = 0;

    public AbstractMagickaConsumer(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, T extendedData) {
        super(type, pos, state);
        this.capacity = capacity;
        this.data = new ExtendedMagickaConsumerData<>(extendedData);
    }

    public int addFluid(Fluid fluid, int amount){

        if(amount == 0){
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

        HashMap<MagicElement, Float> magickaContents = fluid.getMagickaContents();

        if(magickaContents == null){
            System.out.println("magicka contents null");
            return 0;
        }

        int numMagickaTypes = 0;

        // get the total number of magical elements in the fluid
        for(MagicElement element : magickaContents.keySet()){
            if(magickaContents.getOrDefault(element, 0.0f)>0.0f) numMagickaTypes++;
        }

        if(numMagickaTypes == 0){
            System.out.println("no magicka content");
            return 0;
        }

        // evenly divide the fluid between the present elements
        float amountForEachElement = Math.min(spaceLeft, amount)/numMagickaTypes;

        // convert the magical elements in the fluid to magicka fluid, and add it to the container
        for(MagicElement element : magickaContents.keySet()){
            float magickaContent = magickaContents.getOrDefault(element, 0.0f);
            if(magickaContent <= 0) continue;
            addElement(element, amountForEachElement, (magickaContent/100.0f)*numMagickaTypes);
        }

        sync();
        markDirty();

        // amount to remove from the original container
        return (int)Math.ceil(amountForEachElement * numMagickaTypes);
    }

    private void addElement(MagicElement element, float amount, float purity){

        MagickaFluid newFluid = new MagickaFluid(element, amount, purity);

        // check if we already have magicka fluid for the given element
        int alreadyExistsIndex = -1;
        for(int i = 0; i < this.data.getFluids().size(); i++){
            if(this.data.getFluid(i).getElement() == element){
                alreadyExistsIndex = i;
                break;
            }
        }

        if(alreadyExistsIndex == -1){
            this.data.addFluid(newFluid); // add new magicka fluid if one doesn't exist already
        }else{
            this.data.getFluid(alreadyExistsIndex).addOther(newFluid); // add to existing magicka fluid if one already exists
        }
    }

    public float getRemainingSpace(){
        int totalOccupied = 0;

        for(MagickaFluid magickaFluid : this.data.getFluids()){
            totalOccupied += magickaFluid.getAmount();
        }
        return capacity - totalOccupied;
    }

//    @Override
//    protected void syncWithServer() {
//
//    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    protected abstract void incrementProgress();

    protected abstract boolean canUpdate();

    protected abstract void craft();

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
        data.setProgress(progress);
    }

    public int getProgress() {
        return data.getProgress();
    }
    public int getMaxProgress(){
        return maxProgress;
    }
}
