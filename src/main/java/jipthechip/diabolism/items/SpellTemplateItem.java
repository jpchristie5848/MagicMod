package jipthechip.diabolism.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.spell.SpellTemplate;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellTemplateItem extends Item {


    public SpellTemplateItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        SpellTemplate template = DataUtils.readObjectFromItemNbt(stack, SpellTemplate.class);

        if(template != null) {

            Formatting formatting;

            switch (template.getSpellType()){
                case SELF -> formatting = Formatting.AQUA;
                case PROJECTILE -> formatting = Formatting.YELLOW;
                default -> formatting = Formatting.RED;
            }

            tooltip.add(1, Text.literal("Spell Type: ").formatted(Formatting.GRAY).append(Text.literal(template.getSpellType().name()).formatted(formatting)));
        }
    }
}
