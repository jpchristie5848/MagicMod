package jipthechip.diabolism.items;

import jipthechip.diabolism.ItemGroups;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DiabolismItems {

    public static final Item RUNIC_POWDER = new RunicPowder(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP));
    public static final Item BASIC_WAND = new BasicWand(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(1));
    public static final Item VOLATILE_MIXTURE = new Item(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(1));
    public static final Item MORTAR_AND_PESTLE = new Item(new FabricItemSettings().group(ItemGroups.DIABOLISM_ITEM_GROUP).maxCount(1));



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

    public static void registerItems(){
        Registry.register(Registry.ITEM, new Identifier("diabolism", "runic_powder"), RUNIC_POWDER);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "basic_wand"), BASIC_WAND);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "volatile_mixture"), VOLATILE_MIXTURE);
        Registry.register(Registry.ITEM, new Identifier("diabolism", "mortar_and_pestle"), MORTAR_AND_PESTLE);

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


    }
}
