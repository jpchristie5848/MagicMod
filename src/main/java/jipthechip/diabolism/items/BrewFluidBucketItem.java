package jipthechip.diabolism.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Fluid;
import jipthechip.diabolism.entities.blockentities.AbstractFluidContainer;
import jipthechip.diabolism.entities.blockentities.MagicChurner;
import jipthechip.diabolism.entities.blockentities.MagicFermenter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class BrewFluidBucketItem extends Item {

    public BrewFluidBucketItem(Settings settings) {
        super(settings);
    }


    // TODO move to MagicChurner and MagicFermenter Block classes, this method is getting overridden by their onUse methods
//    @Override
//    public ActionResult useOnBlock(ItemUsageContext context) {
//        //System.out.println("called bucket use on block");
//        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
//
//        if(blockEntity instanceof MagicChurner || blockEntity instanceof MagicFermenter){
//
//        }
//
//        return ActionResult.PASS;
//    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Fluid fluid = DataUtils.readObjectFromItemNbt(stack, Fluid.class);

        if(fluid != null) {
            tooltip.add(1, Text.literal("Amount: "+fluid.getAmount()+" mB").formatted(Formatting.YELLOW));
            tooltip.add(2, Text.literal("Elements: "+ Arrays.toString(fluid.getElementContents())).formatted(Formatting.RED));
            tooltip.add(3, Text.literal("Magicka: "+Arrays.toString(fluid.getMagickaContents())).formatted(Formatting.BLUE));
            tooltip.add(4, Text.literal("Color: 0x"+Integer.toHexString(fluid.getColor()).substring(2)).formatted(Formatting.GREEN));
        }
    }
}
