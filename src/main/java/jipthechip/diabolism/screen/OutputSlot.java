package jipthechip.diabolism.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class OutputSlot extends SyncedBlockEntitySlot {

    public OutputSlot(BlockEntity entity, Inventory inventory, int index, int x, int y) {
        super(entity, inventory, index, x, y);
    }

    public boolean canInsert(ItemStack stack) {
        return false;
    }


    public int getMaxItemCount() {
        return 1;
    }
}
