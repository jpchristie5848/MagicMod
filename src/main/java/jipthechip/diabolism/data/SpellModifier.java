package jipthechip.diabolism.data;

import java.io.Serializable;

public class SpellModifier implements Serializable {



    private final int multi;
    private final int precise;
    private final int spread;

    public SpellModifier(int multi, int precise, int spread) {
        this.multi = multi;
        this.precise = precise;
        this.spread = spread;
    }

    public int getMulti() {
        return multi;
    }

    public int getPrecise() {
        return precise;
    }

    public int getSpread() {
        return spread;
    }
}
