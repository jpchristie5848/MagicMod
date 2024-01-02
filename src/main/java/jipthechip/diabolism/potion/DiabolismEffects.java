package jipthechip.diabolism.potion;

import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.mixin.BrewingRecipeRegistryAccessor;
import net.minecraft.entity.effect.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.*;

public class DiabolismEffects {


    public static final List<StatusEffect> STATUS_EFFECTS = new ArrayList<>(Arrays.asList(
            new AwakeningStatusEffect(StatusEffectCategory.HARMFUL, 0x000000),
            new HarmStatusEffect(StatusEffectCategory.HARMFUL, 0x00000),
            new GustStatusEffect(StatusEffectCategory.NEUTRAL, 0xf5e990),
            new UpdraftStatusEffect(StatusEffectCategory.NEUTRAL, 0xf5e990),
            new BurningStatusEffect(StatusEffectCategory.HARMFUL, 0xf75736),
            new WetStatusEffect(StatusEffectCategory.HARMFUL, 0x003FFF),
            new BrokenBonesStatusEffect(StatusEffectCategory.HARMFUL, 0x75430d),
            new ShockStatusEffect(StatusEffectCategory.HARMFUL, 0xeeff00),
            new ParalysisStatusEffect(StatusEffectCategory.HARMFUL, 0xeeff00),
            new ChillyStatusEffect(StatusEffectCategory.HARMFUL, 0xa3daf0),
            new FrozenStatusEffect(StatusEffectCategory.HARMFUL, 0xa3daf0),
            new LifeStatusEffect(StatusEffectCategory.NEUTRAL, 0x22e305),
            new RightToYourThighsStatusEffect(StatusEffectCategory.HARMFUL, 0x000000)
            )
    );

    public static final StatusEffect AWAKENING_STATUS_EFFECT = new AwakeningStatusEffect(StatusEffectCategory.HARMFUL, 0x000000);

    public static final StatusEffect MAGIC_HARM_STATUS_EFFECT = new HarmStatusEffect(StatusEffectCategory.HARMFUL, 0x00000);


    public static final StatusEffect GUST_STATUS_EFFECT = new GustStatusEffect(StatusEffectCategory.NEUTRAL, 0xf5e990);
    public static final StatusEffect UPDRAFT_STATUS_EFFECT = new UpdraftStatusEffect(StatusEffectCategory.NEUTRAL, 0xf5e990);

    public static final StatusEffect BURNING_STATUS_EFFECT = new BurningStatusEffect(StatusEffectCategory.HARMFUL, 0xf75736);

    public static final StatusEffect WET_STATUS_EFFECT = new WetStatusEffect(StatusEffectCategory.HARMFUL, 0x003FFF);

    public static final StatusEffect BROKEN_BONES_STATUS_EFFECT = new BrokenBonesStatusEffect(StatusEffectCategory.HARMFUL, 0x75430d);

    public static final StatusEffect SHOCK_STATUS_EFFECT = new ShockStatusEffect(StatusEffectCategory.HARMFUL, 0xeeff00);

    public static final StatusEffect PARALYSIS_STATUS_EFFECT = new ParalysisStatusEffect(StatusEffectCategory.HARMFUL, 0xeeff00);
    public static final StatusEffect CHILLY_STATUS_EFFECT = new ChillyStatusEffect(StatusEffectCategory.HARMFUL, 0xa3daf0);
    public static final StatusEffect FROZEN_STATUS_EFFECT = new FrozenStatusEffect(StatusEffectCategory.HARMFUL, 0xa3daf0);
    public static final StatusEffect LIFE_STATUS_EFFECT = new LifeStatusEffect(StatusEffectCategory.NEUTRAL, 0x22e305);

    public static final StatusEffect DEATH_STATUS_EFFECT = new DeathStatusEffect(StatusEffectCategory.NEUTRAL, 0x000000);

    public static final StatusEffect RIGHT_TO_YOUR_THIGHS = new RightToYourThighsStatusEffect(StatusEffectCategory.HARMFUL, 0x000000);


    public static final Potion AWAKENING_POTION = new Potion(new StatusEffectInstance(DiabolismEffects.AWAKENING_STATUS_EFFECT, 3600), new StatusEffectInstance(StatusEffects.NAUSEA, 3600), new StatusEffectInstance(StatusEffects.BLINDNESS, 3600));

    public static Map<String, AbstractElementalStatusEffect> ELEMENTAL = new HashMap<>();

    public static Map<String, ClientSyncedStatusEffect> CLIENT_SYNCED = new HashMap<>();

    public static void registerPotions(){
        Registry.register(Registries.POTION, new Identifier("diabolism", "awakening_potion"), AWAKENING_POTION);
    }

    public static void registerPotionEffects(){

        for(StatusEffect effect : STATUS_EFFECTS){
            if(effect instanceof ClientSyncedStatusEffect clientEffect){
                String effectName = clientEffect.getRegistryName();
                Registry.register(Registries.STATUS_EFFECT, new Identifier("diabolism", effectName+"_status_effect"), effect);
                if(effect instanceof AbstractElementalStatusEffect elementalEffect)
                    ELEMENTAL.put(effectName, elementalEffect);
                CLIENT_SYNCED.put(effectName, clientEffect);
            }
        }
    }

    public static void registerPotionRecipes(){
        BrewingRecipeRegistryAccessor.invokeRegisterPotionRecipe(Potions.AWKWARD, DiabolismItems.VOLATILE_MIXTURE, DiabolismEffects.AWAKENING_POTION);
    }


}
