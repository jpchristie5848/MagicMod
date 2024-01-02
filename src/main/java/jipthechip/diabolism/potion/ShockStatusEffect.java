package jipthechip.diabolism.potion;

import jipthechip.diabolism.data.MagicElement;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class ShockStatusEffect extends AbstractElementalStatusEffect{

    private static final List<Item> METAL_ARMORS = Arrays.asList(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
                                                                Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
                                                                Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS);

    protected ShockStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, MagicElement.LIGHTNING);
    }

    private boolean isWearingMetal(LivingEntity entity){
        Iterable<ItemStack> equippedIter = entity.getItemsEquipped();
        for(ItemStack stack : equippedIter){
            if(METAL_ARMORS.contains(stack.getItem())){
                return true;
            }
        }
        return false;
    }

    private boolean isInWater(LivingEntity entity){
        World world = entity.getWorld();
        return world.getBlockState(entity.getBlockPos()).getBlock() == Blocks.WATER;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {

        super.onApplied(entity,amplifier);

        float damage = amplifier / 20;
        float paralysisProbability = amplifier;
        if(entity.hasStatusEffect(DiabolismEffects.WET_STATUS_EFFECT) || isInWater(entity) || isWearingMetal(entity)){
            damage *= 2;
            paralysisProbability *= 2;
        }
        entity.damage(entity.getDamageSources().magic(), damage);
        if (Math.random() < paralysisProbability / 100.0f){
            entity.addStatusEffect(new StatusEffectInstance(DiabolismEffects.ELEMENTAL.get("paralysis"), 200, 50));
        }
    }

    @Override
    public boolean isInstant() {
        return true;
    }
}
