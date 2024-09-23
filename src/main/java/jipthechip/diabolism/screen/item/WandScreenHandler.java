package jipthechip.diabolism.screen.item;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.spell.Wand;
import jipthechip.diabolism.data.spell.WandData;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.screen.DiabolismScreens;
import jipthechip.diabolism.screen.IngredientSlot;
import jipthechip.diabolism.screen.OutputSlot;
import jipthechip.diabolism.screen.SyncedItemDataSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public class WandScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final Wand wand;

    public WandScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(3), getWandFromBuf(buf));
    }

    private static Wand getWandFromBuf(PacketByteBuf buf){
        WandData wandData = (WandData) DataUtils.DeserializeFromString(buf.readString());
        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(buf.readInt(), ItemStack.EMPTY);
        for(int i = 0; i < inventory.size(); i++){
            inventory.set(i, buf.readItemStack());
        }
        ItemStack stack = buf.readItemStack();
        return new Wand(stack, wandData, inventory);
    }

    public WandScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, Wand wand) {
        super(DiabolismScreens.WAND_SCREEN_HANDLER, syncId);

        this.inventory = inventory;
        this.wand = wand;

        inventory.onOpen(playerInventory.player);

        this.addSlot(new SyncedItemDataSlot(wand, inventory, 0, 24, 69));
        this.addSlot(new SyncedItemDataSlot(wand, inventory, 1, 80, 69));
        this.addSlot(new SyncedItemDataSlot(wand, inventory, 2, 136, 69));

        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 159 + row * 18));
            }
        }
        //The player Hotbar
        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 217));
        }

    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        return super.insertItem(stack, startIndex, endIndex, fromLast);
    }

    @Override
    public void sendContentUpdates() {
        super.sendContentUpdates();
    }
}
