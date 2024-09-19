package jipthechip.diabolism.effect;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.*;

public class DiabolismEffects {

    public static final Map<String, DiabolismStatusEffect> MAP = new HashMap<>(){{
            put("awakening",new AwakeningStatusEffect());
            put("broken_bones",new BrokenBonesStatusEffect());
            put("burning",new BurningStatusEffect());
            put("chilly",new ChillyStatusEffect());
            put("death",new DeathStatusEffect());
            put("frozen",new FrozenStatusEffect());
            put("gust",new GustStatusEffect());
            put("harm",new HarmStatusEffect());
            put("life",new LifeStatusEffect());
            put("paralysis",new ParalysisStatusEffect());
            put("right_to_your_thighs",new RightToYourThighsStatusEffect());
            put("shock",new ShockStatusEffect());
            put("updraft",new UpdraftStatusEffect());
            put("wet",new WetStatusEffect());
    }};

    // I tried
    //public static DiabolismEffectsStore EFFECTS_STORE = new DiabolismEffectsStore();

    //public static final Potion AWAKENING_POTION = new Potion(new StatusEffectInstance(DiabolismEffects.AWAKENING_STATUS_EFFECT, 3600), new StatusEffectInstance(StatusEffects.NAUSEA, 3600), new StatusEffectInstance(StatusEffects.BLINDNESS, 3600));
    public static void registerPotions(){
        //Registry.register(Registries.POTION, new Identifier("diabolism", "awakening_potion"), AWAKENING_POTION);
    }

    public static void registerStatusEffects(){

        for(String key : MAP.keySet()){
            DiabolismStatusEffect effect = MAP.get(key);
            effect.register();
        }
    }

    public static void registerPotionRecipes(){
        //BrewingRecipeRegistryAccessor.invokeRegisterPotionRecipe(Potions.AWKWARD, DiabolismItems.VOLATILE_MIXTURE, DiabolismEffects.AWAKENING_POTION);
    }


}
