package jipthechip.diabolism.recipe;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DiabolismRecipes {


    public static void registerRecipes(){
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("diabolism", "damage_durability"), DamageDurabilityRecipeSerializer.INSTANCE);
    }
}
