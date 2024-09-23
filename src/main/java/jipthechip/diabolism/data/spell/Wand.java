package jipthechip.diabolism.data.spell;

import io.wispforest.owo.util.ImplementedInventory;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.items.AbstractSyncedItemData;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.screen.item.WandScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class Wand extends AbstractSyncedItemData<WandData> implements Serializable, ExtendedScreenHandlerFactory, ImplementedInventory {

    private DefaultedList<ItemStack> inventory;


    public Wand(ItemStack stack, WandData wandData){
        super(stack);
        this.inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
        this.data = wandData;
    }

    public Wand(ItemStack stack, WandData wandData, DefaultedList<ItemStack> inventory){
        super(stack);
        this.inventory = inventory;
        this.data = wandData;
    }

    public Wand(ItemStack stack) {
        super(stack);
        this.inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
        this.data = new WandData();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public ItemStack getStack(int i){
        return inventory.get(i);
    }

    public void setStack(int i, ItemStack stack){
        inventory.set(i, stack);
    }

    public DefaultedList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeString(DataUtils.SerializeToString(data));
        buf.writeInt(inventory.size());
        for(ItemStack stack : inventory) buf.writeItemStack(stack);
        buf.writeItemStack(stack);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(DiabolismItems.BASIC_WAND.getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new WandScreenHandler(syncId, playerInventory, this, this);
    }
}
