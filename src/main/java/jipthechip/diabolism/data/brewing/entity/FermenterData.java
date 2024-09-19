package jipthechip.diabolism.data.brewing.entity;

import jipthechip.diabolism.data.brewing.Yeast;

import java.io.Serializable;

public class FermenterData implements Serializable {

    private Yeast yeast;
    private float fermentationProgress;
    private boolean isOpen;

    public FermenterData() {
        yeast = null;
        fermentationProgress = 0;
        isOpen = false;
    }

    public FermenterData(Yeast yeast, float fermentationProgress, boolean isOpen){
        this.yeast = yeast;
        this.fermentationProgress = fermentationProgress;
        this.isOpen = isOpen;
    }

    public Yeast getYeast() {
        return yeast;
    }

    public void setYeast(Yeast yeast) {
        this.yeast = yeast;
    }

    public float getFermentationProgress() {
        return fermentationProgress;
    }

    public void setFermentationProgress(float fermentationProgress) {
        this.fermentationProgress = fermentationProgress;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void toggleOpen(){
        isOpen = !isOpen;
    }
}
