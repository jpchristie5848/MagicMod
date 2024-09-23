package jipthechip.diabolism.items;

import jipthechip.diabolism.ItemGroups;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.data.*;
import jipthechip.diabolism.data.brewing.Fluid;
import jipthechip.diabolism.data.brewing.Yeast;
import jipthechip.diabolism.data.spell.*;
import jipthechip.diabolism.entities.blockentities.AbstractSyncedBlockEntity;
import jipthechip.diabolism.packets.BlockEntitySyncPacket;
import jipthechip.diabolism.packets.ItemSyncPacket;
import jipthechip.diabolism.packets.StatusEffectInstanceData;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponents;
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

    public static final Item SPELL_TEMPLATE = new SpellTemplateItem(new FabricItemSettings().maxCount(64));

    public static final Item MAGICKA_CRYSTAL = new MagickaCrystalItem(new FabricItemSettings().maxCount(64));

    public static final Item SPELL_MODIFIER = new SpellModifierItem(new FabricItemSettings().maxCount(1));

    public static final Item WIZWICH = new WizwichItem(new FabricItemSettings().maxCount(1).food(FoodComponents.COOKIE));

    public static final Item SPELL_GEM = new SpellGem(new FabricItemSettings().maxCount(1));



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

    public static final BlockItem FLUID_PUMP_BLOCKITEM = new FluidPumpBlockItem(DiabolismBlocks.FLUID_PUMP, new FabricItemSettings().maxCount(64));
    public static final BlockItem MAGIC_FERMENTER_BLOCKITEM = new MagicFermenterBlockItem(DiabolismBlocks.MAGIC_FERMENTER, new FabricItemSettings().maxCount(64));

    public static final BlockItem ARCANE_ALTAR_BLOCKITEM = new ArcaneAltarBlockItem(DiabolismBlocks.ARCANE_ALTAR, new FabricItemSettings().maxCount(64));


    public static void registerItems(){
        Registry.register(Registries.ITEM, new Identifier("diabolism", "rune_powder"), RUNE_POWDER);
        registerItemWithData(BASIC_WAND, new Identifier("diabolism", "basic_wand"), Wand.class);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "volatile_mixture"), VOLATILE_MIXTURE);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "mortar_and_pestle"), MORTAR_AND_PESTLE);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "totem_of_thunder"), TOTEM_OF_THUNDER);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "volcanic_powder"), VOLCANIC_POWDER);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "stone_polishing_powder"), STONE_POLISHING_POWDER);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "solar_shard"), SOLAR_SHARD);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "star_shard"), STAR_SHARD);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "brew_fluid_bucket"), BREW_FLUID_BUCKET);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "mystical_yeast"), MYSTICAL_YEAST);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "spell_template"), SPELL_TEMPLATE);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "magicka_crystal"), MAGICKA_CRYSTAL);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "spell_modifier"), SPELL_MODIFIER);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "wizwich"), WIZWICH);
        Registry.register(Registries.ITEM, new Identifier("diabolism", "spell_gem"), SPELL_GEM);


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
        Registry.register(Registries.ITEM, new Identifier("diabolism", "arcane_altar"), ARCANE_ALTAR_BLOCKITEM);



        ItemGroupEvents.modifyEntriesEvent(ItemGroups.DIABOLISM_ITEM_GROUP_KEY).register(entries -> {
            entries.add(RUNE_POWDER);

            ItemStack wandStack = new ItemStack(BASIC_WAND);
            Wand wand = new Wand(wandStack);
            wand.writeToItemNbt();
            entries.add(wandStack);

            entries.add(VOLATILE_MIXTURE);
            entries.add(MORTAR_AND_PESTLE);
            entries.add(TOTEM_OF_THUNDER);
            entries.add(VOLCANIC_POWDER);
            entries.add(STONE_POLISHING_POWDER);
            entries.add(SOLAR_SHARD);
            entries.add(STAR_SHARD);
            entries.add(BREW_FLUID_BUCKET);
            // int air, int fire, int water, int earth, int chaos, int order

            // YEAST

            ItemStack weakAirYeastStack = new ItemStack(MYSTICAL_YEAST);
            Yeast weakAirYeast = Yeast.generateLowTierYeast(MagicElement.AIR,MagicElement.FIRE, 1.1f);
            DataUtils.writeObjectToItemNbt(weakAirYeastStack, weakAirYeast);
            entries.add(weakAirYeastStack);

            ItemStack weakFireYeastStack = new ItemStack(MYSTICAL_YEAST);
            Yeast weakFireYeast = Yeast.generateLowTierYeast(MagicElement.FIRE,MagicElement.AIR, 1.1f);
            DataUtils.writeObjectToItemNbt(weakFireYeastStack, weakFireYeast);
            entries.add(weakFireYeastStack);

            ItemStack weakWaterYeastStack = new ItemStack(MYSTICAL_YEAST);
            Yeast weakWaterYeast = Yeast.generateLowTierYeast(MagicElement.WATER,MagicElement.EARTH, 0.9f);
            DataUtils.writeObjectToItemNbt(weakWaterYeastStack, weakWaterYeast);
            entries.add(weakWaterYeastStack);

            ItemStack weakEarthYeastStack = new ItemStack(MYSTICAL_YEAST);
            Yeast weakEarthYeast = Yeast.generateLowTierYeast(MagicElement.EARTH,MagicElement.WATER, 0.9f);
            DataUtils.writeObjectToItemNbt(weakEarthYeastStack, weakEarthYeast);
            entries.add(weakEarthYeastStack);

            // CRYSTALS

            ItemStack airCrystalStack = new ItemStack(MAGICKA_CRYSTAL);
            MagickaCrystal airCrystal = new MagickaCrystal(MagicElement.AIR, 1);
            DataUtils.writeObjectToItemNbt(airCrystalStack, airCrystal);
            entries.add(airCrystalStack);
            ItemStack airCrystalStack1 = new ItemStack(MAGICKA_CRYSTAL);
            MagickaCrystal airCrystal1 = new MagickaCrystal(MagicElement.AIR, 2);
            DataUtils.writeObjectToItemNbt(airCrystalStack1, airCrystal1);
            entries.add(airCrystalStack1);
            ItemStack airCrystalStack2 = new ItemStack(MAGICKA_CRYSTAL);
            MagickaCrystal airCrystal2 = new MagickaCrystal(MagicElement.AIR, 3);
            DataUtils.writeObjectToItemNbt(airCrystalStack2, airCrystal2);
            entries.add(airCrystalStack2);

            ItemStack fireCrystalStack = new ItemStack(MAGICKA_CRYSTAL);
            MagickaCrystal fireCrystal = new MagickaCrystal(MagicElement.FIRE, 1);
            DataUtils.writeObjectToItemNbt(fireCrystalStack, fireCrystal);
            entries.add(fireCrystalStack);

            ItemStack waterCrystalStack = new ItemStack(MAGICKA_CRYSTAL);
            MagickaCrystal waterCrystal = new MagickaCrystal(MagicElement.WATER, 1);
            DataUtils.writeObjectToItemNbt(waterCrystalStack, waterCrystal);
            entries.add(waterCrystalStack);

            ItemStack earthCrystalStack = new ItemStack(MAGICKA_CRYSTAL);
            MagickaCrystal earthCrystal = new MagickaCrystal(MagicElement.EARTH, 1);
            DataUtils.writeObjectToItemNbt(earthCrystalStack, earthCrystal);
            entries.add(earthCrystalStack);

            // TEMPLATES

            ItemStack spellTemplateSelfStack = new ItemStack(SPELL_TEMPLATE);
            SpellTemplate spellTemplateSelf = new SpellTemplate(SpellType.SELF);
            DataUtils.writeObjectToItemNbt(spellTemplateSelfStack, spellTemplateSelf);
            entries.add(spellTemplateSelfStack);

            ItemStack spellTemplateProjectileStack = new ItemStack(SPELL_TEMPLATE);
            SpellTemplate spellTemplateProjectile = new SpellTemplate(SpellType.PROJECTILE);
            DataUtils.writeObjectToItemNbt(spellTemplateProjectileStack, spellTemplateProjectile);
            entries.add(spellTemplateProjectileStack);

            // MODIFIERS

            ItemStack basicModifierStack = new ItemStack(SPELL_MODIFIER);
            SpellModifier basicSpellModifier = new SpellModifier(0,0);
            DataUtils.writeObjectToItemNbt(basicModifierStack, basicSpellModifier);
            entries.add(basicModifierStack);

            ItemStack preciseModifierStack = new ItemStack(SPELL_MODIFIER);
            SpellModifier preciseSpellModifier = new SpellModifier(0,-1);
            DataUtils.writeObjectToItemNbt(preciseModifierStack, preciseSpellModifier);
            entries.add(preciseModifierStack);

            ItemStack preciseModifierStack2 = new ItemStack(SPELL_MODIFIER);
            SpellModifier preciseSpellModifier2 = new SpellModifier(0,-2);
            DataUtils.writeObjectToItemNbt(preciseModifierStack2, preciseSpellModifier2);
            entries.add(preciseModifierStack2);

            ItemStack preciseModifierStack3 = new ItemStack(SPELL_MODIFIER);
            SpellModifier preciseSpellModifier3 = new SpellModifier(0,-3);
            DataUtils.writeObjectToItemNbt(preciseModifierStack3, preciseSpellModifier3);
            entries.add(preciseModifierStack3);

            ItemStack spreadModifierStack = new ItemStack(SPELL_MODIFIER);
            SpellModifier spreadSpellModifier = new SpellModifier(1,1);
            DataUtils.writeObjectToItemNbt(spreadModifierStack, spreadSpellModifier);
            entries.add(spreadModifierStack);

            ItemStack spreadModifierStack2 = new ItemStack(SPELL_MODIFIER);
            SpellModifier spreadModifier2 = new SpellModifier(2, 2);
            DataUtils.writeObjectToItemNbt(spreadModifierStack2, spreadModifier2);
            entries.add(spreadModifierStack2);

            ItemStack spreadModifierStack3 = new ItemStack(SPELL_MODIFIER);
            SpellModifier spreadModifier3 = new SpellModifier(3, 3);
            DataUtils.writeObjectToItemNbt(spreadModifierStack3, spreadModifier3);
            entries.add(spreadModifierStack3);

            ItemStack allModifierStack = new ItemStack(SPELL_MODIFIER);
            SpellModifier allSpellModifier = new SpellModifier(3, 3);
            DataUtils.writeObjectToItemNbt(allModifierStack, allSpellModifier);
            entries.add(allModifierStack);

            ItemStack wizwichStack = new ItemStack(WIZWICH);
            Wizwich wizwich = new Wizwich("lgbtq");
            DataUtils.writeObjectToItemNbt(wizwichStack, wizwich);
            entries.add(wizwichStack);

            ItemStack wizwichExtremeStack = new ItemStack(WIZWICH);
            Wizwich wizwichExtreme = new Wizwich("lgbtqlgbtqlgbtqlgbtqlgbtqlgbtqlgbtqlgbtqlgbtqlgbtqlgbtqlgbtq");
            DataUtils.writeObjectToItemNbt(wizwichExtremeStack, wizwichExtreme);
            entries.add(wizwichExtremeStack);

            ItemStack spellGlyph = new ItemStack(SPELL_GEM);
            Spell spell = new Spell(SpellType.PROJECTILE, new StatusEffectInstanceData("chilly", 100, 100), new StatusEffectInstanceData("wet", 100, 100));
            DataUtils.writeObjectToItemNbt(spellGlyph, spell);
            entries.add(spellGlyph);

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
            entries.add(ARCANE_ALTAR_BLOCKITEM);
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

    private static <P extends AbstractSyncedItemData> void registerItemWithData(Item item, Identifier id, Class<P> dataClass){
        addSyncPacket(dataClass);
        Registry.register(Registries.ITEM, id, item);
    }

    private static <P extends AbstractSyncedItemData> void addSyncPacket(Class<P> dataClass){
        System.out.println("Registering Sync Packet for item: '"+dataClass.getSimpleName()+"'");
        AbstractSyncedItemData.SYNC_PACKETS.put(dataClass.getSimpleName().toLowerCase(), ItemSyncPacket.registerSyncPacket(dataClass));
    }
    public static void initializeClient(){
        registerItemColorProviders();
        ModelPredicateProviderRegistry.register(POWDER_COVERED_POLISHED_BLACKSTONE_BLOCKITEM, new Identifier("diabolism", "progress"), (itemStack, clientWorld, livingEntity, arg4) ->
                itemStack.hasNbt() ? itemStack.getNbt().getFloat("progress") : 0.0f
        );
    }
}
