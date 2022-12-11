package jipthechip.diabolism.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;

public class DamageDurabilityRecipeSerializer extends ShapelessRecipe.Serializer {

    public static final DamageDurabilityRecipeSerializer INSTANCE = new DamageDurabilityRecipeSerializer();

    @Override
    public ShapelessRecipe read(Identifier id, JsonObject json){
        ShapelessRecipe recipe = super.read(id, json);
        return new DamageDurabilityRecipe(recipe.getId(), recipe.getGroup(), recipe.getOutput(), recipe.getIngredients());
    }

    @Override
    public ShapelessRecipe read(Identifier id, PacketByteBuf buf){
        ShapelessRecipe recipe = super.read(id, buf);
        return new DamageDurabilityRecipe(recipe.getId(), recipe.getGroup(), recipe.getOutput(), recipe.getIngredients());
    }

}
