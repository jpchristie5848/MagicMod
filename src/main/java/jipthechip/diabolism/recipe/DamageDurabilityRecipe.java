package jipthechip.diabolism.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class DamageDurabilityRecipe extends ShapelessRecipe {
    public DamageDurabilityRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input) {
        super(id, group, output, input);
    }

    public DefaultedList<ItemStack> getRemainder(CraftingInventory inv){
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);

        for(int i = 0; i < defaultedList.size(); ++i) {
            ItemStack stack = inv.getStack(i);
                if(stack.isDamageable()){
                    int damage = stack.getDamage();
                    if(damage < stack.getMaxDamage() - 1){
                        stack = stack.copy();
                        stack.setDamage(damage+1);
                        defaultedList.set(i, stack);
                    }
                }else{
                    defaultedList.set(i, stack.getRecipeRemainder());
                }
        }
        return defaultedList;
    }

    public RecipeSerializer<?> getSerializer(){
        return DamageDurabilityRecipeSerializer.INSTANCE;
    }
}
