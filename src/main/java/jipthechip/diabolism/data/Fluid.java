package jipthechip.diabolism.data;

import jipthechip.diabolism.render.RenderDataMappings;
import jipthechip.diabolism.render.FluidRenderData;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Fluid implements Serializable {

    public static final int NUM_ELEMENTS = 6;
    public static final int NUM_MAGICKA = 11;

    protected int amount = 0;

    protected int capacity;

    protected float[] elementContents;

    protected float[] magickaContents;

    protected int color = -1; // fluid color that overrides the default color (-1 for default)

    protected FluidRenderData renderData;

    public Fluid(int amount, float[] elementContents, float[] magickaContents, int fluidColor, FluidRenderData fluidRenderData, int capacity) {
        this.amount = amount;
        this.capacity = capacity;
        this.elementContents = elementContents;
        this.magickaContents = magickaContents;
        this.color = fluidColor;
        this.renderData = fluidRenderData;
    }

    public Fluid(Fluid fluid){
        this.amount = fluid.amount;
        this.capacity = fluid.capacity;
        this.elementContents = fluid.elementContents;
        this.magickaContents = fluid.magickaContents;
        this.color = fluid.color;
        this.renderData = fluid.renderData;
    }

    public int add(Fluid fromFluid){
        if(!isFull()){
            int amountAdded = Math.min(fromFluid.getAmount(), this.capacity - this.amount);
            if(amountAdded > 0){
                if(!this.equals(fromFluid)){
                    addOtherFluid(fromFluid, amountAdded);
                }else{
                    this.amount += amountAdded;
                }
                return amountAdded;
            }
        }
        return 0;
    }

    public int add(Fluid fromFluid, int amount){
        if(!isFull()){

            int amountAdded = Math.min(Math.min(fromFluid.getAmount(), amount), this.capacity - this.amount);

            if(amountAdded > 0){

                if(!this.equals(fromFluid)){
                    addOtherFluid(fromFluid, amountAdded);
                }else{
                    this.amount += amountAdded;
                }
                return amountAdded;
            }
        }
        return 0;
    }

    public void remove(int amount){
        if(this.amount >= amount){
            this.amount -= amount;
        }
    }

    public boolean isFull(){
        return amount >= capacity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = Math.min(Math.max(amount, 0), capacity);
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public int getCapacity(){return capacity;}

    public int getColor(){
        return color == -1 ? renderData == null ? 0 : renderData.getColor() : color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public float[] getElementContents() {
        return elementContents;
    }

    public void addElementContents(BrewIngredient brewIngredient){
        if (elementContents == null || elementContents.length == 0) elementContents = new float[NUM_ELEMENTS];
        elementContents[0] += brewIngredient.air();
        elementContents[1] += brewIngredient.fire();
        elementContents[2] += brewIngredient.water();
        elementContents[3] += brewIngredient.earth();
        elementContents[4] += brewIngredient.chaos();
        elementContents[5] += brewIngredient.order();
    }

    public void setElementContents(int index, float value){
        elementContents[index] = value;
    }

    public void setElementContents(float [] elementContents){
        this.elementContents = elementContents;
    }

    public void removeElementContent(int index, float amount){
        elementContents[index] -= amount;
    }

    public void addMagickaContent(int index, float amount){
        if(magickaContents == null || magickaContents.length == 0){
            magickaContents = new float[NUM_MAGICKA];
        }
        magickaContents[index] += amount;
    }


    public float[] getMagickaContents() {
        return magickaContents;
    }

    public FluidRenderData getRenderData(){
        return renderData;
    }

    public void setRenderData(@Nullable String name){
        this.renderData = name == null ? null : RenderDataMappings.Fluids.get(name);
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Fluid otherFluid){
            return (Objects.equals(this.renderData.getCuboidName(), otherFluid.getRenderData().getCuboidName()) &&
                    (this.getColor() == otherFluid.getColor()) &&
                    Arrays.equals(this.getElementContents(), otherFluid.getElementContents()) &&
                    Arrays.equals(this.getMagickaContents(), otherFluid.getMagickaContents()));
        }
        return false;
    }

    public int getMagickaParticleColor(){

        float magickaSum = 0;

        for(float f : magickaContents){
            magickaSum += f;
        }

        float [] probabilities = new float[magickaContents.length];
        for(int i = 0; i < magickaContents.length; i++){
            probabilities[i] = magickaContents[i] / magickaSum;
        }

        double p = Math.random();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (p <= cumulativeProbability) {
                return Spell.ELEMENT_COLORS[i];
            }
        }

        return 0;
    }

    private void addOtherFluid(Fluid otherFluid, int amount){
        float[] otherElementContents = otherFluid.getElementContents();
        float[] otherMagickaContents = otherFluid.getMagickaContents();

        float[] newElementContents = new float[NUM_ELEMENTS];
        float[] newMagickaContents = new float[NUM_MAGICKA];

        if(otherElementContents != null){
            for(int i = 0; i < NUM_ELEMENTS; i++){
                newElementContents[i] = (elementContents[i] * ((float)this.amount / amount) + otherElementContents[i] * ((float)amount / this.amount))/2;
            }
            elementContents = newElementContents;
        }

        if(otherMagickaContents != null){
            for(int i = 0; i < NUM_MAGICKA; i++){
                newMagickaContents[i] = (magickaContents[i] * ((float)this.amount / amount) + otherMagickaContents[i] * ((float)amount / this.amount))/2;
            }
            magickaContents = newMagickaContents;
        }

        color = (int)((color * ((float)this.amount / amount)) + (otherFluid.getColor() * ((float)amount / this.amount))) / 2;

        this.amount += amount;

        // TODO fluid render data is unchanged, keeping original for now, may need to change later
    }

    @Override
    public String toString(){
        return "{\namount: "+amount+",\ncapacity: "+capacity+",\nelementContents: "+Arrays.toString(elementContents)+",\nmagickaContents: "+Arrays.toString(magickaContents)+",\ncolor: "+ String.format("0x%08X", color) +"\n}";
    }
}
