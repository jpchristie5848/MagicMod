package jipthechip.diabolism.potion;

import jipthechip.diabolism.data.MagicElement;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BrokenBonesStatusEffect extends AbstractElementalStatusEffect {

    protected BrokenBonesStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, MagicElement.EARTH);
    }

}
