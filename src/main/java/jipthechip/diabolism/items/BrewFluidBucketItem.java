package jipthechip.diabolism.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.brewing.Fluid;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class BrewFluidBucketItem extends Item {

    public BrewFluidBucketItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Fluid fluid = DataUtils.readObjectFromItemNbt(stack, Fluid.class);

        if(fluid != null) {
            tooltip.add(1, Text.literal("Amount: "+fluid.getAmount()+" mB").formatted(Formatting.YELLOW));
            tooltip.add(2, Text.literal("Elements: "+ DataUtils.getMapString(fluid.getElementContents())).formatted(Formatting.RED));
            tooltip.add(3, Text.literal("Magicka: "+DataUtils.getMapString(fluid.getMagickaContents())).formatted(Formatting.BLUE));
            tooltip.add(4, Text.literal("Color: 0x"+Integer.toHexString(fluid.getColor())).formatted(Formatting.GREEN));
        }
    }
}
