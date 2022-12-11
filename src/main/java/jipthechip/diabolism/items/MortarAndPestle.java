package jipthechip.diabolism.items;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.mixin.item.RecipeMixin;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;

public class MortarAndPestle extends Item implements FabricItem {
    public MortarAndPestle(Settings settings) {
        super(settings.maxDamageIfAbsent(64));
    }

    public boolean hasRecipeRemainder(){
        return true;
    }
}
