package jipthechip.diabolism.items;

import jipthechip.diabolism.blocks.DoublePolishedBlackstoneBlock;
import jipthechip.diabolism.blocks.RunedBlock;
import jipthechip.diabolism.packets.DiabolismPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class
RunePowder extends Item {

    public RunePowder(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Block block = world.getBlockState(context.getBlockPos()).getBlock();

        if(block instanceof DoublePolishedBlackstoneBlock){
            BlockPos pos = context.getBlockPos();
            BlockState state = world.getBlockState(pos);

            boolean hasRunes = state.get(RunedBlock.HAS_RUNES);
            if(!hasRunes){

                PlayerEntity player = context.getPlayer();
                world.setBlockState(pos, state.with(RunedBlock.HAS_RUNES, true));
                ItemStack stack = context.getStack();
                int count = stack.getCount();

                if(stack.getCount() > 1){
                    stack.setCount(count-1);
                }else{
                    stack = ItemStack.EMPTY;
                }
                player.setStackInHand(context.getHand(), stack);
            }
        }
        return super.useOnBlock(context);
    }
}
