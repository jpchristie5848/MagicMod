package jipthechip.diabolism.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class InventoryHelpers {

    public static ItemStack getItemsFromStackInHand(PlayerEntity player, Hand hand, int amount){

        ItemStack stackInHand = player.getStackInHand(hand);
        int stackCount = stackInHand.getCount();

        if(stackCount >= amount){

            ItemStack returnedStack = new ItemStack(stackInHand.getItem(), amount);
            stackInHand.setCount(stackCount - amount);
            player.setStackInHand(hand, stackInHand.getCount() > 0 ? stackInHand : ItemStack.EMPTY);

            return returnedStack;
        }
        return ItemStack.EMPTY;
    }
}
