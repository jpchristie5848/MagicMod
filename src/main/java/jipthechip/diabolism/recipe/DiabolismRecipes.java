package jipthechip.diabolism.recipe;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DiabolismRecipes {


    public static void registerRecipes(){
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("diabolism", "damage_durability"), DamageDurabilityRecipeSerializer.INSTANCE);
    }
}
