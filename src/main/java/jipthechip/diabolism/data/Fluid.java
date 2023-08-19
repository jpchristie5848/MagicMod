package jipthechip.diabolism.data;

import jipthechip.diabolism.render.RenderDataMappings;
import jipthechip.diabolism.render.FluidRenderData;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Fluid implements Serializable {

    protected int amount = 0;

    protected int capacity;

    protected float[] elementContents;

    protected float[] magickaContents;

    protected int color = -1; // fluid color that overrides the default color (-1 for default)

    public static final int[] ELEMENT_COLORS = new int[]{0xfff0e36c, 0xffed3f1c, 0xff1c7eed, 0xff75430d, 0xff000000, 0xffffffff};

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
        if(equals(fromFluid) && !isFull()){
            //System.out.println("amount in add fluid: "+amount);
            //System.out.println("capacity left: "+(this.capacity - this.amount));
            int amountAdded = Math.min(fromFluid.getAmount(), this.capacity - this.amount);
            //System.out.println("adjusted amount: "+amountAdded);
            if(amountAdded > 0){
                this.amount += amountAdded;
                this.renderData = fromFluid.getRenderData();
                this.elementContents = fromFluid.getElementContents();
                this.color = fromFluid.getColor();
                return amountAdded;
            }
        }
        return 0;
    }

    public int add(Fluid fromFluid, int amount){
        if(equals(fromFluid) && !isFull()){
            //System.out.println("amount in add fluid: "+amount);
            //System.out.println("capacity left: "+(this.capacity - this.amount));
            int amountAdded = Math.min(Math.min(fromFluid.getAmount(), amount), this.capacity - this.amount);
            //System.out.println("adjusted amount: "+amountAdded);
            if(amountAdded > 0){
                this.amount += amountAdded;
                this.renderData = fromFluid.getRenderData();
                this.elementContents = fromFluid.getElementContents();
                this.color = fromFluid.getColor();
                return amountAdded;
            }
        }
        return 0;
    }

    public void remove(int amount){
        if(this.amount >= amount){
            this.amount -= amount;
            System.out.println("removed "+amount+" fluid. New amount: "+this.amount);
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
        if (elementContents == null || elementContents.length == 0) elementContents = new float[6];
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
            magickaContents = new float[6];
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
                return Fluid.ELEMENT_COLORS[i];
            }
        }

        return 0;
    }

    @Override
    public String toString(){
        return "{amount: "+amount+", capacity: "+capacity+", elementContents: "+Arrays.toString(elementContents)+", color: "+ color +"}";
    }
}
