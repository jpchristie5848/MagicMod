package jipthechip.diabolism.data.brewing;

import jipthechip.diabolism.data.MagicElement;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;

public record BrewIngredient(HashMap<MagicElement, Float> elements) {

    public static HashMap<Item, BrewIngredient> mappings = new HashMap<>() {{

        put(Items.APPLE, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 3.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 3.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.BAKED_POTATO, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 1.0f);
            put(MagicElement.WATER, 1.0f);
            put(MagicElement.EARTH, 2.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 1.0f);
        }}));

        put(Items.BEETROOT, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 1.0f);
            put(MagicElement.FIRE, 1.0f);
            put(MagicElement.WATER, 1.0f);
            put(MagicElement.EARTH, 3.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.BEETROOT_SOUP, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 1.0f);
            put(MagicElement.FIRE, 1.0f);
            put(MagicElement.WATER, 2.0f);
            put(MagicElement.EARTH, 1.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 1.0f);
        }}));

        put(Items.BREAD, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 3.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 0.0f);
            put(MagicElement.EARTH, 2.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 1.0f);
        }}));

        put(Items.BROWN_MUSHROOM, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 3.0f);
            put(MagicElement.EARTH, 2.0f);
            put(MagicElement.CHAOS, 1.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.CAKE, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 3.0f);
            put(MagicElement.FIRE, 1.0f);
            put(MagicElement.WATER, 1.0f);
            put(MagicElement.EARTH, 1.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 4.0f);
        }}));

        put(Items.CARROT, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 2.0f);
            put(MagicElement.EARTH, 4.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.CHORUS_FRUIT, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 0.0f);
            put(MagicElement.EARTH, 2.0f);
            put(MagicElement.CHAOS, 4.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.COCOA_BEANS, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 4.0f);
            put(MagicElement.WATER, 2.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.COOKIE, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 2.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 1.0f);
            put(MagicElement.EARTH, 2.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 1.0f);
        }}));

        put(Items.DRIED_KELP, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 2.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 4.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.ENCHANTED_GOLDEN_APPLE, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 4.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 4.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 6.0f);
        }}));

        put(Items.GOLDEN_APPLE, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 3.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 3.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 4.0f);
        }}));

        put(Items.GLOW_BERRIES, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 5.0f);
            put(MagicElement.WATER, 0.0f);
            put(MagicElement.EARTH, 1.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.GOLDEN_CARROT, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 1.0f);
            put(MagicElement.EARTH, 3.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 2.0f);
        }}));

        put(Items.HONEY_BOTTLE, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 1.0f);
            put(MagicElement.FIRE, 2.0f);
            put(MagicElement.WATER, 1.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 1.0f);
        }}));

        put(Items.MELON_SLICE, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 5.0f);
            put(MagicElement.EARTH, 1.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.MUSHROOM_STEW, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 2.0f);
            put(MagicElement.EARTH, 2.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 2.0f);
        }}));

        put(Items.KELP, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 1.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 5.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));


        put(Items.POISONOUS_POTATO, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 1.0f);
            put(MagicElement.EARTH, 4.0f);
            put(MagicElement.CHAOS, 1.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.POTATO, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 2.0f);
            put(MagicElement.EARTH, 4.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.PUMPKIN, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 1.0f);
            put(MagicElement.WATER, 3.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 1.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.PUMPKIN_PIE, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 3.0f);
            put(MagicElement.WATER, 5.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 2.0f);
        }}));

        put(Items.RED_MUSHROOM, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 0.0f);
            put(MagicElement.FIRE, 1.0f);
            put(MagicElement.WATER, 2.0f);
            put(MagicElement.EARTH, 2.0f);
            put(MagicElement.CHAOS, 1.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.SUGAR, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 3.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 2.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 1.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.SUGAR_CANE, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 3.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 3.0f);
            put(MagicElement.EARTH, 0.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.SWEET_BERRIES, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 1.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 4.0f);
            put(MagicElement.EARTH, 1.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));

        put(Items.WHEAT, new BrewIngredient(new HashMap<>(){{
            put(MagicElement.AIR, 4.0f);
            put(MagicElement.FIRE, 0.0f);
            put(MagicElement.WATER, 0.0f);
            put(MagicElement.EARTH, 2.0f);
            put(MagicElement.CHAOS, 0.0f);
            put(MagicElement.ORDER, 0.0f);
        }}));
    }};

    public static boolean isValidIngredient(Item item){
        return mappings.containsKey(item);
    }

}
