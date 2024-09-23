package jipthechip.diabolism.screen;

import jipthechip.diabolism.entities.blockentities.AbstractSyncedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SyncedBlockEntitySlot extends Slot {

    protected BlockEntity entity;
    protected Inventory inventory;

    public SyncedBlockEntitySlot(BlockEntity entity, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.entity = entity;
        this.inventory = inventory;
    }

    @Override
    public void onQuickTransfer(ItemStack newItem, ItemStack original) {
        super.onQuickTransfer(newItem, original);
        //PacketUtils.syncBlockInventoryWithClient(entity.getWorld(), entity.getPos(), inventory);
        if(entity instanceof AbstractSyncedBlockEntity<?> syncedEntity){
            syncedEntity.sync();
        }
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        ItemStack result = super.insertStack(stack, count);
        //PacketUtils.syncBlockInventoryWithClient(entity.getWorld(), entity.getPos(), inventory);
        if(entity instanceof AbstractSyncedBlockEntity<?> syncedEntity){
            syncedEntity.sync();
        }
        return result;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        super.onTakeItem(player, stack);
        //PacketUtils.syncBlockInventoryWithClient(entity.getWorld(), entity.getPos(), inventory);
        if(entity instanceof AbstractSyncedBlockEntity<?> syncedEntity){
            syncedEntity.sync();
        }
    }

    @Override
    protected void onTake(int amount) {
        super.onTake(amount);
        //PacketUtils.syncBlockInventoryWithClient(entity.getWorld(), entity.getPos(), inventory);
        if(entity instanceof AbstractSyncedBlockEntity<?> syncedEntity){
            syncedEntity.sync();
        }
    }
}
