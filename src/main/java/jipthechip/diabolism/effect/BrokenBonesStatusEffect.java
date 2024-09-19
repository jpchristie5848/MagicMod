package jipthechip.diabolism.effect;

import jipthechip.diabolism.data.MagicElement;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BrokenBonesStatusEffect extends AbstractElementalStatusEffect {

    protected BrokenBonesStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x75430d, MagicElement.EARTH, "brokenbones");
    }

}
