package jipthechip.diabolism;

import jipthechip.diabolism.items.DiabolismItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroups {

    public static final ItemGroup DIABOLISM_ITEM_GROUP = FabricItemGroup.builder(new Identifier("diabolism", "diabolism_item_group"))
            .displayName(Text.literal("Diabolism"))
            .icon(()->new ItemStack(DiabolismItems.BASIC_WAND))
            .build();
}
