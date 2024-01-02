package jipthechip.diabolism.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Spell;
import jipthechip.diabolism.data.SpellModifier;
import jipthechip.diabolism.data.StatusEffectInstanceContainer;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellGlyph extends Item {

    public SpellGlyph(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Spell spell = DataUtils.readObjectFromItemNbt(stack, Spell.class);

        if(spell != null) {
            tooltip.add(1, Text.literal("Type: "+spell.getType().name()).formatted(Formatting.GRAY));

            List<StatusEffectInstanceContainer> effects = spell.getEffects();

            for(int i = 0; i < effects.size(); i++){

                int toolTipIndex = (i * 4)+2;
                StatusEffectInstanceContainer effectContainer = effects.get(i);

                tooltip.add(toolTipIndex, Text.literal("Effect "+(i+1)));
                tooltip.add(toolTipIndex+1, Text.literal("Element: "+effectContainer.getEffectElement()).formatted(Formatting.AQUA));
                tooltip.add(toolTipIndex+2, Text.literal("Amplifier: "+effectContainer.getAmplifier()).formatted(Formatting.YELLOW));
                tooltip.add(toolTipIndex+3, Text.literal("Duration: "+effectContainer.getAmplifier()).formatted(Formatting.RED));
            }
        }
    }
}
