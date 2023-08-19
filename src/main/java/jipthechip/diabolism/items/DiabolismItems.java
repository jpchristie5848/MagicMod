package jipthechip.diabolism.items;

import jipthechip.diabolism.ItemGroups;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.data.Fluid;
import jipthechip.diabolism.data.Yeast;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class DiabolismItems {

    public static final Item RUNE_POWDER = new RunePowder(new FabricItemSettings().maxCount(64));
    public static final Item BASIC_WAND = new BasicWand(new FabricItemSettings().maxCount(1));
    public static final Item VOLATILE_MIXTURE = new Item(new FabricItemSettings().maxCount(1));
    public static final Item MORTAR_AND_PESTLE = new MortarAndPestle(new FabricItemSettings().maxCount(1).maxDamage(64).maxDamageIfAbsent(64));

    public static final Item TOTEM_OF_THUNDER = new TotemOfThunderItem(new FabricItemSettings().maxCount(1).maxDamage(10).maxDamageIfAbsent(10).rarity(Rarity.UNCOMMON));

    public static final Item VOLCANIC_POWDER = new Item(new FabricItemSettings().maxCount(64));
    public static final Item STONE_POLISHING_POWDER = new Item(new FabricItemSettings().maxCount(64));

    public static final Item SOLAR_SHARD = new Item(new FabricItemSettings().maxCount(64));

    public static final Item STAR_SHARD = new Item(new FabricItemSettings().maxCount(64));

    public static final Item BREW_FLUID_BUCKET = new BrewFluidBucketItem(new FabricItemSettings().maxCount(1));

    public static final Item MYSTICAL_YEAST = new MysticalYeast(new FabricItemSettings().maxCount(1));

    public static final BlockItem POWDER_COVERED_POLISHED_BLACKSTONE_BLOCKITEM = new PowderCoveredPolishedBlackstoneBlockItem(DiabolismBlocks.POWDER_COVERED_POLISHED_BLACKSTONE,
            new FabricItemSettings().maxCount(1));

    public static final BlockItem DOUBLE_POLISHED_BLACKSTONE_BLOCKITEM = new BlockItem(DiabolismBlocks.DOUBLE_POLISHED_BLACKSTONE,
            new FabricItemSettings().maxCount(64));

    public static final BlockItem STARTER_CRYSTAL_BLOCKITEM = new BlockItem(DiabolismBlocks.STARTER_CRYSTAL,
            new FabricItemSettings().maxCount(1));

    public static final BlockItem CRYSTAL_ALTAR_BLOCKITEM = new BlockItem(DiabolismBlocks.CRYSTAL_ALTAR,
            new FabricItemSettings().maxCount(1));
    public static final BlockItem RUNED_DOUBLE_POLISHED_BLACKSTONE_BLOCKITEM = new BlockItem(DiabolismBlocks.RUNED_DOUBLE_POLISHED_BLACKSTONE,
            new FabricItemSettings().maxCount(64));

    public static final BlockItem STARLIGHT_COLLECTOR_BLOCKITEM = new BlockItem(DiabolismBlocks.STARLIGHT_COLLECTOR, new FabricItemSettings().maxCount(64));
    public static final BlockItem SUNLIGHT_COLLECTOR_BLOCKITEM = new BlockItem(DiabolismBlocks.SUNLIGHT_COLLECTOR, new FabricItemSettings().maxCount(64));

    public static final BlockItem MAGIC_CHURNER_BLOCKITEM = new MagicChurnerBlockItem(DiabolismBlocks.MAGIC_CHURNER, new FabricItemSettings().maxCount(64));

    public static final BlockItem FLUID_PIPE_BLOCKITEM = new BlockItem(DiabolismBlocks.FLUID_PIPE, new FabricItemSettings().maxCount(64));

    public static final BlockItem FLUID_PUMP_BLOCKITEM = new BlockItem(DiabolismBlocks.FLUID_PUMP, new FabricItemSettings().maxCount(64));
    public static final BlockItem MAGIC_FERMENTER_BLOCKITEM = new MagicFermenterBlockItem(DiabolismBlocks.MAGIC_FERMENTER, new FabricItemSettings().maxCount(64));


    public static void registerItems(){
        Registry.register(Registries.ITEM, new Identifier("diabolism", "rune_powder"), RUNE_POWDER);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "basic_wand"), BASIC_WAND);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "volatile_mixture"), VOLATILE_MIXTURE);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "mortar_and_pestle"), MORTAR_AND_PESTLE);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "totem_of_thunder"), TOTEM_OF_THUNDER);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "volcanic_powder"), VOLCANIC_POWDER);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "stone_polishing_powder"), STONE_POLISHING_POWDER);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "solar_shard"), SOLAR_SHARD);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "star_shard"), STAR_SHARD);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "brew_fluid_bucket"), BREW_FLUID_BUCKET);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "mystical_yeast"), MYSTICAL_YEAST);


        Registry.register(Registries.ITEM, new Identifier("diabolism", "powder_covered_polished_blackstone"), POWDER_COVERED_POLISHED_BLACKSTONE_BLOCKITEM);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "double_polished_blackstone"), DOUBLE_POLISHED_BLACKSTONE_BLOCKITEM);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "runed_double_polished_blackstone"), RUNED_DOUBLE_POLISHED_BLACKSTONE_BLOCKITEM);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "starter_crystal"), STARTER_CRYSTAL_BLOCKITEM);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "crystal_altar"), CRYSTAL_ALTAR_BLOCKITEM);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "starlight_collector"), STARLIGHT_COLLECTOR_BLOCKITEM);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "sunlight_collector"), SUNLIGHT_COLLECTOR_BLOCKITEM);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "magic_churner"), MAGIC_CHURNER_BLOCKITEM);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "fluid_pipe"), FLUID_PIPE_BLOCKITEM);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "fluid_pump"), FLUID_PUMP_BLOCKITEM);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "magic_fermenter"), MAGIC_FERMENTER_BLOCKITEM);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.DIABOLISM_ITEM_GROUP).register(entries -> {
            entries.add(RUNE_POWDER);
            entries.add(BASIC_WAND);
            entries.add(VOLATILE_MIXTURE);
            entries.add(MORTAR_AND_PESTLE);
            entries.add(TOTEM_OF_THUNDER);
            entries.add(VOLCANIC_POWDER);
            entries.add(STONE_POLISHING_POWDER);
            entries.add(SOLAR_SHARD);
            entries.add(STAR_SHARD);
            entries.add(BREW_FLUID_BUCKET);
            // int air, int fire, int water, int earth, int chaos, int order


            ItemStack weakAirYeastStack = new ItemStack(MYSTICAL_YEAST);
            Yeast weakAirYeast = Yeast.generateLowTierYeast(0,1, 1.1f);
            DataUtils.writeObjectToItemNbt(weakAirYeastStack, weakAirYeast);
            entries.add(weakAirYeastStack);

            ItemStack weakFireYeastStack = new ItemStack(MYSTICAL_YEAST);
            Yeast weakFireYeast = Yeast.generateLowTierYeast(1,0, 1.1f);
            DataUtils.writeObjectToItemNbt(weakFireYeastStack, weakFireYeast);
            entries.add(weakFireYeastStack);

            ItemStack weakWaterYeastStack = new ItemStack(MYSTICAL_YEAST);
            Yeast weakWaterYeast = Yeast.generateLowTierYeast(2,3, 0.9f);
            DataUtils.writeObjectToItemNbt(weakWaterYeastStack, weakWaterYeast);
            entries.add(weakWaterYeastStack);

            ItemStack weakEarthYeastStack = new ItemStack(MYSTICAL_YEAST);
            Yeast weakEarthYeast = Yeast.generateLowTierYeast(3,2, 0.9f);
            DataUtils.writeObjectToItemNbt(weakEarthYeastStack, weakEarthYeast);
            entries.add(weakEarthYeastStack);

            entries.add(POWDER_COVERED_POLISHED_BLACKSTONE_BLOCKITEM);
            entries.add(DOUBLE_POLISHED_BLACKSTONE_BLOCKITEM);
            entries.add(RUNED_DOUBLE_POLISHED_BLACKSTONE_BLOCKITEM);
            entries.add(STARTER_CRYSTAL_BLOCKITEM);
            entries.add(CRYSTAL_ALTAR_BLOCKITEM);
            entries.add(STARLIGHT_COLLECTOR_BLOCKITEM);
            entries.add(SUNLIGHT_COLLECTOR_BLOCKITEM);
            entries.add(MAGIC_CHURNER_BLOCKITEM);
            entries.add(FLUID_PIPE_BLOCKITEM);
            entries.add(FLUID_PUMP_BLOCKITEM);
            entries.add(MAGIC_FERMENTER_BLOCKITEM);
        });
    }

    private static void registerItemColorProviders(){
        ColorProviderRegistry.ITEM.register((stack, tintIndex)->{
            if(tintIndex == 1){
                NbtCompound nbt = stack.getNbt();
                if(nbt != null){
                    Fluid fluid = DataUtils.readObjectFromItemNbt(stack, Fluid.class);
                    if(fluid != null){
                        return fluid.getColor();
                    }
                }
            }
            return 0xFFFFFF;
        }, BREW_FLUID_BUCKET);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)->{

            Yeast yeast = DataUtils.readObjectFromItemNbt(stack, Yeast.class);
            if(yeast != null){
                return yeast.getColor(tintIndex);
            }
            return 0xFFFFFFFF;
        }, MYSTICAL_YEAST);
    }

    public static void initializeClient(){
        registerItemColorProviders();
        ModelPredicateProviderRegistry.register(POWDER_COVERED_POLISHED_BLACKSTONE_BLOCKITEM, new Identifier("diabolism", "progress"), (itemStack, clientWorld, livingEntity, arg4) ->
                itemStack.hasNbt() ? itemStack.getNbt().getFloat("progress") : 0.0f
        );
    }
}
