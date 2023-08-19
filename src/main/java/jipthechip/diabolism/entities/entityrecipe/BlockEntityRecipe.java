package jipthechip.diabolism.entities.entityrecipe;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;

public interface BlockEntityRecipe {

    public ItemStack getItemResult(BlockEntity be);

    public RecipeBlockResult[] getBlockResult(BlockEntity be);

    public int getFluidCost(BlockEntity be);
    public boolean requirementsMet(BlockEntity be);
}
