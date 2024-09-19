package jipthechip.diabolism.data.brewing;

import jipthechip.diabolism.Utils.ImageUtils;
import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.MagicElementColors;
import jipthechip.diabolism.data.spell.Spell;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Yeast implements Serializable {

    private HashMap<MagicElement, Float> yieldMultipliers;
    private float speedMultiplier;
    private float idealTemperature;


    public Yeast(HashMap<MagicElement, Float> yieldMultipliers, float speedMultiplier, float idealTemperature) {
        this.yieldMultipliers = yieldMultipliers;
        this.speedMultiplier = speedMultiplier;
        this.idealTemperature = idealTemperature;
    }

    public HashMap<MagicElement, Float> getYieldMultipliers() {
        return yieldMultipliers;
    }

    public float getYieldMultiplier(MagicElement element){
        return yieldMultipliers.getOrDefault(element, 0.0f);
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public float getIdealTemperature() {
        return idealTemperature;
    }

    // gets the color of the yeast, which varies depending upon its attributes
    // has a primary and secondary color, index 0 gets primary color, index 1 gets secondary color
    public int getColor(int index){
        MagicElement element = getNthGreatestElement(yieldMultipliers, index+1);
        float weight = ((yieldMultipliers.get(element) - 1.0f)+0.3f)*1.5f;

        Color elementColor = new Color(MagicElementColors.MAP.get(element), true);


        return ImageUtils.getAvgColor(elementColor, new Color(0xFFffefc2, true), weight).getRGB();
    }

    private static MagicElement getNthGreatestElement(HashMap<MagicElement, Float> yieldMultipliers, int n){
        Map.Entry<MagicElement, Float> maxEntry = null;
        ArrayList<MagicElement> elementsToSkip = new ArrayList<>();
        for(int i = 0; i < n; i++){
            for (Map.Entry<MagicElement, Float> entry : yieldMultipliers.entrySet()) {
                if (!elementsToSkip.contains(entry.getKey()) && maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                    maxEntry = entry;
                }
            }
            if(maxEntry != null) elementsToSkip.add(maxEntry.getKey());
        }
        return maxEntry == null ? null : maxEntry.getKey();
    }

    public static Yeast generateLowTierYeast(MagicElement primaryElement, MagicElement secondaryElement, float idealTemperature){
        HashMap<MagicElement, Float> yieldMultipliers = new HashMap<>(){{
            put(MagicElement.AIR, 0.75f);
            put(MagicElement.FIRE, 0.75f);
            put(MagicElement.WATER, 0.75f);
            put(MagicElement.EARTH, 0.75f);
            put(MagicElement.CHAOS, 0.75f);
            put(MagicElement.ORDER, 0.75f);
        }};

        yieldMultipliers.put(primaryElement, 1.2f);
        yieldMultipliers.put(secondaryElement, 1.0f);

        return new Yeast(yieldMultipliers, 0.75f, idealTemperature);
    }
}
