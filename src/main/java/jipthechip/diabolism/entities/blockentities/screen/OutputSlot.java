package jipthechip.diabolism.entities.blockentities.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class OutputSlot extends ClientSyncingSlot {

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
