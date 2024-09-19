package jipthechip.diabolism.data.brewing;

import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.brewing.Fluid;
import jipthechip.diabolism.render.FluidRenderData;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.HashMap;

public class ExtendedFluid<E> extends Fluid implements Serializable {

    E extendedData;

    public ExtendedFluid(int amount, HashMap<MagicElement, Float> elementContents, HashMap<MagicElement, Float> magickaContents, int fluidColor, FluidRenderData fluidRenderData, E extendedData) {
        super(amount, elementContents, magickaContents, fluidColor, fluidRenderData);
        this.extendedData = extendedData;
    }
    public ExtendedFluid(Fluid fluid, E extendedData){
        super(fluid);
        this.extendedData = extendedData;
    }

    public ExtendedFluid(@Nullable E extendedData){
        super();
        this.extendedData = extendedData;
    }

    public E getExtendedData(){
        return extendedData;
    }

    public void setExtendedData(E extendedData){
        this.extendedData = extendedData;
    }
}
