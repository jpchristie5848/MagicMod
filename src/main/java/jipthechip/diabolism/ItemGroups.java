package jipthechip.diabolism;

import jipthechip.diabolism.items.DiabolismItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroups {

    public static final RegistryKey<ItemGroup> DIABOLISM_ITEM_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("diabolism", "diabolism"));

    private static final ItemGroup DIABOLISM_ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.literal("Diabolism"))
            .icon(()->new ItemStack(DiabolismItems.BASIC_WAND))
            .build();

    public static void registerItemGroups(){
        Registry.register(Registries.ITEM_GROUP, DIABOLISM_ITEM_GROUP_KEY, DIABOLISM_ITEM_GROUP);
    }
}
