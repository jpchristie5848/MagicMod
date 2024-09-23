package jipthechip.diabolism.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.spell.Spell;
import jipthechip.diabolism.packets.StatusEffectInstanceData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellGem extends Item {

    public SpellGem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Spell spell = DataUtils.readObjectFromItemNbt(stack, Spell.class);

        if(spell != null) {
            tooltip.add(1, Text.literal("Type: "+spell.getType().name()).formatted(Formatting.GRAY));

            List<StatusEffectInstanceData> effects = spell.getEffects();

            for(int i = 0; i < effects.size(); i++){

                int toolTipIndex = (i * 4)+2;
                StatusEffectInstanceData effectData = effects.get(i);

                tooltip.add(toolTipIndex, Text.literal("Effect "+(i+1)));
                tooltip.add(toolTipIndex+1, Text.literal("Name: "+effectData.getEffectKey()).formatted(Formatting.AQUA));
                tooltip.add(toolTipIndex+2, Text.literal("Amplifier: "+effectData.getAmplifier()).formatted(Formatting.YELLOW));
                tooltip.add(toolTipIndex+3, Text.literal("Duration: "+effectData.getDuration()).formatted(Formatting.RED));
            }
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
