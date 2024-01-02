package jipthechip.diabolism.data;

import java.io.Serializable;

public class MagickaCrystal implements Serializable {

    private final MagicElement element;
    private final int tier;

    public MagickaCrystal(MagicElement element, int tier){
        this.element = element;
        this.tier = tier;
    }

    public MagicElement getElement() {
        return element;
    }

    public int getTier(){
        return tier;
    }
}
