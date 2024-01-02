package jipthechip.diabolism.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Fluid;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.MagickaCrystal;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MagickaCrystalItem extends Item {


    public MagickaCrystalItem(Settings settings) {
        super(settings);
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        MagickaCrystal crystal = DataUtils.readObjectFromItemNbt(stack, MagickaCrystal.class);

        if(crystal != null) {
            tooltip.add(1, Text.literal("Element: "+ crystal.getElement().name()).formatted(Formatting.RED));
            tooltip.add(2, Text.literal("Tier: "+crystal.getTier()).formatted(Formatting.YELLOW));
        }
    }
}
