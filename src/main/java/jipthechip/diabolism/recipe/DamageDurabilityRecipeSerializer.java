package jipthechip.diabolism.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;

public class DamageDurabilityRecipeSerializer extends ShapelessRecipe.Serializer {

    public static final DamageDurabilityRecipeSerializer INSTANCE = new DamageDurabilityRecipeSerializer();

//    @Override
//    public ShapelessRecipe read(JsonObject json){
//        ShapelessRecipe recipe = super.read(json);
//        return new DamageDurabilityRecipe(recipe.getGroup(), recipe.getResult(DynamicRegistryManager.EMPTY), recipe.getIngredients());
//    }

    @Override
    public ShapelessRecipe read(PacketByteBuf buf){
        ShapelessRecipe recipe = super.read(buf);
        return new DamageDurabilityRecipe(recipe.getGroup(), recipe.getResult(DynamicRegistryManager.EMPTY), recipe.getIngredients());
    }

}
