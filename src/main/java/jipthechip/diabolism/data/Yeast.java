package jipthechip.diabolism.data;

import jipthechip.diabolism.Utils.ImageUtils;
import jipthechip.diabolism.Utils.MathUtils;

import java.awt.*;
import java.io.Serializable;

public class Yeast implements Serializable {

    private float[] yieldMultipliers;
    private float speedMultiplier;
    private float idealTemperature;


    public Yeast(float[] yieldMultipliers, float speedMultiplier, float idealTemperature) {
        this.yieldMultipliers = yieldMultipliers;
        this.speedMultiplier = speedMultiplier;
        this.idealTemperature = idealTemperature;
    }

    public float[] getYieldMultipliers() {
        return yieldMultipliers;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public float getIdealTemperature() {
        return idealTemperature;
    }

    public static Yeast generateLowTierYeast(int primaryElement, int secondaryElement, float idealTemperature){
        float[] yieldMultipliers = new float[]{0.75f, 0.75f, 0.75f, 0.75f, 0.75f, 0.75f};
        yieldMultipliers[primaryElement] = 1.2f;
        yieldMultipliers[secondaryElement] = 1.0f;

        return new Yeast(yieldMultipliers, 0.75f, idealTemperature);
    }

    // gets the color of the yeast, which varies depending upon its attributes
    // has a primary and secondary color, index 0 gets primary color, index 1 gets secondary color
    public int getColor(int index){
        int elementIndex = MathUtils.getNthGreatestValue(getYieldMultipliers(), index+1);
        float weight = ((getYieldMultipliers()[elementIndex] - 1.0f)+0.3f)*1.5f;

        Color elementColor = new Color(Fluid.ELEMENT_COLORS[elementIndex], true);


        return ImageUtils.getAvgColor(elementColor, new Color(0xFFffefc2, true), weight).getRGB();
    }
}
