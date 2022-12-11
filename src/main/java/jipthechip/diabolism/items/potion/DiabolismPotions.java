package jipthechip.diabolism.items.potion;

import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.mixin.BrewingRecipeRegistryAccessor;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DiabolismPotions {
    public static final StatusEffect AWAKENING_STATUS_EFFECT = new AwakeningStatusEffect(StatusEffectCategory.HARMFUL, 0000000);
    public static final Potion AWAKENING_POTION = new Potion(new StatusEffectInstance(DiabolismPotions.AWAKENING_STATUS_EFFECT, 3600), new StatusEffectInstance(StatusEffects.NAUSEA, 3600), new StatusEffectInstance(StatusEffects.BLINDNESS, 3600));

    public static void registerPotions(){
        Registry.register(Registry.POTION, new Identifier("diabolism", "awakening_potion"), AWAKENING_POTION);
    }

    public static void registerPotionEffects(){
        Registry.register(Registry.STATUS_EFFECT, new Identifier("diabolism", "awakening_status_effect"), AWAKENING_STATUS_EFFECT);
    }

    public static void registerPotionRecipes(){
        BrewingRecipeRegistryAccessor.invokeRegisterPotionRecipe(Potions.AWKWARD, DiabolismItems.VOLATILE_MIXTURE, DiabolismPotions.AWAKENING_POTION);
    }


}
