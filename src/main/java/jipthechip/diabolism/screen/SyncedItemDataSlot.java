package jipthechip.diabolism.screen;

import jipthechip.diabolism.entities.blockentities.AbstractSyncedBlockEntity;
import jipthechip.diabolism.items.AbstractSyncedItemData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SyncedItemDataSlot extends Slot {
    private final AbstractSyncedItemData itemData;

    public SyncedItemDataSlot(AbstractSyncedItemData itemData, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.itemData = itemData;
    }

    @Override
    public void onQuickTransfer(ItemStack newItem, ItemStack original) {
        super.onQuickTransfer(newItem, original);
        //PacketUtils.syncBlockInventoryWithClient(entity.getWorld(), entity.getPos(), inventory);
        itemData.sync();
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        ItemStack result = super.insertStack(stack, count);
        //PacketUtils.syncBlockInventoryWithClient(entity.getWorld(), entity.getPos(), inventory);
        itemData.sync();
        return result;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        super.onTakeItem(player, stack);
        //PacketUtils.syncBlockInventoryWithClient(entity.getWorld(), entity.getPos(), inventory);
        itemData.sync();
    }

    @Override
    protected void onTake(int amount) {
        super.onTake(amount);
        //PacketUtils.syncBlockInventoryWithClient(entity.getWorld(), entity.getPos(), inventory);
        itemData.sync();
    }
}
