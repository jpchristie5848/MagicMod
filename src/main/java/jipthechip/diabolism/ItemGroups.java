package jipthechip.diabolism;

import jipthechip.diabolism.items.DiabolismItems;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemGroups {

    public static final ItemGroup DIABOLISM_ITEM_GROUP = FabricItemGroupBuilder.create(
                    new Identifier("diabolism", "diabolism_item_group"))
            .icon(() -> new ItemStack(DiabolismItems.RUNE_POWDER))
            .build();
}
