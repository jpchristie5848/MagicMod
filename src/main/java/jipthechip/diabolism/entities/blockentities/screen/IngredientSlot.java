package jipthechip.diabolism.entities.blockentities.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class IngredientSlot extends ClientSyncingSlot {

    Item ingredient;

    public IngredientSlot(BlockEntity entity, Inventory inventory, int index, int x, int y, Item ingredient) {
        super(entity, inventory, index, x, y);
        this.ingredient = ingredient;
    }

    public boolean canInsert(ItemStack stack) {
        return matches(stack);
    }

    public boolean matches(ItemStack stack) {
        return stack.getItem() == ingredient;
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        return super.insertStack(stack, count);

    }

    public int getMaxItemCount() {
        return 1;
    }
}
