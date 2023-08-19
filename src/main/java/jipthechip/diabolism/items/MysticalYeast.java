package jipthechip.diabolism.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Yeast;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MysticalYeast extends Item {

    public MysticalYeast(Settings settings) {
        super(settings);
    }

//    @Override
//    public ActionResult useOnBlock(ItemUsageContext context) {
//        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
//        if(blockEntity instanceof MagicFermenter fermenter){
//
//        }
//        return ActionResult.PASS;
//    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        Yeast yeast = DataUtils.readObjectFromItemNbt(stack, Yeast.class);
        if(yeast != null){
            tooltip.add(1, Text.literal("Yield Multipliers: "+ Arrays.toString(yeast.getYieldMultipliers())).formatted(Formatting.RED));
            tooltip.add(2, Text.literal("Speed Multiplier: "+ yeast.getSpeedMultiplier()).formatted(Formatting.YELLOW));
            tooltip.add(3, Text.literal("Ideal Temperature: "+ yeast.getIdealTemperature()).formatted(Formatting.GREEN));
        }
    }

}
