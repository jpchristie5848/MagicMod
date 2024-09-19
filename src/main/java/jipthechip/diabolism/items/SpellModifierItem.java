package jipthechip.diabolism.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.spell.SpellModifier;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellModifierItem extends Item {

    public SpellModifierItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        SpellModifier modifier = DataUtils.readObjectFromItemNbt(stack, SpellModifier.class);

        if(modifier != null) {
            tooltip.add(1, Text.literal("Multi: "+modifier.multi()).formatted(Formatting.YELLOW));
            tooltip.add(2, Text.literal("Spread: "+modifier.spread()).formatted(Formatting.BLUE));
        }
    }
}
