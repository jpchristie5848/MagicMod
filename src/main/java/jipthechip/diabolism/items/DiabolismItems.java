package jipthechip.diabolism.items;

import jipthechip.diabolism.ItemGroups;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class DiabolismItems {

    public static final Item RUNE_POWDER = new RunePowder(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));
    public static final Item BASIC_WAND = new BasicWand(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(1));
    public static final Item VOLATILE_MIXTURE = new Item(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(1));
    public static final Item MORTAR_AND_PESTLE = new MortarAndPestle(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(1).maxDamage(64).maxDamageIfAbsent(64));

    public static final Item TOTEM_OF_THUNDER = new TotemOfThunderItem(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(1).maxDamage(10).maxDamageIfAbsent(10).rarity(Rarity.UNCOMMON));

    public static final Item VOLCANIC_POWDER = new Item(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(64));
    public static final Item STONE_POLISHING_POWDER = new Item(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(64));


    public static final BlockItem RUNED_GLASS_BLOCKITEM = new BlockItem(DiabolismBlocks.RUNED_GLASS,
                                    new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));
    public static final BlockItem RUNED_COPPER_BLOCKITEM = new BlockItem(DiabolismBlocks.RUNED_COPPER,
            new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));
    public static final BlockItem RUNED_MOSS_BLOCKITEM = new BlockItem(DiabolismBlocks.RUNED_MOSS,
            new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));
    public static final BlockItem RUNED_BONE_BLOCKITEM = new BlockItem(DiabolismBlocks.RUNED_BONE,
            new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));

    public static final BlockItem CONDUCTIVE_ALTAR_BLOCKITEM = new BlockItem(DiabolismBlocks.CONDUCTIVE_ALTAR,
                                    new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));
    public static final BlockItem MOSSY_ALTAR_BLOCKITEM = new BlockItem(DiabolismBlocks.MOSSY_ALTAR,
            new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));
    public static final BlockItem CARVED_ALTAR_BLOCKITEM = new BlockItem(DiabolismBlocks.CARVED_ALTAR,
            new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));

    public static final BlockItem CONDUCTIVE_PILLAR_BLOCKITEM = new BlockItem(DiabolismBlocks.CONDUCTIVE_PILLAR,
            new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));
    public static final BlockItem MOSSY_PILLAR_BLOCKITEM = new BlockItem(DiabolismBlocks.MOSSY_PILLAR,
            new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));
    public static final BlockItem CARVED_PILLAR_BLOCKITEM = new BlockItem(DiabolismBlocks.CARVED_PILLAR,
            new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));

    public static final BlockItem POWDER_COVERED_POLISHED_BLACKSTONE_BLOCKITEM = new PowderCoveredPolishedBlackstoneBlockItem(DiabolismBlocks.POWDER_COVERED_POLISHED_BLACKSTONE,
            new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(1));

    public static final BlockItem DOUBLE_POLISHED_BLACKSTONE_BLOCKITEM = new BlockItem(DiabolismBlocks.DOUBLE_POLISHED_BLACKSTONE,
            new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(64));

    public static void registerItems(){
        Registry.register(Registry.ITEM, new Identifier("diabolism", "rune_powder"), RUNE_POWDER);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "basic_wand"), BASIC_WAND);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "volatile_mixture"), VOLATILE_MIXTURE);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "mortar_and_pestle"), MORTAR_AND_PESTLE);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "totem_of_thunder"), TOTEM_OF_THUNDER);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "volcanic_powder"), VOLCANIC_POWDER);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "stone_polishing_powder"), STONE_POLISHING_POWDER);

        Registry.register(Registry.ITEM, new Identifier("diabolism", "runed_glass"), RUNED_GLASS_BLOCKITEM);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "runed_copper"), RUNED_COPPER_BLOCKITEM);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "runed_moss"), RUNED_MOSS_BLOCKITEM);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "runed_bone"), RUNED_BONE_BLOCKITEM);

        Registry.register(Registry.ITEM, new Identifier("diabolism", "conductive_altar"), CONDUCTIVE_ALTAR_BLOCKITEM);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "mossy_altar"), MOSSY_ALTAR_BLOCKITEM);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "carved_altar"), CARVED_ALTAR_BLOCKITEM);

        Registry.register(Registry.ITEM, new Identifier("diabolism", "conductive_pillar"), CONDUCTIVE_PILLAR_BLOCKITEM);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "mossy_pillar"), MOSSY_PILLAR_BLOCKITEM);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "carved_pillar"), CARVED_PILLAR_BLOCKITEM);

        Registry.register(Registry.ITEM, new Identifier("diabolism", "powder_covered_polished_blackstone"), POWDER_COVERED_POLISHED_BLACKSTONE_BLOCKITEM);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "double_polished_blackstone"), DOUBLE_POLISHED_BLACKSTONE_BLOCKITEM);
    }

    public static void initializeClient(){
        ModelPredicateProviderRegistry.register(POWDER_COVERED_POLISHED_BLACKSTONE_BLOCKITEM, new Identifier("diabolism", "progress"), (itemStack, clientWorld, livingEntity, arg4) ->
                itemStack.hasNbt() ? itemStack.getNbt().getFloat("progress") : 0.0f
        );
    }
}
