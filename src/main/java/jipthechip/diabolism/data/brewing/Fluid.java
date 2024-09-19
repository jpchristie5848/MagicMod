package jipthechip.diabolism.data.brewing;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.MagicElementColors;
import jipthechip.diabolism.data.spell.Spell;
import jipthechip.diabolism.render.RenderDataMappings;
import jipthechip.diabolism.render.FluidRenderData;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Fluid implements Serializable{

    protected int amount = 0;
    protected HashMap<MagicElement, Float> elementContentsMap;
    protected HashMap<MagicElement, Float> magickaContentsMap;

    protected int color = -1; // fluid color that overrides the default color (-1 for default)

    protected FluidRenderData renderData;

    public Fluid(int amount, HashMap<MagicElement, Float> elementContents, HashMap<MagicElement, Float> magickaContents, int fluidColor, FluidRenderData fluidRenderData) {
        this.amount = amount;
        this.elementContentsMap = elementContents;
        this.magickaContentsMap = magickaContents;
        this.color = fluidColor;
        this.renderData = fluidRenderData;
    }

    public Fluid(Fluid fluid){
        this.amount = fluid.amount;
        this.elementContentsMap = fluid.elementContentsMap;
        this.magickaContentsMap = fluid.magickaContentsMap;
        this.color = fluid.color;
        this.renderData = fluid.renderData;
    }

    public Fluid(){
        this.amount = 0;
        this.elementContentsMap = new HashMap<>();
        this.magickaContentsMap = new HashMap<>();
        this.renderData = RenderDataMappings.Fluids.get("churner_fluid");
    }

    public HashMap<MagicElement, Float> getElementContentsMap() {
        return elementContentsMap;
    }

    public HashMap<MagicElement, Float> getMagickaContentsMap() {
        return magickaContentsMap;
    }

    public int add(Fluid fromFluid, int containerCapacity){
        if(!isFull(containerCapacity)){
            int amountAdded = Math.min(fromFluid.getAmount(), containerCapacity - this.amount);
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

    public int add(Fluid fromFluid, int amount, int containerCapacity){
        if(!isFull(containerCapacity)){

            int amountAdded = Math.min(Math.min(fromFluid.getAmount(), amount), containerCapacity - this.amount);

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

    public boolean isFull(int containerCapacity){
        return amount >= containerCapacity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount, int containerCapacity) {
        this.amount = Math.min(Math.max(amount, 0), containerCapacity);
    }

    public int getColor(){
        return color == -1 ? renderData == null ? 0 : renderData.getColor() : color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public float getElementContentFromMap(MagicElement element){
        return elementContentsMap.getOrDefault(element, 0.0f);
    }

    public float getMagickaContentFromMap(MagicElement element){
        return magickaContentsMap.getOrDefault(element, 0.0f);
    }

    public HashMap<MagicElement, Float> getElementContents() {
        return elementContentsMap;
    }

    public boolean hasElementContent(){
        return !contentsAreEmpty(elementContentsMap);
    }

    public void addElementContents(BrewIngredient brewIngredient){
        if (elementContentsMap == null) elementContentsMap = new HashMap<>();
        for(MagicElement element : brewIngredient.elements().keySet()){
            float currentValue = elementContentsMap.getOrDefault(element, 0.0f);
            float ingredientValue = brewIngredient.elements().get(element);
            elementContentsMap.put(element, currentValue + ingredientValue);
        }
    }

    public void setElementContents(MagicElement element, float value){
        if(elementContentsMap == null) elementContentsMap = new HashMap<>();
        elementContentsMap.put(element, value);
    }

    public void setMagickaContents(MagicElement element, float value){
        if(magickaContentsMap == null) magickaContentsMap = new HashMap<>();
        magickaContentsMap.put(element, value);
    }

    public void removeElementContent(MagicElement element, float amount){
        if(elementContentsMap == null) elementContentsMap = new HashMap<>();
        elementContentsMap.put(element, Math.max(elementContentsMap.get(element)-amount, 0.0f));
    }

    public void addMagickaContent(MagicElement element, float amount){
        if(magickaContentsMap == null) magickaContentsMap = new HashMap<>();
        magickaContentsMap.put(element, magickaContentsMap.getOrDefault(element, 0.0f)+amount);
    }


    public HashMap<MagicElement, Float> getMagickaContents() {
        return magickaContentsMap;
    }

    public boolean hasMagickaContent(){
        return !contentsAreEmpty(magickaContentsMap);
    }

    public FluidRenderData getRenderData(){
        return renderData;
    }

    public void setRenderData(@Nullable String name){
        this.renderData = name == null ? null : RenderDataMappings.Fluids.get(name);
    }

    public void reset(){
        amount = 0;
        color = -1;
        renderData = null;
        elementContentsMap = new HashMap<>();
        magickaContentsMap = new HashMap<>();
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Fluid otherFluid){
            return  (getAmount() > 0 && Objects.equals(this.renderData.getCuboidName(), otherFluid.getRenderData().getCuboidName()) &&
                    (this.getColor() == otherFluid.getColor()) &&
                    contentsAreEqual(this.getElementContentsMap(), ((Fluid) other).getElementContentsMap()) &&
                    contentsAreEqual(this.getMagickaContentsMap(), ((Fluid) other).getMagickaContentsMap()));
        }
        return false;
    }

    public int getMagickaParticleColor(){

        float magickaSum = 0;

        for(MagicElement element : magickaContentsMap.keySet()){
            magickaSum += magickaContentsMap.get(element);
        }

        double p = Math.random() * magickaSum;
        float cumulativeProbability = 0.0f;
        for (Map.Entry<MagicElement, Float> entry : magickaContentsMap.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (p <= cumulativeProbability) {
                return MagicElementColors.MAP.get(entry.getKey());
            }
        }

        return 0;
    }

    private void addOtherFluid(Fluid otherFluid, int otherAmount){

        HashMap<MagicElement, Float> otherElementContents = otherFluid.getElementContents();
        HashMap<MagicElement, Float> otherMagickaContents = otherFluid.getMagickaContents();

        if(this.amount == 0){
            this.amount = otherAmount;
            this.elementContentsMap = otherElementContents;
            this.magickaContentsMap = otherMagickaContents;
            this.renderData = otherFluid.getRenderData();
            this.color = otherFluid.getColor();
        }else{
            HashMap<MagicElement, Float> newElementContents = new HashMap<>();
            HashMap<MagicElement, Float> newMagickaContents = new HashMap<>();

            if(otherElementContents != null){
                for(MagicElement element : MagicElement.values()){
                    newElementContents.put(element, (elementContentsMap.get(element) * ((float)this.amount / otherAmount) + otherElementContents.get(element) * ((float)otherAmount / this.amount))/2);
                }
                elementContentsMap = newElementContents;
            }

            if(otherMagickaContents != null){
                for(MagicElement element : MagicElement.values()){
                    newMagickaContents.put(element, (magickaContentsMap.get(element) * ((float)this.amount / otherAmount) + otherMagickaContents.get(element) * ((float)otherAmount / this.amount))/2);
                }
                magickaContentsMap = newMagickaContents;
            }

            color = (int)((color * ((float)this.amount / (otherAmount+this.amount))) + (otherFluid.getColor() * ((float)otherAmount / (otherAmount+this.amount)))) / 2;

            this.amount += otherAmount;
            // TODO fluid render data is unchanged, keeping original for now, may need to change later
        }
    }

    private static boolean contentsAreEqual(HashMap<MagicElement, Float> map1, HashMap<MagicElement, Float> map2){
        if(contentsAreEmpty(map1) ^ contentsAreEmpty(map2)) return false;
        if(contentsAreEmpty(map1)) return true;
        if(!map1.keySet().equals(map2.keySet())) return false;
        for(MagicElement key : map1.keySet()){
            if(!Objects.equals(map1.get(key), map2.get(key))){
                return false;
            }
        }
        return true;
    }

    private static boolean contentsAreEmpty(HashMap<MagicElement, Float> map){
        if(map == null) return true;
        for(MagicElement element : map.keySet()){
            if(map.get(element) > 0.0f) return false;
        }
        return true;
    }

    @Override
    public String toString(){
        return "{\namount: "+amount+",\nelementContents: "+ DataUtils.getMapString(elementContentsMap)+",\nmagickaContents: "+DataUtils.getMapString(magickaContentsMap)+",\ncolor: "+ String.format("0x%08X", color) +"\n}";
    }
}
