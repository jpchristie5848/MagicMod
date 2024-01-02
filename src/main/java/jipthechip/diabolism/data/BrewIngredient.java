package jipthechip.diabolism.data;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;

public record BrewIngredient(int air, int fire, int water, int earth, int chaos, int order) {

    public static HashMap<Item, BrewIngredient> mappings = new HashMap<>() {{
        put(Items.APPLE, new BrewIngredient(3, 0, 3, 0, 0, 0));
        put(Items.BAKED_POTATO, new BrewIngredient(0, 1, 1, 2, 0, 1));
        put(Items.BEETROOT, new BrewIngredient(1, 1, 1, 3, 0, 0));
        put(Items.BEETROOT_SOUP, new BrewIngredient(1, 1, 2, 1, 0, 1));
        put(Items.BREAD, new BrewIngredient(3, 0, 0, 2, 0, 1));
        put(Items.BROWN_MUSHROOM, new BrewIngredient(0, 0, 3, 2, 1, 0));
        put(Items.CAKE, new BrewIngredient(3, 1, 1, 1, 0, 4));
        put(Items.CARROT, new BrewIngredient(0, 0, 2, 4, 0, 0));
        put(Items.CHORUS_FRUIT, new BrewIngredient(0, 0, 0, 2, 4, 0));
        put(Items.COCOA_BEANS, new BrewIngredient(0, 4, 2, 0, 0, 0));
        put(Items.COOKIE, new BrewIngredient(2, 0, 1, 2, 0, 1));
        put(Items.DRIED_KELP, new BrewIngredient(2, 0, 4, 0, 0, 0));
        put(Items.ENCHANTED_GOLDEN_APPLE, new BrewIngredient(4, 0, 4, 0, 0, 6));
        put(Items.GOLDEN_APPLE, new BrewIngredient(3, 0, 3, 0, 0, 4));
        put(Items.GLOW_BERRIES, new BrewIngredient(0, 5, 0, 1, 0, 0));
        put(Items.GOLDEN_CARROT, new BrewIngredient(0, 0, 1, 3, 0, 2));
        put(Items.HONEY_BOTTLE, new BrewIngredient(1, 2, 1, 0, 0, 1));
        put(Items.MELON_SLICE, new BrewIngredient(0, 0, 5, 1, 0, 0));
        put(Items.MUSHROOM_STEW, new BrewIngredient(0, 0, 2, 2, 0, 2));
        put(Items.KELP, new BrewIngredient(1, 0, 5, 0, 0, 0));
        put(Items.POISONOUS_POTATO, new BrewIngredient(0, 0, 1, 4, 1, 0));
        put(Items.POTATO, new BrewIngredient(0, 0, 2, 4, 0, 0));
        put(Items.PUMPKIN, new BrewIngredient(0, 1, 3, 0, 1, 0));
        put(Items.PUMPKIN_PIE, new BrewIngredient(0, 3, 5, 0, 0, 2));
        put(Items.RED_MUSHROOM, new BrewIngredient(0, 1, 2, 2, 1, 0));
        put(Items.SUGAR, new BrewIngredient(3, 0, 2, 0, 1, 0));
        put(Items.SUGAR_CANE, new BrewIngredient(3, 0, 3, 0, 0, 0));
        put(Items.SWEET_BERRIES, new BrewIngredient(1, 0, 4, 1, 0, 0));
        put(Items.WHEAT, new BrewIngredient(4, 0, 0, 2, 0, 0));
    }};

    public static boolean isValidIngredient(Item item){
        return mappings.containsKey(item);
    }

}
