package jipthechip.diabolism.data.brewing.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.io.Serializable;

public class ChurnerData implements Serializable {

    private float mixingProgress;
    private int mixedIngredients;

    public ChurnerData(){
        this.mixingProgress = 0;
        this.mixedIngredients = 0;
    }

    public float getMixingProgress() {
        return mixingProgress;
    }

    public void setMixingProgress(float mixingProgress) {
        this.mixingProgress = mixingProgress;
    }

    public int getMixedIngredients() {
        return mixedIngredients;
    }

    public void setMixedIngredients(int mixedIngredients) {
        this.mixedIngredients = mixedIngredients;
    }
}
