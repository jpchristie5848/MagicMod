package jipthechip.diabolism.potion;

import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.mixin.BrewingRecipeRegistryAccessor;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DiabolismPotions {


    public static final StatusEffect AWAKENING_STATUS_EFFECT = new AwakeningStatusEffect(StatusEffectCategory.HARMFUL, 0x000000);

    public static final StatusEffect MAGIC_HARM_STATUS_EFFECT = new MagicHarmStatusEffect(StatusEffectCategory.HARMFUL, 0x00000);
    public static final StatusEffect CHILLY_STATUS_EFFECT = new ChillyStatusEffect(StatusEffectCategory.HARMFUL, 0x05b6fc);
    public static final StatusEffect FROZEN_STATUS_EFFECT = new FrozenStatusEffect(StatusEffectCategory.HARMFUL, 0xffffff);

    public static final StatusEffect WET_STATUS_EFFECT = new WetStatusEffect(StatusEffectCategory.HARMFUL, 0x003FFF);
    public static final StatusEffect UPDRAFT_STATUS_EFFECT = new UpdraftStatusEffect(StatusEffectCategory.NEUTRAL, 0xf5e990);
    public static final StatusEffect GUST_STATUS_EFFECT = new GustStatusEffect(StatusEffectCategory.NEUTRAL, 0xf5e990);

    public static final Potion AWAKENING_POTION = new Potion(new StatusEffectInstance(DiabolismPotions.AWAKENING_STATUS_EFFECT, 3600), new StatusEffectInstance(StatusEffects.NAUSEA, 3600), new StatusEffectInstance(StatusEffects.BLINDNESS, 3600));

    public static final Map<String, StatusEffect> STATUS_EFFECT_MAP = new HashMap<>(){{
        put("awakening", AWAKENING_STATUS_EFFECT);
        put("magic_harm", MAGIC_HARM_STATUS_EFFECT);
        put("chilly", CHILLY_STATUS_EFFECT);
        put("frozen", FROZEN_STATUS_EFFECT);
        put("wet", WET_STATUS_EFFECT);
        put("updraft", UPDRAFT_STATUS_EFFECT);
        put("gust", GUST_STATUS_EFFECT);
    }};

    public static void registerPotions(){
        Registry.register(Registries.POTION, new Identifier("diabolism", "awakening_potion"), AWAKENING_POTION);
    }

    public static void registerPotionEffects(){
        Registry.register(Registries.STATUS_EFFECT, new Identifier("diabolism", "awakening_status_effect"), AWAKENING_STATUS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("diabolism", "updraft_status_effect"), UPDRAFT_STATUS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("diabolism", "gust_status_effect"), GUST_STATUS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("diabolism", "chilly_status_effect"), CHILLY_STATUS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("diabolism", "frozen_status_effect"), FROZEN_STATUS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("diabolism", "wet_status_effect"), WET_STATUS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("diabolism", "magic_harm_status_effect"), MAGIC_HARM_STATUS_EFFECT);
    }

    public static void registerPotionRecipes(){
        BrewingRecipeRegistryAccessor.invokeRegisterPotionRecipe(Potions.AWKWARD, DiabolismItems.VOLATILE_MIXTURE, DiabolismPotions.AWAKENING_POTION);
    }


}
