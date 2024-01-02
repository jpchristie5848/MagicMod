package jipthechip.diabolism.data;

import java.io.Serializable;

public class MagickaFluid implements Serializable {

    private MagicElement element;
    private float amount;
    private float purity;

    public MagickaFluid(MagicElement element, float amount, float purity){
        this.element = element;
        this.amount = amount;
        this.purity = purity;
    }

    public MagicElement getElement() {
        return element;
    }

    public float getAmount() {
        return amount;
    }

    public float getPurity() {
        return purity;
    }

    public boolean addOther(MagickaFluid fluid){
        if(fluid == null || fluid.getElement() != this.element) return false;

        float magickaAmount = (amount * purity);
        float otherMagickaAmount = fluid.getAmount() * fluid.getPurity();

        System.out.println("amount before: "+amount);
        System.out.println("purity before: "+purity);
        System.out.println("other fluid amount: "+fluid.getAmount());
        System.out.println("other fluid purity: "+fluid.getPurity());

        amount += fluid.getAmount();


        purity = (magickaAmount + otherMagickaAmount) / amount;

        System.out.println("magicka amount: "+magickaAmount);
        System.out.println("other magicka amount: "+otherMagickaAmount);
        System.out.println("amount: "+amount);

        System.out.println("set purity to "+purity);
        return true;
    }

    @Override
    public String toString() {
        return "MagickaFluid{" +
                "element=" + element +
                ", amount=" + amount +
                ", purity=" + purity +
                '}';
    }
}
